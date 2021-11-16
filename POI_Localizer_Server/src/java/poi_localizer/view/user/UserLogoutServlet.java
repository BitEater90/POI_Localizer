package poi_localizer.view.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import poi_localizer.controller.utils.UserController;
import poi_localizer.view.Constants;
import poi_localizer.model.User;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class UserLogoutServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        String userIdString = req.getParameter("user_id");
        if (userIdString == null)
        {
            out.println(Constants.Response.NOT_LOGGED);
            return;
        }
        else
        {
            int userId = 0;
            try
            {
                userId = Integer.parseInt(userIdString);
            }
            catch(NumberFormatException nfe){
                out.println(Constants.Response.NOT_LOGGED);
                return;
            }
            String connectionHash = req.getParameter(Constants.Request.User.CONNECTION_HASH);
            if (connectionHash == null)
            {
                out.println(Constants.Response.NOT_LOGGED);
                return;
            }
            
            String query = "DELETE FROM temp_connections WHERE connection_hash = " + connectionHash;
            User user = UserController.get(userId);
            UserController.logout(user);
            out.println(Constants.Response.User.LOGGED_OUT);
        }
        
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        service(req, res);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        service(req, res);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
