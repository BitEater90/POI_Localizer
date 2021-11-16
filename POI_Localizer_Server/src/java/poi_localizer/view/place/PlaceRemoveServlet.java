package poi_localizer.view.place;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.model.User;
import poi_localizer.model.Place;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceRemoveServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();
        
        //sprawdzenie czy użytkownik jest zalogowany
        User user = PlaceEditUtils.checkIfUserLogged(req, out);
        if (user == null)
        {
            out.println(Constants.Response.NOT_LOGGED);
            return;
        }        
                
        int placeId = 0;
        try
        {
            placeId = Utils.getParameter(req, Constants.Request.Place.PLACE_ID);
            if (placeId <= 0)
            {
                out.println(Constants.Response.Place.INCORRECT_PLACE_NUMBER);
                return;
            }
        }
        catch(Utils.NoParameterException npe1)
        {
            out.println(Constants.Response.Place.NO_PLACE_SPECIFIED);
            return;
        }
        Place place = PlaceController.get(placeId);
        if (place == null)
        {
            out.println(Constants.Response.Place.NO_PLACE_FOUND);
            return;
        }
                
        if (place.getUserId().getUserId() != user.getUserId())
        {
            out.println(Constants.Response.Place.USER_IS_NOT_PLACE_AUTHOR);
            return;
        }       
        
        if (PlaceController.removeQuery(place))
        {
            out.println(Constants.Response.Place.PLACE_REMOVED);
        }
        else
        {
            out.println(Constants.Response.Place.PLACE_NOT_REMOVED);
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


}
