package poi_localizer.controller.utils;
import poi_localizer.model.PlaceType;
import poi_localizer.model.*;
import poi_localizer.controller.jpa.*;
import javax.persistence.*;
import poi_localizer.controller.jpa.exceptions.*;
import java.util.List;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceTypeController {

    private static PlaceTypeJpaController jpa = new PlaceTypeJpaController(HelpingController.getEMF());
        
    public static void remove(PlaceType type)
    {
        try
        {
            jpa.destroy(type.getTypeId());
        }
        catch(IllegalOrphanException ioe)
        {
            System.err.println("Nieprawidłowy obiekt podrzędny");
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Nie znaleziono obiektu typu miejsca");
        }
    }
    
    public static PlaceType edit(PlaceType type, String name, String namePl)
    {
        try
        {
            type.setName(name);
            type.setNamePl(namePl);
            jpa.edit(type);
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Edycja typu miejsca nie powiodła się. Obiekt nie istnieje.");
        }
        catch(Exception e)
        {
            System.err.println("Nieznany błąd.");
        }
        
        return type;
    }
    
    public static PlaceType get(short id)
    {
        EntityManager em = jpa.getEntityManager();
        return em.find(PlaceType.class, id);
    }
    
    public static PlaceType get(String name)
    {
        EntityManager em = jpa.getEntityManager();
        List<PlaceType> types = em.createNamedQuery("PlaceType.findByName").setParameter("name", name).getResultList();
        if ((types == null) || types.isEmpty())
            return null;
        return types.get(0);
    }
    
    public static List<PlaceType> getAll()
    {
        EntityManager em = jpa.getEntityManager();
        return em.createNamedQuery("PlaceType.findAll").getResultList();
    }
    
    public static PlaceType add(String name, String namePl)
    {
        PlaceType type = new PlaceType();       
        type.setName(name);
        type.setNamePl(namePl);
        jpa.create(type);
        return type;
    }
    
}
