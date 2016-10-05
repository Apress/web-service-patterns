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
package com.servicefoundry.books.webservices.servicefactory;
import com.servicefoundry.books.webservices.entities.stubs.*;
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
import java.net.URL;

/**
 *
 * @author  pm141145
 */
public class ProductCollectionServiceFactory {
    
    private ProductCollectionServiceFactory() {
        
    }
    
    public static ProductCollectionServiceFactory getInstance() {
        if(pcsf==null){
            pcsf = new ProductCollectionServiceFactory();
        }
        return pcsf;
    }
    
    public ProductCollectionImpl getService(){
        ProductCollectionImpl pci = null;
        
        ProductCollectionImpl[] productArray = getServices();
        if(productArray != null)
            System.out.println("ProductCollectionServiceFactory::getService # of product collections found="+productArray.length);
        else
            System.out.println("ProductCollectionServiceFactory::getService null product collection array");
        pci = getService(productArray);
        
        return pci;
    }
    
    public ProductCollectionImpl getService(
    ProductCollectionImpl[] pcis ){
        ProductCollectionImpl pci = null;
        if(pcis != null && pcis.length > 0) {
            pci = pcis[0];
        }
        return pci;
    }
    
    public ProductCollectionImpl[] getServices() {
        ProductCollectionImpl[] collections = null;
        
            /*
             * locate all of the services that support the ProductCollection
             * technical model.
             */
        BusinessList bl = getBusinessesSupportingModel();
        if(bl==null) System.out.println("getServices businessList is null");
        BusinessInfos infos = bl.getBusinessInfos();
        collections = new ProductCollectionImpl[infos.size()];
        
        for (int i=0 ; i<infos.size() ; i++) {
            collections[i] = getProductCollectionStub(infos.get(i));
        }
        return collections;
    }
    
    protected UDDIProxy getUDDIProxy(){
        try {
            if(uddi==null){
                ResourceBundle properties = getPropertyBundle();
                if(properties!=null){
                    uddi = new UDDIProxy();
                    uddi.setInquiryURL(properties.getString("uddiInquiryURL"));
                    uddi.setPublishURL(properties.getString("uddiPublishURL"));
                }
            }
        } catch (Exception e){
            uddi = null;
            e.printStackTrace();
        }
        return uddi;
    }
    
    protected AuthToken getAuthToken() {
        try {
            if(authToken==null){
                UDDIProxy uddi = getUDDIProxy();
                if(uddi!=null){
                    authToken = uddi.get_authToken(
                    properties.getString("uddiUserId"),
                    properties.getString("uddiPassword")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authToken;
    }
    
    protected ResourceBundle getPropertyBundle() {
        if(properties==null){
            properties = PropertyResourceBundle.getBundle(propertyFile);
        }
        return properties;
    }
    
    protected BusinessList getBusinessesSupportingModel() {
        BusinessList businessList = null;
        Vector names = new Vector(0);
        DiscoveryURLs discoveryURLs = new  DiscoveryURLs();
        TModelBag tModelBag = new TModelBag();
        TModelKey tModelKey = new TModelKey(getServiceTModelKey());
        tModelBag.add(tModelKey);
        
        IdentifierBag identifierBag = new IdentifierBag();
        CategoryBag categoryBag = new CategoryBag();
        KeyedReference naicsRef = new KeyedReference();
        naicsRef.setTModelKey("UUID:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2");
        naicsRef.setKeyName("naics: Coffee and Tea Manufacturer");
        naicsRef.setKeyValue("31192");
        categoryBag.add(naicsRef);
        
        FindQualifiers findQualifiers = new FindQualifiers();
        try {
            businessList = getUDDIProxy().find_business(null,
            null,
            null,
            categoryBag,
            tModelBag,
            null,
            5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessList;
    }
    
    protected String getServiceTModelKey() {
        // String modelKey = keyCache.get("ProductCollectionTModel");
        String modelKey = "UUID:F128D7A0-2CFE-11D7-9FBA-000629DC0A53";
        System.out.println("ProductCollectionFactory::getServiceTModelKey TModel Value = "+modelKey);
        return modelKey;
    }
    
    protected ProductCollectionImpl getProductCollectionStub(
    BusinessInfo info) {
        ProductCollectionImpl stub = null;
        
        try {
            // locate the binding templates
            ServiceInfos sinfos = info.getServiceInfos();
            int sinfoSize = sinfos.size();
            int i=0;
            BindingTemplate correctTemplate = null;
            AccessPoint ap = null;
            Vector tmodels = new Vector(1);
            tmodels.add(getServiceTModelKey());
            while(correctTemplate==null && i<sinfoSize){
                ServiceInfo sinfo = sinfos.get(i);
                
                // add binding template traversal....
                // argh...does it ever end?????????
                BindingDetail detail = getUDDIProxy().find_binding(
                    new FindQualifiers(),
                    sinfo.getServiceKey(),
                    new TModelBag(tmodels),
                    5);
                    
                int j=0;
                Vector btVector = detail.getBindingTemplateVector();
                while(correctTemplate==null && j<btVector.size()) {
                    BindingTemplate bt = (BindingTemplate)btVector.get(j);
                    ap = bt.getAccessPoint();
                    if(ap!=null) {
                        correctTemplate = bt;
                    }
                    j++;
                }
                i++;
            }
            
            if(correctTemplate!=null && ap!=null) {
                String url = ap.getText();
                ProductCollectionImplServiceLocator pcisl =
                new ProductCollectionImplServiceLocator();
                stub = pcisl.getProductCollection(new URL(url));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return stub;
    }
    
    public final String propertyFile = "build";
    public static ProductCollectionServiceFactory pcsf = null;
    protected UDDIProxy uddi = null;
    protected ResourceBundle properties = null;
    protected AuthToken authToken = null;
    protected UDDIKeyCache keyCache = new UDDIKeyCache();
    
    public static final void main(String args[]){
        ProductCollectionServiceFactory serviceFactory = null;
        serviceFactory = ProductCollectionServiceFactory.getInstance();
        ProductCollectionImpl pci = serviceFactory.getService();
        try {
            Product[] pArray = pci.getProducts();
            System.out.println("Products in Collection");
            for(int i = 0 ; i<pArray.length ; i++){
                System.out.println(pArray[i].getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
