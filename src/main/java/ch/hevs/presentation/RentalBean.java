package ch.hevs.presentation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import ch.hevs.businessobject.Media;
import ch.hevs.businessobject.Rental;
import ch.hevs.businessobject.User;
import ch.hevs.exception.RentalException;
import ch.hevs.service.MediaService;
import ch.hevs.service.RentalService;
import ch.hevs.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * JSF managed bean handling the media rental workflow for end users.
 *
 * <p><b>@SessionScoped</b> — the selected media ID and the transaction result message
 * must survive across the JSF POST-redirect-GET cycle so that the confirmation page can
 * display the outcome after the redirect. A shorter scope such as {@code @RequestScoped}
 * would discard this state before the result view is rendered.</p>
 *
 * <p>Implements {@link Serializable} as required by the CDI session scope for passivation.</p>
 */

@SessionScoped
@Named("rentalBean")
public class RentalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID of the media item selected by the user for rental. */
    private Long selectedMediaId;

    /** Human-readable outcome of the last rental attempt, displayed on the result page. */
    private String transactionResult;

    @Inject
    private RentalService rentalService;

    @Inject
    private MediaService mediaService;

    @Inject
    private UserService userService;

    /** Injected to resolve the currently authenticated user from the security principal. */
    @Inject
    private HttpServletRequest request;

    /**
     * Resolves the currently authenticated user from the WildFly security principal.
     *
     * <p>Delegates entirely to {@link ch.hevs.service.UserService#getUserByPrincipal}
     * so that the principal-to-user mapping stays in the service layer.</p>
     *
     * @return the authenticated {@link User}, or {@code null} if not authenticated
     */
    public User getLoggedInUser() {
        return userService.getUserByPrincipal(request.getUserPrincipal());
    }

    /**
     * Executes the rental for the selected media and the currently logged-in user.
     * Deducts the rental price from the user's balance if the rental succeeds.
     *
     * @return the JSF navigation outcome ({@code "showRentalResult"})
     */
    public String performRental() {
        User loggedIn = getLoggedInUser();
        if (loggedIn == null) {
            this.transactionResult = "Error: no authenticated user.";
            return "showRentalResult";
        }
        if (selectedMediaId == null) {
            this.transactionResult = "Error: no media selected.";
            return "showRentalResult";
        }
        try {
            rentalService.rentMedia(loggedIn.getId(), selectedMediaId);
            this.transactionResult = "Rental successful! Enjoy watching.";
        } catch (RentalException e) {
            this.transactionResult = "Rental failed: " + e.getMessage();
        }
        return "showRentalResult";
    }

    /**
     * Returns all currently active rentals for the logged-in user.
     *
     * @return list of active {@link Rental} entities, or an empty list if not authenticated
     */
    public List<Rental> getMyActiveRentals() {
        User loggedIn = getLoggedInUser();
        if (loggedIn == null) return Collections.emptyList();
        try {
            return rentalService.getActiveRentals(loggedIn.getId());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /** @return the current media catalogue, fetched fresh from the database on each call. */
    public List<Media> getMedias() {
        return mediaService.getAllMedia();
    }

    /** @return the ID of the currently selected media item */
    public Long getSelectedMediaId() { return selectedMediaId; }
    public void setSelectedMediaId(Long selectedMediaId) { this.selectedMediaId = selectedMediaId; }

    /** @return the result message from the last rental attempt */
    public String getTransactionResult() { return transactionResult; }
    public void setTransactionResult(String transactionResult) { this.transactionResult = transactionResult; }
}
