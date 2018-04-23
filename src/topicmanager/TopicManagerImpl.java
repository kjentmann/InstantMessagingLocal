package topicmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherAdmin;
import publisher.PublisherImpl;
import subscriber.Subscriber;

public class TopicManagerImpl implements TopicManager {
    private Map<String,PublisherAdmin> topicMap;

    public TopicManagerImpl() {
       topicMap = new HashMap<String,PublisherAdmin>();
    }
    public boolean isTopic(String topic){
        if( this.topicMap.containsKey(topic)){
            System.out.println("DEBUG: Topic found.");
            return true;
        }
        else{
            System.out.println("DEBUG: Topic don't exist.");
            return false;
        }
        //...
    }

        
    public Set<String> topics(){
        if (topicMap.isEmpty()){
            return null;
        }
        Set<String> topicSet;
        topicSet = new HashSet<String>();
        for (Map.Entry<String, PublisherAdmin> entry : topicMap.entrySet()){
            topicSet.add(entry.getKey());
            System.out.println("DEBUG: Current topics: " + entry.getKey());
        }
        return topicSet;
        //...
    }
    
    public Publisher addPublisherToTopic(String topic){
        PublisherAdmin publishAdm;
        publishAdm = new PublisherImpl(topic);
        publishAdm.incPublishers();
        this.topicMap.put(topic, publishAdm);
        return publishAdm;
        //...
    }
    
    public int removePublisherFromTopic(String topic){
        topicMap.get(topic).decPublishers();
        topicMap.get(topic).detachAllSubscribers();
        this.topicMap.remove(topic);
        return -1;
                //...

    }
    public boolean subscribe(String topic, Subscriber subscriber){
        if(isTopic(topic)){
           this.topicMap.get(topic).attachSubscriber(subscriber);
            return true;
        }else{
            return false;
        }
         //...
    }
    
    public boolean unsubscribe(String topic, Subscriber subscriber){
        if(isTopic(topic)){
            this.topicMap.get(topic).detachSubscriber(subscriber);
            return true;
        }
        else{
            return false;
        }
         //...
    } 
}