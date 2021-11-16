package poi_localizer.controller.utils;
import poi_localizer.model.User;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceType;
import poi_localizer.model.*;
import poi_localizer.controller.jpa.*;
import java.math.*;
import java.util.HashMap;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.metamodel.Metamodel;
import poi_localizer.controller.jpa.exceptions.*;
import java.util.List;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceController {
    
    private final static double RMAX = 6378.137;
    private final static double RMIN = 6356.752;
    private final static int DECIMAL = 3;
    
    public static PlaceJpaController jpa = new PlaceJpaController(HelpingController.getEMF());
    private static UserJpaController userJpa = new UserJpaController(HelpingController.getEMF());
    
    public static boolean removeQuery(Place place)
    {
        boolean res = false;
        String countQuery = "SELECT COUNT(place_id) AS number FROM places";
        String query = "DELETE FROM places WHERE place_id = "+place.getPlaceId();
        EntityManager em = jpa.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Query countQ = em.createNativeQuery(countQuery);
        long countBefore = (Long)countQ.getResultList().get(0);
        Query q = em.createNativeQuery(query);
        q.executeUpdate();
        countQ = em.createNativeQuery(countQuery);
        long countAfter = (Long)countQ.getResultList().get(0);
        transaction.commit();
        return (countBefore - countAfter == 1);
    }
    
    public static boolean remove(Place place)
    {
        boolean res = false;
        try
        {
            int countBefore = jpa.getPlaceCount();
            jpa.destroy(place.getPlaceId());
            int countAfter = jpa.getPlaceCount();
            res = (countBefore - countAfter == 1);
        }
        finally
        {
            return res;
        }
    }
    
   public static int editQuery(String query)
   {
       EntityManager em = jpa.getEntityManager();
       EntityTransaction transaction = em.getTransaction();
       transaction.begin();
       Query q = em.createNativeQuery(query);
       int updatedNumber = q.executeUpdate();
       transaction.commit();
       return updatedNumber;
   }
    
    public static Place edit(Place place, String name, float lat, float lng, String formattedAddress,
            String formattedPhoneNumber, String internationalPhoneNumber,
            Place.PriceLevel priceLevel, float rating, short utcOffset,
            String vicinity, String website)
    {
        try
        {
            place.setName(name);
            place.setLat(lat);
            place.setLng(lng);
            place.setCreationTime(Timer.getDate());
            place.setFormattedAddress(formattedAddress);
            place.setFormattedPhoneNumber(formattedPhoneNumber);
            place.setInternationalPhoneNumber(internationalPhoneNumber);
            place.setPriceLevel(priceLevel);
            place.setUtcOffset(utcOffset);
            place.setVicinity(vicinity);
            place.setWebsite(website);
            place.setRating(rating);
            jpa.edit(place);
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Edycja miejsca nie powiodła się. Obiekt nie istnieje.");
            place = null;
        }
        catch(Exception e)
        {
            System.err.println("Nieznany błąd.");
            place = null;
        }
        finally
        {
            return place;
        }
    }
    
    public static double calculateDistance(double y1, double x1, double y2,
            double x2)
    {
        double arg = Math.pow(Math.cos(Math.PI*y1/180.0)*(x2-x1), 2.0) + Math.pow(y2-y1, 2.0);
        double distance = Math.sqrt(arg)*Math.PI*12756.274/360.0;
        return distance;
    }
    
    private static double radius(double lat1, double lat2)
    {
        return RMAX - (RMAX - RMIN) * Math.sin((lat1+lat2)/2.0);
    }
    
    public static Place get(String name)
    {
        EntityManager em = jpa.getEntityManager();
        List<Place> places = em.createNamedQuery("Place.findByName")
                .setParameter("name", name).getResultList();
        if ((places == null) || (places.isEmpty()))
            return null;
        return places.get(0);
    }
    
    public static List<Place> getApprox(PlaceType type, String name)
    {
        EntityManager em = jpa.getEntityManager();
        List<Place> places = em.createNamedQuery("Place.findByNameAndTypeApprox")
                .setParameter("typeId", type)
                .setParameter("name", "%"+name+"%")
                .getResultList();
        return places;
    }
    
    public static Place get(PlaceType type, String name)
    {
        EntityManager em = jpa.getEntityManager();
        List<Place> places = em.createNamedQuery("Place.findByNameAndType")
                .setParameter("name", name)
                .setParameter("typeId", type)
                .getResultList();
        if ((places == null) || (places.isEmpty()))
            return null;
        return places.get(0);
    }
    
    public static List<Place> getApprox(PlaceType type, String name, String vicinity)
    {
        EntityManager em = jpa.getEntityManager();
        List<Place> places = em.createNamedQuery("Place.findByNameAndVicinityAndTypeApprox")
                .setParameter("typeId", type)
                .setParameter("name", "%"+name+"%")
                .setParameter("vicinity", "%"+vicinity+"%")
                .getResultList();
        return places;
    }
    
    public static Place get(PlaceType type, String name, String vicinity)
    {
        EntityManager em = jpa.getEntityManager();
        List<Place> places = em.createNamedQuery("Place.findByNameAndVicinityAndType")
                .setParameter("name", name)
                .setParameter("typeId", type)
                .setParameter("vicinity", vicinity)
                .getResultList();
        if ((places == null) || (places.isEmpty()))
            return null;
        return places.get(0);
    }
    
    public static Place get(int id)
    {
        return jpa.findPlace(id);
    }
    
    public static List<Place> getByType(PlaceType type)
    {
        EntityManager em = jpa.getEntityManager();
        return em.createNamedQuery("Place.findByType")
                .setParameter("typeId", type).getResultList();
    }
    
    public static Place getByTypeAndCoordinates(PlaceType type, double lng, double lat)
    {
        EntityManager em = jpa.getEntityManager();
        Query query = em.createNativeQuery ("SELECT * FROM places p WHERE ( p.type_id = "
                + String.valueOf(type.getTypeId())
                + ") AND (ROUND(p.lat, "+DECIMAL+") = ROUND("+String.valueOf(lat)
                + ", "+DECIMAL+")) AND (ROUND(p.lng, "+DECIMAL+") = ROUND("
                + String.valueOf(lng)+", "+DECIMAL+"))", Place.class);
        List<Place> places = query.getResultList();
        if (places.isEmpty())
            return null;
        return places.get(0);
    }

    public static List<Place> getByAddressComponents(HashMap<String, String> componentMap)
    {
        if (componentMap == null)
            return null;
        if (componentMap.isEmpty())
            return null;
        
        EntityManager em =jpa.getEntityManager();
        Metamodel m = em.getMetamodel();
       
        String q = "SELECT p.* FROM places p ";
        q += "JOIN address_components ac1 ON p.place_id = ac1.place_id ";
        int size = componentMap.size();
        for (int i=1; i<=size; i++)
        {
            q += "JOIN address_components_joins acj"+i+" ON acj"+i+".component_id = ac"+i+".component_id ";
            q += "JOIN address_component_types act"+i+" ON acj"+i+".type_id = act"+i+".type_id ";
            if (i < size)
            {
                q += ", address_components ac"+(i+1)+" ";
            }
        }
        q += " WHERE ";
        String[] keys = componentMap.keySet().toArray(new String[0]);
        
        for (int i=0; i<size; i++)
        {
            String key1 = keys[i];
            String value1 = componentMap.get(key1); 
            q += "(act"+(i+1)+".name = '"+key1+"' AND ac"+(i+1)+".long_name = '"+value1+"') ";
            if (i < size-1)
            {
                q += "AND ";
            }
        }
        Query query = em.createNativeQuery(q, Place.class);
        return query.getResultList();
    }
   
       
    public static Place getByCoordinates(float lng, float lat)
    {
        EntityManager em = jpa.getEntityManager();
        List<Place> places = em.createNamedQuery("Place.findByCoordinates")
                .setParameter("lng", lng)
                .setParameter("lat", lat)
                .getResultList();
        if (places.isEmpty())
            return null;
        return places.get(0);
    }    

    public static List<Place> getAll()
    {
        EntityManager em = jpa.getEntityManager();
        return em.createNamedQuery("Place.findAll").getResultList();
    }
    
    public static Place add(String name, float lat, float lng, String formattedAddress,
            String formattedPhoneNumber, String internationalPhoneNumber,
            Place.PriceLevel priceLevel, float rating,
            PlaceType type, User user,
            short utcOffset, String vicinity, String website)
    {
        Place place = new Place();       
        place.setName(name);
        place.setLat(lat);
        place.setLng(lng);
        place.setCreationTime(Timer.getDate());
        place.setFormattedAddress(formattedAddress);
        place.setFormattedPhoneNumber(formattedPhoneNumber);
        place.setInternationalPhoneNumber(internationalPhoneNumber);
        place.setPriceLevel(priceLevel);
        place.setTypeId(type);
        place.setUserId(user);
        place.setUtcOffset(utcOffset);
        place.setVicinity(vicinity);
        place.setWebsite(website);
        place.setRating(rating);
        
        jpa.create(place);
        return place;
    }
}
