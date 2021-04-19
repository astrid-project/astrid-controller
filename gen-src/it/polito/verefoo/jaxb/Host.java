//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.19 at 09:25:08 AM CET 
//


package it.polito.verefoo.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SupportedVNF" type="{}SupportedVNFType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="NodeRef" type="{}NodeRefType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="cpu" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="cores" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="diskStorage" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="memory" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="maxVNF" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="type" type="{}TypeOfHost" /&gt;
 *       &lt;attribute name="fixedEndpoint" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="active" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "supportedVNF",
    "nodeRef"
})
@XmlRootElement(name = "Host")
public class Host {

    @XmlElement(name = "SupportedVNF")
    protected List<SupportedVNFType> supportedVNF;
    @XmlElement(name = "NodeRef")
    protected List<NodeRefType> nodeRef;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "cpu", required = true)
    protected int cpu;
    @XmlAttribute(name = "cores", required = true)
    protected int cores;
    @XmlAttribute(name = "diskStorage", required = true)
    protected int diskStorage;
    @XmlAttribute(name = "memory", required = true)
    protected int memory;
    @XmlAttribute(name = "maxVNF")
    protected Integer maxVNF;
    @XmlAttribute(name = "type")
    protected TypeOfHost type;
    @XmlAttribute(name = "fixedEndpoint")
    protected String fixedEndpoint;
    @XmlAttribute(name = "active")
    protected Boolean active;

    /**
     * Gets the value of the supportedVNF property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supportedVNF property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupportedVNF().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupportedVNFType }
     * 
     * 
     */
    public List<SupportedVNFType> getSupportedVNF() {
        if (supportedVNF == null) {
            supportedVNF = new ArrayList<SupportedVNFType>();
        }
        return this.supportedVNF;
    }

    /**
     * Gets the value of the nodeRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodeRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodeRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NodeRefType }
     * 
     * 
     */
    public List<NodeRefType> getNodeRef() {
        if (nodeRef == null) {
            nodeRef = new ArrayList<NodeRefType>();
        }
        return this.nodeRef;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the cpu property.
     * 
     */
    public int getCpu() {
        return cpu;
    }

    /**
     * Sets the value of the cpu property.
     * 
     */
    public void setCpu(int value) {
        this.cpu = value;
    }

    /**
     * Gets the value of the cores property.
     * 
     */
    public int getCores() {
        return cores;
    }

    /**
     * Sets the value of the cores property.
     * 
     */
    public void setCores(int value) {
        this.cores = value;
    }

    /**
     * Gets the value of the diskStorage property.
     * 
     */
    public int getDiskStorage() {
        return diskStorage;
    }

    /**
     * Sets the value of the diskStorage property.
     * 
     */
    public void setDiskStorage(int value) {
        this.diskStorage = value;
    }

    /**
     * Gets the value of the memory property.
     * 
     */
    public int getMemory() {
        return memory;
    }

    /**
     * Sets the value of the memory property.
     * 
     */
    public void setMemory(int value) {
        this.memory = value;
    }

    /**
     * Gets the value of the maxVNF property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxVNF() {
        return maxVNF;
    }

    /**
     * Sets the value of the maxVNF property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxVNF(Integer value) {
        this.maxVNF = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TypeOfHost }
     *     
     */
    public TypeOfHost getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeOfHost }
     *     
     */
    public void setType(TypeOfHost value) {
        this.type = value;
    }

    /**
     * Gets the value of the fixedEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFixedEndpoint() {
        return fixedEndpoint;
    }

    /**
     * Sets the value of the fixedEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFixedEndpoint(String value) {
        this.fixedEndpoint = value;
    }

    /**
     * Gets the value of the active property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isActive() {
        if (active == null) {
            return false;
        } else {
            return active;
        }
    }

    /**
     * Sets the value of the active property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setActive(Boolean value) {
        this.active = value;
    }

}