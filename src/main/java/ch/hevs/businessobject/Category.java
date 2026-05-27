package ch.hevs.businessobject;

import jakarta.persistence.*;

/**
 * JPA entity representing a media category (e.g. Action, Comedy, Drama).
 * <p>
 * Each category has a unique name and can be associated with multiple {@link Media} items.
 * </p>
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique display name of the category. */
    @Column(nullable = false, unique = true)
    private String name;

    /** Default no-arg constructor required by JPA. */
    public Category() {}

    /**
     * Creates a category with the given name.
     *
     * @param name unique name of the category
     */
    public Category(String name) { this.name = name; }

    /** @return the database primary key */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /** @return the category name */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
