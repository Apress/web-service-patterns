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
import java.util.*;
import com.servicefoundry.books.webservices.entities.*;
import javax.jdo.PersistenceManager;
import com.servicefoundry.books.webservices.util.JDOUtilities;
import javax.jdo.Transaction;
import javax.jdo.Extent;
import javax.jdo.Query;

/**
 * A partially implemented business process. This example goes with 
 * Chapter 8. This is only partially implemented to show structure,
 * but should be fully implemented over time.
 * @author pmonday@attbi.com
 */
public class ProductOrderImpl implements Runnable, BusinessProcess {
    /**
     * A simple data pool to communicate information
     * between steps of the process (the activities).
     * The process also uses the data to branch appropriately.
     * This data pool <i>should</i> be persistent to ensure
     * that the process is recoverable.
     */
    Hashtable data = null;
    
    /**
     * Process setup data, the customer identifier
     */
    String customer = null;
    
    /**
     * The ordered product skus, setup data for the process
     */
    String[] productSkus = null;
    
    /**
     * Product quantity information, setup data for the process
     */
    int[] quantity = null;
    
    /**
     * Simple boolean indicating success of the process
     */
    boolean successful = false;
    
    /**
     * A unique identifier for the process itself (not the
     * instance of the running process, but the process
     * itself)
     */
    String key = "ProductOrder";
    
    /**
     * A status attribute that we could check
     * as the process continues
     */
    int status = -1;
    
    /**
     * Sequence of business activities that could be used in the 
     * business process.
     *@link aggregation
     *@associates <{BusinessActivity}>
     */
    private LinkedList sequence;

    /**
     * Indicator for whether the process is complete
     */
    boolean complete = false;
    
    /**
     * Any exceptions that may have occurred while 
     * the process executes.
     */
    Exception e = null;
    
    /**
     * Final return object, if necessary.
     */
    String obj = null;
    
    public ProductOrderImpl(){
    }
    
    /** 
     * Sets up the business process and sets the appropriate 
     * data.
     */
    public ProductOrderImpl(String customer, 
        String[] productSkus, 
        int[] quantity)
    {
        setData(customer,productSkus,quantity);
    }
 
    /**
     * Sets up the business process data and sequences for use
     * by a client.
     */
    protected void setup() {
        sequence = new LinkedList();
        data = new Hashtable(5);
        
        BusinessActivity removeProducts = new RemoveProductQuantity();
        sequence.add(removeProducts);
        
        BusinessActivity shipProducts = new ShipProducts();
        sequence.add(shipProducts);
    }
    
    /**
     * Creates the product order and runs it as a separate
     * process
     * @return the string with the unique identifier of the process
     * that is running
     */
    public String createProductOrder(){
        CreateOrder co = new CreateOrder();
        co.setData(data);
        co.run();
        String key = co.getKey();
        String orderId = (String)co.getReturnValue();
        data.put(key, orderId);
        return orderId;
    }

    /**
     * Processes the order appropriately
     */
    public void processOrder()
    {
        Iterator i = sequence.iterator();
        while(i.hasNext() && successful) {
            BusinessActivity activity = (BusinessActivity)i.next();
            activity.setData(data);
            activity.run();
            successful = activity.isSuccessful();
            data.put(activity.getKey(), activity.getReturnValue());
        }
        
        
    }
    
    /**
     * Runs the business process
     */
    public void run() {
        processOrder();
    }
    
    public Exception getException() {
        return e;
    }
    
    public String getKey() {
        return key;
    }
    
    public Object getReturnValue() {
        return obj;
    }
    
    public int getStatus(String orderId) {
        return status;
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    public boolean isSuccessful() {
        return successful;
    }
    
    /**
     * Reset of business process back to original state
     */
    public void reset() {
    }
    
    /**
     * Sets the bootstrap data and resets the business process
     * to the beginning.
     * @param customer unique customer identifier
     * @param productSkus an array of skus that the customer is ordering
     * @param quantity the quantity of each product ordered, the array 
     * must match the product skus
     */
    public void setData(String customer, 
        String[] productSkus, 
        int[] quantity) {
        setup();
        data.put("PRODUCT_ORDER.CUSTOMER.ID", customer);
        data.put("PRODUCT_ORDER.SKUS", productSkus);
        data.put("PRODUCT_ORDER.QUANTITIES", quantity);
    }
    
    public void setData(Hashtable data){
        this.data = data;
    }
}
