package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.schema.Constants;
import com.pliesveld.flashnote.serializer.DomainObjectSerializer;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "CATEGORY",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_CATEGORY_NAME",columnNames = {"CATEGORY_NAME"}))
public class Category extends DomainBaseEntity<Integer> implements Serializable
{

    private Integer id;
    private String name;
    private String description;
    private Category parentCategory;
    private Set<Category> childCategories = new HashSet<>();

    public Category() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CATEGORY_ID")
    public Integer getId() {
        return id;
    }

    @NotNull
    @Column(name = "CATEGORY_NAME", length = Constants.MAX_CATEGORY_NAME_LENGTH, nullable = false)
    public String getName()
    {
        return name;
    }

    @NotNull
    @Column(name = "CATEGORY_DESC", length = Constants.MAX_CATEGORY_DESCRIPTION_LENGTH, nullable = false)
    public String getDescription() { return description; }

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CATEGORY_PARENT_ID", referencedColumnName = "CATEGORY_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_CATEGORY_PARENT"))
    @LazyToOne(LazyToOneOption.PROXY)
    @JsonSerialize(using = DomainObjectSerializer.class)
    public Category getParentCategory()
    {
        return parentCategory;
    }

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parentCategory", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JsonSerialize(contentUsing = DomainObjectSerializer.class)
    public Set<Category> getChildCategories()
    {
        return childCategories;
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

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setParentCategory(Category parentCategory)
    {
        this.parentCategory = parentCategory;
    }

    protected void setChildCategories(Set<Category> childCategories)
    {
        this.childCategories = childCategories;
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
