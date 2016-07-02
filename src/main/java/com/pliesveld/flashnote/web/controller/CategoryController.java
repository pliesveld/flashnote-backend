package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.service.CategoryService;
import com.pliesveld.flashnote.service.DeckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CategoryService categoryService;

    @Autowired
    DeckService deckService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Category> listAllCategories() {
        return categoryService.allCategories();
    }

    @RequestMapping(value = "/root", method = RequestMethod.GET)
    public List<Category> listRootCategories() {
        return categoryService.rootCategories();
    }
//
//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public List<FlashnoteCategory> listAllCategories()
//    {
//        List<FlashnoteCategory> list = new ArrayList<>();
//        categoryService.allCategories().forEach((category) -> list.add(FlashnoteCategory.convert(category)));
//        return list;
//    }
//
//    @RequestMapping(value = "/root", method = RequestMethod.GET)
//    public List<FlashnoteCategory> listRootCategories()
//    {
//        List<FlashnoteCategory> list = new ArrayList<>();
//        categoryService.rootCategories().forEach((category) -> list.add(FlashnoteCategory.convert(category)));
//        return list;
//    }

    @RequestMapping(value = "/root/{id}", method = RequestMethod.GET)
    public List<Category> listChildCategories(@PathVariable Integer id) {
        return categoryService.childCategories(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Category getCategory(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return category;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<Category> findBySearchTerm(@RequestParam("query") String searchTerm, Pageable pageRequest) {
        return categoryService.findBySearchTerm(searchTerm, pageRequest);
    }

    @RequestMapping(value = "/root/{id}/decks", method = RequestMethod.GET)
    public Page<Deck> listDecksInCategory(@PathVariable Integer id, PageRequest pageRequest) {
        return deckService.findByCategory(id, pageRequest);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createCategory(@Valid @RequestBody Category category) {

        Category new_category = categoryService.createCategory(category);
        return ResponseEntity.created(
                MvcUriComponentsBuilder
                        .fromController(CategoryController.class)
                        .path("/{id}").buildAndExpand(new_category.getId()).toUri())
                .build();
    }

}
