package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.model.json.response.FlashnoteCategory;
import com.pliesveld.flashnote.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
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


}
