package ch.hevs.service;

import java.time.LocalDateTime;
import java.util.List;

import ch.hevs.businessobject.Media;
import ch.hevs.businessobject.Rental;
import ch.hevs.businessobject.User;
import ch.hevs.exception.RentalException;
import ch.hevs.exception.RentalException.Reason;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;

/**
 * CDI service handling all rental business logic.
 *
 * <p><b>@RequestScoped</b> — a new instance is created for each HTTP request and discarded
 * afterwards. Rental operations are short-lived, stateless interactions: no data needs to
 * survive between requests on this bean, so there is no benefit in keeping it alive
 * longer than a single request.</p>
 */
@RequestScoped
public class RentalService {

    /**
     * Transaction-scoped persistence context: the {@code EntityManager} lifetime is tied
     * to the active JTA transaction, ensuring that reads and writes within a single
     * {@code @Transactional} method share the same first-level cache and are committed
     * (or rolled back) atomically.
     */
    @PersistenceContext(unitName = "MarmottePU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    /**
     * Attempts to rent a media item for the given user.
     *
     * <p><b>@Transactional</b> (default propagation: {@code REQUIRED}) — ensures that the
     * balance deduction and the rental record creation are committed atomically within a
     * single JTA transaction. If any step fails, the entire transaction is rolled back,
     * preventing partial state updates (e.g. a deducted balance with no matching rental).</p>
     *
     * @param viewerId the ID of the user requesting the rental
     * @param mediaId  the ID of the media item to rent
     * @throws RentalException if the user or media is not found, the media is already
     *                         rented by this user, or the user's balance is insufficient
     */
    @Transactional
    public void rentMedia(Long viewerId, Long mediaId) throws RentalException {

        // Captured once so that both the duplicate-check and the rental record
        // reference the exact same instant, avoiding any inconsistency.
        LocalDateTime now = LocalDateTime.now();

        User user = em.find(User.class, viewerId);
        if (user == null) {
            throw new RentalException(Reason.USER_NOT_FOUND, "User not found.");
        }

        Media media = em.find(Media.class, mediaId);
        if (media == null) {
            throw new RentalException(Reason.MEDIA_NOT_FOUND, "Media not found.");
        }

        List<Rental> activeRentals = em.createQuery(
                        "SELECT r FROM Rental r WHERE r.user.id = :vId AND r.media.id = :mId AND r.rentalEnd > :now",
                        Rental.class)
                .setParameter("vId", viewerId)
                .setParameter("mId", mediaId)
                .setParameter("now", now)
                .getResultList();

        if (!activeRentals.isEmpty()) {
            throw new RentalException(Reason.ALREADY_RENTED, "This content is already being rented.");
        }

        if (user.getBalance().compareTo(media.getPrice()) < 0) {
            throw new RentalException(Reason.INSUFFICIENT_FUNDS,
                    "Insufficient balance (CHF " + user.getBalance() + " available, CHF " + media.getPrice() + " required).");
        }

        user.setBalance(user.getBalance().subtract(media.getPrice()));

        LocalDateTime end = now.plusDays(media.getRentalDurationInDays());
        Rental rental = new Rental(now, end, media.getPrice(), user, media);
        em.persist(rental);
    }

    /**
     * Retrieves a user by their primary key.
     *
     * @param viewerId the user's ID
     * @return the {@link User}, or {@code null} if not found
     */
    public User getViewerById(Long viewerId) {
        return em.find(User.class, viewerId);
    }

    /**
     * Returns all currently active rentals for the given user.
     * <p>
     * Uses {@code JOIN FETCH} to eagerly load the associated media within a single query,
     * avoiding N+1 select issues when iterating over the results.
     * </p>
     *
     * @param viewerId the user's ID
     * @return list of active (non-expired) {@link Rental} entities
     */
    public List<Rental> getActiveRentals(Long viewerId) {
        return em.createQuery(
                        "SELECT t FROM Rental t JOIN FETCH t.media WHERE t.user.id = :vId AND t.rentalEnd > :now",
                        Rental.class)
                .setParameter("vId", viewerId)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
    }
}
