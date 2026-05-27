package ch.hevs.businessobject;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * JPA entity representing a movie available for rental.
 * <p>
 * Extends {@link Media} and overrides the rental duration to 1 day (24 hours),
 * reflecting a typical single-night movie rental model.
 * </p>
 */
@Entity
public class Movie extends Media {

    /** Duration of the movie in minutes. */
    private int duration;

    /** Default no-arg constructor required by JPA. */
    public Movie() {
        super();
    }

    /**
     * Creates a movie with all required fields.
     *
     * @param title    movie title
     * @param director director name
     * @param price    rental price in CHF
     * @param category media category
     * @param duration duration in minutes
     */
    public Movie(String title, String director, BigDecimal price, Category category, int duration) {
        super(title, director, price, category);
        this.duration = duration;
    }

    /**
     * Movies are rented for 1 day (24 hours).
     *
     * @return {@code 1}
     */
    @Override
    public int getRentalDurationInDays() {
        return 1;
    }

    /** @return the duration of the movie in minutes */
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
