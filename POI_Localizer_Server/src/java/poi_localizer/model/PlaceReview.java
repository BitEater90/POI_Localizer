package poi_localizer.model;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
@Entity
@Table(name = "place_reviews")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaceReview.findAll", query = "SELECT p FROM PlaceReview p"),
    @NamedQuery(name = "PlaceReview.findByReviewId", query = "SELECT p FROM PlaceReview p WHERE p.reviewId = :reviewId"),
    @NamedQuery(name = "PlaceReview.findByReviewTime", query = "SELECT p FROM PlaceReview p WHERE p.reviewTime = :reviewTime"),
    @NamedQuery(name = "PlaceReview.findByText", query = "SELECT p FROM PlaceReview p WHERE p.text = :text"),
    @NamedQuery(name = "PlaceReview.findByAuthorName", query = "SELECT p FROM PlaceReview p WHERE p.authorName = :authorName"),
    @NamedQuery(name = "PlaceReview.findByUser", query = "SELECT p FROM PlaceReview p WHERE p.userId = :userId"),
    @NamedQuery(name = "PlaceReview.findByPlaceAndUser", query = "SELECT p FROM PlaceReview p WHERE p.userId = :userId AND p.placeId = :placeId"),
    @NamedQuery(name = "PlaceReview.findByPlaceAndAuthorName", query = "SELECT p FROM PlaceReview p WHERE p.placeId = :placeId AND p.authorName = :authorName"),
    @NamedQuery(name = "PlaceReview.findByPlaceAndAuthorName", query = "SELECT p FROM PlaceReview p WHERE p.placeId = :placeId AND p.authorName = :authorName"),
    @NamedQuery(name = "PlaceReview.findByPlaceAndAuthorNameAndReviewTime", query = "SELECT p FROM PlaceReview p WHERE p.placeId = :placeId AND p.authorName = :authorName AND p.reviewTime = :reviewTime"),
    @NamedQuery(name = "PlaceReview.findByAuthorUrl", query = "SELECT p FROM PlaceReview p WHERE p.authorUrl = :authorUrl"),
    @NamedQuery(name = "PlaceReview.findByRating", query = "SELECT p FROM PlaceReview p WHERE p.rating = :rating")})
public class PlaceReview implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "review_id")
    private Integer reviewId;
    @Basic(optional = false)
    @Column(name = "review_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewTime;
    @Basic(optional = false)
    @Column(name = "text")
    private String text;
    @Basic(optional = false)
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "author_url")
    private String authorUrl;
    @Basic(optional = false)
    @Column(name = "rating")
    private float rating;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User userId;
    @JoinColumn(name = "place_id", referencedColumnName = "place_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Place placeId;

    public PlaceReview() {
    }

    public PlaceReview(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public PlaceReview(Integer reviewId, Date reviewTime, String text, String authorName, String authorUrl, float rating) {
        this.reviewId = reviewId;
        this.reviewTime = reviewTime;
        this.text = text;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.rating = rating;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Place getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Place placeId) {
        this.placeId = placeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewId != null ? reviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlaceReview)) {
            return false;
        }
        PlaceReview other = (PlaceReview) object;
        if ((this.reviewId == null && other.reviewId != null) || (this.reviewId != null && !this.reviewId.equals(other.reviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        
        StringWriter out = new StringWriter();
        try
        {
            out.write(this.reviewId+"\t");
            
            DateFormat df = new SimpleDateFormat(Place.DATE_FORMAT);
            String utcOffsetString = "";
            short hour = (short)(placeId.getUtcOffset()/(short)100);
            if (hour < 10)
                utcOffsetString = "0";
            utcOffsetString += hour+":";
            short minutes = (short)(placeId.getUtcOffset() - 100 * hour);
            if (minutes < 10)
                utcOffsetString += "0";
            utcOffsetString += minutes;
            
            //out.write("0\t");
                
            if (this.reviewTime == null)
            {
                out.write("0\t");
            }
            else
            {               

                df.setTimeZone(TimeZone.getTimeZone("GMT+"+utcOffsetString));
                String reviewString = df.format(this.reviewTime);
                out.write(reviewString+"\t");
            }

            if (this.text == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.text+"\t");
            }

            if (this.authorName == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.authorName+"\t");
            }
            
            if (this.authorUrl == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.authorUrl+"\t");
            }                
            
            out.write(this.rating+"\t");
            
            if (this.userId != null)
            {
                out.write(this.userId.getUserId()+"\t");
                out.write(this.userId.getLogin()+"\t");
                out.write(this.userId.getName()+"\t");
                out.write(this.userId.getSurname()+"\t");
            }
            else
            {
                out.write("0\t");
            }
            
            out.write(this.placeId.getPlaceId()+"\t");
            out.write(this.placeId.getName()+"\t");
                        
            String output = out.toString();
            out.close();
            return output;
        }
        catch(IOException ioe)
        {
            return null;
        }
    }
    
}
