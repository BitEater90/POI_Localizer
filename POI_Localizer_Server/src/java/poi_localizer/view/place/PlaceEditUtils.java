package poi_localizer.view.place;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import poi_localizer.controller.jpa.*;
import poi_localizer.controller.utils.HelpingController;
import poi_localizer.controller.utils.UserController;
import poi_localizer.model.Place;
import poi_localizer.model.User;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceEditUtils {
    
     //private static EntityManagerFactory emf;// = HelpingController.getEMF();
     public static PlaceJpaController jpa = new PlaceJpaController(HelpingController.getEMF());
      
//     public static void setEntityManagerFactory(EntityManagerFactory f)
//     {
//         emf = f;
//     }
     
     private static EntityManager getEntityManager()
     {
         return jpa.getEntityManager();
         //return emf.createEntityManager();
     }
             
    public static boolean checkName(String name)
    {
        return (name.length() >= 3);
    }
    
    public static boolean checkVicinity(String vicinity)
    {
        return (vicinity.length() >= 5);
    }
    
    public static boolean checkWebsite(String website)
            throws Utils.NoParameterException
    {
        if (website.length() == 0)
            throw new Utils.NoParameterException();
        website = website.toLowerCase();
        return website.matches("^(http\\:\\/\\/)?(www\\.)?([a-z]?[a-z0-9]+\\.)+[a-z]{2,3}$");
    }
        
    public static boolean checkLat(float lat)
    {
        return ((lat >=-90.0) && (lat <= 90.0));
    }
    
    public static boolean checkLng(float lng)
    {
        return ((lng >=-180.0) && (lng <= 180.0));
    }
            
    public static String makeUrl(String cid)
    {
        String url = "http://maps.google.com/maps/place?cid=";
        if (cid.length() == 0)
            return null;
        if (cid.matches("^[1-9][0-9]{19}$"))
        {
            return url + cid;
        }
        else
            return null;
    }
    
    public static boolean checkFormattedPhoneNumber(String number)
            throws Utils.NoParameterException
    {
        if (number.length() == 0)
            throw new Utils.NoParameterException();
        return number.matches("^(\\(0[1-9][0-9]?\\)\\s)?"
                    + "("
                    + "([1-9][0-9]{3}(\\s)?[1-9][0-9]{3})"
                    + "|"
                    + "([1-9][0-9]{2}(\\s)?[1-9][0-9](\\s)?[1-9][0-9])"
                    + "|"
                    + "([1-9][0-9]{2}(\\s)?[0-9]{3}(\\s)?[0-9]{3})"
                    + ")$");
    }

    public static boolean checkFormattedAddress(String address)
            throws Utils.NoParameterException
    {
        if (address.length() == 0)
                throw new Utils.NoParameterException();
        address = address.toLowerCase();
        return address.matches("^"
                + "^((ul\\.|al\\.|aleje|ulica)\\s([a-z]{3,}\\s){1,2}[1-9][0-9]{0,4}[a-z]?\\,\\s[a-z]{3,}(\\s[a-z]{3,})*(\\,\\s[a-z]{3,})?)"
                + "|"
                + "([1-9][0-9]*\\s([a-z]{2,})(\\s[a-z]{2,}){0,2}\\,\\s[a-z]{2,}(\\s[a-z]{2,}){0,2}\\,\\s[a-z]{3,})"
                + "$");
    }
    
    public static boolean checkPlaceOpenNow(String openN)
    {
        if ((openN == null) || (!openN.equals("t")))
            return false;
        else
            return true;
    }
   
    public static boolean checkInternationalPhoneNumber(String number)
            throws Utils.NoParameterException
    {
        if (number.length() == 0)
                throw new Utils.NoParameterException();
        number = "+"+number;
        return number.matches("^\\+[1-9][0-9]?\\s[1-9][0-9]{0,2}\\s"
                + "("
                + "([1-9][0-9]{3}(\\s)?[1-9][0-9]{3})"
                + "|"
                + "([1-9][0-9]{2}(\\s)?[1-9][0-9](\\s)?[1-9][0-9])"
                + "|"
                + "([1-9][0-9]{2}(\\s)?[0-9]{3}(\\s)?[0-9]{3})"
                + ")$");
    }
    
    public static boolean checkPriceLevel(Place.PriceLevel priceLevel)
    {
        return (
                 (priceLevel == Place.PriceLevel.FREE) || 
                 (priceLevel == Place.PriceLevel.INEXPENSIVE)|| 
                 (priceLevel == Place.PriceLevel.MODERATE) ||
                 (priceLevel == Place.PriceLevel.EXPENSIVE) || 
                 (priceLevel == Place.PriceLevel.VERY_EXPENSIVE)
                );
    }
    
    public static Place.PriceLevel makePriceLevel(String priceLevelStr)
    {
        try
        {
            Place.PriceLevel priceLevel = Place.PriceLevel.valueOf(priceLevelStr.toUpperCase());
            if (checkPriceLevel(priceLevel))
                return priceLevel;
            else
                return null;
        }
        catch(NullPointerException npe)
        {
            return null;
        }
    }
    
    public static boolean checkUtcOffset(short utcOffset)
    {
        return ((utcOffset >= 0) && (utcOffset <= 23));
    }
    
    public static User checkIfUserLogged(HttpServletRequest req,
            PrintWriter out)
    {
        try
        {
            String connectionHash = req.getParameter(Constants.Request.User.CONNECTION_HASH);
            String query = "SELECT IF(TIMESTAMPDIFF(MINUTE,log_time, NOW())<=30, user_id, -1)"
                    + " AS logging_result FROM temp_connections WHERE connection_hash = " + connectionHash;
            EntityManager em = getEntityManager();
            Query queryObject = em.createNativeQuery(query);
            List<Long> resultList = queryObject.getResultList(); 
            if (resultList.isEmpty())
            {
                out.println(Constants.Response.NOT_LOGGED);
                return null;
            }
            else
            {
                long result = resultList.get(0);
                if (result != -1)
                {
                    int userId = (int)result;
                    User user = UserController.get(userId);
                    return user;
                }
                else
                {
                    out.println(Constants.Response.NOT_LOGGED);
                    return null;
                }
                
            }
            
        }
        catch(NullPointerException npe)
        {
            out.println(Constants.Response.NOT_LOGGED);
            return null;
        }
    }
    
    private PlaceEditUtils(){}
        
}
