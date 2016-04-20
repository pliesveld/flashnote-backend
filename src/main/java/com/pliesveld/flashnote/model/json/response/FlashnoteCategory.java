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
    Integer parent;

    @JsonProperty
    List<Integer> child_categories;

    public FlashnoteCategory() {
    }

    public FlashnoteCategory(Category category)
    {
        id = category.getId();
        name = category.getName();
        description = category.getDescription();
        contents_count = category.getCount();

        category.getChildCategories().stream().forEach((c) -> LOG.debug("has child id = {}",c.getId()));

        int parent_id = category.getId();
        child_categories = category.getChildCategories().stream().map(Category::getId).filter((id) -> id != parent_id).collect(Collectors.toList());

        parent = category.getParentId();
    }

    public static FlashnoteCategory convert(Category category)
    {
        FlashnoteCategory flashnoteCategory = new FlashnoteCategory(category);
        return flashnoteCategory;
    }


}
