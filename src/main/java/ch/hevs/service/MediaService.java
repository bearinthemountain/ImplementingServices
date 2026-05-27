package ch.hevs.service;

import ch.hevs.businessobject.Category;
import ch.hevs.businessobject.Media;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * CDI service for managing {@link Media} and {@link Category} entities.
 *
 * <p><b>@ApplicationScoped</b> — a single instance is shared for the entire application
 * lifetime. This is appropriate because the service is stateless (no mutable instance
 * fields other than the container-managed {@code EntityManager}) and its operations are
 * safe to invoke concurrently from multiple requests.</p>
 */
@ApplicationScoped
public class MediaService {

    /**
     * Transaction-scoped persistence context: the {@code EntityManager} is bound to the
     * active JTA transaction and is automatically flushed and closed when the transaction
     * commits, preventing stale first-level cache data from leaking across requests.
     */
    @PersistenceContext(unitName = "MarmottePU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    /**
     * Returns all media items currently in the catalogue.
     *
     * @return list of all {@link Media} entities (movies and series)
     */
    public List<Media> getAllMedia() {
        return em.createQuery("SELECT m FROM Media m", Media.class).getResultList();
    }

    /**
     * Persists a new media item to the database.
     *
     * <p><b>@Transactional</b> — wraps the persist in a JTA transaction so that the
     * INSERT is atomic and automatically rolled back on any runtime exception, preventing
     * partial or inconsistent state. The flush happens automatically at transaction commit;
     * an explicit {@code em.flush()} is unnecessary and would force a premature write
     * before the transaction is complete.</p>
     *
     * @param media the media entity to save
     */
    public void saveMedia(Media media) {
        em.persist(media);
    }

    /**
     * Reloads the media list from the database, bypassing the first-level cache.
     *
     * <p>Clears the persistence context before querying so that any cached entity
     * snapshots are discarded and fresh data is loaded from the database.</p>
     *
     * @return an up-to-date list of all {@link Media} entities
     */
    public List<Media> refreshMedia() {
        em.clear();
        return getAllMedia();
    }

    /**
     * Returns all categories available in the catalogue.
     *
     * @return list of all {@link Category} entities
     */
    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    /**
     * Looks up a category by its primary key.
     *
     * @param id the category identifier
     * @return the matching {@link Category}, or {@code null} if not found
     */
    public Category getCategoryById(Long id) {
        return em.find(Category.class, id);
    }
}
