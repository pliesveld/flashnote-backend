package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_CATEGORY_NAME",columnNames = {"CATEGORY_NAME"}))
public class Category extends AbstractAuditableEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CATEGORY_ID")
    private Integer id;

    @Column(name = "CATEGORY_NAME", length = Constants.MAX_CATEGORY_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "CATEGORY_DESC", length = Constants.MAX_CATEGORY_DESCRIPTION_LENGTH, nullable = false)
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "CATEGORY_PARENT_ID", foreignKey = @ForeignKey(name="FK_CATEGORY_PARENT"), updatable = false)
    private Category parentCategory;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name="FK_CATEGORY"), updatable = false)
    @JsonIgnore
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
}
