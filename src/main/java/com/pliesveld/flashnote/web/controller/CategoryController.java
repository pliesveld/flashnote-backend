package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.model.json.response.FlashnoteCategory;
import com.pliesveld.flashnote.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<FlashnoteCategory> listAllCategories()
    {
        List<FlashnoteCategory> list = new ArrayList<>();
        categoryService.allCategories().forEach((category) -> list.add(FlashnoteCategory.convert(category)));
        return list;
    }

    @RequestMapping(value = "/root", method = RequestMethod.GET)
    public List<FlashnoteCategory> listRootCategories()
    {
        List<FlashnoteCategory> list = new ArrayList<>();
        categoryService.rootCategories().forEach((category) -> list.add(FlashnoteCategory.convert(category)));
        return list;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Category getCategory(@PathVariable Integer id)
    {
        Category category = categoryService.getCategoryById(id);
        return category;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createCategory(@Valid @RequestBody Category category)
    {

        Category new_category = categoryService.createCategory(category);
        return ResponseEntity.created(
                MvcUriComponentsBuilder
                        .fromController(CategoryController.class)
                        .path("/{id}").buildAndExpand(new_category.getId()).toUri())
                    .build();
    }


}
