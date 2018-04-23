package main;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import publisher.Publisher;
import subscriber.Subscriber;
import subscriber.SubscriberImpl;
import topicmanager.TopicManager;

public class ClientSwing {

    public Map<String,Subscriber> my_subscriptions;
    Publisher publisher;
    String publisherTopic;
    TopicManager topicManager;
    
    JFrame frame;
    JTextArea topic_list_TextArea;
    public JTextArea messages_TextArea;
    public JTextArea my_subscriptions_TextArea;
    JTextArea publisher_TextArea;
    JTextField argument_TextField;
    
    public ClientSwing(TopicManager topicManager) {
        my_subscriptions = new HashMap<String,Subscriber>();
        publisher = null;
        this.topicManager = topicManager;
    }
    public void createAndShowGUI() {

        frame = new JFrame("Publisher/Subscriber demo");
        frame.setSize(900,350);
        frame.addWindowListener(new CloseWindowHandler());
        
        topic_list_TextArea = new JTextArea(5,10);
        messages_TextArea = new JTextArea(10,20);
        my_subscriptions_TextArea = new JTextArea(5,10);
        publisher_TextArea = new JTextArea(1,10);
        argument_TextField = new JTextField(20);

        JButton show_topics_button = new JButton("show Topics");
        JButton new_publisher_button = new JButton("new Publisher");
        JButton new_subscriber_button = new JButton("new Subscriber");
        JButton to_unsubscribe_button = new JButton("to unsubscribe");
        JButton to_post_an_event_button = new JButton("post an event");
        JButton to_close_the_app = new JButton("close app.");

        show_topics_button.addActionListener(new showTopicsHandler());
        new_publisher_button.addActionListener(new newPublisherHandler());
        new_subscriber_button.addActionListener(new newSubscriberHandler());
        to_unsubscribe_button.addActionListener(new UnsubscribeHandler());
        to_post_an_event_button.addActionListener(new postEventHandler());
        to_close_the_app.addActionListener(new CloseAppHandler());

        JPanel buttonsPannel = new JPanel(new FlowLayout());
        buttonsPannel.add(show_topics_button);
        buttonsPannel.add(new_publisher_button);
        buttonsPannel.add(new_subscriber_button);
        buttonsPannel.add(to_unsubscribe_button);
        buttonsPannel.add(to_post_an_event_button);
        buttonsPannel.add(to_close_the_app);

        JPanel argumentP = new JPanel(new FlowLayout());
        argumentP.add(new JLabel("Write content to set a new_publisher / new_subscriber / unsubscribe / post_event:"));
        argumentP.add(argument_TextField);

        JPanel topicsP = new JPanel();
        topicsP.setLayout(new BoxLayout(topicsP, BoxLayout.PAGE_AXIS));
        topicsP.add(new JLabel("Topics:"));
        topicsP.add(topic_list_TextArea);
        topicsP.add(new JScrollPane(topic_list_TextArea));
        topicsP.add(new JLabel("My Subscriptions:"));
        topicsP.add(my_subscriptions_TextArea);
        topicsP.add(new JScrollPane(my_subscriptions_TextArea));
        topicsP.add(new JLabel("I'm Publisher of topic:"));
        topicsP.add(publisher_TextArea);
        topicsP.add(new JScrollPane(publisher_TextArea));

        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.PAGE_AXIS));
        messagesPanel.add(new JLabel("Messages:"));
        messagesPanel.add(messages_TextArea);
        messagesPanel.add(new JScrollPane(messages_TextArea));

        Container mainPanel = frame.getContentPane();
        mainPanel.add(buttonsPannel, BorderLayout.PAGE_START);
        mainPanel.add(messagesPanel,BorderLayout.CENTER);
        mainPanel.add(argumentP,BorderLayout.PAGE_END);
        mainPanel.add(topicsP,BorderLayout.LINE_START);

        messages_TextArea.setEditable(false);
        topic_list_TextArea.setEditable(false);
        my_subscriptions_TextArea.setEditable(false);
        publisher_TextArea.setEditable(false);
        
        //frame.pack();disable to use manual size of frame.
        frame.setVisible(true);
        argument_TextField.grabFocus();
    }
    
    private static String getCurrentTime() {
    SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm ");//dd/MM/yyyy
    Date now = new Date();
    String strTime = sdfTime.format(now);
    return strTime;
}

    class showTopicsHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (topicManager.topics()!=null){
                topic_list_TextArea.setText(null);
                for (String topic : topicManager.topics()){
                    topic_list_TextArea.append(topic + "\n");
                }
            }
            else{
                topic_list_TextArea.setText(null);
                topic_list_TextArea.append("No topics yet\n");
            }
                argument_TextField.grabFocus();
        }
    }
    
    class newPublisherHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String topic = argument_TextField.getText();
            argument_TextField.setText(null);
            if (topic.isEmpty()){
                messages_TextArea.append(getCurrentTime() + "SYSTEM: Missing input.\n"); 
            }
            else{
                publisher = topicManager.addPublisherToTopic(topic);
                publisherTopic=topic;
                publisher_TextArea.setText(null);
                publisher_TextArea.append(topic + "\n");
                messages_TextArea.append(getCurrentTime() + "SYSTEM: You are publisher of topic '"+ topic + "̈́'.\n"); 
            }
            argument_TextField.grabFocus();
        }
//...
    }
    class newSubscriberHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String topic = argument_TextField.getText();
            argument_TextField.setText(null);
            if (topic.isEmpty()){
                messages_TextArea.append(getCurrentTime() + "SYSTEM: Missing input.\n"); 
                argument_TextField.grabFocus();

            }
            else{
                Subscriber newsubscriber;
                //ClientSwing client = new ClientSwing(topicManager);
                newsubscriber = new SubscriberImpl(ClientSwing.this);
                
                
                if (topicManager.subscribe(topic, newsubscriber)){
                    messages_TextArea.append(getCurrentTime() + " SYSTEM: Subscribed on topic '"+ topic + "̈́'.\n"); 
                    my_subscriptions_TextArea.append(topic + "\n");
           }
           else{
               messages_TextArea.append(getCurrentTime() + "SYSTEM: Topic '"+ topic + "̈́' does not exist.\n");
           }
           argument_TextField.grabFocus();
           
            //topicManager.subscribe(publisherTopic, subscriber)
            //...
        }
    }
    }
    class UnsubscribeHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String topic = argument_TextField.getText();
            argument_TextField.setText(null);
            if (topicManager.unsubscribe(topic,my_subscriptions.get(topic)) ){
                    messages_TextArea.append(getCurrentTime() + "SYSTEM: Unsubscribed on topic '"+ topic + "̈́'.\n"); 
            }
             else{
               messages_TextArea.append(getCurrentTime() + "SYSTEM: Topic '"+ topic + "̈́' does not exist.\n");
           }
           argument_TextField.grabFocus();
            
           
            //...
        }
    }
    class postEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (publisher != null){
                String event = argument_TextField.getText();
                if (!event.isEmpty()){
                    argument_TextField.setText(null);
                    publisher.publish(publisherTopic, event);
                   // publisher.publish("mads", "hello"); FIX
                    messages_TextArea.append(getCurrentTime() + "Published: " + event + "\n");
                }
                }
            else{
                messages_TextArea.append(getCurrentTime() + "SYSTEM: No publisher exist.\n");
            }
            argument_TextField.grabFocus();
            //...
        }
    }
    class CloseAppHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    class CloseWindowHandler implements WindowListener{
        public void windowDeactivated(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}
        public void windowClosing(WindowEvent e) {
            //...
        }
    }
}

