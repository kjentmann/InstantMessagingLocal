package publisher;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import subscriber.Subscriber;

public class PublisherImpl implements PublisherAdmin, Publisher {

    private Set<Subscriber> subscriberSet;
    private int numPublishers;
    private String topic;
    
    public PublisherImpl(String topic){
        subscriberSet = new HashSet<Subscriber>();
        numPublishers = 0;
        this.topic = topic;
    }
    public int incPublishers(){
        return ++numPublishers;
    }
    public int decPublishers(){
        return --numPublishers;
    }
    public void attachSubscriber(Subscriber subscriber) {
        this.subscriberSet.add(subscriber);
        //...
    }
    public void detachSubscriber(Subscriber subscriber) {
        this.subscriberSet.remove(subscriber);
        //...
    }
    public void detachAllSubscribers() {
        this.subscriberSet.clear();
        //...
    }
    public void publish(String topic, String event) {
        //..
        int num =0;
        for (Subscriber sub : subscriberSet){
            num ++;
            sub.onEvent(topic, event);
            System.out.println("DEBUG: Publisher published to subscriber " + num);
        }  
    }
}
