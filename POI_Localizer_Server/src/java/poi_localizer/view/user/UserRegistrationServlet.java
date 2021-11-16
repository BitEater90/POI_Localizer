package poi_localizer.view.user;
import poi_localizer.model.*;
import poi_localizer.controller.utils.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.view.Constants;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class UserRegistrationServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
       
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String mail = req.getParameter("mail");
                
        if ((login == null) || (password == null) || (name == null)
                || (surname == null) || (mail == null))
        {
            out.println(Constants.Response.INPUT_DATA_ERROR);
            return;
        }
        
        if (UserController.userExists(login))
        {
            out.println(Constants.Response.User.LOGIN_OCCUPIED);
            return;
        }
        
        if (UserController.mailExists(mail))
        {
            out.println(Constants.Response.User.MAIL_OCCUPIED);
            return;
        }
        
        User user = UserController.add(login, password, mail, name, surname, true);
        if (user != null)
        {
            out.println(Constants.Response.User.USER_ADDED);
        }
        else
        {
            out.println(Constants.Response.User.ERROR_ADDING_USER);
        } 
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    
        service(req, res);
    }
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        service(req, res);
    }
    
    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
