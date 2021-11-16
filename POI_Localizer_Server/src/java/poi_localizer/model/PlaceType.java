package poi_localizer.model;

import java.io.Serializable;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.StringTokenizer;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.IOException;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */
@Entity
@Table(name = "place_types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaceType.findAll", query = "SELECT p FROM PlaceType p"),
    @NamedQuery(name = "PlaceType.findByTypeId", query = "SELECT p FROM PlaceType p WHERE p.typeId = :typeId"),
    @NamedQuery(name = "PlaceType.findByName", query = "SELECT p FROM PlaceType p WHERE p.name = :name"),
    @NamedQuery(name = "PlaceType.findByNamePl", query = "SELECT p FROM PlaceType p WHERE p.namePl = :namePl")})
public class PlaceType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "type_id")
    private Short typeId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "name_pl")
    private String namePl;
    @OneToMany(mappedBy = "typeId", fetch = FetchType.EAGER)
    private List<Place> placeList;
    
    

    public PlaceType() {
    }

    public PlaceType(Short typeId) {
        this.typeId = typeId;
    }

    public PlaceType(Short typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }
    
    public void addPlace(Place type)
    {
        if (this.placeList == null)
            this.placeList = new ArrayList<Place>();
        this.placeList.add(type);
    }
    
    public void removePlace(Place type)
    {
        this.placeList.remove(type);
    }
       
    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePl() {
        return namePl;
    }

    public void setNamePl(String namePl) {
        this.namePl = namePl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaceType)) {
            return false;
        }
        PlaceType other = (PlaceType) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        
        StringWriter out = new StringWriter();
        out.write(this.typeId+"\t");
        
        if (this.name == null)
        {
            out.write("0\t");
        }
        else
        {
            out.write(this.name+"\t");
        }
        
        if (this.namePl == null)
        {
            out.write("0\t");
        }
        else
        {
            out.write(this.namePl+"\t");
        }
        
        if (this.placeList == null)
        {
            out.write("0\t");
        }
        else if (this.placeList.isEmpty())
        {
            out.write("0\t");
        }
        else
        {
            int size = this.placeList.size();
            out.write(""+size+"\t");
//            for (Place place : this.placeList)
//            {
//                out.write(place.getPlaceId()+"\t");
//                out.write(place.getName()+"\t");
//            }
        }
        
        String outString = out.toString();
        try
        {
            out.close();
        }
        catch(IOException ioe)
        {
            return null;
        }
        return outString;
        
    }
    
}
