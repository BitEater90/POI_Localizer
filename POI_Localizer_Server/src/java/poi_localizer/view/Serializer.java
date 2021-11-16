package poi_localizer.view;
import poi_localizer.model.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class Serializer {
    
    private Serializer(){}
    
    public static String serialize(Object object)
    {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            return bo.toString();
        }
        catch(IOException e)
        {
            return null;
        }
    }
    
    public static Object deserialize(String str)
    {
        try
        {
            byte[] bytes = str.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        }
        catch(ClassNotFoundException cnfe)
        {
            return null;
        }
        catch(IOException e)
        {
            return null;
        }
    }
    
}
