/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adagent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Kawai
 */
@Entity
@Table(name = "tEmployee", catalog = "ADagent", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "TEmployee.findAll", query = "SELECT t FROM TEmployee t"),
    @NamedQuery(name = "TEmployee.findByEID", query = "SELECT t FROM TEmployee t WHERE t.eID = :eID"),
    @NamedQuery(name = "TEmployee.findByEName", query = "SELECT t FROM TEmployee t WHERE t.eName = :eName"),
    @NamedQuery(name = "TEmployee.findByEPassword", query = "SELECT t FROM TEmployee t WHERE t.ePassword = :ePassword")})
public class TEmployee implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "eID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer eID;
    @Column(name = "eName")
    private String eName;
    @Column(name = "ePassword")
    private String ePassword;
    @OneToMany(mappedBy = "tEmployee")
    private List<TOrder> tOrderList;

    public TEmployee() {
    }

    public TEmployee(Integer eID) {
        this.eID = eID;
    }

    public Integer getEID() {
        return eID;
    }

    public void setEID(Integer eID) {
        Integer oldEID = this.eID;
        this.eID = eID;
        changeSupport.firePropertyChange("EID", oldEID, eID);
    }

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        String oldEName = this.eName;
        this.eName = eName;
        changeSupport.firePropertyChange("EName", oldEName, eName);
    }

    public String getEPassword() {
        return ePassword;
    }

    public void setEPassword(String ePassword) {
        String oldEPassword = this.ePassword;
        this.ePassword = ePassword;
        changeSupport.firePropertyChange("EPassword", oldEPassword, ePassword);
    }

    public List<TOrder> getTOrderList() {
        return tOrderList;
    }

    public void setTOrderList(List<TOrder> tOrderList) {
        this.tOrderList = tOrderList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eID != null ? eID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TEmployee)) {
            return false;
        }
        TEmployee other = (TEmployee) object;
        if ((this.eID == null && other.eID != null) || (this.eID != null && !this.eID.equals(other.eID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "adagent.TEmployee[eID=" + eID + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
