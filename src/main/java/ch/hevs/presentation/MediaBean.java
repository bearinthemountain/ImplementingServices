package ch.hevs.presentation;

import ch.hevs.businessobject.Media;
import ch.hevs.service.MediaService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

/**
 * JSF managed bean exposing the catalogue of available media items.
 *
 * <p>The media list is fetched directly from {@link MediaService} on each call so that
 * the view always reflects the current state of the database without any caching or
 * refresh logic.</p>
 *
 * <p>Implements {@link Serializable} as required by the CDI session scope for passivation.</p>
 */
@SessionScoped
@Named("mediaBean")
public class MediaBean implements Serializable {

    @Inject
    MediaService mediaService;

    /**
     * Returns the current media catalogue, fetched fresh from the database on each call.
     *
     * @return list of {@link Media} entities
     */
    public List<Media> getMedias() {
        return mediaService.getAllMedia();
    }
}
