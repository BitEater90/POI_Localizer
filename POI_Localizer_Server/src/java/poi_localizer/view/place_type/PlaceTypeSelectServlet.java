package poi_localizer.view.place_type;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.model.PlaceType;
import poi_localizer.controller.jpa.*;
import poi_localizer.controller.utils.*;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceTypeSelectServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        List<PlaceType> types = null;
        types = PlaceTypeController.getAll();
        int size = types.size();
        if (size > 0)
        {
            out.println(Constants.Response.Place.Type.DATA_ON_MULTIPLE_TYPES);
            String outputStr = "";
            outputStr += "" + size + "\t";
            
            for (PlaceType type : types)
            {
               String str = type.toString();
               outputStr += str;
            }
            
            out.println(outputStr);
        }
       else
       {
           out.println(Constants.Response.Place.Type.NO_TYPE_FOUND);
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
