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
import javax.jdo.PersistenceManager;
import com.servicefoundry.books.webservices.util.JDOUtilities;
import javax.jdo.Transaction;
import javax.jdo.Extent;
import javax.jdo.Query;
import java.util.Iterator;
import com.servicefoundry.books.webservices.util.KeyGenerator;
import java.util.Vector;

/** 
 * CustomerCollectionImpl represents the P.T. Monday Coffee Company
 * customers in a simple business object collection interface.
 * The class uses JDO as a persistence mechanism for CustomerImpl
 * classes. 
 *
 * The class is an example of the "Business Object Collection" pattern
 * and contains an implementation of the "Data Transfer Collection" pattern.
 *
 * @author pmonday@attbi.com
 * @stereotype service implementation 
 */
public class CustomerCollectionImpl {
    /**
     * Removes a single customer based on the customer key.
     * This method simply returns without error if the customer
     * does not exist in the database.
     * @param key a unique customer key, typically retrieved from a customer object
     */
    public void removeCustomer(CustomerKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(CustomerImpl.class, false);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "customerId==id";
        String parameter = "String id";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(key.customerId);
        CustomerImpl removedCustomer = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            removedCustomer = (CustomerImpl)it.next();
            pm.deletePersistent(removedCustomer);
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
    }

    /**
     * Add a customer to the customer collection. If the key for the
     * customer (the "id" instance variable) 
     * is null, one will be generated before placing the
     * new customer instance into the database.
     * @param customer a new customer object, the customer key does not have to be populated
     */
    public CustomerKey addCustomer(CustomerImpl customer) {
        System.out.println("Adding Customer");
        CustomerKey key = null;
        if (customer.getCustomerId() == null) {
            customer.setCustomerId(KeyGenerator.generateKey(KeyGenerator.CUSTOMER));
        }
        key = new CustomerKey();
        key.customerId = customer.getCustomerId();
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        System.out.println("Making Persistent");
        pm.makePersistent(customer);
        // commit the transaction, all changes made between
        //	the begin and commit are set to the db
        System.out.println("Ending Persistence");
        t.commit();
        System.out.println("Returning Key");
        return key;
    }

    /**
     * Retrieve a single customer instance based on the customer key.
     * @param key a unique customer key
     * @return null if the customer key does not denote an existing customer, 
     * otherwise a fully populated customer instance is returned.
     */
    public CustomerImpl getCustomer(CustomerKey key) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(CustomerImpl.class, false);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "customerId==id";
        String parameter = "String id";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Object coll = q.execute(key.customerId);
        System.out.println("Class is " + coll.getClass().getName());
        Collection c = (Collection)coll;
        CustomerImpl locatedCustomer = null;
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            locatedCustomer = (CustomerImpl)it.next();
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        return locatedCustomer;
    }

    /**
     * Retrieve a customer based on their last and first names. Because
     * names are not unique identifiers, an array of customers is returned
     * to the caller.
     * @param lastName the last name of a customer, may not be null
     * @param firstName the first name of a customer, may not be null
     * @return an array of customer objects that match the first AND last name
     */
    public CustomerImpl[] getCustomersLastFirst(String lastName, String firstName) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(CustomerImpl.class, false);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "lastName==last & firstName=first";
        String parameter = "String last, String first";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(lastName, firstName);
        pm.retrieveAll(c);
        CustomerImpl returnCustomers[] = new CustomerImpl[c.size()];
        Iterator i = c.iterator();
        int j = 0;
        while (i.hasNext()) {
            returnCustomers[j] = (CustomerImpl)i.next();
            j++;
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        return returnCustomers;
    }

    /**
     * Retrieve an array of customers based on their last name. Because
     * customers are not uniquely identified by the last name, an array
     * return value must be used.
     * @param lastName the last name of a customer
     * @return an array of customer objects that have a matched last name
     */
    public CustomerImpl[] getCustomersLast(String lastName) {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(CustomerImpl.class, false);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        String filter = "lastName==last";
        String parameter = "String last";
        Query q = pm.newQuery(ext, filter);
        q.declareParameters(parameter);
        Collection c = (Collection)q.execute(lastName);
        pm.retrieveAll(c);
        CustomerImpl returnCustomers[] = new CustomerImpl[c.size()];
        Iterator i = c.iterator();
        int j = 0;
        while (i.hasNext()) {
            returnCustomers[j] = (CustomerImpl)i.next();
            j++;
        }
        q.closeAll();
        //	the begin and commit are set to the db
        t.commit();
        return returnCustomers;
    }

    /**
     * Retrieve all customers known to P.T. Monday Coffee Company
     * @return an array of populated customer objects, all of those
     * known to the P.T. Monday Coffee Company
     */
    public CustomerImpl[] getCustomers() {
        PersistenceManager pm = getPersistenceManager();
        // retrieve the current transaction
        Transaction t = pm.currentTransaction();
        // start a new transaction
        t.begin();
        Extent ext = pm.getExtent(CustomerImpl.class, false);
        // Do a simple query for a particular department
        // first create a filter with a parameter that can be passed in
        Iterator i = ext.iterator();
        Vector v = new Vector(10);
        while (i.hasNext()) {
            v.add(i.next());
        }
        CustomerImpl[] returnCustomers = new CustomerImpl[v.size()];
        for (int j = 0; j < v.size(); j++) {
            returnCustomers[j] = (CustomerImpl)v.elementAt(j);
        }
        //	the begin and commit are set to the db
        t.commit();
        return returnCustomers;
    }
    
    /**
     * Retrieves customer summary information for ALL customers. The summary
     * information is based on the Data Transfer Collection pattern.
     * @return an array of customer summary information data transfer objects.
     */
    public CustomerSummaryInformation[] getCustomerSummaryInformations() {
        CustomerImpl[] customers = getCustomers();
        CustomerSummaryInformation[] infos =
            new CustomerSummaryInformation[customers.length];
        for (int i = 0; i < customers.length; i++) {
            infos[i] = new CustomerSummaryInformation();
            infos[i].firstName = customers[i].getFirstName();
            infos[i].lastName = customers[i].getLastName();
            infos[i].id = customers[i].getCustomerId();
        }
        return infos;
    }
    
    /**
     * Uses the data transfer collection pattern to set summary information for
     * a group of customers. Individual customer summary information objects 
     * must contain a valid key (the "id" instance variable). This pattern
     * implementation does NOT support partial population.
     * @param infos an array of valid customer summary information objects (these
     * objects are data transfer objects).
     */
    public void setCustomerSummaryInformations(CustomerSummaryInformation[] infos) {
        if (infos != null) {
            PersistenceManager pm = getPersistenceManager();
            // retrieve the current transaction
            Transaction t = pm.currentTransaction();
            // start a new transaction
            for (int i = 0; i < infos.length; i++) {
                CustomerKey customerKey = new CustomerKey();
                customerKey.customerId = infos[i].getId();
                CustomerImpl customer = getCustomer(customerKey);
                customer.setFirstName(infos[i].getFirstName());
                customer.setLastName(infos[i].getLastName());
                t.begin();
                pm.makePersistent(customer);
                t.commit();
            }
        }
    }

    /**
     * Retrieves the JDO Persistence Manager. Refer to the 
     * JDO specification for more details.
     * @return an instance of the JDO persistence manager
     */
    protected PersistenceManager getPersistenceManager() {
        return JDOUtilities.getPersistenceManager();
    }

    /**
     * Stores an instances of the JDO persistence manager so that
     * it does not have to be retrieved on every request.
     */
    protected PersistenceManager pm = null;

    /** Test customer collections */
    public static void main(String[] args) {
        // add a customer
        AddressImpl ai = new AddressImpl();
        ai.setAddressLine1("Line 1");
        ai.setAddressLine2("Line 2");
        ai.setCity("Highlands Ranch");
        ai.setState("CO");
        ai.setZipCode("80129");
        CustomerImpl ci = new CustomerImpl();
        ci.setAddress(ai);
        ci.setFirstName("Paul");
        ci.setLastName("Monday");
        ci.setCustomerId("1");
        CustomerCollectionImpl cci = new CustomerCollectionImpl();
        cci.addCustomer(ci);
        // query for the customer
        CustomerKey ck = new CustomerKey();
        ck.customerId = "1";
        CustomerImpl ci2 = cci.getCustomer(ck);
        System.out.println("Customer Retrieved:  " + ci2);
        CustomerImpl[] ci3 = cci.getCustomers();
        if (ci3 != null) {
            System.out.println("Collection retrieved");
            for (int i = 0; i < ci3.length; i++) {
                System.out.println(ci3[i].toString());
            }
        } else {
            System.out.println("ci3, no customers retrieved");
        }
        // delete the customer
        cci.removeCustomer(ck);
    }

}
