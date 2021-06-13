
package org.mytech.entities;


/**
 * This is the business interface for CustInerct enterprise bean.
 */
public interface CustInerctLocalBusiness {
    public abstract java.math.BigDecimal getCustomerid();

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
    
}
