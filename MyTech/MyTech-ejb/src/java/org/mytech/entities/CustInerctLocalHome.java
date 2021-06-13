
package org.mytech.entities;


/**
 * This is the local-home interface for CustInerct enterprise bean.
 */
public interface CustInerctLocalHome extends javax.ejb.EJBLocalHome {
    
    org.mytech.entities.CustInerctLocal findByPrimaryKey(java.math.BigDecimal key)  throws javax.ejb.FinderException;

    public org.mytech.entities.CustInerctLocal create(java.math.BigDecimal customerid, java.sql.Time meetingtime, java.sql.Date meetingdate, java.lang.Integer mode, java.lang.String contact, java.lang.String note) throws javax.ejb.CreateException;

    /**
     * 
     */
    java.util.Collection findByCustomerid(java.math.BigDecimal customerid) throws javax.ejb.FinderException;

    /**
     * 
     */
    java.util.Collection findByMode(java.lang.Integer mode) throws javax.ejb.FinderException;

    /**
     * 
     */
    java.util.Collection findByContact(java.lang.String contact) throws javax.ejb.FinderException;

    /**
     * 
     */
    java.util.Collection findByNote(java.lang.String note) throws javax.ejb.FinderException;
    
    
}
