/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adagent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "tContType", catalog = "ADagent", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "TContType.findAll", query = "SELECT t FROM TContType t"),
    @NamedQuery(name = "TContType.findByCID", query = "SELECT t FROM TContType t WHERE t.cID = :cID"),
    @NamedQuery(name = "TContType.findByContentName", query = "SELECT t FROM TContType t WHERE t.contentName = :contentName"),
    @NamedQuery(name = "TContType.findByContentCost", query = "SELECT t FROM TContType t WHERE t.contentCost = :contentCost")})
public class TContType implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cID")
    private Short cID;
    @Column(name = "ContentName")
    private String contentName;
    @Column(name = "ContentCost")
    private BigDecimal contentCost;

    public TContType() {
    }

    public TContType(Short cID) {
        this.cID = cID;
    }

    public Short getCID() {
        return cID;
    }

    public void setCID(Short cID) {
        Short oldCID = this.cID;
        this.cID = cID;
        changeSupport.firePropertyChange("CID", oldCID, cID);
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        String oldContentName = this.contentName;
        this.contentName = contentName;
        changeSupport.firePropertyChange("contentName", oldContentName, contentName);
    }

    public BigDecimal getContentCost() {
        return contentCost;
    }

    public void setContentCost(BigDecimal contentCost) {
        BigDecimal oldContentCost = this.contentCost;
        this.contentCost = contentCost;
        changeSupport.firePropertyChange("contentCost", oldContentCost, contentCost);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cID != null ? cID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TContType)) {
            return false;
        }
        TContType other = (TContType) object;
        if ((this.cID == null && other.cID != null) || (this.cID != null && !this.cID.equals(other.cID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "adagent.TContType[cID=" + cID + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
