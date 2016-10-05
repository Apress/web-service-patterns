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
package com.servicefoundry.books.webservices.patterns.eventmonitor;
import com.servicefoundry.books.webservices.processes.stubs.*;
import java.util.Observable;
import com.servicefoundry.books.webservices.processes.ProductOrderManager;

/**
 * The Event Monitor class monitors an Order Web Services for changes
 * in the order status, then notifies any observers that have registered
 * with it.
 * @author  pmonday@attbi.com
 */
public class ProductOrderEventMonitor extends Observable implements Runnable {
    
    /** The frequency in milliseconds to poll the holder of the event data.
     */
    protected int frequencyInMillis;
    
    /**
     * debug flag
     */
    private static final boolean debug = true;
    
    private String orderId = null;
    private OrderStatusInfoSnapshot lnkOrderStatusInfoSnapshot;
    private ProductOrderManager lnkProductOrderManager;
    
    /** Creates a new instance of EventMonitor */
    private ProductOrderEventMonitor(String orderId) {
        this.orderId = orderId;
    }
    
    public ProductOrderEventMonitor(String orderId, int frequency) {
        this.orderId = orderId;
        this.frequencyInMillis = frequency;
    }
    
    /**
     * Run the polling cycle as a thread. Each cycle will check
     * an order for a change, if a change occurred, then notify
     * any observers
     */
    public void run() {
        OrderStatusInfoSnapshot snapshot = new OrderStatusInfoSnapshot();
        OrderStatusInfoSnapshot currentData = new OrderStatusInfoSnapshot();
        
        try {
            ProductOrderManagerService service = 
                new ProductOrderManagerServiceLocator();
            com.servicefoundry.books.webservices.processes.stubs.ProductOrderManager 
                port = 
                (com.servicefoundry.books.webservices.processes.stubs.ProductOrderManager)
                service.getProductOrderManager();
            
            while(true){
                int status = port.getStatus(orderId);
                currentData.setData(status);
                if(!snapshot.equals(currentData)){
                    snapshot.setData(status);
                    setChanged();
                    notifyObservers(snapshot);
                }
                try {
                    Thread.sleep(frequencyInMillis);
                } catch (InterruptedException ie) {
                    
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
