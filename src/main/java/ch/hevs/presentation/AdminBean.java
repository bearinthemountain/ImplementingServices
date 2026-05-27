package ch.hevs.presentation;

import java.io.Serializable;
import java.util.List;

import ch.hevs.businessobject.Category;
import ch.hevs.businessobject.Movie;
import ch.hevs.businessobject.Serie;
import ch.hevs.service.MediaService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;

/**
 * JSF managed bean for the administrator interface, allowing admins to add new
 * movies and series to the catalogue.
 *
 * <p><b>@SessionScoped</b> — the form fields (title, director, price, etc.) must survive
 * across the JSF POST-redirect-GET cycle so that the result message and any partially
 * filled data remain available after form submission. A shorter scope such as
 * {@code @RequestScoped} would discard the form state before the view is re-rendered.</p>
 *
 * <p>Implements {@link Serializable} as required by the CDI session scope for passivation.</p>
 */
@SessionScoped
@Named("adminBean")
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Type of media to add; defaults to {@code "MOVIE"}. */
    private String mediaType = "MOVIE";
    private String title;
    private String director;
    private BigDecimal price = BigDecimal.ZERO;
    private Long categoryId;

    /** Duration in minutes (applies to movies only). */
    private int duration;

    /** Number of seasons (applies to series only). */
    private int seasons;

    /** Total number of episodes (applies to series only). */
    private int episodes;

    /** Feedback message displayed to the admin after a form submission. */
    private String addResult;

    @Inject
    private MediaService mediaService;

    /**
     * Loads all available categories for the category selection dropdown.
     *
     * @return list of all {@link Category} entities
     */
    public List<Category> getCategories() {
        return mediaService.getAllCategories();
    }

    /**
     * Validates form input and saves the new media item to the catalogue.
     *
     * @return the JSF navigation outcome ({@code "admin"} to stay on the admin page)
     */
    public String addMedia() {
        try {
            if (title == null || title.isBlank() || director == null || director.isBlank()) {
                addResult = "Error: title and director are required.";
                return "admin";
            }
            Category category = categoryId != null ? mediaService.getCategoryById(categoryId) : null;

            if ("MOVIE".equals(mediaType)) {
                Movie movie = new Movie(title, director, price, category, duration);
                mediaService.saveMedia(movie);
            } else {
                Serie serie = new Serie(title, director, price, category, seasons, episodes);
                mediaService.saveMedia(serie);
            }
            addResult = "\"" + title + "\" successfully added to the catalogue.";
            resetForm();
        } catch (Exception e) {
            addResult = "Error while adding: " + e.getMessage();
        }
        return "admin";
    }

    /** Clears all form fields after a successful submission. */
    private void resetForm() {
        title = null;
        director = null;
        price = BigDecimal.ZERO;
        categoryId = null;
        duration = 0;
        seasons = 0;
        episodes = 0;
    }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getSeasons() { return seasons; }
    public void setSeasons(int seasons) { this.seasons = seasons; }

    public int getEpisodes() { return episodes; }
    public void setEpisodes(int episodes) { this.episodes = episodes; }

    public String getAddResult() { return addResult; }
}
