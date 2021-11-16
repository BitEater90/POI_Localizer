package poi_localizer.view.place_review;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.persistence.RollbackException;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.controller.utils.PlaceReviewController;
import poi_localizer.controller.utils.UserController;
import poi_localizer.model.User;
import poi_localizer.model.Place;
import poi_localizer.model.PlaceReview;
import poi_localizer.view.Constants;
import poi_localizer.view.Serializer;
import poi_localizer.view.Utils;
import poi_localizer.view.place.PlaceEditUtils;


/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class ReviewAddServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
       
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
                
     
        int placeId = 0;
        try
        {
            placeId = Utils.getParameter(req, Constants.Request.Place.PLACE_ID);
            if (placeId <= 0)
            {
                out.println(Constants.Response.Place.INCORRECT_PLACE_NUMBER);
                return;
            }
        }
        catch(Utils.NoParameterException npe1)
        {
            out.println(Constants.Response.Place.NO_PLACE_SPECIFIED);
            return;
        }
        
        Place place = PlaceController.get(placeId);
        if (place == null)
        {
            out.println(Constants.Response.Place.NO_PLACE_FOUND);
            return;
        } 
        
        String text = req.getParameter(Constants.Request.Place.Review.TEXT);
        if (text == null)
        {
            out.println(Constants.Response.Place.Review.INCORRECT_TEXT);
            return;
        }
        else if (text.length() < 5)
        {
            out.println(Constants.Response.Place.Review.TEXT_TOO_SHORT);
            return;
        }
        else if (text.length() > 200)
        {
            out.println(Constants.Response.Place.Review.TEXT_TOO_LONG);
            return;
        }
        text = Utils.unfloor(text);
        int userId = 0;

        String userIdString = req.getParameter(Constants.Request.Place.Review.USER_ID);
        String authorName = req.getParameter(Constants.Request.Place.Review.AUTHOR_NAME);

        User authorVar = null;
        String authorNameVar = null;

        if (userIdString == null)
        {
            if (authorName == null)
            {
                out.println(Constants.Response.Place.Review.NO_USER_ID);
                return;
            }
            else
            {
                authorName = Utils.unfloor(authorName);
                if ((authorName.length() < 3) || (authorName.length() > 100))
                {
                    out.println(Constants.Response.Place.Review.INCORRECT_AUTHOR_NAME);
                    return;
                }

                authorNameVar = authorName;
            }
        }
        else
        {
            userId = Integer.parseInt(userIdString);
            if (userId <= 0)
            {
                if (authorName == null)
                {
                    out.println(Constants.Response.Place.Review.NO_USER_ID);
                    return;
                }
                else
                {
                    if ((authorName.length() < 3) || (authorName.length() > 100))
                    {
                        out.println(Constants.Response.Place.Review.INCORRECT_AUTHOR_NAME);
                        return;
                    }

                    authorNameVar = authorName;
                }
            }

            User author = UserController.get(userId);
            if (author == null)
            {
                if (authorName == null)
                {
                    out.println(Constants.Response.Place.Review.NO_USER_ID);
                    return;
                }
                else
                {
                    if ((authorName.length() < 3) || (authorName.length() > 100))
                    {
                        out.println(Constants.Response.Place.Review.INCORRECT_AUTHOR_NAME);
                        return;
                    }
                    authorNameVar = authorName;
                }
            }
            else
            {
                authorVar = author;
                authorNameVar = author.getName()+" "+author.getSurname();
            }
        }
       
        String authorUrl = req.getParameter(Constants.Request.Place.Review.AUTHOR_URL);
        if (authorUrl == null)
        {
//            out.println(Constants.Response.Place.Review.NO_AUTHOR_URL_CRITERION);
//            return;
        }
        else
        {
            if ((authorUrl.length() < 5) || (authorUrl.length() > 200))
            {
                out.println(Constants.Response.Place.Review.INCORRECT_AUTHOR_URL);
                return;
            }
            
            try
            {
                if (!PlaceEditUtils.checkWebsite(authorUrl))
                {
                    out.println(Constants.Response.Place.Review.INCORRECT_AUTHOR_URL);
                    return;
                }
            }
            catch(Utils.NoParameterException npe){}
        }
        
        
        String ratingString = req.getParameter(Constants.Request.Place.Review.RATING);
        float rating = (float)0.0;
        
        if (ratingString == null)
        {
            out.println(Constants.Response.Place.Review.NO_RATING_CRITERION);
            return;
        }
        else
        {
            rating = Float.parseFloat(ratingString);
            if (rating <= (float)0.0)
            {
                out.println(Constants.Response.Place.Review.INCORRECT_RATING);
                return;
            }           
        }      
        
        Date reviewTime = new Date();

        try
        {
            PlaceReview review = PlaceReviewController.add(reviewTime, text, authorNameVar,
                    authorUrl, rating, authorVar, place);
            if (review == null)
            {
                out.println(Constants.Response.Place.Review.REVIEW_NOT_ADDED);
                return;
            }
            else
            {
                PlaceReviewController.calculatePlaceRating(place.getPlaceId());
                out.println(Constants.Response.Place.Review.REVIEW_ADDED);
                return;
            }
        }
        catch(RollbackException re)
        {
            out.println(Constants.Response.Place.Review.REVIEW_DUPLICATED);
            return;
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
