package poi_localizer.view.place;

import poi_localizer.view.place.PlaceEditUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import poi_localizer.controller.utils.PlaceController;
import poi_localizer.model.User;
import poi_localizer.model.Place;
import poi_localizer.view.Constants;
import poi_localizer.view.Utils;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
public class PlaceEditServlet extends HttpServlet {

    private Place editPlace(HttpServletRequest req, HttpServletResponse res,
            PrintWriter out, User user) {
        int placeId = 0;
        try {
            placeId = Utils.getParameter(req, Constants.Request.Place.PLACE_ID);
            if (placeId <= 0) {
                out.println(Constants.Response.Place.INCORRECT_PLACE_NUMBER);
                return null;
            }
        } catch (Utils.NoParameterException npe1) {
            out.println(Constants.Response.Place.NO_PLACE_SPECIFIED);
            return null;
        }

        Place place = PlaceController.get(placeId);
        if (place == null) {
            out.println(Constants.Response.Place.NO_PLACE_FOUND);
            return null;
        }

        if (place.getUserId().getUserId() != user.getUserId()) {
            out.println(Constants.Response.Place.USER_IS_NOT_PLACE_AUTHOR);
            return null;
        }

        String name = null;
        try {
            name = req.getParameter(Constants.Request.Place.Addition.NAME);
            if ((name == null) || (!PlaceEditUtils.checkName(name))) {
                throw new Utils.NoParameterException();
            }
            name = Utils.unfloor(name);
        } catch (Utils.NoParameterException npe) {
            name = place.getName();
        }

        float lat = (float) 0.0;
        try {
            lat = Utils.getParameterFloat(req, Constants.Request.Place.Addition.LAT);
            if (!PlaceEditUtils.checkLat(lat)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe1) {
            lat = place.getLat();
        }

        float lng = (float) 0.0;
        try {
            lng = Utils.getParameterFloat(req, Constants.Request.Place.Addition.LNG);
            if (!PlaceEditUtils.checkLng(lng)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe2) {
            lng = place.getLng();
        }

        float rating = place.getRating();

        String formattedPhoneNumber = null;
        try {
            formattedPhoneNumber = req.getParameter(Constants.Request.Place.Addition.FORMATTED_PHONE_NUMBER);
            if (formattedPhoneNumber == null) {
                throw new Utils.NoParameterException();
            }
            formattedPhoneNumber = Utils.unfloor(formattedPhoneNumber);
            if (!PlaceEditUtils.checkFormattedPhoneNumber(formattedPhoneNumber)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe3) {
            formattedPhoneNumber = place.getFormattedPhoneNumber();
        }

        String formattedAddress = null;
        try {
            formattedAddress = req.getParameter(Constants.Request.Place.Addition.FORMATTED_ADDRESS);
            if (formattedAddress == null) {
                throw new Utils.NoParameterException();
            }
            formattedAddress = Utils.unfloor(formattedAddress);
            if (!PlaceEditUtils.checkFormattedAddress(formattedAddress)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe4) {
            formattedAddress = place.getFormattedAddress();
        }

        String website = null;
        try {
            website = req.getParameter(Constants.Request.Place.Addition.WEBSITE);
            if (website == null) {
                throw new Utils.NoParameterException();
            }
            if (!PlaceEditUtils.checkWebsite(website)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe5) {
            website = place.getWebsite();
        }

        String vicinity = null;
        try {
            vicinity = req.getParameter(Constants.Request.Place.Addition.VICINITY);
            if (vicinity == null) {
                throw new Utils.NoParameterException();
            }

            if (!PlaceEditUtils.checkVicinity(vicinity)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe7) {
            vicinity = place.getVicinity();
        }

        String internationalPhoneNumber = null;
        try {
            internationalPhoneNumber = req.getParameter(Constants.Request.Place.Addition.INTERNATIONAL_PHONE_NUMBER);
            if (internationalPhoneNumber == null) {
                throw new Utils.NoParameterException();
            }
            if (!PlaceEditUtils.checkInternationalPhoneNumber(internationalPhoneNumber)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe8) {
            internationalPhoneNumber = place.getInternationalPhoneNumber();
        }

        String priceLevelStr = req.getParameter(Constants.Request.Place.Addition.PRICE_LEVEL);
        Place.PriceLevel priceLevel = null;
        if (priceLevelStr != null) {
            priceLevel = PlaceEditUtils.makePriceLevel(priceLevelStr);
        } else {
            priceLevel = place.getPriceLevel();
        }

        short utcOffset = 0;
        try {
            utcOffset = (short) Utils.getParameter(req, Constants.Request.Place.Addition.UTC_OFFSET);
            if (!PlaceEditUtils.checkUtcOffset(utcOffset)) {
                throw new Utils.NoParameterException();
            }
        } catch (Utils.NoParameterException npe10) {
            utcOffset = place.getUtcOffset();
        }

        place = PlaceController.edit(place, name, lat, lng,
                formattedAddress, formattedPhoneNumber, internationalPhoneNumber,
                priceLevel, rating, utcOffset, vicinity, website);

        if (place != null) {
            out.println(Constants.Response.Place.Addition.PLACE_EDITION_COMPLETED);
            return place;
        } else {
            out.println(Constants.Response.Place.Addition.PLACE_EDITION_FAILED);
            return null;
        }

    }

    private Place editPlaceQuery(HttpServletRequest req, HttpServletResponse res,
            PrintWriter out, User user) {
        
        String query = "UPDATE places SET ";

        int placeId = 0;
        try {
            placeId = Utils.getParameter(req, Constants.Request.Place.PLACE_ID);
            if (placeId <= 0) {
                out.println(Constants.Response.Place.INCORRECT_PLACE_NUMBER);
                return null;
            }
        } catch (Utils.NoParameterException npe1) {
            out.println(Constants.Response.Place.NO_PLACE_SPECIFIED);
            return null;
        }

        Place place = PlaceController.get(placeId);
        if (place == null) {
            out.println(Constants.Response.Place.NO_PLACE_FOUND);
            return null;
        }

        if (place.getUserId().getUserId() != user.getUserId()) {
            out.println(Constants.Response.Place.USER_IS_NOT_PLACE_AUTHOR);
            return null;
        }

        String name = null;
        name = req.getParameter(Constants.Request.Place.Addition.NAME);
        if ((name != null) && PlaceEditUtils.checkName(name)) {
            name = Utils.unfloor(name);
            query += "name = '" + name + "', ";
        }

        float lat = (float) 0.0;
        try {
            lat = Utils.getParameterFloat(req, Constants.Request.Place.Addition.LAT);
            if (!PlaceEditUtils.checkLat(lat)) {
                throw new Utils.NoParameterException();
            }
            query += "lat = " + lat + ", ";
        } catch (Utils.NoParameterException npe1) {}

        float lng = (float) 0.0;
        try {
            lng = Utils.getParameterFloat(req, Constants.Request.Place.Addition.LNG);
            if (!PlaceEditUtils.checkLng(lng)) {
                throw new Utils.NoParameterException();
            }
            query += "lng = " + lng + ", ";
        } catch (Utils.NoParameterException npe2) {}

        String formattedPhoneNumber = null;
        formattedPhoneNumber = req.getParameter(Constants.Request.Place.Addition.FORMATTED_PHONE_NUMBER);
        if (formattedPhoneNumber != null) {
            formattedPhoneNumber = Utils.unfloor(formattedPhoneNumber);
            try
            {
                if (PlaceEditUtils.checkFormattedPhoneNumber(formattedPhoneNumber)) {
                    query += "formatted_phone_number = '"+formattedPhoneNumber+"', ";
                }
            }
            catch(Utils.NoParameterException npe){}
        }

        String formattedAddress = null;
        formattedAddress = req.getParameter(Constants.Request.Place.Addition.FORMATTED_ADDRESS);
        if (formattedAddress != null) {
            formattedAddress = Utils.unfloor(formattedAddress);
            try
            {
                if (PlaceEditUtils.checkFormattedAddress(formattedAddress)) {
                    query += "formatted_address = '"+formattedAddress+"', ";
                }
            }
            catch(Utils.NoParameterException npe){}
        }

        String website = null;
        try {
            website = req.getParameter(Constants.Request.Place.Addition.WEBSITE);
            if (website != null)
            {
                if (PlaceEditUtils.checkWebsite(website)) {
                    query += "website = '"+website+"', ";
                }
            }
        } catch (Utils.NoParameterException npe5) {
            website = place.getWebsite();
        }

        String vicinity = null;
        vicinity = req.getParameter(Constants.Request.Place.Addition.VICINITY);
        if (vicinity != null) {
            if (PlaceEditUtils.checkVicinity(vicinity)) {
                query += "vicinity = '"+vicinity+", ";
            }
        }

        String internationalPhoneNumber = null;
        try {
            internationalPhoneNumber = req.getParameter(Constants.Request.Place.Addition.INTERNATIONAL_PHONE_NUMBER);
            if (internationalPhoneNumber != null)
            {
                if (PlaceEditUtils.checkInternationalPhoneNumber(internationalPhoneNumber)) {
                    query += "international_phone_number = '"+internationalPhoneNumber+"', ";
                }
            }
        } catch (Utils.NoParameterException npe8) {}

        String priceLevelStr = req.getParameter(Constants.Request.Place.Addition.PRICE_LEVEL);
        Place.PriceLevel priceLevel = null;
        if (priceLevelStr != null) {
            priceLevel = PlaceEditUtils.makePriceLevel(priceLevelStr);
            query += "price_level = "+priceLevel.ordinal()+", ";
        } 

        short utcOffset = 0;
        try {
            utcOffset = (short) Utils.getParameter(req, Constants.Request.Place.Addition.UTC_OFFSET);
            if (PlaceEditUtils.checkUtcOffset(utcOffset)) {
                query += "utc_offset"+utcOffset+", ";
            }
        } catch (Utils.NoParameterException npe10) {}
        
        query = query.substring(0, query.lastIndexOf(", "));
        query += " WHERE place_id=" + placeId;
        
        int updatedNumber = PlaceController.editQuery(query);
        if (updatedNumber == 0)
            return null;
        place = PlaceController.get(placeId);
        return place;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        HttpSession session = req.getSession();
        PrintWriter out = res.getWriter();

        User user = PlaceEditUtils.checkIfUserLogged(req, out);
        if (user == null) {
            out.println(Constants.Response.NOT_LOGGED);
            return;
        }

        Place place = editPlaceQuery(req, res, out, user);

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
}
