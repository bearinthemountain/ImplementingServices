package ch.hevs.businessobject;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * JPA entity representing a TV series available for rental.
 * <p>
 * Extends {@link Media} and overrides the rental duration to 30 days,
 * reflecting a one-month subscription-style rental suitable for multi-episode content.
 * </p>
 */
@Entity
public class Serie extends Media {

    /** Number of seasons in the series. */
    private int seasons;

    /** Total number of episodes across all seasons. */
    private int episodes;

    /** Default no-arg constructor required by JPA. */
    public Serie() {
        super();
    }

    /**
     * Creates a series with all required fields.
     *
     * @param title    series title
     * @param director creator or showrunner name
     * @param price    rental price in CHF
     * @param category media category
     * @param seasons  number of seasons
     * @param episodes total number of episodes
     */
    public Serie(String title, String director, BigDecimal price, Category category, int seasons, int episodes) {
        super(title, director, price, category);
        this.seasons = seasons;
        this.episodes = episodes;
    }

    /**
     * Series are rented for 30 days, giving the viewer enough time to watch multiple episodes.
     *
     * @return {@code 30}
     */
    @Override
    public int getRentalDurationInDays() {
        return 30;
    }

    /** @return the number of seasons */
    public int getSeasons() { return seasons; }
    public void setSeasons(int seasons) { this.seasons = seasons; }

    /** @return the total number of episodes */
    public int getEpisodes() { return episodes; }
    public void setEpisodes(int episodes) { this.episodes = episodes; }
}
