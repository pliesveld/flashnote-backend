package com.pliesveld.flashnote.util.schema;


import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.repository.CategoryRepository;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class CategoryDefaults {
    private static final Logger LOG = LogManager.getLogger();

    static ClassPathResource resource = new ClassPathResource("categories.csv",CategoryDefaults.class);

    @Autowired
    CategoryRepository categoryRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    /**
     * Loads a CSV file with the following fields:
     * <id>,"<category_title>","<category_description>",<parent-id>
     *
     * The id field is assumed to be a unique integer.  parent-id must either be an id in the file
     * or zero.
     *
     * The Parses the file into a CSV record.  extractor parses each record into a Category object.  It keeps
     * track of any id->parent-id mappings in idChildToParentMap;  Any record that has a parent-id, that is not_root
     * returns true, has their id saved in children for the third phase.
     *
     * In the second phase; each Category object is saved using the categoryRepository.  When the entity is persisted,
     * the managed object now contains a new id field from the database persistence.  This before id and after id
     * for the category is mapped to idMap;
     *
     * In the third phase the children collection of category id is iterated.  Each id corresponds to the category in the
     * csv file.  The parent-id is loaded from the symbol table idChildToParentMap.  The tuple (id, parent-id) is then
     * mapped from csv context to db context using idMap.
     *
     * The corresponding managed entities are loaded by their database ids from categoryRepository.
     * Using managed object state, updating the java model of an Category reference using the method
     * parent.setChildCategory(child), the entitymanager queues an update to the underlying persistence layer.
     *
     * Finally, we call em.flush(); so that the transaction that has been built so far is flushed to the database.
     */
    public void saveFromCsv() throws Exception {
        StringBuilder DEBUG_CSV_MAP = new StringBuilder();

        CSVParser parser = CSVParser.parse(resource.getFile(), Charset.defaultCharset(), CSVFormat.DEFAULT);

        Map<Integer,Integer> idMap = new HashMap<>(100);
        Map<Integer,Integer> idChildToParentMap = new HashMap<>(100);

        BiConsumer<Integer,Integer> storeIdMap = (src,dst) -> idMap.put(src,dst);

        Predicate<Integer> not_root = (id) -> id != 0;

        Set<Integer> children = new HashSet<>(14);

        //phase #1
        Function<CSVRecord,Category> extractor = (record) -> {
            int category_id = Integer.parseInt(record.get(0));
            String category_title = record.get(1);
            String category_desc = record.get(2);
            int category_parent_id = Integer.parseInt(record.get(3));

            if(not_root.test(category_parent_id))
            {
                DEBUG_CSV_MAP.append("found category parent->child mapping " + category_parent_id + " -> " + category_id);
                idChildToParentMap.put(category_id, category_parent_id);
                children.add(category_id);
            }

            Category category = new Category();
            category.setId(category_id);
            category.setName(category_title);
            category.setDescription(category_desc);
            return category;
        };

        Function<Integer,Integer> idCSVtoDB = (csv_id) -> idMap.get(csv_id);

        // phase #2
        Consumer<Category> persist = (category) -> {
            int id_src = category.getId();
            Category dst = categoryRepository.save(category);
            int id_dst = dst.getId();
            idMap.put(id_src,id_dst);
            dst = null;
        };

        Consumer<CSVRecord> collector = (record) -> persist.accept(extractor.apply(record));
        parser.forEach(collector::accept);

        assertEquals(4, categoryRepository.count());

        Consumer<Category> debugger = (category) -> LOG.debug(pretty_print(category));
        categoryRepository.findAll().forEach(debugger::accept);


        // phase #3
        children.forEach((child) -> {
            int csv_parent_id = idChildToParentMap.get(child);
            int db_child_id = idCSVtoDB.apply(child);
            int db_parent_id = idCSVtoDB.apply(csv_parent_id);

            Category child_cat = categoryRepository.findOne(db_child_id);
            LOG.debug("loading child " + pretty_print(child_cat));
            Category parent_cat = categoryRepository.findOne(db_parent_id);
            LOG.debug("loading parent " + pretty_print(parent_cat));


            parent_cat.addChildCategory(child_cat);
            LOG.debug("Adding to parent "+ pretty_print(parent_cat) + " a child " + pretty_print(child_cat));

//            categoryRepository.save(parent_cat);
 //           categoryRepository.save(child_cat);
            child_cat = null;
            parent_cat = null;
        });


        LOG.info("csv parent -> child");
        LOG.info(DEBUG_CSV_MAP);

        idMap.entrySet().forEach((entry) -> {
            LOG.debug("csv " + entry.getKey() + " -> db " + entry.getValue() );
        });

        categoryRepository.findAll().forEach(debugger::accept);


    }

    // pls no circular references
    private String pretty_print(Category category) {
        if(category == null)
            return "null";
        return "[Category id=" + category.getId() +", name=" + category.getName() + ", parent=" + pretty_print(category.getParentCategory()) + "]";
    }


}
