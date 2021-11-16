/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.ArrayList;
import java.util.List;
import poi_localizer.model.PlaceType;

/**
 *
 * @author Kraw1
 */
public class PlaceTypeJpaController implements Serializable {

    public PlaceTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaceType placeType) {
        if (placeType.getPlaceList() == null) {
            placeType.setPlaceList(new ArrayList<Place>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Place> attachedPlaceList = new ArrayList<Place>();
            for (Place placeListPlaceToAttach : placeType.getPlaceList()) {
                placeListPlaceToAttach = em.getReference(placeListPlaceToAttach.getClass(), placeListPlaceToAttach.getPlaceId());
                attachedPlaceList.add(placeListPlaceToAttach);
            }
            placeType.setPlaceList(attachedPlaceList);
            em.persist(placeType);
            for (Place placeListPlace : placeType.getPlaceList()) {
                PlaceType oldTypeIdOfPlaceListPlace = placeListPlace.getTypeId();
                placeListPlace.setTypeId(placeType);
                placeListPlace = em.merge(placeListPlace);
                if (oldTypeIdOfPlaceListPlace != null) {
                    oldTypeIdOfPlaceListPlace.getPlaceList().remove(placeListPlace);
                    oldTypeIdOfPlaceListPlace = em.merge(oldTypeIdOfPlaceListPlace);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaceType placeType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlaceType persistentPlaceType = em.find(PlaceType.class, placeType.getTypeId());
            List<Place> placeListOld = persistentPlaceType.getPlaceList();
            List<Place> placeListNew = placeType.getPlaceList();
            List<String> illegalOrphanMessages = null;
            for (Place placeListOldPlace : placeListOld) {
                if (!placeListNew.contains(placeListOldPlace)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Place " + placeListOldPlace + " since its typeId field is not nullable.");
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
            placeType.setPlaceList(placeListNew);
            placeType = em.merge(placeType);
            for (Place placeListNewPlace : placeListNew) {
                if (!placeListOld.contains(placeListNewPlace)) {
                    PlaceType oldTypeIdOfPlaceListNewPlace = placeListNewPlace.getTypeId();
                    placeListNewPlace.setTypeId(placeType);
                    placeListNewPlace = em.merge(placeListNewPlace);
                    if (oldTypeIdOfPlaceListNewPlace != null && !oldTypeIdOfPlaceListNewPlace.equals(placeType)) {
                        oldTypeIdOfPlaceListNewPlace.getPlaceList().remove(placeListNewPlace);
                        oldTypeIdOfPlaceListNewPlace = em.merge(oldTypeIdOfPlaceListNewPlace);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = placeType.getTypeId();
                if (findPlaceType(id) == null) {
                    throw new NonexistentEntityException("The placeType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlaceType placeType;
            try {
                placeType = em.getReference(PlaceType.class, id);
                placeType.getTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The placeType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Place> placeListOrphanCheck = placeType.getPlaceList();
            for (Place placeListOrphanCheckPlace : placeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PlaceType (" + placeType + ") cannot be destroyed since the Place " + placeListOrphanCheckPlace + " in its placeList field has a non-nullable typeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(placeType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlaceType> findPlaceTypeEntities() {
        return findPlaceTypeEntities(true, -1, -1);
    }

    public List<PlaceType> findPlaceTypeEntities(int maxResults, int firstResult) {
        return findPlaceTypeEntities(false, maxResults, firstResult);
    }

    private List<PlaceType> findPlaceTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaceType.class));
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

    public PlaceType findPlaceType(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaceType.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaceTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaceType> rt = cq.from(PlaceType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
