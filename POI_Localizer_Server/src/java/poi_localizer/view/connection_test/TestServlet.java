package poi_localizer.view.connection_test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class TestServlet extends HttpServlet {

@Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        PrintWriter out = res.getWriter();
        String p = req.getParameter("quest");
        if (p == null)
        {
            out.print("Strange_application");
            return;
        }
        if (p.equals("me_mobile"))
        {
            out.print("Servlet_welcomes_you");
        }
        else
        {
            out.print("Strange_application");
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
