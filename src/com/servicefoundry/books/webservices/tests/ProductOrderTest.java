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
package com.servicefoundry.books.webservices.tests;
import com.servicefoundry.books.webservices.processes.stubs.*;
import java.util.Observable;

/**
 * The Event Monitor class monitors an Order Web Services for changes
 * in the order status, then notifies any observers that have registered
 * with it.
 * @author  pmonday@attbi.com
 */
public class ProductOrderTest {
    
    /**
     * debug flag
     */
    private static final boolean debug = true;
    
    private String orderId = null;
    
    /** Creates a new instance of EventMonitor */
    private ProductOrderTest(String orderId) {
        this.orderId = orderId;
    }
    
    /**
     * Test the asynchronous business process path
     * First starts the business process through the manager, then
     * registers with the process for updates.
     */
    public static void testAsynchronousBusinessProcess() {
        try {
            /*
             * Retrieve the product order manager which processes
             * business processes in an asynchronous fashion
             */
            ProductOrderManagerService service = 
                new ProductOrderManagerServiceLocator();
            ProductOrderManager port = service.getProductOrderManager();
            /*
             * Update the customer ID and the productSku with
             * an appropriate set of keys from your database.
             * Obtain this by browsing the product and customer
             * tables.
             */
            String customer = "1049073017981";
            String productSkus[] = {"1049073020315"};
            int quantity[] = {2};
            
            /*
             * Request the product order manager to create
             * a product order. There is no guarantee that
             * the product order will be completed on return
             */
            String jobIdentifier = port.createProductOrder(
                customer, 
                productSkus, 
                quantity);
            System.out.println("Asynchronous Job ID = "+jobIdentifier);
            /*
             * Add an observer to the product order so that we're
             * notified when the status changes
             */
            port.addObserver(
                jobIdentifier, 
                "http://localhost:8080/axis/services/ProductOrderObserver");
            
            /*
             * Retrieve the job status, just for kicks...a real product
             * order wouldn't have changed this fast
             */
            int jobStatus = port.getStatus(jobIdentifier);
            System.out.println("Asynchronous Job Status = "+jobStatus);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Check if your product ids and customer ids match an existing id in the database (see errata.html)");
        }
    }

    /**
     * Test the synchronous business process path
     * Looks up the synchronous business process, ProductOrder,
     * then calls the proper processing sequence.
     */
    public static void testSynchronousBusinessProcess() {
        try {

            ProductOrderImplService service = 
                new ProductOrderImplServiceLocator();
            ProductOrderImpl port = service.getProductOrder();
            /*
             * Update the customer ID and the productSku with
             * an appropriate set of keys from your database.
             * Obtain this by browsing the product and customer
             * tables.
             */
            String customer = "1049073017981";
            String productSkus[] = {"1049073020315"};
            int quantity[] = {2};
            /*
             * The synchronous business process is set up to
             * be three method calls...one to set the data,
             * one to create the order itself, and the next
             * to process the order. In reality, a synchronous
             * business process would combine all of these
             */
            port.setData(customer, productSkus, quantity);
            /*
             * Create the product order
             */
            String jobIdentifier = port.createProductOrder();
            System.out.println("Synchronous Job ID = "+jobIdentifier);
            
            /*
             * Process the order
             */
            port.processOrder();
            
            /*
             * Get the job status. Whereas the asynchronous 
             * product order would not be finished by now,
             * a synchronous process IS completed.
             */
            int jobStatus = port.getStatus(jobIdentifier);
            System.out.println("Synchronous Job Status = "+jobStatus);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Check if your product ids and customer ids match an existing id in the database (see errata.html)");
        }
    }
    
    public static void main(String args[]){
        if("-s".equals(args[0])){
            // use the synchronous business process
            testSynchronousBusinessProcess();
        } else {
            // use the asynchronous business process
            testAsynchronousBusinessProcess();
            
        }
    }
    
}
