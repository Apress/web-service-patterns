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
public class InventoryCollectionImpl {
    protected PersistenceManager getPersistenceManager() {
        return JDOUtilities.getPersistenceManager();
    }

    public void removeInventoryItem(ProductKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(InventoryItemImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "sku==querySku";
        String parameter = "String querySku";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(key.sku);
        InventoryItemImpl removedItem = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            removedItem = (InventoryItemImpl)it.next();
            pm.deletePersistent(removedItem);
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
    }

    public InventoryItemImpl getInventoryItem(ProductKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(InventoryItemImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "sku==querySku";
        String parameter = "String querySku";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Object coll = q.execute(key.sku);
        System.out.println("Class is " + coll.getClass().getName());
        Collection c = (Collection)coll;
        InventoryItemImpl locatedItem = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            locatedItem = (InventoryItemImpl)it.next();
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        return locatedItem;
    }

    public InventoryItemImpl[] getInventoryItems() {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(InventoryItemImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while(i.hasNext()){
            v.add(i.next());
        }
        
        InventoryItemImpl[] returnItems = new InventoryItemImpl[v.size()];
        for(int j=0 ; j<v.size() ; j++){
            returnItems[j] = (InventoryItemImpl)v.elementAt(j);
        }
        
        //	the begin and commit are set to the db
        t.commit();
        
        return returnItems;
    }

    public InventoryItemImpl[] getInventoryItemsByName(String name) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(InventoryItemImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "name==productName";
        String parameter = "String productName";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(name);
        pm.retrieveAll(c);
        
        InventoryItemImpl returnItems[] = new InventoryItemImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnItems[j] = (InventoryItemImpl)i.next();
            j++;
        }
        
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        
        return returnItems;
    }

    public void addInventoryItem(InventoryItemImpl item) {
        if (item.getSku() == null) {
            item.setSku(KeyGenerator.generateKey(KeyGenerator.SKU));
        }
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        pm.makePersistent(item);
        // commit the transaction, all changes made between
        //	the begin and commit are set to the db
        t.commit();
    }
    public UnroastedCoffeeBeansImpl[] getUnroastedCoffeeBeansInventoryItems() {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(UnroastedCoffeeBeansImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while(i.hasNext()){
            v.add(i.next());
        }
        
        UnroastedCoffeeBeansImpl[] returnProducts = new UnroastedCoffeeBeansImpl[v.size()];
        for(int j=0 ; j<v.size() ; j++){
            returnProducts[j] = (UnroastedCoffeeBeansImpl)v.elementAt(j);
        }
        
        //	the begin and commit are set to the db
        t.commit();
        
        return returnProducts;
    }

    public UnroastedCoffeeBeansImpl[] getUnroastedCoffeeBeansByName(String name) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(UnroastedCoffeeBeansImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "name==productName";
        String parameter = "String productName";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(name);
        pm.retrieveAll(c);
        
        UnroastedCoffeeBeansImpl returnProducts[] = new UnroastedCoffeeBeansImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnProducts[j] = (UnroastedCoffeeBeansImpl)i.next();
            j++;
        }
        
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        
        return returnProducts;
    }

    public void addUnroastedCoffeeBeansInventoryItem(UnroastedCoffeeBeansImpl item){
        addInventoryItem((InventoryItemImpl)item);
    }
    
    protected PersistenceManager pm = null;
}
