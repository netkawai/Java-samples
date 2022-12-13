/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Kawai
 */
@Entity
@Table(name = "POST_COST")
@NamedQueries({
    @NamedQuery(name = "PostCost.findAll", query = "SELECT p FROM PostCost p"),
    @NamedQuery(name = "PostCost.findByCostId", query = "SELECT p FROM PostCost p WHERE p.costId = :costId"),
    @NamedQuery(name = "PostCost.findByWeight", query = "SELECT p FROM PostCost p WHERE p.weight = :weight"),
    @NamedQuery(name = "PostCost.findByServiceType", query = "SELECT p FROM PostCost p WHERE p.serviceType = :serviceType"),
    @NamedQuery(name = "PostCost.findByCost", query = "SELECT p FROM PostCost p WHERE p.cost = :cost")})
public class PostCost implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COST_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer costId;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private String weight;
    @Basic(optional = false)
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Basic(optional = false)
    @Column(name = "COST")
    private BigDecimal cost;
    @OneToMany(mappedBy = "postCost")
    private Collection<PostOrder> postOrderCollection;

    public PostCost() {
    }

    public PostCost(Integer costId) {
        this.costId = costId;
    }

    public PostCost(Integer costId, String weight, String serviceType, BigDecimal cost) {
        this.costId = costId;
        this.weight = weight;
        this.serviceType = serviceType;
        this.cost = cost;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Collection<PostOrder> getPostOrderCollection() {
        return postOrderCollection;
    }

    public void setPostOrderCollection(Collection<PostOrder> postOrderCollection) {
        this.postOrderCollection = postOrderCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (costId != null ? costId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PostCost)) {
            return false;
        }
        PostCost other = (PostCost) object;
        if ((this.costId == null && other.costId != null) || (this.costId != null && !this.costId.equals(other.costId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aptech.tars.entities.PostCost[costId=" + costId + "]";
    }

}
