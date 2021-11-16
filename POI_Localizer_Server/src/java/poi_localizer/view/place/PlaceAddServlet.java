package poi_localizer.view.place;
import poi_localizer.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.RollbackException;
import java.util.Date;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.controller.utils.PlaceTypeController;
import poi_localizer.model.PlaceType;
import poi_localizer.model.Place;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceAddServlet extends HttpServlet {
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
               
        //sprawdzenie czy użytkownik jest zalogowany
        User user = PlaceEditUtils.checkIfUserLogged(req, out);
        if (user == null)
        {
            out.println(Constants.Response.NOT_LOGGED);
            return;
        }
        
        try
        {
            String name = Utils.unfloor(req.getParameter(Constants.Request.Place.Addition.NAME));        
            if (!PlaceEditUtils.checkName(name))
            {
                out.println(Constants.Response.Place.Addition.NAME_TOO_SHORT);
                return;
            }
            String vicinity = Utils.unfloor(req.getParameter(Constants.Request.Place.Addition.VICINITY));        
            if (!PlaceEditUtils.checkVicinity(vicinity))
            {
                out.println(Constants.Response.Place.Addition.VICINITY_NAME_TOO_SHORT);
                return;                
            }
                        
            String formattedPhoneNumber = Utils.unfloor(
                    req.getParameter(Constants.Request.Place.Addition.FORMATTED_PHONE_NUMBER));        
            if (!PlaceEditUtils.checkFormattedPhoneNumber(formattedPhoneNumber))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_PHONE_NUMBER);
                return;
            }

            String website = req.getParameter(Constants.Request.Place.Addition.WEBSITE);        
            if (!PlaceEditUtils.checkWebsite(website))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_WEBSITE);
                return;
            }
            
            //userId wzięte od zalogowanego użytkownika
            
            //creationTime <- aktualny czas
            Date dt = new Date();
            long creationTime = dt.getTime();
            
            String formattedAddress = Utils.unfloor(
                    req.getParameter(Constants.Request.Place.Addition.FORMATTED_ADDRESS));     
            
            if (!PlaceEditUtils.checkFormattedAddress(formattedAddress))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_ADDRESS);
                return;
            }
                        
            float lat = Utils.getParameterFloat(req, Constants.Request.Place.Addition.LAT);
            if (!PlaceEditUtils.checkLat(lat))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_LAT);
                return;
            }
            
            float lng = Utils.getParameterFloat(req, Constants.Request.Place.Addition.LNG);
            if (!PlaceEditUtils.checkLng(lng))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_LNG);
                return;
            }
            
            //rating powinien byc ustalony osobno, na podstawie
            //ocen wystawionych przez uzytkownikow
            
            String internationalPhoneNumber = Utils.unfloor(
                    req.getParameter(Constants.Request.Place.Addition.INTERNATIONAL_PHONE_NUMBER));        
            if (!PlaceEditUtils.checkInternationalPhoneNumber(internationalPhoneNumber))
            {
                out.println(Constants.Response.Place
                        .Addition.INCORRECT_INTERNATIONAL_PHONE_NUMBER);
                return;
            }
            
            String priceLevelStr = req.getParameter(Constants.Request.Place.Addition.PRICE_LEVEL);
            if (priceLevelStr == null)
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_PRICE_LEVEL);
                return;
            }
            
            priceLevelStr = priceLevelStr.toUpperCase();
            Place.PriceLevel priceLevel = Place.PriceLevel.valueOf(priceLevelStr);
                        
//            Place.PriceLevel priceLevel = 
//                Place.PriceLevel.valueOf(req.getParameter(Constants.Request.Place.Addition.PRICE_LEVEL).toUpperCase());
            if (!PlaceEditUtils.checkPriceLevel(priceLevel))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_PRICE_LEVEL);
                return;
            }
            
            short utcOffset = (short)Utils.getParameter(req, Constants.Request.Place.Addition.UTC_OFFSET);
            if (!PlaceEditUtils.checkUtcOffset(utcOffset))
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_UTC_OFFSET);
                return;                
            }
            
            int typeId = 0;
            try
            {
                typeId = Utils.getParameter(req, Constants.Request.Place.Addition.TYPE_ID);
            }
            catch(Utils.NoParameterException npe1)
            {
                out.println(Constants.Response.Place.Addition.INCORRECT_TYPE_ID);
                return;
            }
                                    
            PlaceType type = PlaceTypeController.get((short)typeId);          
            
//            public static Place add(String name, float lat, float lng, String formattedAddress,
//            String formattedPhoneNumber, String internationalPhoneNumber,
//            Place.PriceLevel priceLevel, float rating,
//            PlaceType type, String url, User user,
//            short utcOffset, String vicinity, String website)
            Place place = PlaceController.add(name, lat, lng, formattedAddress, formattedPhoneNumber,
                    internationalPhoneNumber, priceLevel, (float)0.0, type, user, utcOffset,
                    vicinity, website);     
            
            if (place == null)
            {
                out.println(Constants.Response.Place.Addition.PLACE_NOT_ADDED);
                return;
            }
            else
            {
                out.println(Constants.Response.Place.Addition.PLACE_ADDED);
                return;
            }
            
        }
        catch(NullPointerException npe2)
        {
            out.println(Constants.Response.INPUT_DATA_ERROR);
            return;
        }
        catch(Utils.NoParameterException npe3)
        {
            out.println(Constants.Response.INPUT_DATA_ERROR);
            return;
        }
        catch(RollbackException re)
        {
            out.println(Constants.Response.Place.Addition.PLACE_DUPLICATE);
            return;
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
