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

import java.util.Collection;
import java.util.LinkedList;

public class OrderImpl {
    public OrderImpl(){
    }
    
    public int getStatus(){
    	return status;
    }

    public void setStatus(int status){
    	this.status = status;
    }

    public String getCustomerId(){ 
        return customerId;
    }

    public void setCustomerId(String customerId){ 
        this.customerId = customerId;
    }

    public OrderLineImpl[] getOrderLines(){
        OrderLineImpl[] lines = new OrderLineImpl[this.orderLines.size()];
        for(int i=0 ; i<orderLines.size() ; i++){
            lines[i] = (OrderLineImpl)orderLines.get(i);
        }
        return lines;
    }

    public void setOrderLines(OrderLineImpl[] orderLines){
        this.orderLines = new LinkedList();
        for(int i=0 ; i<orderLines.length ; i++){
            this.orderLines.add(orderLines[i]);
        }
    }

    /** Getter for property orderId.
     * @return Value of property orderId.
     */
    public String getOrderId() {
        return this.orderId;
    }
    
    /** Setter for property orderId.
     * @param orderId New value of property orderId.
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    /**
     *@link aggregation
     *      @associates <{OrderLineImpl}>
     * @clientCardinality 1
     * @supplierCardinality 1..*
     * @directed
     */
    public LinkedList orderLines;
    public static final int ORDER_ONHOLD=0;
    public static final int ORDER_INCOMPLETE=1;
    public static final int ORDER_READY=2;
    public static final int ORDER_SHIPPED=3;
    public int status = ORDER_INCOMPLETE;
    public String orderId;
    public String customerId;
}
