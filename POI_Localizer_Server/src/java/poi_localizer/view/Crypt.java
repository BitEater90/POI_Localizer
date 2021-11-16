package poi_localizer.view;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

/**
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class Crypt {
    
    public enum KeySize { 
        
        SIZE_1024(1024), SIZE_2048(2048), SIZE_4096(4096); 
        
        private int value;
                
        private KeySize(int value)
        {
            this.value = value;
        }
    };
    
    private final static String ALGORITHM = "RSA";
    private int size;
    
    private boolean buildPublicKey()
    {
        try
        {
            byte[] bytes = new byte[]{
                48, -126, 2, 118, 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 4, -126,
                2, 96, 48, -126, 2, 92, 2, 1, 0, 2, -127, -127, 0, -91, 92, -115, 11, -88, 67, 31, -86, 9,
                -85, 82, -11, -94, 7, -98, 30, 36, 5, -26, -23, -17, 87, -83, 116, 89, 22, 53, -26, 114,
                26, 93, 85, 105, -56, -76, -80, -77, 23, 3, -17, 65, -64, -92, -76, 77, 86, -107, 113,
                -56, -5, 14, -3, -41, 106, 13, 104, 124, -89, 121, -81, -79, 73, -43, -127, -52, -94,
                78, 64, 82, -84, -105, 24, 123, -45, -10, -30, 45, 74, 104, -98, 103, -52, -112, -103,
                5, -91, 82, 3, 63, -113, -48, -13, -89, 100, 54, 1, -81, 8, 88, -20, 127, 28, -119, -128,
                15, -73, 122, 79, -68, -76, -12, -123, 51, -126, -100, 12, -128, -120, 99, -18, -44, -16,
                79, -88, 82, 36, -59, 15, 2, 3, 1, 0, 1, 2, -127, -127, 0, -126, -86, -10, -67, -114, 113,
                -76, 52, -66, 61, 70, 105, -39, 102, -95, -104, 121, -88, -68, 70, -127, 29, -52, 111,
                -46, -15, -51, 23, -45, 116, 103, -22, 85, -108, 6, -52, 86, 72, 74, -112, -90, 105, -98,
                -4, -26, -36, 19, 109, 125, 126, 125, -29, 50, 116, -104, 49, -57, -58, -29, 78, 101, 14,
                -50, 9, -30, 74, 52, -85, 10, 28, 55, -29, -80, -9, -49, 3, 30, -80, 121, -76, 56, 81, 119,
                54, -4, 108, 4, 122, -21, 38, -108, -25, 57, 92, 91, 93, -38, -82, 35, -50, -117, -78, 117,
                96, 81, -116, 92, 101, -41, -23, -124, -128, 36, 42, 115, 6, 10, -67, 90, 47, -88, -66, 116,
                -19, 93, 74, 70, -55, 2, 65, 0, -21, -88, 72, 65, -84, 75, -115, 19, -103, -37, 69, 47, 119,
                111, 35, -18, 115, -102, 38, 72, 77, 91, -5, -59, -49, 121, 18, -52, -75, 1, -35, 62, -9,
                -125, -105, 82, -84, -117, 38, -123, 79, 82, 64, -82, 49, -29, 63, -102, 105, -16, 118,
                -33, -109, -65, 105, 83, 14, -45, 15, -110, -74, -64, -70, -35, 2, 65, 0, -77, -94, -46,
                -57, -39, 108, 45, 43, -25, -16, -58, 103, -15, -83, 125, -67, 95, -6, 82, 28, 77, 121,
                -36, 109, 71, -5, 45, -76, 69, 46, -105, 105, 73, 116, -103, 91, -38, -1, 11, 71, -13,
                -85, 78, -111, -25, -83, -127, 51, -90, 17, -25, -113, 56, 29, 77, 116, 32, -10, 77, -79,
                -69, 32, -14, -37, 2, 64, 22, 73, 101, -4, 0, 5, 109, 36, 43, 106, -41, 82, 51, -29, 104, -20,
                -122, 54, 79, -28, -118, -100, 102, 101, 64, -57, -36, -123, 75, -95, 15, -127, -126, -5,
                8, -10, -94, -90, -50, 79, -47, 62, -96, 43, 89, 64, 122, -91, 105, -37, 122, 2, -113, 0,
                -115, 63, 115, -87, 118, 81, -109, -61, 10, 105, 2, 64, 29, 86, 55, 20, 58, 86, -25, 93,
                -89, 72, 116, 46, -97, -110, -108, -39, 107, -90, 70, -24, -90, 98, -125, 116, 57, 112,
                52, -5, -98, -36, -56, 15, -126, -42, -113, 1, 40, 87, -60, -80, -32, -125, -106, -106,
                79, 126, -49, 125, -18, -34, 55, -79, -11, 25, 28, 0, 30, 45, -5, 108, 78, 66, 72, 71, 2, 64,
                109, -85, 2, 25, -46, 78, 50, -25, 34, 113, 110, -126, -84, 0, 115, 54, -2, -23, -1, 112, -98,
                67, -13, 91, -39, 113, -100, 103, -44, 74, -70, 73, -70, -76, 96, 117, 26, -70, -112, 87, -101,
                -11, -23, 114, -71, -102, 4, 46, 37, 40, 83, 86, -80, -49, 40, 33, 23, -105, 52, 88, -55, 8, -122, 26};

            publicKey = 
                    KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(bytes));
            return true;
        }
        catch(InvalidKeySpecException ikse)
        {
            return false;
        }
        catch(NoSuchAlgorithmException nsae)
        {
            return false;
        }
    }
    
    private boolean buildPrivateKey()
    {
        try
        {
            byte[] bytes = new byte[]{
                48, -127, -97, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -127,
                -115, 0, 48, -127, -119, 2, -127, -127, 0, -91, 92, -115, 11, -88, 67, 31,
                -86, 9, -85, 82, -11, -94, 7, -98, 30, 36, 5, -26, -23, -17, 87, -83, 116, 89,
                22, 53, -26, 114, 26, 93, 85, 105, -56, -76, -80, -77, 23, 3, -17, 65, -64, -92,
                -76, 77, 86, -107, 113, -56, -5, 14, -3, -41, 106, 13, 104, 124, -89, 121, -81,
                -79, 73, -43, -127, -52, -94, 78, 64, 82, -84, -105, 24, 123, -45, -10, -30, 45,
                74, 104, -98, 103, -52, -112, -103, 5, -91, 82, 3, 63, -113, -48, -13, -89, 100,
                54, 1, -81, 8, 88, -20, 127, 28, -119, -128, 15, -73, 122, 79, -68, -76, -12,
                -123, 51, -126, -100, 12, -128, -120, 99, -18, -44, -16, 79, -88, 82, 36, -59,
                15, 2, 3, 1, 0, 1
            };

            privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(bytes));
            return true;
        }
        catch(InvalidKeySpecException ike)
        {
            return false;
        }
        catch(NoSuchAlgorithmException nsae)
        {
            return false;
        }
    }
    
    public Crypt()
    {        
    }
    
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    public PublicKey getPublicKey()
    {
        return publicKey;
    }
    
    public PrivateKey getPrivateKey()
    {
        return privateKey;
    }
    
    public String getPublicKeyString()
    {
        return new String(publicKey.getEncoded());
    }
    
    public String getPrivateKeyString()
    {
        return new String(privateKey.getEncoded());
    }
    
    public String decrypt(byte[] text, PrivateKey key)
    {
        String str = null;
        byte[] decryptedText = null;
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(text);
            str = new String(decryptedText);
        }
        catch (InvalidKeyException ike)
        {
            System.out.println(Messages.INVALID_KEY);
        }
        catch (IllegalBlockSizeException ibse)
        {
            System.out.println(Messages.ILLEGAL_BLOCK_SIZE);
        }
        catch(BadPaddingException bpe)
        {
            bpe.printStackTrace();
        }
        catch (NoSuchPaddingException nspe)
        {
            nspe.printStackTrace();
        }
        catch(NoSuchAlgorithmException nsae)
        {
            System.out.println(Messages.NO_SUCH_ALGORITHM);
        }
        finally
        {
            return str;
        }
    }
     
    public byte[] encrypt(String text, PublicKey key)
    {
        byte[] cipherBlocks = null;
        
        try
        {
            Cipher cipher = Cipher.getInstance(ALGORITHM); 
            try
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                cipherBlocks = cipher.doFinal(text.getBytes());
            }
            catch(BadPaddingException bpe)
            {
                bpe.printStackTrace();
            }
            catch(IllegalBlockSizeException ibse)
            {
                System.out.println(Messages.ILLEGAL_BLOCK_SIZE);
            }
            catch(InvalidKeyException ike)
            {
                System.out.println(Messages.INVALID_KEY);
            }
        }
        catch(NoSuchPaddingException nspe)
        {
            nspe.printStackTrace();
        }
        catch(NoSuchAlgorithmException nsae)
        {
            System.out.println(Messages.NO_SUCH_ALGORITHM);
        }
        finally
        {
            return cipherBlocks;
        }
    }

}
