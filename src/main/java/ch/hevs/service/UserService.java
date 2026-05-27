package ch.hevs.service;

import ch.hevs.businessobject.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;

import java.security.Principal;
import java.util.List;

/**
 * CDI service for querying {@link User} entities.
 *
 * <p><b>@RequestScoped</b> — a new instance is created per HTTP request. User lookups are
 * infrequent and stateless, so there is no benefit in keeping this bean alive beyond the
 * duration of a single request. This also ensures the first-level cache does not retain
 * stale user data across requests.</p>
 */
@RequestScoped
public class UserService {

    /**
     * Transaction-scoped persistence context: bound to the active JTA transaction so
     * that the first-level cache is reset between requests, preventing stale reads.
     */
    @PersistenceContext(unitName = "MarmottePU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;


    /**
     * Resolves the currently authenticated user from a security {@link Principal}.
     *
     * <p>This is the single authoritative place where the HTTP principal name is
     * mapped to a domain {@link User}. Presentation beans should call this method
     * instead of performing the lookup themselves, keeping security resolution out
     * of the view layer.</p>
     *
     * @param principal the security principal from {@code HttpServletRequest.getUserPrincipal()}
     * @return the matching {@link User}, or {@code null} if the principal is {@code null}
     *         or no user with that last name exists
     */
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return null;
        return getUserByLastname(principal.getName());
    }

    /**
     * Finds a user by their last name.
     * <p>
     * Returns the first match if multiple users share the same last name.
     * Returns {@code null} if no match is found.
     * </p>
     *
     * @param lastname the last name to search for
     * @return the first matching {@link User}, or {@code null}
     */
    public User getUserByLastname(String lastname) {
        List<User> results = em.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.lastname) = LOWER(:lastname)", User.class)
                .setParameter("lastname", lastname)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

}
