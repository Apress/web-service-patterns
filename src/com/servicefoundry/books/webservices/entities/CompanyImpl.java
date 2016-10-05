/**
 * Copyright (c) 2003, Paul B. Monday
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 * Neither the name of the Service Foundry nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.servicefoundry.books.webservices.entities;
import com.servicefoundry.books.webservices.util.Publish;
import org.uddi4j.client.*;
import org.uddi4j.response.*;
import org.uddi4j.datatype.*;
import org.uddi4j.datatype.business.*;
import org.uddi4j.datatype.tmodel.*;
import org.uddi4j.util.*;
import org.uddi4j.datatype.service.*;
import org.uddi4j.datatype.binding.*;
import java.util.*;

/**
 * A simple company implementation. This class implementation is fun
 * since it simply derives data from the UDDI representation of the 
 * company. We could easily expand this company definition to include
 * other UDDI data, like contacts and addresses.
 * @author pmonday@attbi.com
 * @stereotype service implementation 
 */
public class CompanyImpl {
    /**
     * The property file that we'll use to obtain information
     * necessary to interact with UDDI (like userid and password)
     */
    public static final String propertyFile = "build";
    
    /**
     * The name of the TModel that we'll search for
     */
    public static final String PRODUCT_COLLECTION_TMODEL_NAME="Product Collection tModel";

    /**
     * The business entity that we retrieve when the contructor is called. We can use
     * this business entity throughout the life of the object instance
     */
    protected BusinessEntity businessEntity = null;
    
    /**
     * A debug variable 
     */
    private static final boolean debug = true;
    
    /**
     * Constructor
     * Populated the company data from the UDDI entry. Because of the 
     * UDDI interaction, this constructor call tends to be a bit slow.
     * There are a variety of ways we could speed it up or refactor it.
     */
    public CompanyImpl() throws Exception {
        ResourceBundle properties = PropertyResourceBundle.getBundle(propertyFile);
        UDDIProxy uddi = new UDDIProxy();
        uddi.setInquiryURL(properties.getString("uddiInquiryURL"));
        uddi.setPublishURL(properties.getString("uddiPublishURL"));
        AuthToken token = uddi.get_authToken(
        properties.getString("uddiUserId"),
        properties.getString("uddiPassword"));
        businessEntity = Publish.findBusinessByName(uddi, properties);
        if(debug) System.out.println("CompanyImpl() found businessEntity "+businessEntity);
        // get the name
        name = businessEntity.getNameString();
        if(debug) System.out.println("CompanyImpl() name="+name);
        // get the address
        
        // get the contact by the company name
        Contacts contacts = businessEntity.getContacts();
        Vector contactVector = contacts.getContactVector();
        if(contactVector != null){
            if(debug) System.out.println("Contact Vector size="+contactVector.size());
            Iterator i = contactVector.iterator();
            while(address==null && i.hasNext()){
                Contact c = (Contact)i.next();
                String contactName = c.getPersonNameString();
       
                
                if(contactName.equals(name)){
                    /*
                     * Manipulating addresses in a standardized way is
                     * a messy proposition with UDDI. While there are some
                     * standards available, it can be difficult to track down
                     * what the standards entail or how to properly use them.
                     * This code does its best to do it in a standardized way,
                     * please refer to the UDDI specification (this part was
                     * weak at the time of printing).
                     */
                    address = new AddressImpl();
                    Vector addressVector = c.getAddressVector();
                    if(addressVector.size()>0){
                        Address primaryAddress = (Address)addressVector.elementAt(0);
                        Vector addressLineVector = primaryAddress.getAddressLineVector();
                        
                        Iterator j = addressLineVector.iterator();
                        while(j.hasNext()){
                            AddressLine al = (AddressLine)j.next();
                            String keyValue = al.getKeyValue();
                            if(keyValue.equals("30")){
                                address.setState(al.getText());
                            } else if(keyValue.equals("70")){
                                address.setAddressLine1(al.getText());
                            } else if(keyValue.equals("40")){
                                address.setCity(al.getText());
                            } else if(keyValue.equals("25")){
                                address.setZipCode(al.getText());
                            } else if(keyValue.equals("60")){
                                address.setAddressLine2(al.getText());
                            }
                        }
                    }
                }
            }
        } else {
            if(debug) System.out.println("Contact Vector NULL");
        }
    }
    
    /**
     * Returns the contact name
     * @return full name of the contact for the company
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the address of the company
     * @return address object instance
     */
    public AddressImpl getAddress(){
        return address;
    }

    /**
     * Sets the address of the company. This DOES NOT make changes to 
     * the UDDI structure, the address is only changed within this
     * local instance.
     * @param address The new company address
     */
    public void setAddress(AddressImpl address){
        this.address = address;
    }
    
/*
 *  Removed link due to a JDO problem
    public WarehouseImpl getWarehouse(){
        return warehouse;
    }
 */
    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @clientCardinality 1
     * @directed
     */
    // private WarehouseImpl warehouse = new WarehouseImpl();
    
    /**
     * The company address. 
     * @supplierCardinality 1
     * @clientCardinality 1
     * @link aggregationByValue
     */
    private AddressImpl address;
    /**
     * The contact name for this particular company.
     */
    private String name;
}
