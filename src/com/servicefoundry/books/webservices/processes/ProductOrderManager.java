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
package com.servicefoundry.books.webservices.processes;
import com.servicefoundry.books.webservices.patterns.observer.stubs.*;
import java.util.*;
import java.net.*;
import com.servicefoundry.books.webservices.entities.*;
import javax.jdo.PersistenceManager;
import com.servicefoundry.books.webservices.util.JDOUtilities;
import javax.jdo.Transaction;
import javax.jdo.Extent;
import javax.jdo.Query;

/**
 *
 * @author  default
 * @stereotype web service
 */
public class ProductOrderManager {
    
    /** Creates a new instance of ProductOrderManager */
    public ProductOrderManager() {
    }
    
    public String createProductOrder(String customer,String[] productSkus, int[] quantity) 
        throws java.rmi.RemoteException
    {
        String key = null;
        try {
            ProductOrderImpl poi = new ProductOrderImpl(
                customer,
                productSkus,
                quantity);
            
            key = poi.createProductOrder();
            Thread t = new Thread(poi);
            t.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
    
    public int getStatus(String orderId) throws java.rmi.RemoteException {
        int status;
        OrderCollectionImpl collection = new OrderCollectionImpl();
        OrderKey key = new OrderKey();
        key.orderId = orderId;
        OrderImpl order = collection.getOrder(key);
        status = order.getStatus();
        return status;
    }
    
    public void addObserver(String orderId, String subscriptionUrl){
        Vector orderListeners = null;
        orderListeners = (Vector)listeners.get(orderId);
        if(orderListeners==null){
            orderListeners = new Vector(2);
            listeners.put(orderId, orderListeners);
        }
        if(!orderListeners.contains(subscriptionUrl)){
            try {
                URL url = new URL(subscriptionUrl);
                orderListeners.add(url);
            } catch (MalformedURLException mue){
                mue.printStackTrace();
            }
        }
    }
    
    public void deleteObserver(String orderId, String subscriptionUrl) {
        Vector orderListeners = null;
        orderListeners = (Vector)listeners.get(orderId);
        if(orderListeners!=null){
            orderListeners.remove(subscriptionUrl);
        }
    }
    
    protected void notify(String orderId){
        Vector orderListeners = null;
        orderListeners = (Vector)listeners.get(orderId);
        if(orderListeners!=null){
            for(int i=0 ; i<orderListeners.size() ; i++){
                try {
                    ProductOrderObserverService service = 
                        new ProductOrderObserverServiceLocator();
                    ProductOrderObserver port = 
                        service.getProductOrderObserver(
                            (URL)orderListeners.elementAt(i)
                        );
                    port.update(orderId);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    protected PersistenceManager getPersistenceManager() {
        return JDOUtilities.getPersistenceManager();
    }
    
    protected PersistenceManager pm = null;
    protected Hashtable listeners = new Hashtable(2);

    /**
     * @clientCardinality 1
     * @supplierCardinality 0..* 
     */
    private ProductOrderImpl lnkProductOrderImpl;
}
