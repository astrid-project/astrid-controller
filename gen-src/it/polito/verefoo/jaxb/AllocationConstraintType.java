//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.19 at 09:25:08 AM CET 
//


package it.polito.verefoo.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AllocationConstraintType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AllocationConstraintType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="forbidden"/&gt;
 *     &lt;enumeration value="forced"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AllocationConstraintType")
@XmlEnum
public enum AllocationConstraintType {

    @XmlEnumValue("forbidden")
    FORBIDDEN("forbidden"),
    @XmlEnumValue("forced")
    FORCED("forced");
    private final String value;

    AllocationConstraintType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AllocationConstraintType fromValue(String v) {
        for (AllocationConstraintType c: AllocationConstraintType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
