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
@Table(name = "tPageType", catalog = "ADagent", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "TPageType.findAll", query = "SELECT t FROM TPageType t"),
    @NamedQuery(name = "TPageType.findByPID", query = "SELECT t FROM TPageType t WHERE t.pID = :pID"),
    @NamedQuery(name = "TPageType.findByPageName", query = "SELECT t FROM TPageType t WHERE t.pageName = :pageName"),
    @NamedQuery(name = "TPageType.findByPageCost", query = "SELECT t FROM TPageType t WHERE t.pageCost = :pageCost")})
public class TPageType implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "pID")
    private Short pID;
    @Column(name = "PageName")
    private String pageName;
    @Column(name = "PageCost")
    private BigDecimal pageCost;

    public TPageType() {
    }

    public TPageType(Short pID) {
        this.pID = pID;
    }

    public Short getPID() {
        return pID;
    }

    public void setPID(Short pID) {
        Short oldPID = this.pID;
        this.pID = pID;
        changeSupport.firePropertyChange("PID", oldPID, pID);
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        String oldPageName = this.pageName;
        this.pageName = pageName;
        changeSupport.firePropertyChange("pageName", oldPageName, pageName);
    }

    public BigDecimal getPageCost() {
        return pageCost;
    }

    public void setPageCost(BigDecimal pageCost) {
        BigDecimal oldPageCost = this.pageCost;
        this.pageCost = pageCost;
        changeSupport.firePropertyChange("pageCost", oldPageCost, pageCost);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pID != null ? pID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TPageType)) {
            return false;
        }
        TPageType other = (TPageType) object;
        if ((this.pID == null && other.pID != null) || (this.pID != null && !this.pID.equals(other.pID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "adagent.TPageType[pID=" + pID + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
