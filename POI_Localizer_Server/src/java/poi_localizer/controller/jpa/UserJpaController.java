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
import java.util.ArrayList;
import java.util.List;
import poi_localizer.model.Place;
import poi_localizer.model.User;

/**
 *
 * @author Bartosz Krawczyk
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {

        if (user.getPlaceList() == null) {
            user.setPlaceList(new ArrayList<Place>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Place> attachedPlaceList = new ArrayList<Place>();
            for (Place placeListPlaceToAttach : user.getPlaceList()) {
                placeListPlaceToAttach = em.getReference(placeListPlaceToAttach.getClass(), placeListPlaceToAttach.getPlaceId());
                attachedPlaceList.add(placeListPlaceToAttach);
            }
            user.setPlaceList(attachedPlaceList);
            em.persist(user);

            for (Place placeListPlace : user.getPlaceList()) {
                User oldUserIdOfPlaceListPlace = placeListPlace.getUserId();
                placeListPlace.setUserId(user);
                placeListPlace = em.merge(placeListPlace);
                if (oldUserIdOfPlaceListPlace != null) {
                    oldUserIdOfPlaceListPlace.getPlaceList().remove(placeListPlace);
                    oldUserIdOfPlaceListPlace = em.merge(oldUserIdOfPlaceListPlace);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getUserId());
            List<Place> placeListOld = persistentUser.getPlaceList();
            List<Place> placeListNew = user.getPlaceList();
            List<String> illegalOrphanMessages = null;
            for (Place placeListOldPlace : placeListOld) {
                if (!placeListNew.contains(placeListOldPlace)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Place " + placeListOldPlace + " since its userId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }

            List<Place> attachedPlaceListNew = new ArrayList<Place>();
            for (Place placeListNewPlaceToAttach : placeListNew) {
                placeListNewPlaceToAttach = em.getReference(placeListNewPlaceToAttach.getClass(), placeListNewPlaceToAttach.getPlaceId());
                attachedPlaceListNew.add(placeListNewPlaceToAttach);
            }
            placeListNew = attachedPlaceListNew;
            user.setPlaceList(placeListNew);
            user = em.merge(user);

            for (Place placeListNewPlace : placeListNew) {
                if (!placeListOld.contains(placeListNewPlace)) {
                    User oldUserIdOfPlaceListNewPlace = placeListNewPlace.getUserId();
                    placeListNewPlace.setUserId(user);
                    placeListNewPlace = em.merge(placeListNewPlace);
                    if (oldUserIdOfPlaceListNewPlace != null && !oldUserIdOfPlaceListNewPlace.equals(user)) {
                        oldUserIdOfPlaceListNewPlace.getPlaceList().remove(placeListNewPlace);
                        oldUserIdOfPlaceListNewPlace = em.merge(oldUserIdOfPlaceListNewPlace);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getUserId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;

            List<Place> placeListOrphanCheck = user.getPlaceList();
            for (Place placeListOrphanCheckPlace : placeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Place " + placeListOrphanCheckPlace + " in its placeList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
