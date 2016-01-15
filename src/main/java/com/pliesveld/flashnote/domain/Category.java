package com.pliesveld.flashnote.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Short id;

    @Column(name = "CATEGORY_NAME", unique = true, length = 17, nullable = false)
    private String name;

    @Column(name = "CATEGORY_DESC", nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORY_PARENT_ID")
    private Category parentCategory;

    @OneToMany
    @JoinColumn(name = "CATEGORY_ID")
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

    public Short getId()
    {
        return id;
    }

    public void setId(Short id)
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
