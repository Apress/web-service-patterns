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
import java.util.Iterator;
import java.util.Vector;
import javax.jdo.Query;
import java.util.Collection;
import java.util.Date;

public class WarehouseImpl {
    public WarehouseProductImpl[] getProductsInWarehouse() {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        t.begin();
        Extent ext = pm.getExtent(WarehouseProductImpl.class, false);
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while (i.hasNext()) {
            v.add(i.next());
        }
        WarehouseProductImpl[] returnProducts =
        new WarehouseProductImpl[v.size()];
        for (int j = 0; j < v.size(); j++) {
            returnProducts[j] = (WarehouseProductImpl)v.elementAt(j);
        }
        t.commit();
        return returnProducts;
    }
    
    public WarehouseInventoryItemImpl[] getInventoryItemsInWarehouse() {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        t.begin();
        Extent ext = pm.getExtent(WarehouseProductImpl.class, false);
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while (i.hasNext()) {
            v.add(i.next());
        }
        WarehouseInventoryItemImpl[] returnItems =
        new WarehouseInventoryItemImpl[v.size()];
        for (int j = 0; j < v.size(); j++) {
            returnItems[j] = (WarehouseInventoryItemImpl)v.elementAt(j);
        }
        //	the begin and commit are set to the db
        t.commit();
        return returnItems;
    }
    
    public void addInventoryItemToWarehouse(String sku, int quantity) {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        
        Date d = new Date();
        
        WarehouseInventoryItemImpl wiii = new WarehouseInventoryItemImpl();
        wiii.setEnterWarehouseDate(d);
        wiii.setQuantity(quantity);
        wiii.setSku(sku);
        
        t.begin();
        pm.makePersistent(wiii);
        t.commit();
    }
    
    public void addProductToWarehouse(String sku, int quantity) {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        Date d = new Date();
        
        WarehouseProductImpl wpi = new WarehouseProductImpl();
        wpi.setEnterWarehouseDate(d);
        wpi.setQuantity(quantity);
        wpi.setSku(sku);
        
        t.begin();
        pm.makePersistent(wpi);
        t.commit();
    }
    
    /**
     * Remove Product from Warehouse
     * Create a list of items that should be removed from the warehouse.  This will
     * take the oldest things in the warehouse first, and span warehouses.
     * Caller should verify that the number of items returned matches the number
     * requested.  If the warehouse runs out, only those available will be returned
     * and the caller must take corrective action
     */
    public WarehouseProductImpl[] removeProductFromWarehouse(String sku, int quantity) {
        WarehouseProductImpl[] warehousePickList = null;
        PersistenceManager pm = getPersistenceManager();
        boolean alreadyActive = true;
        
        Transaction t = pm.currentTransaction();
        if(!t.isActive()){
            t.begin();
            alreadyActive = false;
        }
        
        // get all of the products from the warehouse, sort in ascending
        //	order based on the date
        WarehouseProductImpl[] existingProducts = getProductsInWarehouseBySku(sku);
        if(existingProducts.length>0){
            // go through each product quantity resetting the quantity to the
            //	amount removed, remove record if it's 0
            // there are two things that can happen:
            //	1) warehouse item is used up, delete the record and
            //		copy the item to the "pick" list
            //	2) warehouse item is partially used, change the quantity
            //		in the record and create a new object for the pick list
            int remainingQuantity = quantity;
            int i=0;
            Vector pickVector = new Vector(3);
            
            while(remainingQuantity > 0 && i<existingProducts.length){
                WarehouseProductImpl currentProduct = null;
                currentProduct = new WarehouseProductImpl();
                currentProduct.setEnterWarehouseDate(existingProducts[i].getEnterWarehouseDate());
                currentProduct.setQuantity(existingProducts[i].getQuantity());
                currentProduct.setSku(existingProducts[i].getSku());
                /*
                try {
                    currentProduct = (WarehouseProductImpl)existingProducts[i].clone();
                } catch(CloneNotSupportedException cnse){
                    cnse.printStackTrace();
                }
                */
                int currentQuantity = currentProduct.getQuantity();
                
                if(currentQuantity > remainingQuantity){
                    remainingQuantity = 0;
                    existingProducts[i].setQuantity(currentQuantity-remainingQuantity);
                    currentProduct.setQuantity(remainingQuantity);
                    pickVector.add(currentProduct);
                } else if(currentQuantity<=remainingQuantity) {
                    remainingQuantity -= currentQuantity;
                    pm.deletePersistent(existingProducts[i]);
                    pickVector.add(currentProduct);
                }
                
                i++;
            }
            
            warehousePickList = new WarehouseProductImpl[pickVector.size()];
            for(i=0 ; i<pickVector.size() ; i++){
                warehousePickList[i] = (WarehouseProductImpl)pickVector.elementAt(i);
            }
        }
        if(!alreadyActive){
            t.commit();
        }
        return warehousePickList;
    }
    
    public WarehouseInventoryItemImpl[] removeInventoryItemFromWarehouse(String sku, int quantity) {
        WarehouseInventoryItemImpl[] warehousePickList = null;
        PersistenceManager pm = getPersistenceManager();
        
        boolean alreadyActive = true;
        
        Transaction t = pm.currentTransaction();
        if(!t.isActive()){
            t.begin();
            alreadyActive = false;
        }
        
        // get all of the products from the warehouse, sort in ascending
        //	order based on the date
        WarehouseInventoryItemImpl[] existingItems = getInventoryItemsInWarehouseBySku(sku);
        if(existingItems.length>0){
            // go through each product quantity resetting the quantity to the
            //	amount removed, remove record if it's 0
            // there are two things that can happen:
            //	1) warehouse item is used up, delete the record and
            //		copy the item to the "pick" list
            //	2) warehouse item is partially used, change the quantity
            //		in the record and create a new object for the pick list
            int remainingQuantity = quantity;
            int i=0;
            Vector pickVector = new Vector(3);
            
            while(remainingQuantity > 0 && i<existingItems.length){
                WarehouseInventoryItemImpl currentItem = null;
                currentItem = new WarehouseInventoryItemImpl();
                currentItem.setEnterWarehouseDate(existingItems[i].getEnterWarehouseDate());
                currentItem.setQuantity(existingItems[i].getQuantity());
                currentItem.setSku(existingItems[i].getSku());
                /*
                try {
                    currentItem = (WarehouseInventoryItemImpl)existingItems[i].clone();
                } catch(CloneNotSupportedException cnse){
                    cnse.printStackTrace();
                }
                 */
                int currentQuantity = currentItem.getQuantity();
                
                if(currentQuantity > remainingQuantity){
                    remainingQuantity = 0;
                    existingItems[i].setQuantity(currentQuantity-remainingQuantity);
                    currentItem.setQuantity(remainingQuantity);
                    pickVector.add(currentItem);
                } else if(currentQuantity<=remainingQuantity) {
                    remainingQuantity -= currentQuantity;
                    pm.deletePersistent(existingItems[i]);
                    pickVector.add(currentItem);
                }
                
                i++;
            }
            
            warehousePickList = new WarehouseInventoryItemImpl[pickVector.size()];
            for(i=0 ; i<pickVector.size() ; i++){
                warehousePickList[i] = (WarehouseInventoryItemImpl)pickVector.elementAt(i);
            }
        }
        
        if(!alreadyActive){
            t.commit();
        }
        return warehousePickList;
    }
    
    public WarehouseProductImpl[] getProductsInWarehouseBySku(String sku) {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        boolean startedTx = false;
        if(!t.isActive()) {
            t.begin();
            startedTx = true;
        }
        Extent ext = pm.getExtent(WarehouseProductImpl.class, false);
        String filter = "sku==requestedSku";
        String parameter = "String requestedSku";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        q.setOrdering("enterWarehouseDate ascending");
        Collection c = (Collection)q.execute(sku);
        pm.retrieveAll(c);
        
        WarehouseProductImpl returnProducts[] = new WarehouseProductImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnProducts[j] = (WarehouseProductImpl)i.next();
            j++;
        }
        
        q.closeAll();
        
        if(startedTx){
            t.commit();
        }
        
        return returnProducts;
    }
    
    public WarehouseInventoryItemImpl[] getInventoryItemsInWarehouseBySku(String sku) {
        PersistenceManager pm = getPersistenceManager();
        Transaction t = pm.currentTransaction();
        boolean startedTx = false;
        if(!t.isActive()) {
            t.begin();
            startedTx = true;
        }
        Extent ext = pm.getExtent(WarehouseInventoryItemImpl.class, false);
        String filter = "sku==requestedSku";
        String parameter = "String requestedSku";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        q.setOrdering("enterWarehouseDate ascending");
        Collection c = (Collection)q.execute(sku);
        pm.retrieveAll(c);
        
        WarehouseInventoryItemImpl returnItems[] = new WarehouseInventoryItemImpl[c.size()];
        Iterator i = c.iterator();
        int j=0;
        while(i.hasNext()){
            returnItems[j] = (WarehouseInventoryItemImpl)i.next();
            j++;
        }
        
        q.closeAll();
        
        if(startedTx){
            t.commit();
        }
        
        return returnItems;
    }
    
    protected PersistenceManager getPersistenceManager() {
        return JDOUtilities.getPersistenceManager();
    }
    
    protected PersistenceManager pm = null;
}
