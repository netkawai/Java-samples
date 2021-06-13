/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaxbsample1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kawai
 */
@XmlRootElement
@XmlType(name="PointX")
@XmlAccessorType(XmlAccessType.FIELD)
public class Point {

    @XmlElements({
        @XmlElement(name="ix", type=Integer.class),
        @XmlElement(name="dx", type=Double.class)
    })
    Number x;

    @XmlElements({
        @XmlElement(name="iy", type=Integer.class),
        @XmlElement(name="dy", type=Double.class)
    })
    Number y;

}
