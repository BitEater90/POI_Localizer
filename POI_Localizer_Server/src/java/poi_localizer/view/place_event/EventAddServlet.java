package poi_localizer.view.place_event;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.RollbackException;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.controller.utils.PlaceEventController;
import poi_localizer.model.User;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceEvent;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;
import poi_localizer.view.place.PlaceEditUtils;


/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class EventAddServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
       
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
                
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
        
        String name = req.getParameter(Constants.Request.Place.Event.NAME);
        if ((name == null) || (name.length() < 3))
        {
            out.println(Constants.Response.Place.Event.NO_NAME_CRITERION);
            return;
        }
        
        else if (name.length() > 100)
        {
            out.println(Constants.Response.Place.Event.NAME_TOO_LONG);
            return;
        }
        name = Utils.unfloor(name);
        String timeStr = req.getParameter(Constants.Request.Place.Event.START_TIME);
        if ((timeStr == null) || (timeStr.length() ==  0))
        {
            out.println(Constants.Response.Place.Event.NO_START_TIME_CRITERION);
            return;
        }
        
        long startTimeL = Long.parseLong(timeStr);
        Date startTime = new Date();

        try
        {
            startTime.setTime(startTimeL);
            int day = startTime.getDate();
            int month = startTime.getMonth();
            int year = startTime.getYear();
            
            List<PlaceEvent> events = PlaceEventController.getEventsByStartDate(place, day, month, year);
            out.println(Constants.Response.Place.Event.DATA_ON_MULTIPLE_EVENTS);
            out.print(events.size()+"\t");
            for (PlaceEvent event : events)
            {
                String str = event.toString();
                out.print(str);
            }            
                        
        }
        catch(ClassCastException cce)
        {
            out.println(Constants.Response.Place.Event.INCORRECT_START_TIME);
            return;
        }
        
        String summary = null;
        summary = req.getParameter(Constants.Request.Place.Event.SUMMARY);
        if ((summary == null) || (summary.length() < 3))
        {
            out.println(Constants.Response.Place.Event.NO_SUMMARY_CRITERION);
            return;
        }
        else if (summary.length() > 500)
        {
            out.println(Constants.Response.Place.Event.SUMMARY_TO_LONG);
            return;            
        }
        summary = Utils.unfloor(summary);
        String url = null;
        url = req.getParameter(Constants.Request.Place.Event.URL);
        try
        {
            if (url == null)
            {
                throw new Utils.NoParameterException();
            }
            if (!PlaceEditUtils.checkWebsite(url))
            {
                out.println(Constants.Response.Place.Event.INCORRECT_URL);
                return;
            }
                        
        }
        catch(Utils.NoParameterException npe)
        {
            out.println(Constants.Response.Place.Event.NO_URL_CRITERION);
            return;
        }
        
        try
        {
            PlaceEvent event = PlaceEventController.add(name, startTime, summary, url, place);
            if (event == null)
            {
                out.println(Constants.Response.Place.Event.EVENT_NOT_ADDED);
                return;
            }
            else
            {
               out.println(Constants.Response.Place.Event.EVENT_ADDED);
                return;
            }
        }
        catch(RollbackException re)
        {
            out.println(Constants.Response.Place.Event.EVENT_DUPLICATED);
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
