package ch.hevs.businessobject;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Abstract JPA entity representing a rentable media item.
 * <p>
 * Uses the {@code JOINED} inheritance strategy so that each concrete subtype
 * ({@link Movie}, {@link Serie}) has its own table joined back to the {@code MEDIA}
 * root table, avoiding sparse nullable columns while keeping a clean schema.
 * </p>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Title of the media item; must not be null. */
    @Column(nullable = false)
    private String title;

    /** Director or creator of the media item. */
    private String director;

    /** Rental price in CHF; stored as NUMERIC(10,2) to guarantee two decimal places. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Category this media belongs to.
     * Loaded lazily to avoid unnecessary joins when only scalar fields are needed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /** Default no-arg constructor required by JPA. */
    public Media() {}

    /**
     * Creates a media item with all required fields.
     *
     * @param title    media title
     * @param director director or creator name
     * @param price    rental price in CHF
     * @param category category this media belongs to
     */
    public Media(String title, String director, BigDecimal price, Category category) {
        this.title = title;
        this.director = director;
        this.price = price;
        this.category = category;
    }

    /**
     * Returns the rental duration in calendar days for this specific media type.
     *
     * @return number of days the media may be rented
     */
    public abstract int getRentalDurationInDays();

    /** @return the database primary key */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /** @return the media title */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    /** @return the director or creator name */
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    /** @return the rental price in CHF */
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
