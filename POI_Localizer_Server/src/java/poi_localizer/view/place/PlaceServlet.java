package poi_localizer.view.place;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceServlet extends HttpServlet {
   
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
            
    {
        try
        {
            int action = Utils.getParameter(req, Constants.Request.ACTION);
            PrintWriter out = res.getWriter();
            
            if (action == Constants.Request.Place.Action.SELECT)
            {
                req.getRequestDispatcher("/place/select").forward(req, res);
                return;
            }         

            if (action == Constants.Request.Place.Action.ADD)
            {
                req.getRequestDispatcher("/place/add").forward(req, res);
                return;                
            }
            else if (action == Constants.Request.Place.Action.EDIT)
            {
                req.getRequestDispatcher("/place/edit").forward(req, res);
                return;                
            }
            else if (action == Constants.Request.Place.Action.REMOVE)
            {
                req.getRequestDispatcher("/place/remove").forward(req, res);
                return;
            }
        }
        catch(Utils.NoParameterException npe)
        {
            res.setContentType("text/plain");
            PrintWriter out = res.getWriter();
            out.println(Constants.Response.NULL_ACTION);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
