package com.pliesveld.flashnote.model.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.model.json.base.JsonWebResponseSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Component
public class FlashnoteCategory implements JsonWebResponseSerializable {
    private static final Logger LOG = LogManager.getLogger();

    @JsonProperty
    Integer id;

    @JsonProperty
    String name;

    @JsonProperty
    String description;

    @JsonProperty
    int contents_count;

    @JsonProperty
    List<Integer> childCategories;

    public FlashnoteCategory() {
    }

    public FlashnoteCategory(Category category)
    {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.contents_count = category.getCount();

        LOG.debug("Inflashnote converter");
        LOG.debug("Parent id = {}",category.getId());
        category.getChildCategories().stream().forEach((c) -> LOG.debug("has child id = {}",c.getId()));

        int parent_id = category.getId();
        this.childCategories = category.getChildCategories().stream().map(Category::getId).filter((id) -> id != parent_id).collect(Collectors.toList());
    }

    public static FlashnoteCategory convert(Category category)
    {
        FlashnoteCategory flashnoteCategory = new FlashnoteCategory(category);
        return flashnoteCategory;
    }


}
