/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaxbsample1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kawai
 */
@XmlRootElement(name="blood")
@XmlEnum(String.class)
@XmlType
public enum BloodType {
    A, B, O, AB
}
