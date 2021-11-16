package poi_localizer.controller.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import poi_localizer.controller.jpa.exceptions.IllegalOrphanException;
import poi_localizer.controller.jpa.exceptions.NonexistentEntityException;
import poi_localizer.model.Place;
import poi_localizer.model.User;
import poi_localizer.model.PlaceType;
import poi_localizer.model.PlaceEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceJpaController implements Serializable {

    public PlaceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Place place) {
        if (place.getPlaceEventList() == null) {
            place.setPlaceEventList(new ArrayList<PlaceEvent>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userId = place.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                place.setUserId(userId);
            }
            PlaceType typeId = place.getTypeId();
            if (typeId != null) {
                typeId = em.getReference(typeId.getClass(), typeId.getTypeId());
                place.setTypeId(typeId);
            }
            List<PlaceEvent> attachedPlaceEventList = new ArrayList<PlaceEvent>();
            for (PlaceEvent placeEventListPlaceEventToAttach : place.getPlaceEventList()) {
                placeEventListPlaceEventToAttach = em.getReference(placeEventListPlaceEventToAttach.getClass(), placeEventListPlaceEventToAttach.getEventId());
                attachedPlaceEventList.add(placeEventListPlaceEventToAttach);
            }
            place.setPlaceEventList(attachedPlaceEventList);
            em.persist(place);
            if (userId != null) {
                userId.getPlaceList().add(place);
                userId = em.merge(userId);
            }
            if (typeId != null) {
                typeId.getPlaceList().add(place);
                typeId = em.merge(typeId);
            }
            for (PlaceEvent placeEventListPlaceEvent : place.getPlaceEventList()) {
                Place oldPlaceIdOfPlaceEventListPlaceEvent = placeEventListPlaceEvent.getPlaceId();
                placeEventListPlaceEvent.setPlaceId(place);
                placeEventListPlaceEvent = em.merge(placeEventListPlaceEvent);
                if (oldPlaceIdOfPlaceEventListPlaceEvent != null) {
                    oldPlaceIdOfPlaceEventListPlaceEvent.getPlaceEventList().remove(placeEventListPlaceEvent);
                    oldPlaceIdOfPlaceEventListPlaceEvent = em.merge(oldPlaceIdOfPlaceEventListPlaceEvent);
                }
            }

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Place place) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            //em.getTransaction().begin();
            Place persistentPlace = em.find(Place.class, place.getPlaceId());
            User userIdOld = persistentPlace.getUserId();
            User userIdNew = place.getUserId();
            PlaceType typeIdOld = persistentPlace.getTypeId();
            PlaceType typeIdNew = place.getTypeId();
 
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                place.setUserId(userIdNew);
            }
            if (typeIdNew != null) {
                typeIdNew = em.getReference(typeIdNew.getClass(), typeIdNew.getTypeId());
                place.setTypeId(typeIdNew);
            }
 
            place = em.merge(place);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPlaceList().remove(place);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPlaceList().add(place);
                userIdNew = em.merge(userIdNew);
            }
            if (typeIdOld != null && !typeIdOld.equals(typeIdNew)) {
                typeIdOld.getPlaceList().remove(place);
                typeIdOld = em.merge(typeIdOld);
            }
            if (typeIdNew != null && !typeIdNew.equals(typeIdOld)) {
                typeIdNew.getPlaceList().add(place);
                typeIdNew = em.merge(typeIdNew);
            }

            //em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = place.getPlaceId();
                if (findPlace(id) == null) {
                    throw new NonexistentEntityException("The place with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Place place;
            try {
                place = em.getReference(Place.class, id);
                place.getPlaceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The place with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PlaceEvent> placeEventListOrphanCheck = place.getPlaceEventList();
            for (PlaceEvent placeEventListOrphanCheckPlaceEvent : placeEventListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Place (" + place + ") cannot be destroyed since the PlaceEvent " + placeEventListOrphanCheckPlaceEvent + " in its placeEventList field has a non-nullable placeId field.");
            }
            
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User userId = place.getUserId();
            if (userId != null) {
                userId.getPlaceList().remove(place);
                userId = em.merge(userId);
            }
            PlaceType typeId = place.getTypeId();
            if (typeId != null) {
                typeId.getPlaceList().remove(place);
                typeId = em.merge(typeId);
            }
            em.remove(place);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Place> findPlaceEntities() {
        return findPlaceEntities(true, -1, -1);
    }

    public List<Place> findPlaceEntities(int maxResults, int firstResult) {
        return findPlaceEntities(false, maxResults, firstResult);
    }

    private List<Place> findPlaceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Place.class));
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

    public Place findPlace(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Place.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Place> rt = cq.from(Place.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
