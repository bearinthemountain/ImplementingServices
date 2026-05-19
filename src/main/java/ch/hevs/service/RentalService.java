package ch.hevs.service;

import java.time.LocalDateTime;
import java.util.List;

import ch.hevs.businessobject.Media;
import ch.hevs.businessobject.Transaction;
import ch.hevs.businessobject.Viewer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;


@RequestScoped
public class RentalService { //implements Serializable

	/**
	 *
	 */
	//private static final long serialVersionUID = 1L
	@PersistenceContext(unitName = "MarmottePU", type=PersistenceContextType.TRANSACTION)
	private EntityManager em;

	//Default transactional is Required
	@Transactional
	public void rentMedia(Long viewerId, Long mediaId) throws Exception {


		Viewer viewer = em.find(Viewer.class, viewerId);
		Media media = em.find(Media.class, mediaId);

		if (viewer == null || media == null) {
			throw new Exception("Viewer or Media not found.");
		}
		//Honest Generation IA for the SQL Request
		List<Transaction> activeRentals = em.createQuery(
						"SELECT t FROM Transaction t WHERE t.viewer.id = :vId AND t.media.id = :mId AND t.rentalEnd > :now",
						Transaction.class)
				.setParameter("vId", viewerId)
				.setParameter("mId", mediaId)
				.setParameter("now", LocalDateTime.now())
				.getResultList();

		if (!activeRentals.isEmpty()) {
			throw new Exception("You already have an active rental for this content!");
		}

		if (viewer.getBalance() < media.getPrice()) {
			throw new Exception("Insufficient funds in your wallet.");
		}

		viewer.setBalance(viewer.getBalance() - media.getPrice());
		em.merge(viewer);

		LocalDateTime start = LocalDateTime.now();
		int durationInDays = media.getRentalDurationInDays();
		LocalDateTime end = start.plusDays(durationInDays);

		Transaction transaction = new Transaction(start, end, media.getPrice(), viewer, media);
		em.persist(transaction);
	}

	public List<Media> getAllMedia() {
	 return em.createQuery("SELECT m from Media m", Media.class).getResultList();

	}

	public Viewer getViewerById(Long viewerId) {
		return em.find(Viewer.class, viewerId);
	}

	public List<Transaction> getActiveRentals(Long viewerId) {
		//Honest Generation IA for the SQL Request
		return em.createQuery(
						"SELECT t FROM Transaction t JOIN FETCH t.media WHERE t.viewer.id = :vId AND t.rentalEnd > :now",
						Transaction.class)
				.setParameter("vId", viewerId)
				.setParameter("now", LocalDateTime.now())
				.getResultList();
	}

	public List<Viewer> getAllViewers() {
		return em.createQuery("SELECT v FROM Viewer v", Viewer.class).getResultList();
	}
}
