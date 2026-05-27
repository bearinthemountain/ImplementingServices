package ch.hevs.businessobject;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity representing a rental transaction between a {@link User} and a {@link Media} item.
 * <p>
 * Records the rental window (start/end dates), the price charged at the time of rental,
 * and references to the viewer and the rented media.
 * </p>
 */
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Date and time when the rental period begins. */
    @Column(nullable = false)
    private LocalDateTime rentalStart;

    /** Date and time when the rental period expires. */
    @Column(nullable = false)
    private LocalDateTime rentalEnd;

    /**
     * Price applied at the time of rental — stored as a snapshot to protect
     * against future price changes affecting past transactions.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal appliedPrice;

    /**
     * The user who rented the media.
     * Cascades PERSIST and MERGE so that user balance updates within a rental
     * transaction are propagated transparently without a separate merge call.
     * Loaded lazily to avoid unnecessary joins when only scalar fields are needed.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id", nullable = false)
    private User user;

    /**
     * The media item being rented.
     * Loaded lazily to avoid unnecessary joins when the rental record is queried
     * without requiring media details.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    /** Default no-arg constructor required by JPA. */
    public Rental() {}

    /**
     * Creates a fully initialised rental.
     *
     * @param rentalStart  start of the rental period
     * @param rentalEnd    end (expiry) of the rental period
     * @param appliedPrice price charged for this rental
     * @param user         the viewer who is renting
     * @param media        the media item being rented
     */
    public Rental(LocalDateTime rentalStart, LocalDateTime rentalEnd, BigDecimal appliedPrice, User user, Media media) {
        this.rentalStart = rentalStart;
        this.rentalEnd = rentalEnd;
        this.appliedPrice = appliedPrice;
        this.user = user;
        this.media = media;
    }

    /**
     * Checks whether the rental period has already expired.
     *
     * @return {@code true} if the current time is after {@code rentalEnd}
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(rentalEnd);
    }

    /** @return the database primary key */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /** @return the start of the rental period */
    public LocalDateTime getRentalStart() { return rentalStart; }
    public void setRentalStart(LocalDateTime rentalStart) { this.rentalStart = rentalStart; }

    /** @return the end (expiry) of the rental period */
    public LocalDateTime getRentalEnd() { return rentalEnd; }
    public void setRentalEnd(LocalDateTime rentalEnd) { this.rentalEnd = rentalEnd; }

    /** @return the price charged for this rental */
    public BigDecimal getAppliedPrice() { return appliedPrice; }
    public void setAppliedPrice(BigDecimal appliedPrice) { this.appliedPrice = appliedPrice; }

    /** @return the user who rented the media */
    public User getViewer() { return user; }
    public void setViewer(User user) { this.user = user; }

    /** @return the media item that was rented */
    public Media getMedia() { return media; }
    public void setMedia(Media media) { this.media = media; }
}
