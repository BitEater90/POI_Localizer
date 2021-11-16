package poi_localizer.view.place_event;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.controller.utils.PlaceEventController;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceEvent;
import poi_localizer.view.Utils;
import poi_localizer.view.Constants;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class EventSelectServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        int placeId = 0;
        try
        {
            placeId = Utils.getParameter(req, Constants.Request.Place.PLACE_ID);
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
            
        int criteria = 0;
        try
        {
            criteria = Utils.getParameter(req, Constants.Request.Place.Event.CRITERIA);
        }
        catch(Utils.NoParameterException npe2)
        {
            out.println(Constants.Response.Place.Event.NO_CRITERIA_SPECIFIED);
            return;
        }
        
        switch(criteria)
        {
            case Constants.Request.Place.Event.Criteria.ALL_EVENTS :
            {
                List<PlaceEvent> events = PlaceEventController.getAllByPlace(place);
                if ((events == null) || (events.isEmpty()))
                {
                    out.println(Constants.Response.Place.Event.NO_EVENTS_FOUND);
                    return;
                }
                out.println(Constants.Response.Place.Event.DATA_ON_MULTIPLE_EVENTS);
                out.print(events.size()+"\t");
                String outString = "";
                for (PlaceEvent event : events)
                {
                    String str = event.toString();
                    outString += str;
                }
                out.println(outString);
                
            } break;
            case Constants.Request.Place.Event.Criteria.NAME :
            {
                String name = req.getParameter(Constants.Request.Place.Event.NAME);
                PlaceEvent event = PlaceEventController.getByName(place, name);
                if (event == null)
                {
                    out.println(Constants.Response.Place.Event.NO_EVENTS_FOUND);
                }
                else
                {
                    out.println(Constants.Response.Place.Event.DATA_ON_EVENT);
                    String str = event.toString();
                    out.println(str);
                }
                
            } break;
            case Constants.Request.Place.Event.Criteria.START_DATE :
            {
                String timeStr = req.getParameter(Constants.Request.Place.Event.START_TIME);
                if ((timeStr == null) || (timeStr.length() == 0))
                {
                    out.println(Constants.Response.Place.Event.NO_START_TIME_CRITERION);
                    return;
                }
                
                try
                {
                    long startTime = Long.parseLong(timeStr);
                    Date requestedTime = new Date(startTime);
                    int day = requestedTime.getDate();
                    int month = requestedTime.getMonth();
                    int year = requestedTime.getYear();

                    String outString = "";
                    List<PlaceEvent> events = PlaceEventController.getEventsByStartDate(place, day, month, year);                         
                    out.println(Constants.Response.Place.Event.DATA_ON_MULTIPLE_EVENTS);
                    out.print(events.size()+"\t");
                    outString = "";
                    for (PlaceEvent event : events)
                    {
                        String str = event.toString();
                        outString += str;
                    }
                    
                    out.println(outString);
                }  
                catch(ClassCastException cce)
                {
                    out.println(Constants.Response.Place.Event.INCORRECT_START_TIME);
                    return;
                }
                
            } break;
            default :;
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
