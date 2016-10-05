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
import com.servicefoundry.books.webservices.entities.*;
import java.util.Hashtable;
import java.util.Vector;
/**
 *
 * @author  default
 */
public class ShipProducts implements BusinessActivity {
    
    Exception e = null;
    boolean successful = false;
    boolean complete = false;
    String key = "ProductsInStock";
    Vector obj = new Vector();
    Hashtable values = null;
    
    /** Creates a new instance of RemoveProductQuantity */
    public ShipProducts() {
    }
    
    public synchronized Exception getException() {
        return e;
    }
    
    public synchronized String getKey() {
        return key;
    }
    
    public synchronized Object getReturnValue() {
        return obj;
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    public synchronized boolean isSuccessful() {
        return successful;
    }
    
    public synchronized void reset() {
        e = null;
        obj = new Vector();
        complete = false;
        successful = false;
        this.values = null;       
    }
    
    public synchronized void setData(Hashtable values) {
        this.values = values;
    }
    
    /** When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public synchronized void run() {
        if(values==null){
            successful = false;
            e = new DataNotAvailable();
            complete = true;
            return;
        }
        
        String[] productSkus = (String[])values.get("PRODUCT_ORDER.SKUS");
        int[] quantity = (int[])values.get("PRODUCT_ORDER.QUANTITIES");
        if( (productSkus==null || quantity==null)
            ||
            (productSkus.length!=quantity.length)){
            successful = false;
            e = new DataNotAvailable();
            complete = true;
            return;
        }
        WarehouseImpl wi = new WarehouseImpl();
        
        for(int i = 0; i<productSkus.length ; i++){
            WarehouseProductImpl[] products = 
                wi.removeProductFromWarehouse(productSkus[i], quantity[i]);
            if(products != null) {
                for(int j=0; j<products.length ; j++){
                    obj.add(products[j]);
                }
            }
        }
    }
    
}
