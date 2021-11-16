package poi_localizer.view;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;
import poi_localizer.view.Crypt;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class Out {
    
    PrintWriter out;
    
    public Out(HttpServletResponse res)
    {
        try
        {
            out = res.getWriter();
        }
        catch(IOException ioe)
        {
            out = null;
        }
    }
    
    public void println(String text)
    {
        Crypt crypt = new Crypt();
        byte[] array = crypt.encrypt(text, crypt.getPublicKey());
        String byteArray = ParamCoder.codeByteArray(array);
        out.println(byteArray);
    }
            
}
