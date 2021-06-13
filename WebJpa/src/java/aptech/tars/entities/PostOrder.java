/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Kawai
 */
@Entity
@Table(name = "POST_ORDER")
@NamedQueries({
    @NamedQuery(name = "PostOrder.findAll", query = "SELECT p FROM PostOrder p"),
    @NamedQuery(name = "PostOrder.findByOrderNum", query = "SELECT p FROM PostOrder p WHERE p.orderNum = :orderNum"),
    @NamedQuery(name = "PostOrder.findByCustomerContact", query = "SELECT p FROM PostOrder p WHERE p.customerContact = :customerContact"),
    @NamedQuery(name = "PostOrder.findBySrcAddr", query = "SELECT p FROM PostOrder p WHERE p.srcAddr = :srcAddr"),
    @NamedQuery(name = "PostOrder.findByDstAddr", query = "SELECT p FROM PostOrder p WHERE p.dstAddr = :dstAddr"),
    @NamedQuery(name = "PostOrder.findByQuantity", query = "SELECT p FROM PostOrder p WHERE p.quantity = :quantity")})
public class PostOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ORDER_NUM")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer orderNum;
    @Column(name = "CUSTOMER_CONTACT")
    private String customerContact;
    @Column(name = "SRC_ADDR")
    private String srcAddr;
    @Column(name = "DST_ADDR")
    private String dstAddr;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Lob
    @Column(name = "TRACKING")
    private String tracking;
    @JoinColumn(name = "COST_ID", referencedColumnName = "COST_ID")
    @ManyToOne
    private PostCost postCost;
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID")
    @ManyToOne
    private Employee employee;

    public PostOrder() {
    }


    public Integer getOrderNum() {
        return orderNum;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public PostCost getPostCost() {
        return postCost;
    }

    public void setPostCost(PostCost postCost) {
        this.postCost = postCost;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderNum != null ? orderNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PostOrder)) {
            return false;
        }
        PostOrder other = (PostOrder) object;
        if ((this.orderNum == null && other.orderNum != null) || (this.orderNum != null && !this.orderNum.equals(other.orderNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aptech.tars.entities.PostOrder[orderNum=" + orderNum + "]";
    }

}
