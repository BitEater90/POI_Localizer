package poi_localizer.controller.utils;
import poi_localizer.model.User;
import poi_localizer.model.Place;
import poi_localizer.model.*;
import poi_localizer.controller.jpa.*;
import poi_localizer.controller.jpa.exceptions.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.sql.Date;
import java.math.BigDecimal;
import poi_localizer.view.Constants;

/**
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class UserController {
    
    private static UserJpaController jpa = new UserJpaController(HelpingController.getEMF());
    
    private UserController(){}
    
    public static void remove(User user)
    {
        try
        {
            List<Place> places = user.getPlaceList();
            if ((places != null) && !(places.isEmpty()))
            {
                for (Place place : places)
                {
                    place.setUserId(null);
                }
            }
            jpa.destroy(user.getUserId());
            user = null;
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Nie znaleziono obiektu użytkownika");
        }
        catch(IllegalOrphanException ioe)
        {
            System.err.println("Nieprawidłowy obiekt podrzędny");
        }
    }
    
    public static boolean userExists(String login)
    {
        return (jpa.getEntityManager()
                .createNamedQuery("User.findByLogin", User.class)
                .setParameter("login", login).getResultList().size() > 0);
    }    
    
    public static boolean mailExists(String mail)
    {
        return (jpa.getEntityManager()
                .createNamedQuery("User.findByMail", User.class)
                .setParameter("mail", mail).getResultList().size() > 0);     
    }
    
    public static boolean checkIfUserExists(int id)
    {
        List<User> users = jpa.getEntityManager()
                .createNamedQuery("User.findByUserId", User.class)
                .getResultList();
        return (users.size() == 1);
    }
    
    public static void logout(User user)
    {
        EntityManager em = jpa.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        if (transaction != null)
        {
            transaction.begin();
            String query = "DELETE FROM temp_connections WHERE user_id="+user.getUserId();
            Query queryObject = em.createNativeQuery(query);
            queryObject.executeUpdate();
            transaction.commit();
        }
        
    }
    
    public static UserReturner login(String login, String pass)
    {
        List<User> users = jpa.getEntityManager()
               .createNamedQuery("User.findByLoggingData", User.class)
               .setParameter("login", login)
               .setParameter("pass", pass)
               .getResultList();
        if (!users.isEmpty())
        {
            
            UserReturner returner = null;
           //zapisz rekord logowania
            User user = users.get(0);
            
            EntityManager em = jpa.getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            
            String query = "SELECT TIMESTAMPDIFF(MINUTE,log_time, NOW()) AS minutes_elapsed";
            query += " FROM temp_connections WHERE user_id="+user.getUserId();
            Query queryObject = em.createNativeQuery(query);
            List<Long> minutes_elapsed = queryObject.getResultList(); 
            
            String insertQuery = "INSERT INTO temp_connections VALUES(NULL, ";
            insertQuery += user.getUserId()+", ";
            insertQuery += "RAND() * ( POW(10, 21)-1 - POW(10, 20) ) + POW(10, 20), ";
            insertQuery += "CURRENT_TIMESTAMP()";
            insertQuery += ")";
                                  
            
            if (minutes_elapsed.isEmpty())
            {
                queryObject = em.createNativeQuery(insertQuery);
                queryObject.executeUpdate();
                
            }
            else
            {
                Long minObject = minutes_elapsed.get(0);
                long min = minutes_elapsed.get(0).longValue();

                if (min > Constants.MAX_CONNECTION_TIME)
                {
                    query = "DELETE FROM temp_connections WHERE user_id="+user.getUserId();
                    queryObject = em.createNativeQuery(query);
                    queryObject.executeUpdate();
                    
                    queryObject = em.createNativeQuery(insertQuery);
                    queryObject.executeUpdate();

                }
            }
            
            query = "SELECT connection_hash FROM temp_connections WHERE user_id="+user.getUserId();
            queryObject = em.createNativeQuery(query);
            List<BigDecimal> hashTags = queryObject.getResultList();
            BigDecimal hashTag = hashTags.get(0);
            returner = new UserReturner(user, hashTag);

            transaction.commit();
            
            return returner;
        }
        else
            return null;
    }
    
    public static User edit(User user, String login, String password, String mail, String name,
            String surname, boolean isActive)
    {
        try
        {
            user.setLogin(login);
            user.setPass(password);
            user.setMail(mail);
            user.setName(name);
            user.setSurname(surname);
            user.setIsActive(isActive);
            jpa.edit(user);
        }
        catch(NonexistentEntityException nee)
        {
            System.err.println("Edycja użytkownika nie powiodła się. Obiekt nie istnieje.");
        }
        catch(Exception e)
        {
            System.err.println("Nieznany błąd.");
        }
        
        return user;
    }
    
    public static User get(String login)
    {
        EntityManager em = jpa.getEntityManager();
        List<User> users = em.createNamedQuery("User.findByLogin").setParameter("login", login).getResultList();
        if ((users == null) || users.isEmpty())
            return null;
        return users.get(0);
    }
    
    public static User get(int id)
    {
        EntityManager em = jpa.getEntityManager();
        List<User> users = em.createNamedQuery("User.findByUserId")
                .setParameter("userId", id).getResultList();
        if ((users == null) || users.isEmpty())
            return null;
        return users.get(0);
    }
    
    public static List<User> getAll()
    {
        EntityManager em = jpa.getEntityManager();
        return em.createNamedQuery("User.findAll").getResultList();
    }
    
    public static User add(String login, String password, String mail, String name,
            String surname, boolean isActive)
    {
        User user = new User();
        user.setLogin(login);
        user.setPass(password);
        user.setMail(mail);
        user.setName(name);
        user.setSurname(surname);
        user.setIsActive(isActive);
        Date registrationDate = Timer.getDate();
        user.setRegistrationDate(registrationDate);        
        
        jpa.create(user);
        return user;
    }
    
}
