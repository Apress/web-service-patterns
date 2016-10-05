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

import javax.jdo.PersistenceManager;
import com.servicefoundry.books.webservices.util.JDOUtilities;
import javax.jdo.Transaction;
import javax.jdo.Extent;
import javax.jdo.Query;
import java.util.Collection;
import java.util.Iterator;
import com.servicefoundry.books.webservices.util.KeyGenerator;
import java.util.Vector;
public class OrderCollectionImpl {
    protected PersistenceManager getPersistenceManager() {
        return JDOUtilities.getPersistenceManager();
    }

    public void removeOrder(OrderKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(OrderImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "orderId==queryId";
        String parameter = "String queryId";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(key.orderId);
        OrderImpl removedOrder = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            removedOrder = (OrderImpl)it.next();
            pm.deletePersistent(removedOrder);
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
    }

    public void changeOrderStatus(OrderKey key, int status) {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        boolean newTransaction = false;
        if(!t.isActive()){
            t.begin();
            newTransaction = true;
        }
        OrderImpl order = getOrder(key);
        if(order!=null) {
            order.setStatus(status);
            pm.makePersistent(order);    
        }
    
        pm.makePersistent(order);    
        if(newTransaction){
            t.commit();
        }
    }

    public OrderImpl getOrder(OrderKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        boolean newTransaction = false;
        if(!t.isActive()){
            t.begin();
            newTransaction = true;
        }
        Extent ext = pm.getExtent(OrderImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "orderId==queryId";
        String parameter = "String queryId";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Object coll = q.execute(key.orderId);
        System.out.println("Class is " + coll.getClass().getName());
        Collection c = (Collection)coll;
        OrderImpl locatedOrder = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            locatedOrder = (OrderImpl)it.next();
        }
        q.closeAll();
        if(newTransaction){
            t.commit();
        }
        return locatedOrder;
    }

    public OrderImpl[] getOrders() {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(OrderImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while(i.hasNext()){
            v.add(i.next());
        }
        
        OrderImpl[] returnOrders = new OrderImpl[v.size()];
        for(int j=0 ; j<v.size() ; j++){
            returnOrders[j] = (OrderImpl)v.elementAt(j);
        }
        
        t.commit();
        
        return returnOrders;
    }

    public OrderImpl[] getOrdersByCustomer(CustomerKey customer) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(OrderImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "customerId==queryId";
        String parameter = "String queryId";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(customer.customerId);
        pm.retrieveAll(c);
        
        OrderImpl returnOrders[] = new OrderImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnOrders[j] = (OrderImpl)i.next();
            j++;
        }
        
        q.closeAll();
        t.commit();
        
        return returnOrders;
    }

    public OrderKey addOrder(OrderImpl order) {
        OrderKey orderKey = null;
        if (order.getOrderId() == null) {
            order.setOrderId(KeyGenerator.generateKey(KeyGenerator.ORDER));
        }
        orderKey = new OrderKey();
        orderKey.orderId = order.getOrderId();
        
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        boolean newTransaction = false;
        if(!t.isActive()){
            t.begin();
            newTransaction = true;
        }
        pm.makePersistent(order);

        if(newTransaction){
            t.commit();
        }
        return orderKey;
    }

    
    protected PersistenceManager pm = null;
}
