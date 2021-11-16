package poi_localizer.controller.utils;
import poi_localizer.model.*;
import poi_localizer.controller.jpa.*;
import javax.persistence.*;
import poi_localizer.controller.jpa.exceptions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceEventController {

    private static PlaceEventJpaController jpa = new PlaceEventJpaController(HelpingController.getEMF());
        
    public static void remove(PlaceEvent event)
    {
        try
        {
            jpa.destroy(event.getEventId());
        }
        catch(NonexistentEntityException nee)
        {
        }
        
    }
    
   public static List<PlaceEvent> getEventsByStartDate(Place place, int day, int month, int year)
   {
       List<PlaceEvent> events = PlaceEventController.getAllByPlace(place);
       List<PlaceEvent> resultEvents = new ArrayList<PlaceEvent>();
       
       for (PlaceEvent event : events)
       {
           Date startDate = event.getStartTime();
           int eventStartDay = startDate.getDate();
           int eventStartMonth = startDate.getMonth();
           int eventStartYear = startDate.getYear();
           
           if ((day == eventStartDay) && (month == eventStartMonth) && (year == eventStartYear))
           {
               resultEvents.add(event);
           }
       }
       
       return resultEvents;
   }
    
    
   public static PlaceEvent edit(PlaceEvent event, String name, Date startTime,
           String summary, String url)
    {
        try
        {
            event.setName(name);
            event.setStartTime(startTime);
            event.setSummary(summary);
            event.setUrl(url);
            jpa.edit(event);
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Edycja zdarzenia nie powiodła się. Obiekt nie istnieje.");
        }
        catch(Exception e)
        {
            System.err.println("Nieznany błąd.");
        }
        
        return event;
    }
   
    public static PlaceEvent get(String name)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceEvent> events = em.createNamedQuery("PlaceEvent.findByName")
                .setParameter("name", name).getResultList();
        if ((events == null) || events.isEmpty())
            return null;
        return events.get(0);
    }
    
    public static List<PlaceEvent> getAllByPlace(Place place)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceEvent> events = em.createNamedQuery("Place.findAllByPlaceId")
                .setParameter("place", place).getResultList();
        return events;
    }
    
    public static PlaceEvent getByName(Place place, String name)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceEvent> events = em.createNamedQuery("PlaceEvent.findByPlaceAndName")
                .setParameter("place", place)
                .setParameter("name", name)
                .getResultList();
        if ((events == null) || (events.isEmpty()))
            return null;
        return events.get(0);
    }
    
    public static List<PlaceEvent> getAll()
    {
        EntityManager em = jpa.getEntityManager();
        return em.createNamedQuery("PlaceEvent.findAll").getResultList();
    }
    
    
    public static PlaceEvent add(String name, Date startTime,
           String summary, String url, Place place)
    {
        PlaceEvent event = new PlaceEvent();       
        event.setName(name);
        event.setStartTime(startTime);
        event.setSummary(summary);
        event.setUrl(url);
        event.setPlaceId(place);
        place.addEvent(event);
        jpa.create(event);
        return event;
    }
    
}
