package ch.hevs.exception;

/**
 * Checked exception thrown by {@link ch.hevs.service.RentalService} when a rental
 * request cannot be fulfilled.
 * <p>
 * The {@link Reason} enum lets callers react programmatically to each failure case
 * without parsing the human-readable message string.
 * </p>
 */
public class RentalException extends Exception {

    /**
     * Enumeration of the reasons why a rental request may fail.
     */
    public enum Reason {
        /** The requested user does not exist in the database. */
        USER_NOT_FOUND,
        /** The requested media does not exist in the database. */
        MEDIA_NOT_FOUND,
        /** The user already has an active rental for the same media item. */
        ALREADY_RENTED,
        /** The user's account balance is too low to cover the rental price. */
        INSUFFICIENT_FUNDS
    }

    /** The specific reason this exception was thrown. */
    private final Reason reason;

    /**
     * Constructs a new {@code RentalException}.
     *
     * @param reason  the cause of the rental failure
     * @param message a human-readable description of the failure
     */
    public RentalException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    /**
     * Returns the machine-readable reason for the failure.
     *
     * @return the {@link Reason} enum value
     */
    public Reason getReason() {
        return reason;
    }
}
