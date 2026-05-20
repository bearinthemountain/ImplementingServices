package ch.hevs.service;

import ch.hevs.businessobject.Media;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;

import java.util.List;


//Comme c'est un service qui est fréquemment appelé, on le garde en application Scope
@ApplicationScoped
public class MediaService {
    @PersistenceContext(unitName = "MarmottePU", type= PersistenceContextType.TRANSACTION)
    private EntityManager em;

    public List<Media> getAllMedia() {
        return em.createQuery("SELECT m from Media m", Media.class).getResultList();

    }
    public void saveMedia(Media media){
        // save return em.createQuery()
        //Refresh la liste
    }

    public List<Media> refreshMedia(){
        return this.getAllMedia();
    }
}
