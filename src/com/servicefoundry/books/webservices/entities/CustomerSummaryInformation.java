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

public class CustomerSummaryInformation {
    public String lastName;
    public String firstName;
    public String id;
    
    public CustomerSummaryInformation(){
    }
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public String getLastName() {
        return lastName;
    }    
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property id.
     * @return Value of property id.
     */ 
    public String getId() {
        return id;
    }
    
    /** Setter for property id.
     * @param id New value of property id.
     */
    public void setId(String id) {
        this.id = id;
    }
    
}
