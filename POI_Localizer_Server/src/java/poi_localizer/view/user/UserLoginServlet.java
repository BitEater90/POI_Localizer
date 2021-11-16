package poi_localizer.view.user;

import poi_localizer.model.User;
import poi_localizer.controller.utils.UserController;
import poi_localizer.controller.utils.UserReturner;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.view.Constants;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class UserLoginServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        String login = req.getParameter(Constants.Request.User.LOGIN);
        String password = req.getParameter(Constants.Request.User.PASSWORD);
        
        if ((login == null) || (password == null))
        {
            out.println(Constants.Response.INPUT_DATA_ERROR);
            return;
        }
        
        if (login.isEmpty())
        {
            out.println(Constants.Response.User.LOGIN_EMPTY);
        }
        else if (password.isEmpty())
        {
            out.println(Constants.Response.User.PASSWORD_EMPTY);
        }
        else
        {
            UserReturner userReturner = UserController.login(login, password);
            if (userReturner == null)
            {
                out.println(Constants.Response.NOT_LOGGED);
            }
            else
            {
                User user = userReturner.getUser();
                out.println(Constants.Response.User.LOGGED_IN);
                out.println(user.getUserId());
                out.println(userReturner.getConnectionHash());
            }
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
