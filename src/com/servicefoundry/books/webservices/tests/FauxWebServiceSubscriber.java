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
import java.io.*;
import java.net.*;
import javax.swing.JTextArea;
/**
 *
 * @author  pm141145
 * @stereotype faux web service implementation
 */
public class FauxWebServiceSubscriber implements Runnable {
    
    JTextArea ivTextArea = null;
    int port = 9091;    

    /** @link dependency */
    /*# com.servicefoundry.books.webservices.models.fauximplementation.Subscriber lnkSubscriber; */
    
    FauxWebServiceSubscriber(JTextArea textArea) {
        ivTextArea = textArea;
    }

    FauxWebServiceSubscriber(int port, JTextArea textArea) {
        ivTextArea = textArea;
        this.port = port;
    }
    
    public void run() {
        while (true) {
            try {
                ServerSocket srv = new ServerSocket(port);
                // Wait for connection from client.
                Socket socket = srv.accept();
                String message = readMessage(socket);
                sendResponse(socket, message);
                ivTextArea.setText(message);
            } catch (IOException e) {
            }
        }
    }
    
    String readMessage(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        int val = is.read();
        StringBuffer sb = new StringBuffer();
        while(val!=-1) {
            sb.append((char)val);
            val = is.read();
            System.out.println((char)val);
            if(sb.toString().toLowerCase().endsWith("/soapenv:body>")){
                System.out.println("Completed Message");
                break;
            }
        }
        System.out.println(sb.toString());
        is.close();
        return sb.toString();
    }
    
    void sendResponse(Socket socket, String message) throws IOException {
        BufferedWriter bw = 
            new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        bw.write("HTTP/1.1 200 OK");
        bw.flush();
        bw.write("Content-Type: text/xml; charset=utf-8");
        bw.flush();
        //        sb.append("Date: Fri, 17 Jan 2003 01:56:35 GMT");
        // bw.write("Server: ServiceFoundry/Faux");
        bw.write("Connection: close");
        bw.flush();
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.flush();
        bw.write("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        bw.flush();
        bw.write("<soapenv:Body>");
        bw.flush();
        bw.write("<ns1:updateResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://localhost:8080/axis/services/Subscriber\"/>");
        bw.flush();
        bw.write("</soapenv:Body>");
        bw.flush();
        bw.write("</soapenv:Envelope>");
        bw.flush();
        bw.close();
    }
}
