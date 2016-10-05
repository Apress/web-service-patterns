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
 * A simple test program that retrieves the reference to the Company 
 * Web Service and retrieves simple data from the Web Service.
 * This is an example of interaction with a simple Business Object 
 * pattern implementation.
 *
 * Be sure that your UDDI information is published from Chapter 5 as
 * the Company Web Service relies on the information.
 * @author  pmonday@attbi.com
 */
public class TestCompany {

    public TestCompany() {
    }
    
    /**
     * Requests data from the Company Web Service.
     * @param args the command line arguments, no arguments used
     */
    public static void main(String[] args) {
        try {
            CompanyImplService service = 
                new CompanyImplServiceLocator();
            /*
             * I used port 8080 below, but left the URL connection in
             * so that you could quickly change it and use the monitor
             * from an earlier chapter to observe the SOAP data moving 
             * between the client and the server
             */
            java.net.URL url = 
                new java.net.URL("http://localhost:8080/axis/services/Company");
            CompanyImpl port = service.getCompany(url);
            String name = port.getName();
            System.out.println(name);
            CompanyAddress address = port.getAddress();
            System.out.println(address.getAddressLine1()
                + " " 
                + address.getAddressLine2());
            System.out.println(address.getCity() 
                + ", " + address.getState() 
                + ", " + address.getZipCode());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
