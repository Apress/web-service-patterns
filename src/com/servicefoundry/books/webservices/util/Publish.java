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
package com.servicefoundry.books.webservices.util;
import com.servicefoundry.books.webservices.entities.UDDIKeyCache;
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
 * <p>
 * This class creates a UDDI structure for the P.T. Monday Coffee Company
 * and populates it with contact data and the Product Collection web service
 * information. This class is also responsible for searching and removing
 * elements from the UDDI registry.
 * </p>
 * <p>
 * There are two options for running this program, "publish" or "unpublish".
 * </p>
 * <p>
 * <b>NOTE: </b>This class needs a good refactoring. I've started by inserting
 * a UDDI cache so I don't have to continuously look things up, but this is
 * just a start. I need to implement a "service cache" design pattern here.
 * </p>
 * @author  pmonday@attbi.com
 */
public class Publish {
    
    /**
     * The property file gets used to retrieve various dynamic information,
     * like the service name.
     */
    public static final String propertyFile = "build";
    
    /**
     * A key cache used to save information about TModel keys and difficult
     * to locate information. We want to avoid having to lookup information
     * and traversing the UDDI structures if we don't need to.
     */
    public static UDDIKeyCache keyCache = new UDDIKeyCache();
    
    /**
     * A readable name for the product collection tmodel
     */ 
    public static final String PRODUCT_COLLECTION_TMODEL_NAME=
       "Product Collection tModel";
    
    /** Creates a new instance of Publish */
    public Publish() {
    }

    /**
     * Locates a business based on a uddiBusinessName. For this method,
     * the business name gets extracted from the resource bundle. You would
     * probably want to generalize this method for your own needs.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param properties the resource bundle containing the "uddiBusinessName" key and value
     * @return a UDDI business entity object instance
     */
    public static BusinessEntity findBusinessByName(
        UDDIProxy uddi, ResourceBundle properties) 
        throws Exception 
    {
        BusinessEntity rtrnValue = null;
        Name name = new Name(properties.getString("uddiBusinessName"));
        Vector names = new Vector(1);
        names.add(name);
        BusinessList list = uddi.find_business(
            names, null, null, null, null, null, 1);
        BusinessInfos infos = list.getBusinessInfos();
        if(infos.size()>=1){
            BusinessInfo info = infos.get(0);
            String businessKey = info.getBusinessKey();
            BusinessDetail business = uddi.get_businessDetail(businessKey);
            Vector businessEntities = business.getBusinessEntityVector();
            rtrnValue = (BusinessEntity)businessEntities.elementAt(0);
        }
        return rtrnValue;
    }

    /**
     * Creates a coffee and tea business based on the name in the property bundle.
     *
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authInfo authorization information necessary to modify the UDDI registry
     * @param properties the resource bundle containing the "uddiBusinessName" key and value
     * @return a UDDI business entity object instance
     */
    public static BusinessEntity createBusiness(
        UDDIProxy uddi,
        String authInfo,
        ResourceBundle properties)
        throws Exception 
    {
        BusinessEntity rtrnValue = null;
        
        BusinessEntity newEntity =
        new BusinessEntity("",
            properties.getString("uddiBusinessName"));
        
        // add the NAICS coffee roasting code so we can be found
        CategoryBag catBag = new CategoryBag();
        KeyedReference naicsRef = new KeyedReference();
        naicsRef.setTModelKey("UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
        naicsRef.setKeyName("naics: Coffee and Tea Manufact");
        naicsRef.setKeyValue("31192");
        catBag.add(naicsRef);
        newEntity.setCategoryBag(catBag);
        // now add a contact
        Contacts contacts = new Contacts();
        
        {
            Contact primaryContact = new Contact("Paul Monday");
            Vector addresses = new Vector(1);
            Address address = new Address();
            address.setTModelKey("uddi:ubr.uddi.org:postalAddress");
            Vector addressLines = new Vector(6);
            AddressLine ad0 = new AddressLine("500");
            ad0.setKeyName("House Number");
            ad0.setKeyValue("70");
            AddressLine ad1 = new AddressLine("Eldorado Boulevard");
            ad1.setKeyName("Street");
            ad1.setKeyValue("60");
            AddressLine ad2 = new AddressLine("Broomfield");
            ad2.setKeyName("City");
            ad2.setKeyValue("40");
            AddressLine ad3 = new AddressLine("CO");
            ad3.setKeyName("State");
            ad3.setKeyValue("30");
            AddressLine ad4 = new AddressLine("80021");
            ad4.setKeyName("Zip Code");
            ad4.setKeyValue("25");
            AddressLine ad5 = new AddressLine("USA");
            ad5.setKeyName("Country");
            ad5.setKeyValue("20");
            addressLines.add(ad0);
            addressLines.add(ad1);
            addressLines.add(ad2);
            addressLines.add(ad3);
            addressLines.add(ad4);
            addressLines.add(ad5);
            address.setAddressLineVector(addressLines);
            addresses.add(address);
            primaryContact.setAddressVector(addresses);
            Vector emailVector = new Vector(2);
            emailVector.add(new Email("pmonday@attbi.com"));
            emailVector.add(new Email("pmonday@hotmail.com"));
            primaryContact.setEmailVector(emailVector);
            contacts.add(primaryContact);
        }
        {
            Contact primaryContact = new Contact(
                properties.getString("uddiBusinessName"));
            Vector addresses = new Vector(1);
            Address address = new Address();
            address.setTModelKey("uddi:ubr.uddi.org:postalAddress");
            Vector addressLines = new Vector(6);
            AddressLine ad0 = new AddressLine("100");
            ad0.setKeyName("House Number");
            ad0.setKeyValue("70");
            AddressLine ad1 = new AddressLine("Eldorado Boulevard");
            ad1.setKeyName("Street");
            ad1.setKeyValue("60");
            AddressLine ad2 = new AddressLine("Broomfield");
            ad2.setKeyName("City");
            ad2.setKeyValue("40");
            AddressLine ad3 = new AddressLine("CO");
            ad3.setKeyName("State");
            ad3.setKeyValue("30");
            AddressLine ad4 = new AddressLine("80021");
            ad4.setKeyName("Zip Code");
            ad4.setKeyValue("25");
            AddressLine ad5 = new AddressLine("USA");
            ad5.setKeyName("Country");
            ad5.setKeyValue("20");
            addressLines.add(ad0);
            addressLines.add(ad1);
            addressLines.add(ad2);
            addressLines.add(ad3);
            addressLines.add(ad4);
            addressLines.add(ad5);
            address.setAddressLineVector(addressLines);
            addresses.add(address);
            primaryContact.setAddressVector(addresses);
            Vector emailVector = new Vector(2);
            emailVector.add(new Email("info@servicefoundry.com"));
            primaryContact.setEmailVector(emailVector);
            contacts.add(primaryContact);
        }
        
        newEntity.setContacts(contacts);
        
        // add the business entity description
        newEntity.setDefaultDescriptionString(
        "P.T. Monday Coffee Company Business Description");
        
        
        Vector newEntities = new Vector(1);
        newEntities.add(newEntity);
        BusinessDetail business =
        uddi.save_business(authInfo, newEntities);
        
        Vector businessEntities =
        business.getBusinessEntityVector();
        rtrnValue = (BusinessEntity)businessEntities.elementAt(0);
        
        return rtrnValue;
    }
    
    /**
     * Removes a business from the UDDI structure.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authToken authorization information necessary to modify the UDDI registry
     * @param businessEntity the business entity to delete
     * @return always returns true
     */
    public static boolean deleteBusiness(
        UDDIProxy uddi, 
        String authToken, 
        BusinessEntity businessEntity) 
        throws Exception 
    {
        boolean rtrnValue = true;
        
        uddi.delete_business(authToken, businessEntity.getBusinessKey());
        
        return rtrnValue;
    }
    
    /**
     * Deletes tmodels for the unpublish path.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authToken authorization information necessary to modify the UDDI registry
     * @param businessKey no longer used 
     * @return always returns true
     */
    public static boolean deleteTModels(
        UDDIProxy uddi, 
        String authToken,  
        String businessKey) 
        throws Exception 
    {
        boolean rtrnValue = true;
        
        TModelList tModelList = uddi.find_tModel(
            Publish.PRODUCT_COLLECTION_TMODEL_NAME,
            null,
            null,
            null,
            5);
        
        TModelInfos tmodelInfos = tModelList.getTModelInfos();
        int size = tmodelInfos.size();
        for(int i=0 ; i<size ; i++){
            TModelInfo info = (TModelInfo)tmodelInfos.get(i);
            String key = info.getTModelKey();
            System.out.println("Deleting TModel: "+key);
            uddi.delete_tModel(authToken, key);
        }
        
        return rtrnValue;
    }
    
    /**
     * Creates TModels for the service to be offered in UDDI.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authToken authorization information necessary to modify the UDDI registry
     * @param properties the resource bundle containing the "uddiBusinessName" key and value
     */
    public static void createTModels(
        UDDIProxy uddi, 
        String authToken, 
        ResourceBundle properties) 
        throws Exception 
    {
        TModel tm = new TModel();
        tm.setName(Publish.PRODUCT_COLLECTION_TMODEL_NAME);
        tm.setTModelKey("");
        
        OverviewDoc od = new OverviewDoc();
        od.setOverviewURL(
            properties.getString("axisbaseserviceurl")+
            "/services/ProductCollection?wsdl");
        tm.setOverviewDoc(od);
        
        Vector keyedReferences = new Vector(1);
        KeyedReference kr1 =
        new KeyedReference("uddi-org:types", "wsdlSpec");
        kr1.setTModelKey("UUID:C1ACF26D-9672-4404-9D70-39B756E62AB4");
        keyedReferences.add(kr1);
        
        CategoryBag cb = new CategoryBag();
        cb.setKeyedReferenceVector(keyedReferences);
        tm.setCategoryBag(cb);
        
        Vector tModels = new Vector(5);
        tModels.add(tm);
        
        TModelDetail tModelDetail =
        uddi.save_tModel(authToken, tModels);
        Vector tmodels = tModelDetail.getTModelVector();
        TModel tmodel = (TModel)tmodels.elementAt(0);
        String tModelKey = tmodel.getTModelKey();
        keyCache.add("ProductCollectionTModel", tModelKey);
    }
    
    /**
     * Creates a service in UDDI.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authInfo authorization information necessary to modify the UDDI registry
     * @param businessKey key of the P.T. Monday Coffee Company
     * @return a UDDI business entity object instance
     */
    public static ServiceDetail createService(
        UDDIProxy uddi, 
        String authInfo, 
        String businessKey) throws Exception 
    {
        ServiceDetail rtrnValue = null;
        
        Vector services = new Vector(1);
        
        BusinessService bs = new BusinessService();
        Vector names = new Vector(1);
        names.add(new Name("Product Collection"));
        bs.setNameVector(names);
        bs.setBusinessKey(businessKey);
        bs.setServiceKey("");
        
        Vector templateVector = new Vector(0);
        BindingTemplates bindingTemplates = new BindingTemplates();
        bindingTemplates.setBindingTemplateVector(templateVector);
        bs.setBindingTemplates(bindingTemplates);
        
        // add keyed references here...
        services.add(bs);
        
        // at services for others
        rtrnValue = uddi.save_service(authInfo, services);
        
        return rtrnValue;
    }
    
    /**
     * Creates a binding detail in UDDI.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authInfo authorization information necessary to modify the UDDI registry
     * @param serviceDetail a service detail object containing a set of business services
     * @param properties the resource bundle containing the "uddiBusinessName" key and value
     * @return a UDDI business entity object instance
     */
    public static BindingDetail createBindings(
        UDDIProxy uddi, 
        String authInfo, 
        ServiceDetail serviceDetail, 
        ResourceBundle properties) throws Exception 
    {
        BindingDetail rtrnValue = null;
        
        Vector businessServices =
            serviceDetail.getBusinessServiceVector();
        BusinessService businessService =
        (BusinessService)businessServices.elementAt(0);
        String businessServiceKey = businessService.getServiceKey();
        
        BindingTemplate bt = new BindingTemplate();
        bt.setServiceKey(businessServiceKey);
        bt.setBindingKey("");
        
        AccessPoint ap = new AccessPoint();
        ap.setURLType("http");
        ap.setText(
        properties.getString("axisbaseserviceurl")+
            "/services/ProductCollection");
        bt.setAccessPoint(ap);
        
        OverviewDoc od = new OverviewDoc();
        od.setOverviewURL(
        properties.getString("axisbaseserviceurl")+
            "/services/ProductCollection?wsdl");
        InstanceDetails instanceDetails = new InstanceDetails();
        instanceDetails.setOverviewDoc(od);
        
        TModelInstanceInfo tmii = new TModelInstanceInfo(
            keyCache.get("ProductCollectionTModel"));
        tmii.setInstanceDetails(instanceDetails);
        Vector tmiiv = new Vector();
        tmiiv.addElement(tmii);
        TModelInstanceDetails tmid = new TModelInstanceDetails();
        tmid.setTModelInstanceInfoVector(tmiiv);
        bt.setTModelInstanceDetails(tmid);
        
        Vector entities = new Vector();
        entities.addElement(bt);
        rtrnValue = uddi.save_binding(authInfo, entities);
        
        return rtrnValue;
    }
    
    /**
     * Locates a business based on the previous tmodel numbers stuffed into
     * the key cache.
     * @param uddi a proxy to the UDDI directory that you are using
     * @param authInfo authorization information necessary to modify the UDDI registry
     */
    public static void testLocate(
        UDDIProxy uddi, 
        String authInfo) throws Exception 
    {
        Vector names = new Vector(0);
        DiscoveryURLs discoveryURLs = new  DiscoveryURLs();
        
        TModelBag tModelBag = new TModelBag();
        String tModelKeyString = keyCache.get("ProductCollectionTModel");
        System.out.println("Searching for businesses that implement tModel "+tModelKeyString);
        TModelKey tModelKey = new TModelKey(tModelKeyString);
        tModelBag.add(tModelKey);
        
        IdentifierBag identifierBag = new IdentifierBag();
        
        CategoryBag categoryBag = new CategoryBag();
        KeyedReference naicsRef = new KeyedReference();
        naicsRef.setTModelKey("UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
        naicsRef.setKeyName("naics: Coffee and Tea Manufacturer");
        naicsRef.setKeyValue("31192");
        categoryBag.add(naicsRef);
        
        FindQualifiers findQualifiers = new FindQualifiers();
        
        BusinessList businessList = uddi.find_business(null,
            null,
            null,
            categoryBag,
            tModelBag,
            null,
            5);
        
        BusinessInfos businessInfos = businessList.getBusinessInfos();
        System.out.println("Located the following businesses...");
        for(int i=0 ; i<businessInfos.size() ; i++){
            BusinessInfo bi = businessInfos.get(i);
            String name = bi.getNameString();
            System.out.println("\tName="+name);
        }
    }
    
    /**
     * Publishes or removes the P.T. Monday Coffee Company to or from the
     * UDDI location specified in the property file. Information about the user
     * and password to use for UDDI is also available in the property file, the
     * property file used is hardcoded as "build.properties".
     *
     * @param args the command line arguments, single parameter containing either
     * "publish" or "unpublish". This determines the behavior of the main class
     * to either publish to UDDI or remove the entries from UDDI.
     */
    public static void main(String[] args) {
        try {
            // get the build properties to obtain uddi parameters
            ResourceBundle properties = PropertyResourceBundle.getBundle(propertyFile);
            System.out.println("Inquiry URL="+properties.getString("uddiInquiryURL"));
            System.out.println("Publish URL="+properties.getString("uddiPublishURL"));
            
            UDDIProxy uddi = new UDDIProxy();
            uddi.setInquiryURL(properties.getString("uddiInquiryURL"));
            uddi.setPublishURL(properties.getString("uddiPublishURL"));
            AuthToken token = uddi.get_authToken(
            properties.getString("uddiUserId"),
            properties.getString("uddiPassword"));
            
            System.out.println("Locating the business...");
            BusinessEntity businessEntity = Publish.findBusinessByName(uddi, properties);
            if((args.length!=0 && args[0].equals("publish")) || args.length==0){
                if(businessEntity==null) {
                    System.out.println("\tBusiness Not Found...Create Business...");
                    businessEntity = Publish.createBusiness(uddi, token.getAuthInfoString(), properties);
                } else {
                    System.out.println("Business Found!");
                }
                
                System.out.println("Business should have been created...");
                if(businessEntity!=null) {
                    System.out.println("Business key:  "+businessEntity.getBusinessKey());
                    ServiceDetail serviceDetail = Publish.createService(uddi, token.getAuthInfoString(), businessEntity.getBusinessKey());
                    Publish.createTModels(uddi, token.getAuthInfoString(), properties);
                    BindingDetail bindingDetail = Publish.createBindings(uddi, token.getAuthInfoString(), serviceDetail, properties);
                    Publish.testLocate(uddi, token.getAuthInfoString());
                } else {
                    System.out.println("\tOH OH Business is null...exit ");
                }
                
            } else if(args[0].equals("delete")) {
                if(businessEntity != null){
                    System.out.println("Deleting Business Information");
                    System.out.println("Business key:  "+businessEntity.getBusinessKey());
                    Publish.deleteBusiness(uddi, token.getAuthInfoString(), businessEntity);
                    Publish.deleteTModels(uddi, token.getAuthInfoString(), businessEntity.getBusinessKey());
                } else {
                    System.out.println("Cannot delete business, cannot find it");
                }
                
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
