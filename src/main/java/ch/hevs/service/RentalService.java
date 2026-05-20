package ch.hevs.service;

import java.time.LocalDateTime;
import java.util.List;

import ch.hevs.businessobject.Media;
import ch.hevs.businessobject.Rental;
import ch.hevs.businessobject.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;


//Uniquement appelé lors de rental, donc on le lasisse en Request Scope
@RequestScoped
public class RentalService { //implements Serializable


	//private static final long serialVersionUID = 1L
	@PersistenceContext(unitName = "MarmottePU", type=PersistenceContextType.TRANSACTION)
	private EntityManager em;

	//Default transactional is Required
	@Transactional
	public void rentMedia(Long viewerId, Long mediaId) throws Exception {

		User user = em.find(User.class, viewerId);
		Media media = em.find(Media.class, mediaId);

		if (user == null || media == null) {
			throw new Exception("Viewer or Media not found.");
		}
		//Honest Generation IA for the SQL Request
		List<Rental> activeRentals = em.createQuery(
						"SELECT r FROM Rental r WHERE r.user.id = :vId AND r.media.id = :mId AND r.rentalEnd > :now",
						Rental.class)
				.setParameter("vId", viewerId)
				.setParameter("mId", mediaId)
				.setParameter("now", LocalDateTime.now())
				.getResultList();

		if (!activeRentals.isEmpty()) {
			throw new Exception("You already have an active rental for this content!");
		}

		if (user.getBalance() < media.getPrice()) {
			throw new Exception("Insufficient funds in your wallet.");
		}

		user.setBalance(user.getBalance() - media.getPrice());
		em.merge(user);

		LocalDateTime start = LocalDateTime.now();
		int durationInDays = media.getRentalDurationInDays();
		LocalDateTime end = start.plusDays(durationInDays);

		Rental rental = new Rental(start, end, media.getPrice(), user, media);
		em.persist(rental);
	}


	public User getViewerById(Long viewerId) {
		return em.find(User.class, viewerId);
	}

	public List<Rental> getActiveRentals(Long viewerId) {
		//Honest Generation IA for the SQL Request
		return em.createQuery(
						"SELECT t FROM Rental t JOIN FETCH t.media WHERE t.viewer.id = :vId AND t.rentalEnd > :now",
						Rental.class)
				.setParameter("vId", viewerId)
				.setParameter("now", LocalDateTime.now())
				.getResultList();
	}


}
