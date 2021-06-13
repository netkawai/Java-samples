/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaxbsample1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kawai
 */
@XmlRootElement(name="employee")
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonName {
    // Field of first
    @XmlElement(nillable = true)
    private String first;
    // Field of middle
    @XmlElement(defaultValue = "minami")
    private String middle;
    // Field of last
    @XmlAttribute
    public String last;

    // set Property for First
    public void setFirst(String first) {
        this.first = first;
    }

    // get Property for first
    public String getFirst() {
        return first;
    }
    // set Property for First
    private void setMiddle(String middle) {
        this.middle = middle;
    }

    // get Property for first
    private String getMiddle() {
        return middle;
    }

}
