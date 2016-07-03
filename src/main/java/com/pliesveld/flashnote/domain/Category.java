package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;
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
@EntityListeners(value = {LogEntityListener.class})
@Table(name = "CATEGORY",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_CATEGORY_NAME", columnNames = {"CATEGORY_NAME"}))
public class Category extends DomainBaseEntity<Integer> implements Serializable {

    private static final long serialVersionUID = -8686613244259132147L;
    private Integer id;
    private String name;
    private String description;
    private Category parentCategory;
    private Set<Category> childCategories = new HashSet<>();

    public Category() {
        super();
    }

    public Category(final String name, final String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public Category(final String name, final String description, @NotNull final Category parentCategory) {
        this(name, description);
        parentCategory.addChildCategory(this);

    }


    @Id
    @SequenceGenerator(name = "category_gen", sequenceName = "category_id_seq", initialValue = 500)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_gen")
    @Column(name = "CATEGORY_ID")
    @JsonView(Views.Summary.class)
    public Integer getId() {
        return id;
    }

    @NotNull
    @Column(name = "CATEGORY_NAME", length = Constants.MAX_CATEGORY_NAME_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    public String getName() {
        return name;
    }

    @NotNull
    @Column(name = "CATEGORY_DESC", length = Constants.MAX_CATEGORY_DESCRIPTION_LENGTH, nullable = false)
    public String getDescription() {
        return description;
    }

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CATEGORY_PARENT_ID", referencedColumnName = "CATEGORY_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_CATEGORY_PARENT"))
    @LazyToOne(LazyToOneOption.PROXY)
    @JsonSerialize(using = DomainObjectSerializer.class)
    @JsonView(Views.SummaryWithCollections.class)
    public Category getParentCategory() {
        return parentCategory;
    }

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parentCategory", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JsonSerialize(contentUsing = DomainObjectSerializer.class)
    @JsonView(Views.SummaryWithCollections.class)
    public Set<Category> getChildCategories() {
        return childCategories;
    }

    public boolean isParent(String category) {
        Category c = this;
        do {
            if (c.getName().equals(category)) return true;
            c = c.getParentCategory();
        } while (c != null);
        return false;
    }

    public boolean isParent(Category category) {
        return this.isParent(category.getName());
    }

    public void addChildCategory(Category childCategory) {
        if (childCategory == null) throw new IllegalArgumentException("Null child category!");

        if (childCategory.getParentCategory() != null)
            childCategory.getParentCategory().getChildCategories().remove(childCategory);

        childCategory.setParentCategory(this);
        childCategories.add(childCategory);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    protected void setChildCategories(Set<Category> childCategories) {
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
