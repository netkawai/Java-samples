/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adagent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Kawai
 */
@Entity
@Table(name = "tSizeType", catalog = "ADagent", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "TSizeType.findAll", query = "SELECT t FROM TSizeType t"),
    @NamedQuery(name = "TSizeType.findBySID", query = "SELECT t FROM TSizeType t WHERE t.sID = :sID"),
    @NamedQuery(name = "TSizeType.findBySizeName", query = "SELECT t FROM TSizeType t WHERE t.sizeName = :sizeName"),
    @NamedQuery(name = "TSizeType.findBySizeWeight", query = "SELECT t FROM TSizeType t WHERE t.sizeWeight = :sizeWeight")})
public class TSizeType implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "sID")
    private Short sID;
    @Column(name = "SizeName")
    private String sizeName;
    @Column(name = "SizeWeight")
    private Double sizeWeight;

    public TSizeType() {
    }

    public TSizeType(Short sID) {
        this.sID = sID;
    }

    public Short getSID() {
        return sID;
    }

    public void setSID(Short sID) {
        Short oldSID = this.sID;
        this.sID = sID;
        changeSupport.firePropertyChange("SID", oldSID, sID);
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        String oldSizeName = this.sizeName;
        this.sizeName = sizeName;
        changeSupport.firePropertyChange("sizeName", oldSizeName, sizeName);
    }

    public Double getSizeWeight() {
        return sizeWeight;
    }

    public void setSizeWeight(Double sizeWeight) {
        Double oldSizeWeight = this.sizeWeight;
        this.sizeWeight = sizeWeight;
        changeSupport.firePropertyChange("sizeWeight", oldSizeWeight, sizeWeight);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sID != null ? sID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TSizeType)) {
            return false;
        }
        TSizeType other = (TSizeType) object;
        if ((this.sID == null && other.sID != null) || (this.sID != null && !this.sID.equals(other.sID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "adagent.TSizeType[sID=" + sID + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
