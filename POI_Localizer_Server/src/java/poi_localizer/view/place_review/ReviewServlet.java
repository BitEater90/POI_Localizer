package poi_localizer.view.place_review;
import poi_localizer.view.place.PlaceEditUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;
import poi_localizer.model.User;


/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class ReviewServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try
        {
            int action = Utils.getParameter(req, Constants.Request.ACTION);
            PrintWriter out = res.getWriter();
            
            if (action == Constants.Request.Place.Review.Action.SELECT)
            {
                req.getRequestDispatcher("/place/review/select").forward(req, res);
                return;
            }
            else if (action == Constants.Request.Place.Review.Action.ADD)
            {
                req.getRequestDispatcher("/place/review/add").forward(req, res);
                return;                
            }
            
            User user = PlaceEditUtils.checkIfUserLogged(req, out);
            if (user == null) {
                out.println(Constants.Response.NOT_LOGGED);
                return;
            }
            if (action == Constants.Request.Place.Review.Action.REMOVE)
            {
                req.getRequestDispatcher("/place/review/remove").forward(req, res);
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
