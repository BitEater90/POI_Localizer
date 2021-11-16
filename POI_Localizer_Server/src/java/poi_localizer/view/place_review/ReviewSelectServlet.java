package poi_localizer.view.place_review;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.controller.utils.PlaceReviewController;
import poi_localizer.controller.utils.UserController;
import poi_localizer.model.Place;
import poi_localizer.model.User;
import poi_localizer.model.PlaceReview;
import poi_localizer.view.Utils;
import poi_localizer.view.Constants;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class ReviewSelectServlet extends HttpServlet {

    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        int placeId = 0;
        try
        {
            placeId = Utils.getParameter(req, Constants.Request.Place.PLACE_ID);
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
            
        int criteria = 0;
        try
        {
            criteria = Utils.getParameter(req, Constants.Request.Place.Review.CRITERIA);
        }
        catch(Utils.NoParameterException npe2)
        {
            out.println(Constants.Response.Place.Review.NO_CRITERIA_SPECIFIED);
            return;
        }
        
        switch(criteria)
        {
            case Constants.Request.Place.Review.Criteria.ALL_REVIEWS :
            {
                List<PlaceReview> reviews = PlaceReviewController.getAll();
                if ((reviews == null) || (reviews.isEmpty()))
                {
                    out.println(Constants.Response.Place.Review.NO_REVIEWS_FOUND);
                    return;
                }
                out.println(Constants.Response.Place.Review.DATA_ON_MULTIPLE_REVIEWS);
                String outString = "";
                int size = 0;
                for (PlaceReview review : reviews)
                {
                    if (review.getPlaceId().getPlaceId() != placeId)
                    {
                        continue;
                    }
                    String str = review.toString();
                    outString += str;
                    size++;
                }
                outString = size + "\t" + outString;
                out.println(outString);
                
            } break;
            case Constants.Request.Place.Review.Criteria.PLACE_AND_AUTHOR_NAME :
            {
                String authorName = req.getParameter(Constants.Request.Place.Review.AUTHOR_NAME);
                List<PlaceReview> reviews = PlaceReviewController.getAllByPlaceAndAuthorName(place, authorName);
                if ((reviews == null) || reviews.isEmpty())
                {
                    out.println(Constants.Response.Place.Review.NO_REVIEWS_FOUND);
                }
                else
                {
                    out.println(Constants.Response.Place.Review.DATA_ON_MULTIPLE_REVIEWS);
                    String outString = "";
                    outString += reviews.size()+"\t";
                    for (PlaceReview review : reviews)
                    {
                        String str = review.toString();
                        outString += str;
                    }
                    out.println(outString);
                }
            
            } break;
            case Constants.Request.Place.Review.Criteria.AUTHOR_NAME :
            {
                String authorName = req.getParameter(Constants.Request.Place.Review.AUTHOR_NAME);
                List<PlaceReview> reviews = PlaceReviewController.getAllByAuthorName(authorName);
                if ((reviews == null) || reviews.isEmpty())
                {
                    out.println(Constants.Response.Place.Review.NO_REVIEWS_FOUND);
                }
                else
                {
                    out.println(Constants.Response.Place.Review.DATA_ON_MULTIPLE_REVIEWS);
                    String outString = "";
                    outString += reviews.size()+"\t";
                    for (PlaceReview review : reviews)
                    {
                        String str = review.toString();
                        outString += str;
                    }
                    out.println(outString);
                }
                
            } break;
            case Constants.Request.Place.Review.Criteria.SINGLE :
            {
                String authorName = req.getParameter(Constants.Request.Place.Review.AUTHOR_NAME);
                String reviewTimeString = req.getParameter(Constants.Request.Place.Review.REVIEW_TIME);
                long reviewTimeLong = Long.parseLong(reviewTimeString);
                Date reviewTime = new Date(reviewTimeLong);
                
                PlaceReview review = PlaceReviewController.get(place, authorName, reviewTime);

                if (review == null)
                {
                    out.println(Constants.Response.Place.Review.NO_REVIEWS_FOUND);
                }
                else
                {
                    out.println(Constants.Response.Place.Review.DATA_ON_REVIEW);
                    out.println(review.toString());
                        
                }
            
            } break;    
            case Constants.Request.Place.Review.Criteria.PLACE_AND_USER :
            {
                String userIdString = req.getParameter(Constants.Request.Place.Review.USER_ID);
                int userId = Integer.parseInt(userIdString);
                User user = UserController.get(userId);
                List<PlaceReview> reviews = PlaceReviewController.getAllByPlaceAndUser(place, user);
                if ((reviews == null) || reviews.isEmpty())
                {
                    out.println(Constants.Response.Place.Review.NO_REVIEWS_FOUND);
                }
                else
                {
                    out.println(Constants.Response.Place.Review.DATA_ON_MULTIPLE_REVIEWS);
                    String outString = "";
                    outString += reviews.size()+"\t";
                    for (PlaceReview review : reviews)
                    {
                        String str = review.toString();
                        outString += str;
                    }
                    out.println(outString);
                }
            
            } break;  
            case Constants.Request.Place.Review.Criteria.USER :
            {
                String userIdString = req.getParameter(Constants.Request.Place.Review.USER_ID);
                int userId = Integer.parseInt(userIdString);
                User user = UserController.get(userId);
                List<PlaceReview> reviews = PlaceReviewController.getAllByUser(user);
                if ((reviews == null) || reviews.isEmpty())
                {
                    out.println(Constants.Response.Place.Review.NO_REVIEWS_FOUND);
                }
                else
                {
                    out.println(Constants.Response.Place.Review.DATA_ON_MULTIPLE_REVIEWS);
                    String outString = "";
                    outString += reviews.size()+"\t";
                    for (PlaceReview review : reviews)
                    {
                        String str = review.toString();
                        outString += str;
                    }
                    out.println(outString);
                }
            
            } break; 
                
                
            default :;
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
