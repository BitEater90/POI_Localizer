package poi_localizer.model;

import java.io.Serializable;
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
import java.io.StringWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
@Entity
@Table(name = "place_events")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaceEvent.findAll", query = "SELECT p FROM PlaceEvent p"),
    @NamedQuery(name = "Place.findAllByPlaceId", query = "SELECT p FROM PlaceEvent p WHERE p.placeId = :place"),
    @NamedQuery(name = "PlaceEvent.findByEventId", query = "SELECT p FROM PlaceEvent p WHERE p.eventId = :event"),
    @NamedQuery(name = "PlaceEvent.findByStartTime", query = "SELECT p FROM PlaceEvent p WHERE p.startTime = :startTime"),
    @NamedQuery(name = "PlaceEvent.findBySummary", query = "SELECT p FROM PlaceEvent p WHERE p.summary = :summary"),
    @NamedQuery(name = "PlaceEvent.findByPlaceAndName", query = "SELECT p FROM PlaceEvent p WHERE p.placeId = :place AND p.name = :name"),
    @NamedQuery(name = "PlaceEvent.findByName", query = "SELECT p FROM PlaceEvent p WHERE p.name = :name"),
    @NamedQuery(name = "PlaceEvent.findByUrl", query = "SELECT p FROM PlaceEvent p WHERE p.url = :url")})
public class PlaceEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "event_id")
    private Integer eventId;
    @Basic(optional = false)
    @Column(name = "name", unique=true)
    private String name;
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @Column(name = "summary")
    private String summary;
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "place_id", referencedColumnName = "place_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Place placeId;

    public PlaceEvent() {
    }

    public PlaceEvent(Integer eventId) {
        this.eventId = eventId;
    }

   
    public PlaceEvent(Integer eventId, String name, Date startTime,
            String summary, String url, Place place) {
        this.eventId = eventId;
        this.name = name;
        this.startTime = startTime;
        this.summary = summary;
        this.url = url;
        this.placeId = place;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
       
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlaceEvent)) {
            return false;
        }
        PlaceEvent other = (PlaceEvent) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        
        StringWriter out = new StringWriter();
        try
        {
            out.write(this.eventId+"\t");

            if (this.name == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.name+"\t");
            }

            if (this.summary == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.summary+"\t");
            }

            if (this.url == null)
            {
                out.write("0\t");
            }
            else
            {
                out.write(this.url+"\t");
            }

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
                
            out.write(this.placeId.getPlaceId()+"\t");
            out.write(this.placeId.getName()+"\t");
            out.write(utcOffsetString+"\t");

            if (this.startTime == null)
            {
                out.write("0\t");
            }
            else
            {               

                df.setTimeZone(TimeZone.getTimeZone("GMT+"+utcOffsetString));
                String startString = df.format(this.startTime);
                out.write(startString+"\t");
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
    
}
