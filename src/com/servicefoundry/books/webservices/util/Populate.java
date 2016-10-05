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
import com.servicefoundry.books.webservices.entities.stubs.*;

public class Populate {
    
    public static void createCustomers(CustomerCollectionImpl customerPort) throws java.rmi.RemoteException {
        {
            InternetAddress iAddress = new InternetAddress();
            iAddress.setPrimaryEmailAddress("pmonday@attbi.com");
            iAddress.setSecondaryEmailAddress("pmonday@hotmail.com");
            iAddress.setPrimaryUrl("http://www.servicefoundry.com");
            
            CreditAccount cAccount = new CreditAccount();
            cAccount.setAccount("PBM0101");
            cAccount.setAccountType("Visa");
            cAccount.setDefaultAccount(true);
            cAccount.setExpirationDate("01/01");
            
            Address sAddress = new Address();
            sAddress.setAddressLine1("1 Place of Business");
            sAddress.setAddressLine2("DEN-PM");
            sAddress.setCity("Denver");
            sAddress.setState("CO");
            sAddress.setZipCode("80237");
            
            CustomerInformation cInformation = new CustomerInformation();
            cInformation.setCreditAccount(cAccount);
            cInformation.setInternetAddress(iAddress);
            cInformation.setShippingAddress(sAddress);
            
            Address cAddress = new Address();
            cAddress.setAddressLine1("10 Home Way");
            cAddress.setAddressLine2("");
            cAddress.setCity("Highlands Ranch");
            cAddress.setState("CO");
            cAddress.setZipCode("80129");
            
            Customer customer = new Customer();
            customer.setAddress(cAddress);
            customer.setCustomerInformation(cInformation);
            customer.setFirstName("Paul");
            customer.setLastName("Monday");
            System.out.println("Adding Customer");
            customerPort.addCustomer(customer);
        }
    }
    
    public static void createProducts(ProductCollectionImpl productPort) throws java.rmi.RemoteException {
        {
            RoastedCoffeeBeans rcb = new RoastedCoffeeBeans();
            rcb.setDescription("Bold and rich");
            rcb.setName("French");
            rcb.setPounds(1.0f);
            rcb.setRetailPrice(9.95f);
            rcb.setType(0);
            rcb.setWholesalePrice(7.00f);
        
            System.out.println("Adding Product");
            productPort.addRoastedCoffeeBeansProduct(rcb);
        }
    }

    public static void createInventoryItems(InventoryCollectionImpl inventoryPort) throws java.rmi.RemoteException {
        {
            UnroastedCoffeeBeans urcb = new UnroastedCoffeeBeans();
            urcb.setDescription("Quality beans from Hawaii");
            urcb.setName("Kona");
            urcb.setPounds(10.0f);
            urcb.setPurchasePrice(50.0);
            urcb.setType(1);
            System.out.println("Adding Inventory Item");
            inventoryPort.addUnroastedCoffeeBeansInventoryItem(urcb);
        }
    }
    
    public static void createWarehouse(WarehouseImpl warehousePort, ProductCollectionImpl productPort, InventoryCollectionImpl inventoryPort) throws java.rmi.RemoteException {
        {
            // retrieve the products and their sku's
            Product[] ps = productPort.getProducts();
            for(int i=0 ; i<ps.length ; i++){
                System.out.println("Adding Product to Warehouse");
                warehousePort.addProductToWarehouse(ps[i].getSku(), 5);
            }
            
        }
        {
            // retrieve the inventory items and their sku's
            InventoryItem[] iis = inventoryPort.getInventoryItems();
            for(int i=0 ; i<iis.length ; i++){
                System.out.println("Adding Inventory Item to Warehouse");
                warehousePort.addInventoryItemToWarehouse(iis[i].getSku(), 5);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            CustomerCollectionImplService customerService = new CustomerCollectionImplServiceLocator();
            CustomerCollectionImpl customerPort = customerService.getCustomerCollection();
            InventoryCollectionImplService inventoryService = new InventoryCollectionImplServiceLocator();
            InventoryCollectionImpl inventoryPort = inventoryService.getInventoryCollection();
            ProductCollectionImplService productService = new ProductCollectionImplServiceLocator();
            ProductCollectionImpl productPort = productService.getProductCollection();
            WarehouseImplService warehouseService = new WarehouseImplServiceLocator();
            WarehouseImpl warehousePort = warehouseService.getWarehouse();
            createCustomers(customerPort);
            createProducts(productPort);
            createInventoryItems(inventoryPort);
            createWarehouse(warehousePort, productPort, inventoryPort);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
