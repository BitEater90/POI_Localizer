package poi_localizer.view;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.security.PrivateKey;
import poi_localizer.controller.utils.UserController;
import poi_localizer.model.User;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class Utils {
    
    private Utils(){}
    
   
    public static Image openImage(String source)
    {
        File file = new File(source);
        if (!file.exists())
            return null;
        if ((!file.isFile()) || (!file.canRead()))
            return null;
        try
        {
            Image image = ImageIO.read(file);
            return image;
        }
        catch(IOException ioe)
        {
            return null;
        }
    }
    
    public static String serializeImageFromSource(String source)
    {
        if (source == null)
            return null;
        Image image = openImage(source);
        if (image == null)
            return null;
        String str = Serializer.serialize(image);
        return str;
    }
    
    public static void deletePhotoFile(File file)
    {
        if (file.exists() && file.isFile())
        {
            String path = file.getAbsolutePath();
            path = path.substring(0, path.lastIndexOf("\\"));
            File placeDir = new File(path);
            file.delete();
            if (placeDir.isDirectory() && (placeDir.listFiles().length == 0))
            {
                placeDir.delete();
            }
        }
    }
        
    public static User checkIfUserLogged(HttpServletRequest req,
            PrintWriter out)
    {
        HttpSession session = req.getSession();
        try
        {
            String userIdAttr = session.getAttribute("userId").toString();
            if (userIdAttr == null)
            {
                out.println(Constants.Response.NOT_LOGGED);
                return null;
            }

            int userId = Integer.valueOf(userIdAttr);
            User user = UserController.get(userId);
            if (user == null)
            {
                out.println(Constants.Response.NOT_LOGGED);
                return null;
            }
            return user;
        }
        catch(NullPointerException npe)
        {
            out.println(Constants.Response.NOT_LOGGED);
            return null;
        }
    }
    
    public static class NoParameterException extends Exception
    {
        public NoParameterException(){}
        
        @Override
        public String toString() {
            return "NoParameterException{" + '}';
        }
    }
    
    public class NoCriteriaException extends Exception
    {
        @Override
        public String toString()
        {
            return "NoCriteriaException{" + '}';
        }
    }
    
    public static File getPlaceDir(String dir)
    {
        File placeDir = new File(dir);
        if (!placeDir.exists())
        {
            if (placeDir.mkdir())
            {
                return placeDir;
            }
            else
                return null;
        }
        return placeDir;
    }
    
    
    
    private static String decryptParameter(String bytesValue)
    {
        Crypt crypt = new Crypt();

        //konwersja zaszyfrowanego parametru na tablicę bajtów
        byte[] bytes = ParamCoder.decodeByteArray(bytesValue);
        PrivateKey key = crypt.getPrivateKey();
        String decryptedParam = crypt.decrypt(bytes, key);
        return decryptedParam;
    }
    
    public static float getParameterFloat(HttpServletRequest req, String param)
            throws NoParameterException
    {
        try
        {
            String bytesValue =  req.getParameter(param);  
            if (bytesValue.indexOf(",") >= 0)
            {
                bytesValue = bytesValue.replace(',', '.');
            }
           // String decryptedParam = Utils.decryptParameter(bytesValue);                        
            return Float.valueOf(bytesValue);
        }
        catch(NullPointerException npe)
        {
            throw new NoParameterException();
        }
        catch(NumberFormatException nfe)
        {
            throw new NoParameterException();
        }
    }
   
    public static double getParameterDouble(HttpServletRequest req, String param)
            throws NoParameterException
    {
        try
        {
            String bytesValue =  req.getParameter(param);  
            if (bytesValue.indexOf(",") >= 0)
            {
                bytesValue = bytesValue.replace(',', '.');
            }
            return Double.valueOf(bytesValue);
        }
        catch(NullPointerException npe)
        {
            throw new NoParameterException();
        }
        catch(NumberFormatException nfe)
        {
            throw new NoParameterException();
        }
    }
    
    public static  int getParameter(HttpServletRequest req, String param)
            throws NoParameterException
    {
        try
        {
            String bytesValue =  req.getParameter(param);  
            /*String decryptedParam = Utils.decryptParameter(bytesValue);    
            return Integer.valueOf(decryptedParam);*/
            return Integer.valueOf(bytesValue);
        }
        catch(NullPointerException npe)
        {
            throw new NoParameterException();
        }
        catch(NumberFormatException nfe)
        {
            throw new NoParameterException();
        }
    }
    
    public static String unfloor(String str)
    {
        return str.replace("_", " ");
    }
}
