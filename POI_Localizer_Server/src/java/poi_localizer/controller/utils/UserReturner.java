package poi_localizer.controller.utils;
import poi_localizer.model.User;
import java.math.BigDecimal;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class UserReturner {
    
    private User user;
    private BigDecimal connectionHash;

    public UserReturner(User user, BigDecimal connectionHash) {
        this.user = user;
        this.connectionHash = connectionHash;
    }
    
    public UserReturner()
    {
        this.user = null;
        this.connectionHash = new BigDecimal(0);
    }

    public BigDecimal getConnectionHash() {
        return connectionHash;
    }

    public void setConnectionHash(BigDecimal connectionHash) {
        this.connectionHash = connectionHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    
}
