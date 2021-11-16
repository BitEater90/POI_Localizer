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
import poi_localizer.model.PlaceReview;
import poi_localizer.model.User;
import poi_localizer.model.Place;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceReviewJpaController implements Serializable {

    public PlaceReviewJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaceReview placeReview) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userId = placeReview.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                placeReview.setUserId(userId);
            }
            Place placeId = placeReview.getPlaceId();
            if (placeId != null) {
                placeId = em.getReference(placeId.getClass(), placeId.getPlaceId());
                placeReview.setPlaceId(placeId);
            }
            em.persist(placeReview);
            if (userId != null) {
                userId.getPlaceReviewsList().add(placeReview);
                userId = em.merge(userId);
            }
            if (placeId != null) {
                placeId.getPlaceReviewList().add(placeReview);
                placeId = em.merge(placeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaceReview placeReview) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlaceReview persistentPlaceReview = em.find(PlaceReview.class, placeReview.getReviewId());
            User userIdOld = persistentPlaceReview.getUserId();
            User userIdNew = placeReview.getUserId();
            Place placeIdOld = persistentPlaceReview.getPlaceId();
            Place placeIdNew = placeReview.getPlaceId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                placeReview.setUserId(userIdNew);
            }
            if (placeIdNew != null) {
                placeIdNew = em.getReference(placeIdNew.getClass(), placeIdNew.getPlaceId());
                placeReview.setPlaceId(placeIdNew);
            }
            placeReview = em.merge(placeReview);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPlaceReviewsList().remove(placeReview);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPlaceReviewsList().add(placeReview);
                userIdNew = em.merge(userIdNew);
            }
            if (placeIdOld != null && !placeIdOld.equals(placeIdNew)) {
                placeIdOld.getPlaceReviewList().remove(placeReview);
                placeIdOld = em.merge(placeIdOld);
            }
            if (placeIdNew != null && !placeIdNew.equals(placeIdOld)) {
                placeIdNew.getPlaceReviewList().add(placeReview);
                placeIdNew = em.merge(placeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = placeReview.getReviewId();
                if (findPlaceReview(id) == null) {
                    throw new NonexistentEntityException("The placeReview with id " + id + " no longer exists.");
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
            PlaceReview placeReview;
            try {
                placeReview = em.getReference(PlaceReview.class, id);
                placeReview.getReviewId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The placeReview with id " + id + " no longer exists.", enfe);
            }
            User userId = placeReview.getUserId();
            if (userId != null) {
                userId.getPlaceReviewsList().remove(placeReview);
                userId = em.merge(userId);
            }
            Place placeId = placeReview.getPlaceId();
            if (placeId != null) {
                placeId.getPlaceReviewList().remove(placeReview);
                placeId = em.merge(placeId);
            }
            em.remove(placeReview);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlaceReview> findPlaceReviewEntities() {
        return findPlaceReviewEntities(true, -1, -1);
    }

    public List<PlaceReview> findPlaceReviewEntities(int maxResults, int firstResult) {
        return findPlaceReviewEntities(false, maxResults, firstResult);
    }

    private List<PlaceReview> findPlaceReviewEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaceReview.class));
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

    public PlaceReview findPlaceReview(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaceReview.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaceReviewCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaceReview> rt = cq.from(PlaceReview.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
