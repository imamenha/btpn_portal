//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.13 at 11:41:31 AM ICT 
//


package id.co.btpn.corp.dev.appmdwdev02.com_btpn_emoney_ws.wowaccountservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getAccount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commonParam" type="{http://APPMDWDEV02.dev.corp.btpn.co.id/com.btpn.emoney.ws:wowAccountServices}CommonParam2"/>
 *         &lt;element name="account" type="{http://APPMDWDEV02.dev.corp.btpn.co.id/com.btpn.emoney.ws:wowAccountServices}account2"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAccount", propOrder = {
    "commonParam",
    "account"
})
@XmlRootElement(name = "getAccount")
public class GetAccount {

    @XmlElement(required = true, nillable = true)
    protected CommonParam2 commonParam;
    @XmlElement(required = true, nillable = true)
    protected Account2 account;

    /**
     * Gets the value of the commonParam property.
     * 
     * @return
     *     possible object is
     *     {@link CommonParam2 }
     *     
     */
    public CommonParam2 getCommonParam() {
        return commonParam;
    }

    /**
     * Sets the value of the commonParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommonParam2 }
     *     
     */
    public void setCommonParam(CommonParam2 value) {
        this.commonParam = value;
    }

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link Account2 }
     *     
     */
    public Account2 getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link Account2 }
     *     
     */
    public void setAccount(Account2 value) {
        this.account = value;
    }

}
