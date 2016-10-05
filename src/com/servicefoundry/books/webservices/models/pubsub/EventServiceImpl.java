/*
 * ProductOrderManager.java
 *
 * Created on November 3, 2002, 7:53 PM
 */

package com.servicefoundry.books.webservices.models.pubsub;
import com.servicefoundry.books.webservices.eventservice.stubs.*;
import java.util.*;
import java.net.*;
import com.servicefoundry.books.webservices.entities.*;


/**
 *
 * @author  default
 */
public class EventServiceImpl implements EventService {
    
    /** Creates a new instance of ProductOrderManager */
    public EventServiceImpl() {
    }
    
    public void addSubscriber(String topic, String subscriptionUrl){
        Vector subscribers = null;
        subscribers = (Vector)topics.get(topic);
        if(subscribers==null){
            subscribers = new Vector(2);
            topics.put(topic, subscribers);
        }
        if(!subscribers.contains(subscriptionUrl)){
            try {
                URL url = new URL(subscriptionUrl);
                subscribers.add(url);
            } catch (MalformedURLException mue){
                mue.printStackTrace();
            }
        }
    }
    
    public void removeSubscriber(String topic, String subscriptionUrl) {
        Vector subscribers = null;
        subscribers = (Vector)topics.get(topic);
        if(subscribers!=null){
            subscribers.remove(subscriptionUrl);
        }
    }
    
    public void publish(String topic, String data){
        Vector subscribers = null;
        subscribers = (Vector)topics.get(topic);
        if(subscribers!=null){
            for(int i=0 ; i<subscribers.size() ; i++){
                try {
                    SubscriberService service = 
                        new SubscriberServiceLocator();
                    com.servicefoundry.books.webservices.eventservice.stubs.Subscriber 
                        port = service.getSubscriber(
                            (URL)subscribers.elementAt(i)
                        );
                    port.update(topic, data);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    protected Hashtable topics = new Hashtable(2);

    /**
     * @clientCardinality 0..*
     * @supplierCardinality 0..*
     * @directed 
     */
    private SubscriberImpl lnkSubscriberImpl;
}
