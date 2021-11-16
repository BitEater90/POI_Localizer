package poi_localizer.controller.utils;
import javax.persistence.*;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class HelpingController {
    
    private HelpingController(){}
       
    public static EntityManagerFactory getEMF()
    {
        return Persistence.createEntityManagerFactory("POI_LocalizerPU");
    }
    
    
}
