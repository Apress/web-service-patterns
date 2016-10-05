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
 *
 * @author  pm7218301
 */
public class TestDataTransferObject {
    
    /** Creates a new instance of TestCustomerCollectionWebService */
    public TestDataTransferObject() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            CustomerCollectionImplService service = 
                new CustomerCollectionImplServiceLocator();
            CustomerCollectionImpl port = 
                service.getCustomerCollection();
            CustomerSummaryInformation[] infos =
                port.getCustomerSummaryInformations();
            System.out.println("Printing Customer Summaries");
            
            for(int i=0 ; i<infos.length ; i++){
                System.out.println(infos[i].getId()+"\t"
                    +infos[i].getLastName() +", "
                    +infos[i].getFirstName());
            }
           
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
