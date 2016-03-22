package com.pliesveld.flashnote.model.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.model.json.base.JsonWebResponseSerializable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Component
public class FlashnoteCategory implements JsonWebResponseSerializable {
    @JsonProperty
    Integer id;

    @JsonProperty
    String name;

    @JsonProperty
    String description;

    @JsonProperty
    List<Integer> childCategories;

    public FlashnoteCategory() {
    }

    public FlashnoteCategory(Category category)
    {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();

        List<Integer> list = new ArrayList<>();
        category.getChildCategories().forEach((subcategory) -> {
            Integer subcategory_id = subcategory.getId();

            if(subcategory_id != this.id)
            {
                list.add(subcategory_id);
            }
        });
        this.childCategories = list;


    }

    public static FlashnoteCategory convert(Category category)
    {
        FlashnoteCategory flashnoteCategory = new FlashnoteCategory(category);
        return flashnoteCategory;
    }

    public FlashnoteCategory(Integer id, String name, String description, List<Integer> childCategories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.childCategories = childCategories;
    }


}
