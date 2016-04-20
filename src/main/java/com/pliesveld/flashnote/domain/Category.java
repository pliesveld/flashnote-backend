package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Entity
@Table(name = "CATEGORY",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_CATEGORY_NAME",columnNames = {"CATEGORY_NAME"}))
public class Category extends AbstractAuditableEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CATEGORY_ID")
    private Integer id;

    @NotNull
    @Column(name = "CATEGORY_NAME", length = Constants.MAX_CATEGORY_NAME_LENGTH, nullable = false)
    private String name;

    @NotNull
    @Column(name = "CATEGORY_DESC", length = Constants.MAX_CATEGORY_DESCRIPTION_LENGTH, nullable = false)
    private String description;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Category parentCategory;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parentCategory")
    private Set<Category> childCategories = new HashSet<>();

    public Category() {
    }

    public boolean isParent(String category)
    {
        Category c = this;
        do {
            if (c.getName().equals(category)) return true;
            c = c.getParentCategory();
        } while(c != null);
        return false;
    }

    public boolean isParent(Category category)
    {
        return this.isParent(category.getName());
    }

    public void addChildCategory(Category childCategory)
    {
        if (childCategory == null) throw new IllegalArgumentException("Null child category!");

        if (childCategory.getParentCategory() != null)
            childCategory.getParentCategory().getChildCategories().remove(childCategory);

        childCategory.setParentCategory(this);
        childCategories.add(childCategory);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Category getParentCategory()
    {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory)
    {
        this.parentCategory = parentCategory;
    }

    public Set<Category> getChildCategories()
    {
        return childCategories;
    }

    public void setChildCategories(Set<Category> childCategories)
    {
        this.childCategories = childCategories;
    }


    @JsonProperty("contents_count")
    @Transient
    public int getCount() {return ThreadLocalRandom.current().nextInt(0,15);}

    @Transient
    @JsonProperty("parent")
    public Integer getParentId() {
        return this.getParentCategory() == null ? null : this.getParentCategory().getId();
    }

    @Transient
    @JsonProperty("children")
    public Set<Integer> getChildrenIds() {
        return this.getChildCategories().stream().map(Category::getId).collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Category)) {
            return false;
        }
        final Category other = (Category) obj;
        return Objects.equals(getName(), other.getName());
    }
}
