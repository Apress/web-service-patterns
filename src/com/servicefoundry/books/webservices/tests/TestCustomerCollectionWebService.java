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
import com.servicefoundry.books.webservices.entities.stubs.*;

/**
 * Tests the customer collection web service and serves as the example
 * for Chapter 4 and again for Chapter 7. This class uses an Architecture Adapter generated
 * with Apache Axis tools (WSDL2Java) to communicate with the Customer
 * Collection as if it were a Java object instance.
 * @author  pmonday@attbi.com
 */
public class TestCustomerCollectionWebService {
    
    /** Creates a new instance of TestCustomerCollectionWebService */
    public TestCustomerCollectionWebService() {
    }
    
    /**
     * Simple main program that adds a customer to the customer collection.
     * @param args the command line arguments, none required for this app.
     */
    public static void main(String[] args) {
        try {
            CustomerCollectionImplService service = 
                new CustomerCollectionImplServiceLocator();
            CustomerCollectionImpl port = service.getCustomerCollection();
            
            // create a local customer instance (which we will later
            // insert into the customer collection)
            Address ai = new Address();
            ai.setAddressLine1("Web Service Line 1");
            ai.setAddressLine2("Web Service Line 2");
            ai.setCity("Highlands Ranch");
            ai.setState("CO");
            ai.setZipCode("80129");
            Customer ci = new Customer();
            ci.setAddress(ai);
            ci.setFirstName("Paul (Web)");
            ci.setLastName("Monday");
            
            // add the new customer through the Web Service port
            port.addCustomer(ci);
            
            // retrieve ALL customers from the collection web service
            Customer[] ci3 = port.getCustomers();
            if(ci3!=null) {
                
                System.out.println("Collection retrieved");
                for(int i=0 ; i<ci3.length ; i++){
                    System.out.println(ci3[i].getCustomerId()+","
                        +ci3[i].getLastName()+","
                        +ci3[i].getFirstName());
                    System.out.println("Address");
                    Address temp = ci3[i].getAddress();
                    System.out.println("\t"+temp.getAddressLine1());
                    System.out.println("\t"+temp.getAddressLine2());
                    System.out.println("\t"+temp.getCity());
                    System.out.println("\t"+temp.getState());
                    System.out.println("\t"+temp.getZipCode());
                    CustomerKey ck = new CustomerKey();
                    ck.setCustomerId(ci3[i].getCustomerId());
                    
                    // remove the customer we just added
                    port.removeCustomer(ck);
                }
            } else {
                System.out.println("ci3, no customers retrieved");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
