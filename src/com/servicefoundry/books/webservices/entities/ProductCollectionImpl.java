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

/**
 * @stereotype web service
 */
public class ProductCollectionImpl {
    protected PersistenceManager getPersistenceManager() {
        return JDOUtilities.getPersistenceManager();
    }    
    
    public void removeProduct(ProductKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(ProductImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "sku==querySku";
        String parameter = "String querySku";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(key.sku);
        ProductImpl removedProduct = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            removedProduct = (ProductImpl)it.next();
            pm.deletePersistent(removedProduct);
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
    }

    public ProductImpl getProduct(ProductKey key) {
        boolean newTransaction = false;
        
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        
        if(!t.isActive()){
            t.begin();
            newTransaction = true;
        }
        
        Extent ext = pm.getExtent(ProductImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "sku==querySku";
        String parameter = "String querySku";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Object coll = q.execute(key.sku);
        System.out.println("Class is " + coll.getClass().getName());
        Collection c = (Collection)coll;
        ProductImpl locatedProduct = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            locatedProduct = (ProductImpl)it.next();
        }
        q.closeAll();
        
        if(newTransaction){
            t.commit();
        }
        return locatedProduct;
    }
    
    public ProductImpl[] getProducts() {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        t.begin();
        Extent ext = pm.getExtent(ProductImpl.class, true);
        
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while(i.hasNext()){
            v.add(i.next());
        }
        
        ProductImpl[] returnProducts = new ProductImpl[v.size()];
        for(int j=0 ; j<v.size() ; j++){
            returnProducts[j] = (ProductImpl)v.elementAt(j);
        }
        
        //	the begin and commit are set to the db
        t.commit();
        
        return returnProducts;
    }
    
    public RoastedCoffeeBeansImpl[] getRoastedCoffeeBeansProducts() {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(RoastedCoffeeBeansImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while(i.hasNext()){
            v.add(i.next());
        }
        
        RoastedCoffeeBeansImpl[] returnProducts = new RoastedCoffeeBeansImpl[v.size()];
        for(int j=0 ; j<v.size() ; j++){
            returnProducts[j] = (RoastedCoffeeBeansImpl)v.elementAt(j);
        }
        
        //	the begin and commit are set to the db
        t.commit();
        
        return returnProducts;
    }
    
    public RoastedCoffeeBeansImpl[] getRoastedCoffeeBeansByName(String name) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(RoastedCoffeeBeansImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "name==productName";
        String parameter = "String productName";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(name);
        pm.retrieveAll(c);
        
        RoastedCoffeeBeansImpl returnProducts[] = new RoastedCoffeeBeansImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnProducts[j] = (RoastedCoffeeBeansImpl)i.next();
            j++;
        }
        
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        
        return returnProducts;
    }
    
    public void addRoastedCoffeeBeansProduct(RoastedCoffeeBeansImpl product){
        addProduct((ProductImpl)product);
    }
    
    public ProductImpl[] getProductsByName(String name) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(ProductImpl.class, true);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "name==productName";
        String parameter = "String productName";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(name);
        pm.retrieveAll(c);
        
        ProductImpl returnProducts[] = new ProductImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnProducts[j] = (ProductImpl)i.next();
            j++;
        }
        
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        
        return returnProducts;
    }
    
    public void addProduct(ProductImpl product) {
        if (product.getSku() == null) {
            product.setSku(KeyGenerator.generateKey(KeyGenerator.SKU));
        }
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        pm.makePersistent(product);
        // commit the transaction, all changes made between
        //	the begin and commit are set to the db
        t.commit();
    }
    
    public ProductSummaryInformation[]
        getProductSummaryInformations(String[] keys) {
        ProductSummaryInformation[] infos = null;
        ProductImpl[] products = getProducts();
        Vector values = new Vector(keys.length);
        for(int i=0 ; i<keys.length ; i++){
            values.add(keys[i]);
        }
        if(products!=null) {
            infos = new ProductSummaryInformation[products.length];
            for(int i=0 ; i<products.length ; i++){
                infos[i] = new ProductSummaryInformation();
                infos[i].sku = products[i].getSku();
                if(values.contains("description"))
                    infos[i].description = products[i].getDescription();
                if(values.contains("retailPrice"))
                    infos[i].retailPrice = products[i].getRetailPrice();
                if(values.contains("name"))
                    infos[i].name = products[i].getName();
            }
        }
        return infos;
    }
    
    public ProductSummaryInformation[]
    getProductSummaryInformations() {
        ProductSummaryInformation[] infos = null;
        ProductImpl[] products = getProducts();
        if(products!=null) {
            infos = new ProductSummaryInformation[products.length];
            for(int i=0 ; i<products.length ; i++){
                infos[i] = new ProductSummaryInformation();
                infos[i].description = products[i].getDescription();
                infos[i].sku = products[i].getSku();
                infos[i].retailPrice = products[i].getRetailPrice();
                infos[i].name = products[i].getName();
            }
        }
        return infos;
    }
    
    public void setProductSummaryInformations(
        ProductSummaryInformation[] infos) {
        
        ProductImpl[] updates = new ProductImpl[infos.length];
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        t.begin();
        for(int i=0 ; i<infos.length ; i++){
            ProductKey pk = new ProductKey();
            pk.sku = infos[i].sku;
            updates[i] = getProduct(pk);
            if(infos[i].name != null)
                updates[i].setName(infos[i].name);
            if(infos[i].description != null)
                updates[i].setDescription(infos[i].description);
            if(infos[i].retailPrice != -1f)
                updates[i].setRetailPrice(infos[i].retailPrice);
        }
        t.commit();
    }
    
    protected PersistenceManager pm = null;
}
