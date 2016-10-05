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
 * A simple class that can easily be turned into a Web Service.
 * @author pmonday@attbi.com
 */
public class SmallCompanyImpl {
    public String getName(){
            return name;
        }

    public void setName(String name){
            this.name = name;
        }

    public String getAddressLine2(){
            return addressLine2;
        }

    public void setAddressLine2(String addressLine2){
            this.addressLine2 = addressLine2;
        }

    public String getAddressLine1(){
            return addressLine1;
        }

    public void setAddressLine1(String addressLine1){
            this.addressLine1 = addressLine1;
        }

    public String getCity(){
            return city;
        }

    public void setCity(String city){
            this.city = city;
        }

    public String getState(){
            return state;
        }

    public void setState(String state){
            this.state = state;
        }

    public String getZipCode(){
            return zipCode;
        }

    public void setZipCode(String zipCode){
            this.zipCode = zipCode;
        }

    private String name="P.T. Monday Coffee Company";
    private String addressLine2="Line1";
    private String addressLine1="Line2";
    private String city="Highlands Ranch";
    private String state="CO";
    private String zipCode="80129";
}
