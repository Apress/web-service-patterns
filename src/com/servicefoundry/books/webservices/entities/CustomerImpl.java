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

/**
 * Represents a single P.T. Monday Coffee Company customer.
 * This class is an example of the "Business Object Pattern" and implements
 * the "Data Transfer Object" pattern to allow quick retrieval of a customer
 * summary (The Data Transfer Object pattern was actually commented out due
 * to a problem...I'll work to get it reinserted later).
 *
 * Data for this class is typically populated through JDO from the 
 * customer collection class.
 *
 * @author pmonday@attbi.com
 */
public class CustomerImpl {
    
    public CustomerImpl(){
    }
    public AddressImpl getAddress(){
            return address;
        }

    public void setAddress(AddressImpl address){
            this.address = address;
        }

    public CustomerInformationImpl getCustomerInformation(){
            return customerInformation;
        }
    public void setCustomerInformation(CustomerInformationImpl customerInformation){
            this.customerInformation = customerInformation;
        }
    public String getFirstName(){
            return firstName;
        }

    public void setFirstName(String firstName){
            this.firstName = firstName;
        }

    public String getLastName(){
            return lastName;
        }

    public void setLastName(String lastName){
            this.lastName = lastName;
        }

	public String toString() {
		String rtrn = customerId+": "+lastName + "," + firstName;
        return rtrn;
    }

    public String getCustomerId(){ return customerId; }

    public void setCustomerId(String customerId){ this.customerId = customerId; }

    /*
    public CustomerSummaryInformation getCustomerSummaryInformation() {
        CustomerSummaryInformation info =
            new CustomerSummaryInformation();
        info.setId(this.customerId);
        info.setFirstName(this.firstName);
        info.setLastName(this.lastName);
	return info;
    }

    public void setCustomerSummaryInformation(CustomerSummaryInformation info) {
        this.firstName = info.getFirstName();
        this.lastName = info.getLastName();
    }
    */
    
    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @clientCardinality 1
     * @directed 
     */
    private AddressImpl address;

    /**
     * @clientCardinality 1
     * @supplierCardinality 0..1
     * @link aggregationByValue 
     */
    private CustomerInformationImpl customerInformation;
    private String firstName;
    private String lastName;
    private String customerId;
}
