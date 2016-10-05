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
package com.servicefoundry.books.webservices.eventservice;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.MalformedURLException;
import javax.swing.JTextArea;

/** Unicast remote object implementing remote interface.
 *
 * @author pm141145
 * @version 1.0
 */
public class ApplicationSubscriberImpl 
    extends java.rmi.server.UnicastRemoteObject 
    implements ApplicationSubscriber {
        
    JTextArea ivTextArea = null;
    /** Constructs ApplicationSubscriberImpl object and exports it on default port.
     */
    public ApplicationSubscriberImpl(JTextArea textArea) throws RemoteException {
        super();
        ivTextArea = textArea;
    }
    
    /** Constructs ApplicationSubscriberImpl object and exports it on specified port.
     * @param port The port for exporting
     */
    public ApplicationSubscriberImpl(int port, JTextArea textArea) 
        throws RemoteException {
        super(port);
        ivTextArea = textArea;
    }
    
    /** Register ApplicationSubscriberImpl object with the RMI registry.
     * @param name - name identifying the service in the RMI registry
     * @param create - create local registry if necessary
     * @throw RemoteException if cannot be exported or bound to RMI registry
     * @throw MalformedURLException if name cannot be used to construct a valid URL
     * @throw IllegalArgumentException if null passed as name
     */
    public static void registerToRegistry(
        String name, Remote obj, boolean create) 
        throws RemoteException, MalformedURLException{
        
        if (name == null) 
            throw new IllegalArgumentException(
            "registration name can not be null"
            );
        
        try {
            Naming.rebind(name, obj);
        } catch (RemoteException ex){
            if (create) {
                Registry r = 
                    LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                r.rebind(name, obj);
            } else throw ex;
        }
    }
    
    public void update(String eventId, String data) throws RemoteException {
        ivTextArea.append("Event ID: "+eventId);
        ivTextArea.append("Event Data: "+data);
    }
}
