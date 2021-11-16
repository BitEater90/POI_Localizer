package poi_localizer.controller.utils;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceReview;
import poi_localizer.controller.jpa.exceptions.*;
import poi_localizer.model.*;
import poi_localizer.controller.jpa.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceReviewController {
    
    private static PlaceReviewJpaController jpa = new PlaceReviewJpaController(HelpingController.getEMF());
        
    public static void remove(PlaceReview review)
    {
        try
        {
            jpa.destroy(review.getReviewId());
        }
        catch(NonexistentEntityException nee)
        {
        }
        
    }
    
    public static boolean calculatePlaceRating(int placeId)
    {
        EntityManager em = jpa.getEntityManager();
        Query query = em.createNativeQuery(
                "SELECT AVG(rating) AS rating FROM place_reviews WHERE place_id="+placeId);
        EntityTransaction t = em.getTransaction();
        if (t == null)
        {
            return false;
        }
        t.begin();
        List results = query.getResultList();
        if ((results == null) || results.isEmpty())
        {
            t.rollback();
            return false;
        }
        Double rating = (Double)results.get(0);
        String q = "UPDATE places SET rating = "+rating+" WHERE place_id="+placeId;
        query = em.createNativeQuery(q);
        int number = query.executeUpdate();
        if (number <= 0)
        {
            t.rollback();
            return false;
        }
        t.commit();
        return true;
    }
     
    
   public static PlaceReview edit(PlaceReview review, Date reviewTime, String text, String authorName,
           String authorUrl, float rating)
    {
        try
        {
            review.setReviewTime(reviewTime);
            review.setText(text);
            review.setAuthorName(authorName);
            review.setAuthorUrl(authorUrl);
            review.setRating(rating);
            jpa.edit(review);
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Edycja opinii nie powiodła się. Obiekt nie istnieje.");
        }
        catch(Exception e)
        {
            System.err.println("Nieznany błąd.");
        }
        
        return review;
    }
   
   public static PlaceReview get(Place place, String authorName, Date reviewTime)
   {
       EntityManager em = jpa.getEntityManager();
       List<PlaceReview> reviews = em.createNamedQuery("PlaceReview.findByPlaceAndAuthorNameAndReviewTime", PlaceReview.class)
               .setParameter("authorName", authorName)
               .setParameter("placeId", place)
               .setParameter("reviewTime", reviewTime)
               .getResultList();
               //.getSingleResult();
       if (reviews.isEmpty())
           return null;
       else 
           return reviews.get(0);
   }
    
    public static List<PlaceReview> getAllByAuthorName(String authorName)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceReview> reviews = em.createNamedQuery("PlaceReview.findByAuthorName")
                .setParameter("authorName", authorName).getResultList();
        return reviews;
    }
    
    public static List<PlaceReview> getAllByPlaceAndAuthorName(Place place, String authorName)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceReview> reviews = em.createNamedQuery("PlaceReview.findByPlaceAndAuthorName")
                .setParameter("authorName", authorName)
                .setParameter("placeId", place).getResultList();
        return reviews;
    }
    
    public static List<PlaceReview> getAllByUser(User user)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceReview> reviews = em.createNamedQuery("PlaceReview.findByUser")
                .setParameter("userId", user).getResultList();
        return reviews;
    }
    
    public static List<PlaceReview> getAllByPlaceAndUser(Place place, User user)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceReview> reviews = em.createNamedQuery("PlaceReview.findByPlaceAndUser")
                .setParameter("userId", user)
                .setParameter("placeId", place).getResultList();
        return reviews;
    }
        
    
    public static List<PlaceReview> getAllByPlace(Place place)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceReview> reviews = em.createNamedQuery("PlaceReview.findAllByPlaceId")
                .setParameter("place", place).getResultList();
        return reviews;
    }
    
   
    public static List<PlaceReview> getAll()
    {
        EntityManager em = jpa.getEntityManager();
        return em.createNamedQuery("PlaceReview.findAll").getResultList();
    }
    
    public static PlaceReview add(Date reviewTime, String text, String authorName,
            String authorUrl, float rating, User user, Place place)
    {
        PlaceReview review = new PlaceReview();       
        review.setReviewTime(reviewTime);
        review.setText(text);
        review.setAuthorName(authorName);
        review.setAuthorUrl(authorUrl);
        review.setRating(rating);
        review.setUserId(user);
        review.setPlaceId(place);
        
        place.addReview(review);
        jpa.create(review);
        return review;
    }
    
}
