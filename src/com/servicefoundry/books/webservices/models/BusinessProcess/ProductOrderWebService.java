/* Generated by Together */

package com.servicefoundry.books.webservices.models.BusinessProcess;

import com.servicefoundry.books.webservices.processes.ProductOrderImpl;
/**
 * @stereotype web service 
 */
public class ProductOrderWebService {
    public String createProductOrder(){
        CreateOrder co = new CreateOrder();
        co.setData(data);
        co.run();
        String key = co.getKey();
        String orderId = (String)co.getReturnValue();
        data.put(key, orderId);
        return orderId;
    }

    private ProductOrderImpl lnkProductOrderImpl;
}