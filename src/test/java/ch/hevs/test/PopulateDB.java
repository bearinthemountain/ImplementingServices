package ch.hevs.test;

import java.sql.SQLException;
import org.junit.Test;

import ch.hevs.businessobject.Category;
import ch.hevs.businessobject.Movie;
import ch.hevs.businessobject.Serie;
import ch.hevs.businessobject.Viewer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import junit.framework.TestCase;

public class PopulateDB extends TestCase {

	@Test
	public void test() throws SQLException, ClassNotFoundException {

		EntityTransaction tx = null;
		try {

			EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarmottePU_unitTest");
			EntityManager em = emf.createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			// 1. Création des Catégories (Requis pour les films et séries)
			Category catAction = new Category();
			catAction.setName("Action");

			Category catSciFi = new Category();
			catSciFi.setName("Science-Fiction");

			Category catDrama = new Category();
			catDrama.setName("Drame");

			em.persist(catAction);
			em.persist(catSciFi);
			em.persist(catDrama);

			// 2. Création des Films (Movies)
			// Paramètres : Title, Director, Price, Category, Duration (minutes)
			Movie m1 = new Movie("Inception", "Christopher Nolan", 4.90, catSciFi, 148);
			Movie m2 = new Movie("The Dark Knight", "Christopher Nolan", 3.90, catAction, 152);
			Movie m3 = new Movie("Interstellar", "Christopher Nolan", 4.90, catSciFi, 169);
			Movie m4 = new Movie("Gladiator", "Ridley Scott", 2.90, catDrama, 155);

			em.persist(m1);
			em.persist(m2);
			em.persist(m3);
			em.persist(m4);

			// 3. Création des Séries (Series)
			// Paramètres : Title, Director, Price, Category, Seasons, Episodes
			Serie s1 = new Serie("Breaking Bad", "Vince Gilligan", 19.90, catDrama, 5, 62);
			Serie s2 = new Serie("Stranger Things", "The Duffer Brothers", 14.90, catSciFi, 4, 34);
			Serie s3 = new Serie("The Mandalorian", "Jon Favreau", 12.90, catAction, 3, 24);

			em.persist(s1);
			em.persist(s2);
			em.persist(s3);

			// 4. Création des Viewers (Spectateurs)
			// Ajusté selon le constructeur : firstname, lastname, email, password
			Viewer c1 = new Viewer("Luc", "Dumoulin", "luc@dumoulin.com", 100);
			Viewer c2 = new Viewer("Michel", "Platini", "michel@platini.com", 100);
			Viewer c3 = new Viewer("Jean-Pierre", "Papin", "papin@jp.com", 1000);

			em.persist(c1);
			em.persist(c2);
			em.persist(c3);

			// Validation en base de données
			tx.commit();
			System.out.println("La base de données a été peuplée avec succès !");

		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback(); // Annule tout en cas d'erreur
			}
			e.printStackTrace();
		}
	}
}