package poi_localizer.view.user;

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
public class UserServlet extends HttpServlet {
    
      
    @Override 
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        this.service(req, res);
    }
        
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        try
        {
            int action = Utils.getParameter(req, Constants.Request.ACTION);
            if (action == Constants.Request.User.Action.REGISTER)
            {
                req.getRequestDispatcher("/user/register").forward(req, res);
            }
            else if (action == Constants.Request.User.Action.LOGIN)
            {
                req.getRequestDispatcher("/user/login").forward(req, res);
            }
            else if (action == Constants.Request.User.Action.REMOVE)
            {
                req.getRequestDispatcher("/user/remove").forward(req, res);
            }
            else if (action == Constants.Request.User.Action.LOGOUT)
            {
                req.getRequestDispatcher("/user/logout").forward(req, res);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    
        this.service(req, res);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
