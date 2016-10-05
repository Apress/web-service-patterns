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
import java.util.Observer;
import com.servicefoundry.books.webservices.entities.stubs.*;

/**
 * Changes an order status directly through the OrderCollection.
 * Because a business process simply watches the status in the order,
 * this will alter the business processes status.
 * @author  pmonday@attbi.com
 */
public class ModifyOrderStatus {

    public ModifyOrderStatus() {
    }
    
    /**
     * @param args command line arguments.  arg[0] is the order identifier
     * arg[1] is the status to change to.
     */
    public static void main(String[] args) {
        try {
            OrderCollectionImplService service = 
                new OrderCollectionImplServiceLocator();
            OrderCollectionImpl port = service.getOrderCollection();
        
            OrderKey key = new OrderKey();
            key.setOrderId(args[0]);
            int status = Integer.parseInt(args[1]);
            port.changeOrderStatus(key, status);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
