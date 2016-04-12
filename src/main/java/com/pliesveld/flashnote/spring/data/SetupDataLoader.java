package com.pliesveld.flashnote.spring.data;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.repository.CategoryRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.pliesveld.flashnote.domain.StudentRole.*;



@Component
@Transactional
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LogManager.getLogger();

    private boolean alreadySetup = false;

    final private static String DEFAULT_PASSWORD = "password";

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if(alreadySetup)
            return;

        createStudentIfNotFound("new@example.com",     ROLE_ACCOUNT,    "new");
        createStudentIfNotFound("student@example.com", ROLE_USER,       "basic");
        createStudentIfNotFound("premium@example.com", ROLE_PREMIUM,    "premium");
        createStudentIfNotFound("mod@example.com",     ROLE_MODERATOR,  "moderator");
        createStudentIfNotFound("admin@example.com",   ROLE_ADMIN,      "admin");



        Category test_category = createCategoryIfNotFound("TEST CATEGORY", "A sample container for testing.");
        Category test_category_nested = createCategoryIfNotFound("TEST SUB CATEGORY", "A sample sub-category for testing.");
        createCategoryRelationship(test_category,test_category_nested);
    }



    @Transactional
    private Student createStudentIfNotFound(String email, StudentRole role, String name) {
        Student student = studentRepository.findOneByEmail(email);
        if( student == null )
        {
            student = new Student();
            student.setEmail(email);
            student.setRole(role);
            student.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

            StudentDetails studentDetails = new StudentDetails(name);
            studentDetails.setStudent(student);
            studentRepository.save(student);
        }

        return student;
    }

    @Transactional
    private void createCategoryRelationship(Category parent, Category child) {
        //entityManager.merge(parent);
        //entityManager.merge(child);


        parent.addChildCategory(child);
       // child.setParentCategory(parent);

        parent = categoryRepository.save(parent);
        child = categoryRepository.save(child);



        LOG.debug("parent id = {}", parent.getId());
        LOG.debug("child id = {}",child.getId());
        if(child.getParentCategory() != null)
        {
            LOG.debug("Child's parent category id {}",child.getParentCategory().getId());
        } else {
            LOG.error("child should have a parent");
        }

        LOG.debug("Parent's children");
        parent.getChildCategories().forEach(c -> LOG.debug("child id {}",c.getId()));



    }

    //@Transactional
    private Category createCategoryIfNotFound(String name, String description)
    {
        Category category;
        category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
        /*
        Category category = categoryRepository.findOneByNameEquals(name);

        if( category == null )
        {
            category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);
        }

        return category;
        */
    }

/*
    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
    */

}
