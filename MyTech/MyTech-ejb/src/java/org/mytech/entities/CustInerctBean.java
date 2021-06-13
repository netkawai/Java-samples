package org.mytech.entities;

import javax.ejb.*;

/**
 * This is the bean class for the CustInerctBean enterprise bean.
 * Created Nov 3, 2010 4:14:07 PM
 * @author apt
 */
public abstract class CustInerctBean implements javax.ejb.EntityBean, org.mytech.entities.CustInerctLocalBusiness {
    private javax.ejb.EntityContext context;
    
    // <editor-fold defaultstate="collapsed" desc="EJB infrastructure methods. Click on the + sign on the left to edit the code.">
    // TODO Consider creating Transfer Object to encapsulate data
    // TODO Review finder methods
    /**
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(javax.ejb.EntityContext aContext) {
        context = aContext;
    }
    
    /**
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() {
        
    }
    
    /**
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() {
        
    }
    
    /**
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove() {
        
    }
    
    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() {
        context = null;
    }
    
    /**
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() {
        
    }
    
    /**
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() {
        
    }
    // </editor-fold>
    
    
    public abstract java.math.BigDecimal getCustomerid();
    public abstract void setCustomerid(java.math.BigDecimal customerid);
    
    public abstract java.sql.Time getMeetingtime();
    public abstract void setMeetingtime(java.sql.Time meetingtime);
    
    public abstract java.sql.Date getMeetingdate();
    public abstract void setMeetingdate(java.sql.Date meetingdate);
    
    public abstract java.lang.Integer getMode();
    public abstract void setMode(java.lang.Integer mode);
    
    public abstract java.lang.String getContact();
    public abstract void setContact(java.lang.String contact);
    
    public abstract java.lang.String getNote();
    public abstract void setNote(java.lang.String note);
    
    
    public java.math.BigDecimal ejbCreate(java.math.BigDecimal customerid, java.sql.Time meetingtime, java.sql.Date meetingdate, java.lang.Integer mode, java.lang.String contact, java.lang.String note)  throws javax.ejb.CreateException {
        if (customerid == null) {
            throw new javax.ejb.CreateException("The field \"customerid\" must not be null");
        }
        
        // TODO add additional validation code, throw CreateException if data is not valid
        setCustomerid(customerid);
        setMeetingtime(meetingtime);
        setMeetingdate(meetingdate);
        setMode(mode);
        setContact(contact);
        setNote(note);
        
        return null;
    }
    
    public void ejbPostCreate(java.math.BigDecimal customerid, java.sql.Time meetingtime, java.sql.Date meetingdate, java.lang.Integer mode, java.lang.String contact, java.lang.String note) {
        // TODO populate relationships here if appropriate
        
    }
}
