package poi_localizer.controller.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import poi_localizer.controller.jpa.exceptions.NonexistentEntityException;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceEvent;

/**
 *
 * @author Kraw1
 */
public class PlaceEventJpaController implements Serializable {

    public PlaceEventJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaceEvent placeEvent) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Place placeId = placeEvent.getPlaceId();
            if (placeId != null) {
                placeId = em.getReference(placeId.getClass(), placeId.getPlaceId());
                placeEvent.setPlaceId(placeId);
            }
            em.persist(placeEvent);
            if (placeId != null) {
                placeId.getPlaceEventList().add(placeEvent);
                placeId = em.merge(placeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaceEvent placeEvent) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlaceEvent persistentPlaceEvent = em.find(PlaceEvent.class, placeEvent.getEventId());
            Place placeIdOld = persistentPlaceEvent.getPlaceId();
            Place placeIdNew = placeEvent.getPlaceId();
            if (placeIdNew != null) {
                placeIdNew = em.getReference(placeIdNew.getClass(), placeIdNew.getPlaceId());
                placeEvent.setPlaceId(placeIdNew);
            }
            placeEvent = em.merge(placeEvent);
            if (placeIdOld != null && !placeIdOld.equals(placeIdNew)) {
                placeIdOld.getPlaceEventList().remove(placeEvent);
                placeIdOld = em.merge(placeIdOld);
            }
            if (placeIdNew != null && !placeIdNew.equals(placeIdOld)) {
                placeIdNew.getPlaceEventList().add(placeEvent);
                placeIdNew = em.merge(placeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = placeEvent.getEventId();
                if (findPlaceEvent(id) == null) {
                    throw new NonexistentEntityException("The placeEvent with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlaceEvent placeEvent;
            try {
                placeEvent = em.getReference(PlaceEvent.class, id);
                placeEvent.getEventId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The placeEvent with id " + id + " no longer exists.", enfe);
            }
            Place placeId = placeEvent.getPlaceId();
            if (placeId != null) {
                placeId.getPlaceEventList().remove(placeEvent);
                placeId = em.merge(placeId);
            }
            em.remove(placeEvent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlaceEvent> findPlaceEventEntities() {
        return findPlaceEventEntities(true, -1, -1);
    }

    public List<PlaceEvent> findPlaceEventEntities(int maxResults, int firstResult) {
        return findPlaceEventEntities(false, maxResults, firstResult);
    }

    private List<PlaceEvent> findPlaceEventEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaceEvent.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PlaceEvent findPlaceEvent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaceEvent.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaceEventCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaceEvent> rt = cq.from(PlaceEvent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
