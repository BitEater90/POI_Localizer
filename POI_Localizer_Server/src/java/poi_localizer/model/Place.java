package poi_localizer.model;

import java.io.Serializable;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
@Entity
@Table(name = "places")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Place.findAll", query = "SELECT p FROM Place p"),
    @NamedQuery(name = "Place.findByPlaceId", query = "SELECT p FROM Place p WHERE p.placeId = :placeId"),
    @NamedQuery(name = "Place.findByType", query = "SELECT p FROM Place p WHERE p.typeId = :typeId"),
    @NamedQuery(name = "Place.findByCoordinates", query = "SELECT p FROM Place p WHERE p.lng = :lng"),
    @NamedQuery(name = "Place.findByNameAndType", query = "SELECT p FROM Place p WHERE (p.name = :name) AND (p.typeId = :typeId)"),
    @NamedQuery(name = "Place.findByNameAndTypeApprox", query = "SELECT p FROM Place p WHERE (p.typeId = :typeId) AND (p.name LIKE :name)"),
    @NamedQuery(name = "Place.findByNameAndVicinityAndType", query = "SELECT p FROM Place p WHERE (p.name = :name) AND (p.vicinity = :vicinity) AND (p.typeId = :typeId)"),
    @NamedQuery(name = "Place.findByNameAndVicinityAndTypeApprox", query = "SELECT p FROM Place p WHERE (p.typeId = :typeId) AND (p.vicinity LIKE :vicinity) AND (p.name LIKE :name)"),
    @NamedQuery(name = "Place.findByVicinity", query = "SELECT p FROM Place p WHERE p.vicinity = :vicinity"),
    @NamedQuery(name = "Place.findByFormattedPhoneNumber", query = "SELECT p FROM Place p WHERE p.formattedPhoneNumber = :formattedPhoneNumber"),
    @NamedQuery(name = "Place.findByFormattedAddress", query = "SELECT p FROM Place p WHERE p.formattedAddress = :formattedAddress"),
    @NamedQuery(name = "Place.findByLat", query = "SELECT p FROM Place p WHERE p.lat = :lat"),
    @NamedQuery(name = "Place.findByLng", query = "SELECT p FROM Place p WHERE p.lng = :lng"),
    @NamedQuery(name = "Place.findByRating", query = "SELECT p FROM Place p WHERE p.rating = :rating"),
    @NamedQuery(name = "Place.findByInternationalPhoneNumber", query = "SELECT p FROM Place p WHERE p.internationalPhoneNumber = :internationalPhoneNumber"),
    @NamedQuery(name = "Place.findByWebsite", query = "SELECT p FROM Place p WHERE p.website = :website"),
    @NamedQuery(name = "Place.findByCreationTime", query = "SELECT p FROM Place p WHERE p.creationTime = :creationTime"),
    @NamedQuery(name = "Place.findByPriceLevel", query = "SELECT p FROM Place p WHERE p.priceLevel = :priceLevel"),
    @NamedQuery(name = "Place.findByUtcOffset", query = "SELECT p FROM Place p WHERE p.utcOffset = :utcOffset")})
public class Place implements Serializable {
    @Basic(optional = false)
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Column(name = "price_level")
    private PriceLevel priceLevel;
    
    
    public enum PriceLevel { 
        FREE,
        INEXPENSIVE,
        MODERATE,
        EXPENSIVE, 
        VERY_EXPENSIVE;
    };
    
    public static String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss z";
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "place_id")
    private Integer placeId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "vicinity")
    private String vicinity;
    @Column(name = "formatted_phone_number")
    private String formattedPhoneNumber;
    @Column(name = "formatted_address")
    private String formattedAddress;
    @Basic(optional = false)
    @Column(name = "lat")
    private float lat;
    @Basic(optional = false)
    @Column(name = "lng")
    private float lng;
    @Basic(optional = false)
    @Column(name = "rating")
    private float rating;
    @Column(name = "international_phone_number")
    private String internationalPhoneNumber;
    @Column(name = "website")
    private String website;
    @Basic(optional = false)
    @Column(name = "utc_offset")
    private short utcOffset;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "placeId", fetch = FetchType.EAGER)
    private List<PlaceReview> placeReviewList;
    
    @OneToMany(mappedBy = "placeId", fetch = FetchType.EAGER)
    private transient List<PlaceEvent> placeEventList;
    
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private PlaceType typeId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userId;
    
    public Place() {
        this.placeEventList = new ArrayList<PlaceEvent>();
        this.placeReviewList = new ArrayList<PlaceReview>();
    }

    public Place(Integer placeId) {
        this.placeId = placeId;
        this.placeEventList = new ArrayList<PlaceEvent>();
        this.placeReviewList = new ArrayList<PlaceReview>();
    }
    
    public Place(Integer placeId, String name, String vicinity, float lat, float lng, float rating) {
        this.placeId = placeId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.placeEventList = new ArrayList<PlaceEvent>();
        this.placeReviewList = new ArrayList<PlaceReview>();
    }
    
    public Place(String name, String vicinity, float lat, float lng, float rating, Date creationTime, 
            short utcOffset) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.creationTime = creationTime;
        this.utcOffset = utcOffset;
        this.placeEventList = new ArrayList<PlaceEvent>();
        this.placeReviewList = new ArrayList<PlaceReview>();
    }

    public Place(Integer placeId, String name, String vicinity, float lat, float lng,
            float rating, Date creationTime, short utcOffset) {
        this.placeId = placeId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.creationTime = creationTime;
        this.utcOffset = utcOffset;
        this.placeEventList = new ArrayList<PlaceEvent>();
        this.placeReviewList = new ArrayList<PlaceReview>();
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
       
    public PlaceType getTypeId() {
        return typeId;
    }

    public void setTypeId(PlaceType typeId) {
        this.typeId = typeId;
    }
     
    public List<PlaceEvent> getPlaceEventList() {
        return placeEventList;
    }

    public void setPlaceEventList(List<PlaceEvent> placeEventList) {
        this.placeEventList = placeEventList;
    }
    
    public void addEvent(PlaceEvent event)
    {
        this.placeEventList.add(event);
    }
    
    public void removeEvent(PlaceEvent event)
    {
        this.placeEventList.remove(event);
    }
        
    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public PriceLevel getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(PriceLevel priceLevel) {
        this.priceLevel = priceLevel;
    }

    public short getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(short utcOffset) {
        this.utcOffset = utcOffset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placeId != null ? placeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Place)) {
            return false;
        }
        Place other = (Place) object;
        if ((this.placeId == null && other.placeId != null) || (this.placeId != null && !this.placeId.equals(other.placeId))) {
            return false;
        }
        return true;
    }
    
    public static String makeUtcOffsetString(Place place)
    {
        String utcOffsetString = "";
        short hour = (short)(place.getUtcOffset()/(short)100);
        if (hour < 10)
            utcOffsetString = "0";
        utcOffsetString += hour+":";
        short minutes = (short)(place.getUtcOffset() - 100 * hour);
        if (minutes < 10)
            utcOffsetString += "0";
        utcOffsetString += minutes;
        return utcOffsetString;
    }
    
    public String toStringSimple() {
        
        try
        {
            StringWriter out = new StringWriter();
            out.write(this.placeId+"\t");
            
            out.write(this.typeId.getTypeId()+"\t");
            out.write(this.typeId.getName()+"\t");
            out.write(this.typeId.getNamePl()+"\t");
            
            out.write(this.priceLevel.name()+"\t");
            
            if (this.name == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.name+"\t");
            }
            
            if (this.vicinity == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.vicinity+"\t");
            }
            
            if (this.formattedPhoneNumber == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.formattedPhoneNumber+"\t");
            }
            
            if (this.formattedAddress == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.formattedAddress+"\t");
            }
            
            out.write(this.lat + "\t");
            out.write(this.lng + "\t");
            out.write(this.rating + "\t");
            
            if (this.internationalPhoneNumber == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.internationalPhoneNumber + "\t");
            }
            
            if (this.website == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.website + "\t");
            }
            
            //Blok danych użytkownika
            if (this.userId.getUserId() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getUserId()+"\t");
            }
            
            if (this.userId.getLogin() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getLogin()+"\t");
            }
            
            if (this.userId.getName() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getName()+"\t");
            }
            
            if (this.userId.getSurname() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getSurname()+"\t");
            }
            //Koniec bloku danych użytkownika 
           
            String output = out.toString();
            out.close();
            return output;
        }
        catch(IOException ioe)
        {
            return null;
        }
    }
    
    @Override
    public String toString() {
        
        try
        {
            StringWriter out = new StringWriter();
            out.write(this.placeId+"\t");
            
            if (this.formattedAddress == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.formattedAddress+"\t");
            }
            
            if (this.formattedPhoneNumber == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.formattedPhoneNumber+"\t");
            }

            out.write(this.utcOffset+"\t");
            
            if (this.creationTime == null)
            {
                out.write("0\t");
            }
            else
            {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                String utcOffsetString = Place.makeUtcOffsetString(this);
                df.setTimeZone(TimeZone.getTimeZone("GMT+"+utcOffsetString));
                String creationString = df.format(creationTime);
                out.write(creationString+"\t");
            }

            if (this.internationalPhoneNumber == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.internationalPhoneNumber+"\t");
            }
            
            out.write(this.lat+"\t");
            out.write(this.lng+"\t");
            
            if (this.name == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.name+"\t");
            }
            
            out.write(this.priceLevel+"\t");
            out.write(this.rating+"\t");
            out.write(this.typeId.getTypeId()+"\t");
            out.write(this.typeId.getName()+"\t");
            out.write(this.typeId.getNamePl()+"\t");
            
            //Blok danych użytkownika
            if (this.userId.getUserId() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getUserId()+"\t");
            }
            
            if (this.userId.getLogin() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getLogin()+"\t");
            }
            
            if (this.userId.getName() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getName()+"\t");
            }
            
            if (this.userId.getSurname() == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.userId.getSurname()+"\t");
            }
            //Koniec bloku danych użytkownika           
            
            if (this.vicinity == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.vicinity+"\t");
            }
            
            if (this.website == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.website+"\t");
            }
            
            String output = out.toString();
            out.close();
            return output;
        }
        catch(IOException ioe)
        {
            return null;
        }
    }

    @XmlTransient
    public List<PlaceReview> getPlaceReviewList() {
        return placeReviewList;
    } 

    public void setPlaceReviewList(List<PlaceReview> placeReviewsList) {
        this.placeReviewList = placeReviewsList;
    }
    
    public void addReview(PlaceReview review)
    {
        placeReviewList.add(review);
    }
    
    public void removeReview(PlaceReview review)
    {
        placeReviewList.remove(review);
    }
    
}
