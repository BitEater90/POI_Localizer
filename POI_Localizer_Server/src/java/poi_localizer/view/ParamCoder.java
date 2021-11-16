package poi_localizer.view;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class ParamCoder {
    
    private ParamCoder(){}
    
    public static String codeByteArray(byte[] array)
    {
        int length = array.length;
        String str = "";
        for (int i=0; i<length; i++)
        {
            str += "" + array[i];
            if (i < length-1)
            {
                str += "_";
            }
        }
        return str;
    }
    
    public static byte[] decodeByteArray(String str)
    {
        int length = str.length();
        byte[] bytes = new byte[length];
        String[] strParts = str.split("_");
        
        for (int i=0; i<length; i++)
        {
            bytes[i] = Byte.valueOf(strParts[i]);
        }
        return bytes;
    }           
    
    
}
