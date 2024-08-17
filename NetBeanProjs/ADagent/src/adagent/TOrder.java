/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adagent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Kawai
 */
@Entity
@Table(name = "tOrder", catalog = "ADagent", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "TOrder.findAll", query = "SELECT t FROM TOrder t"),
    @NamedQuery(name = "TOrder.findByOID", query = "SELECT t FROM TOrder t WHERE t.oID = :oID"),
    @NamedQuery(name = "TOrder.findByCName", query = "SELECT t FROM TOrder t WHERE t.cName = :cName"),
    @NamedQuery(name = "TOrder.findByACont", query = "SELECT t FROM TOrder t WHERE t.aCont = :aCont"),
    @NamedQuery(name = "TOrder.findByASize", query = "SELECT t FROM TOrder t WHERE t.aSize = :aSize"),
    @NamedQuery(name = "TOrder.findByAPage", query = "SELECT t FROM TOrder t WHERE t.aPage = :aPage"),
    @NamedQuery(name = "TOrder.findByABill", query = "SELECT t FROM TOrder t WHERE t.aBill = :aBill"),
    @NamedQuery(name = "TOrder.findByAPostingDate", query = "SELECT t FROM TOrder t WHERE t.aPostingDate = :aPostingDate")})
public class TOrder implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "oID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer oID;
    @Column(name = "cName")
    private String cName;
    @Column(name = "aCont")
    private Short aCont;
    @Column(name = "aSize")
    private Short aSize;
    @Column(name = "aPage")
    private Short aPage;
    @Column(name = "aBill")
    private BigDecimal aBill;
    @Column(name = "aPostingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aPostingDate;
    @JoinColumn(name = "eID", referencedColumnName = "eID")
    @ManyToOne
    private TEmployee tEmployee;

    public TOrder() {
    }

    public TOrder(Integer oID) {
        this.oID = oID;
    }

    public Integer getOID() {
        return oID;
    }

    public void setOID(Integer oID) {
        Integer oldOID = this.oID;
        this.oID = oID;
        changeSupport.firePropertyChange("OID", oldOID, oID);
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        String oldCName = this.cName;
        this.cName = cName;
        changeSupport.firePropertyChange("CName", oldCName, cName);
    }

    public Short getACont() {
        return aCont;
    }

    public void setACont(Short aCont) {
        Short oldACont = this.aCont;
        this.aCont = aCont;
        changeSupport.firePropertyChange("ACont", oldACont, aCont);
    }

    public Short getASize() {
        return aSize;
    }

    public void setASize(Short aSize) {
        Short oldASize = this.aSize;
        this.aSize = aSize;
        changeSupport.firePropertyChange("ASize", oldASize, aSize);
    }

    public Short getAPage() {
        return aPage;
    }

    public void setAPage(Short aPage) {
        Short oldAPage = this.aPage;
        this.aPage = aPage;
        changeSupport.firePropertyChange("APage", oldAPage, aPage);
    }

    public BigDecimal getABill() {
        return aBill;
    }

    public void setABill(BigDecimal aBill) {
        BigDecimal oldABill = this.aBill;
        this.aBill = aBill;
        changeSupport.firePropertyChange("ABill", oldABill, aBill);
    }

    public Date getAPostingDate() {
        return aPostingDate;
    }

    public void setAPostingDate(Date aPostingDate) {
        Date oldAPostingDate = this.aPostingDate;
        this.aPostingDate = aPostingDate;
        changeSupport.firePropertyChange("APostingDate", oldAPostingDate, aPostingDate);
    }

    public TEmployee getTEmployee() {
        return tEmployee;
    }

    public void setTEmployee(TEmployee tEmployee) {
        TEmployee oldTEmployee = this.tEmployee;
        this.tEmployee = tEmployee;
        changeSupport.firePropertyChange("TEmployee", oldTEmployee, tEmployee);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oID != null ? oID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TOrder)) {
            return false;
        }
        TOrder other = (TOrder) object;
        if ((this.oID == null && other.oID != null) || (this.oID != null && !this.oID.equals(other.oID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "adagent.TOrder[oID=" + oID + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
