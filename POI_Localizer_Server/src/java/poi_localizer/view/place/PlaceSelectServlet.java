package poi_localizer.view.place;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceType;
import poi_localizer.controller.utils.*;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceSelectServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        try
        {
          
            short typeId = (short)Utils.getParameter(req, Constants.Request.Place.TYPE);
            PlaceType type = PlaceTypeController.get(typeId);

            if (type == null)
            {
                out.println(Constants.Response.Place.NO_PLACE_TYPE);
                return;
            }

            int criteria = Utils.getParameter(req, Constants.Request
                    .Place.CRITERIA);
            Place place = null;
            List<Place> places = null;
            String vicinity = null;

            switch(criteria)
            {
                case Constants.Request.Place.Criteria.NAME_AND_VICINITY : {

                    vicinity = Utils.unfloor(req.getParameter(Constants.Request.Place.VICINITY));
                    if (vicinity == null)
                    {
                        out.println(Constants.Response.Place.NO_VICINITY_CRITERION);
                        return;
                    }                   
                    
                }
                case Constants.Request.Place.Criteria.NAME : {

                    String name = Utils.unfloor(req.getParameter(Constants.Request.Place.NAME));
                    if (name == null)
                    {
                        out.println(Constants.Response.Place
                                .NO_NAME_CRITERION);
                        return;
                    }
                    
                    boolean exactFit = true;
                    String exactFitS = req.getParameter(Constants.Request.Place.Criteria.Name.EXACT_FIT);
                    if (exactFitS != null)
                    {
                        exactFit = exactFitS.equals("t");
                    }

                    if (exactFit)
                    {
                        if (vicinity == null)
                        {
                            place = PlaceController.get(type, name);
                        }
                        else
                        {
                            place = PlaceController.get(type, name, vicinity);
                        }
                        if (place != null)
                        {
                            String str = place.toStringSimple();
                            out.println(Constants.Response.Place.DATA_ON_PLACE);
                            out.println(str);
                            return;
                        }
                        else
                        {
                            out.println(Constants.Response.Place.NO_PLACE_FOUND);
                        }
                    }
                    else
                    {   
                        if (vicinity == null)
                        {
                            places = PlaceController
                                    .getApprox(type, name);
                        }
                        else
                        {
                            places = PlaceController
                                    .getApprox(type, name, vicinity);
                        }
                        int size = places.size();
                        if (size > 0)
                        {
                            out.println(Constants.Response.Place.DATA_ON_PLACE_APPROX);
                            out.print(size+"\t");
                            for (Place approxPlace : places)
                            {
                                String str = approxPlace.toStringSimple();
                                out.print(str);
                            }
                        }
                        else
                        {
                            out.println(Constants.Response.Place.NO_PLACE_FOUND);
                        }
                    }
                } break;
                case Constants.Request.Place.Criteria.COORDINATES : {

                   String range = req.getParameter(Constants.Request.Place.RANGE);  
                   if (range == null)
                   {
                       out.println(Constants.Response.Place.NO_RANGE_CRITERION);
                       return;
                   }

                   float longitude = Utils.getParameterFloat(req, Constants.Request.Place.LONGITUDE);
                   float latitude = Utils.getParameterFloat(req, Constants.Request.Place.LATITUDE);

                   if (range.equals(Constants.Request.Place.Range.AREA))
                   {
                       float longitude2 = Utils.getParameterFloat(req, Constants.Request.Place.LONGITUDE_2);
                       float latitude2 = Utils.getParameterFloat(req, Constants.Request.Place.LATITUDE_2);

                       float[] lng = new float[2];
                       float[] lat = new float[2];
                       
                       if (longitude < longitude2)
                       {
                           lng[0] = longitude;
                           lng[1] = longitude2;
                       }
                       else
                       {
                           lng[0] = longitude2;
                           lng[1] = longitude;
                       }

                       if (latitude < latitude2)
                       {
                           lat[0] = latitude;
                           lat[1] = latitude2;
                       }
                       else
                       {
                           lat[0] = latitude2;
                           lat[1] = latitude;
                       }
                       
                       List<Place> foundPlaces = PlaceController.getByType(type);
                       places = new ArrayList<Place>();
                       for (Place foundPlace : foundPlaces)
                       {
                           double foundLat = foundPlace.getLat();
                           double foundLng = foundPlace.getLng();

                           if ((foundLat >= lat[0]) && (foundLat <= lat[1]))
                           {
                               if ((foundLng >= lng[0]) && (foundLng <= lng[1]))
                               {
                                   places.add(foundPlace);
                               }
                           }
                       }

                       int size = places.size();
                       if (size > 0)
                       {
                           out.println(Constants.Response.Place.DATA_ON_PLACE_AREA);
                           out.print(size+"\t");
                           for (Place approxPlace : places)
                           {
                               String str = approxPlace.toStringSimple();
                               out.print(str);
                           }
                       }
                       else
                       {
                           out.println(Constants.Response.Place.NO_PLACE_FOUND);
                       }
                   }
                   else if (range.equals(Constants.Request.Place.Range.RADIAL))
                   {
                       double radius = Utils.getParameterDouble(req, Constants.Request.Place.Range.RADIUS);

                       List<Place> foundPlaces = PlaceController.getByType(type);
                       places = new ArrayList<Place>();
                       for (Place foundPlace : foundPlaces)
                       {
                           double foundLat = foundPlace.getLat();
                           double foundLng = foundPlace.getLng();

                           double distance = PlaceController
                                   .calculateDistance(foundLat, foundLng,
                                   latitude, longitude);

                           if (distance <= radius)
                           {
                               places.add(foundPlace);
                           }
                       }

                       int size = places.size();
                       if (size > 0)
                       {
                           out.println(Constants.Response.Place.DATA_ON_PLACE_RADIAL);
                           out.print(size+"\t");
                           for (Place approxPlace : places)
                           {
                               String str = approxPlace.toStringSimple();
                               out.print(str);
                           }
                       }
                       else
                       {
                           out.println(Constants.Response.Place.NO_PLACE_FOUND);
                       }
                   }
                   else if (range.equals(Constants.Request.Place.Range.EXACT))
                   {
                        place = PlaceController.getByTypeAndCoordinates(type, 
                            longitude, latitude);
                        if (place != null)
                        {
                            String str = place.toStringSimple();
                            out.println(Constants.Response.Place.DATA_ON_PLACE);
                            out.println(str);
                        }
                        else
                        {
                            out.println(Constants.Response.Place.NO_PLACE_FOUND);
                        }
                   }
                   else
                   {
                       out.println(Constants.Response.Place.NO_RANGE_CRITERION);
                       return;
                   }
                } break;
                default :;
            }
        }
        catch(Utils.NoParameterException nce)
        {
            out.println(Constants.Response.INPUT_DATA_ERROR);
            return;
        }
    }        
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        service(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        service(req, res);
    }
   
}
