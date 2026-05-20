package ch.hevs.service;

import ch.hevs.businessobject.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;

import java.util.List;


//Peu souvent appelé donc en Request scope
@RequestScoped
public class UserService {
    @PersistenceContext(unitName = "MarmottePU", type= PersistenceContextType.TRANSACTION)
    private EntityManager em;

    public List<User> getAllViewers() {
        return em.createQuery("SELECT v FROM User v", User.class).getResultList();
    }
}
