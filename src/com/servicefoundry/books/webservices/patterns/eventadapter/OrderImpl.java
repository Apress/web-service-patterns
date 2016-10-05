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
package com.servicefoundry.books.webservices.patterns.eventadapter;
import java.util.Hashtable;

/**
 *
 * @author  default
 */
public class OrderImpl extends java.util.Observable {
    public static final int ORDER_ONHOLD=0;
    public static final int ORDER_INCOMPLETE=1;
    public static final int ORDER_READY=2;
    public static final int ORDER_SHIPPED=3;
    
    /** Holds value of property orderDetails. */
    private Hashtable orderDetails;    
    
    /** Holds value of property orderHeader. */
    private OrderHeaderImpl orderHeader;
    
    /** Holds value of property status. */
    private int status;
    
    /** Creates a new instance of OrderImpl */
    public OrderImpl() {
    }
    
    /** Getter for property orderDetails.
     * @return Value of property orderDetails.
     */
    public Hashtable getOrderDetails() {
        return this.orderDetails;
    }
    
    /** Setter for property orderDetails.
     * @param orderDetails New value of property orderDetails.
     */
    public void setOrderDetails(Hashtable orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    /** Getter for property orderHeader.
     * @return Value of property orderHeader.
     */
    public OrderHeaderImpl getOrderHeader() {
        return this.orderHeader;
    }
    
    /** Setter for property orderHeader.
     * @param orderHeader New value of property orderHeader.
     */
    public void setOrderHeader(OrderHeaderImpl orderHeader) {
        this.orderHeader = orderHeader;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return this.status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
}
