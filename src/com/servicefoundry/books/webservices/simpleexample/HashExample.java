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
package com.servicefoundry.books.webservices.simpleexample;
import java.util.Vector;
/**
 *
 * @author  default
 */
public class HashExample {
    
    /** Creates a new instance of HashExample */
    public HashExample() {
        Vector v = new Vector(1);
        SimplePerson p1 = new SimplePerson();
        p1.name = "Paul";
        System.out.println("Putting Paul into the Hashtable");
        v.add(p1);
        
        SimplePerson p2 = (SimplePerson)v.get(0);
        System.out.println("Getting the Person and Changing the Name");
        p2.name = "Paul2";
        
        SimplePerson p3 = (SimplePerson)v.get(0);
        System.out.println("Name from Hashtable was: "+p3.name);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new HashExample();
    }
    
    class SimplePerson {
        public String name;
    }
}
