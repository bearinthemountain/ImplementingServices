package ch.hevs.test;

import java.math.BigDecimal;



import ch.hevs.businessobject.User;
import org.junit.Test;

import ch.hevs.businessobject.Category;
import ch.hevs.businessobject.Movie;
import ch.hevs.businessobject.Serie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import junit.framework.TestCase;

public class PopulateDB extends TestCase {



    @Test
    public void test() throws  ClassNotFoundException {

        EntityTransaction tx = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarmottePU_unitTest");
            EntityManager em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            // The password and role columns are intentionally not mapped in the User JPA entity
            // (WildFly's login module reads them directly from SQL), so they are not generated
            // by the drop-and-create schema action — add them manually here.
            em.createNativeQuery("ALTER TABLE APPLICATION_USER ADD COLUMN password VARCHAR(255)").executeUpdate();
            em.createNativeQuery("ALTER TABLE APPLICATION_USER ADD COLUMN role VARCHAR(50)").executeUpdate();

            // Categories
            Category catAction = new Category("Action");
            Category catSciFi  = new Category("Science-Fiction");
            Category catDrama  = new Category("Drama");

            em.persist(catAction);
            em.persist(catSciFi);
            em.persist(catDrama);

            // Movies
            em.persist(new Movie("Inception",       "Christopher Nolan", new BigDecimal("4.90"), catSciFi,  148));
            em.persist(new Movie("The Dark Knight",  "Christopher Nolan", new BigDecimal("3.90"), catAction, 152));
            em.persist(new Movie("Interstellar",     "Christopher Nolan", new BigDecimal("4.90"), catSciFi,  169));
            em.persist(new Movie("Gladiator",        "Ridley Scott",      new BigDecimal("2.90"), catDrama,  155));

            // Series
            em.persist(new Serie("Breaking Bad",     "Vince Gilligan",      new BigDecimal("19.90"), catDrama,  5, 62));
            em.persist(new Serie("Stranger Things",  "The Duffer Brothers", new BigDecimal("14.90"), catSciFi,  4, 34));
            em.persist(new Serie("The Mandalorian",  "Jon Favreau",         new BigDecimal("12.90"), catAction, 3, 24));

            // Users — business fields persisted via JPA; security fields (password, role)
            // set via native SQL since they are WildFly's responsibility, not the entity's.
            User luc    = new User("Dumoulin", "Luc",          new BigDecimal("100.00"));
            User michel = new User("Platini",  "Michel",       new BigDecimal("100.00"));
            User jp     = new User("Papin",    "Jean-Pierre",  new BigDecimal("1000.00"));

            em.persist(luc);
            em.persist(michel);
            em.persist(jp);
            em.flush(); // force ID generation before the native UPDATE


            tx.commit();
            System.out.println("Database successfully populated.");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
