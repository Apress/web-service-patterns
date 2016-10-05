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
/**
 * Additional customer information that is not typically a part of the primary
 * customer record. We may add additional data to this class later as we have
 * new mechanisms to contact our customers or we collect more information
 * about our customers.
 *
 * The collection of credit cards was removed due to a JDO problem early
 * in the cycle, I never reinserted it.
 *
 * @author pmonday@attbi.com
 */
public class CustomerInformationImpl {
    public CustomerInformationImpl() {
    }

    public InternetAddressImpl getInternetAddress(){
            return internetAddress;
        }

    public void setInternetAddress(InternetAddressImpl internetAddress){
            this.internetAddress = internetAddress;
        }

    public AddressImpl getShippingAddress(){
            return shippingAddress;
        }

    public void setShippingAddress(AddressImpl shippingAddress){
            this.shippingAddress = shippingAddress;
        }

    public CreditAccountImpl getCreditAccount(){
            return creditAccount;
        }

    public void setCreditAccount(CreditAccountImpl creditAccount){
            this.creditAccount = creditAccount;
        }

/*
    public Collection getCreditAccounts(){
            return creditAccounts;
        }

    public void setCreditAccounts(Collection creditAccounts){
            this.creditAccounts = creditAccounts;
        }
*/


    /**
     * @clientCardinality 1
     * @supplierCardinality 0..1
     * @link aggregationByValue
     * @directed 
     */
    private InternetAddressImpl internetAddress;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     * @link aggregationByValue 
     */
    private AddressImpl shippingAddress;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     * @link aggregation 
     */
    private CreditAccountImpl
        creditAccount;

    /**
     *@link aggregation
     *      @associates <{CreditAccountImpl}>
     * @clientCardinality 1
     * @supplierCardinality 0..*
     * @directed
     */
    /*private Collection creditAccounts;*/
}
