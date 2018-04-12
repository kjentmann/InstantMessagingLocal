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
            System.out.println("DEBUG: Topic dont exist.");
            return false;
        }
    }
        //...

        
    public Set<String> topics(){
        
        if (topicMap.isEmpty()){
            return null;
        }
        Set<String> topicSet;
        topicSet = new HashSet<String>();
       // Map<String, PublisherAdmin> map = topicMap;
        for (Map.Entry<String, PublisherAdmin> entry : topicMap.entrySet())
        {
            topicSet.add(entry.getKey());
            System.out.println("topic: " + entry.getKey());
        }
        return topicSet;
        
        //...
    }
    
    public Publisher addPublisherToTopic(String topic){        
        //...
        return null;
    }
    public int removePublisherFromTopic(String topic){
        //...
        return -1;
    }
    public boolean subscribe(String topic, Subscriber subscriber){
        //this.topicMap.put(topic, )
        //...        
        return true;
    }
    public boolean unsubscribe(String topic, Subscriber subscriber){
        //...
        return true;
    }
}