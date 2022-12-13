/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaxbsample1;

/**
 *
 * @author Kawai
 */

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name="")
public class Album {
    @XmlAttribute
    String title;

    @XmlElementWrapper(name = "songs")
    @XmlElement(name = "song")
    List<String> songs;
}