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
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
/**
 * A simple client that uses SOAP calls to communicate with 
 * the small company Web Service. Use the ant "tcpmon" target
 * to run an Axis utility used to intercept the SOAP calls. Set the "Listen Port"
 * to a port other than the one the Web Service uses, like 8089, then 
 * set the target hostname and the Web Service port appropriately.
 * Typically, you will use localhost and port 8080.
 * service. tcpmon serves as a router after reading the SOAP message
 * and displaying it.
 * @author pmonday@attbi.com
 */
public class DirectSOAPTestClient {
    
    /** Creates a new instance of DirectSOAPTestClient */
    public DirectSOAPTestClient() {
    }
    
    /**
     * Main program, no parameters used.
     * @param args the command line arguments, no arguments are used
     */
    public static void main(String[] args) {
       try {
           // The target location of the Web Service. Target this at
           // the web service itself, or at the port that tcpmon is
           // listening on.
           String endpoint = 
                    "http://localhost:8089/axis/services/SmallCompanyImpl";
     
           // Refer to the Axis JavaDoc for more information on 
           // creating and sending SOAP requests directly from 
           // the SOAP API.
           Service  service = new Service();
           Call     call    = (Call) service.createCall();

           call.setTargetEndpointAddress( new java.net.URL(endpoint) );
           call.setOperationName("getName");
           call.setReturnType(XMLType.XSD_STRING);
           String ret = (String) call.invoke(new Object[]{});

           System.out.println("Received '" + ret + "'");
       } catch (Exception e) {
           System.err.println(e.toString());
       }
    }
    
}
