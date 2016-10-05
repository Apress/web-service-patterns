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
package com.servicefoundry.books.webservices.util;
import java.net.*;
import java.io.*;

/**
 *
 * @author  default
 */
public class WSDLRetrieve {
    
    /** Creates a new instance of WSDLRetrieve */
    public WSDLRetrieve() {
    }
    
    /**
     * Retrieve a WSDL file and write it to an output file
     * @param args URL WriteLocation
     */
    public static void main(String[] args) {
        if(args.length != 2) {
            usage();
            System.exit(1);
        }
        try {
            URL url = new URL(args[0]);
            URLConnection connection = url.openConnection();
            InputStream wsdlContents = connection.getInputStream();
            File wsdlFile = new File(args[1]);
            OutputStream wsdlOutputStream = new FileOutputStream(wsdlFile);
            int value = wsdlContents.read();
            while(value != -1) {
                wsdlOutputStream.write(value);
                value = wsdlContents.read();
            }
            wsdlOutputStream.close();
            wsdlContents.close();
        } catch (Exception e) {
        }
        
    }
    
    public static void usage() {
        System.out.println("WSDLRetrieve URL WriteLocation");
    }
    
}
