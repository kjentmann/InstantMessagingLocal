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
    public String clientName;
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
        publisherTopic=null;

    }
    public void createAndShowGUI() {

        frame = new JFrame("DSIT - Mads Fornes - Pub/Sub   " + clientName);
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

        //frame.pack();disabled to use manual frame size.
        frame.setVisible(true);
        argument_TextField.grabFocus();
    }

     private static String getTime() {
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
            return sdfTime.format(new Date());
        }
     private String getArg(){
        String arg = argument_TextField.getText();
        argument_TextField.setText(null);
        argument_TextField.grabFocus();
        return arg;
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
            String topic = getArg();
            if (topic.isEmpty()){
                messages_TextArea.append(getTime() + "SYSTEM: Missing input.\n");
            }
            else{
                    if (publisherTopic!=null){
                        topicManager.removePublisherFromTopic(publisherTopic);
                    }
                publisher = topicManager.addPublisherToTopic(topic);
                publisherTopic=topic;
                publisher_TextArea.setText(null);
                publisher_TextArea.append(topic + "\n");
                messages_TextArea.append(getTime() + "SYSTEM: You are publisher of topic '"+ topic + "̈́'.\n");
            }
        }
    }

    class newSubscriberHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String topic = getArg();
            if (topic.isEmpty() || my_subscriptions.containsKey(topic) || topic == publisherTopic)
                messages_TextArea.append(getTime() + "SYSTEM: Subscriber exist, publishing on same client or missing input.\n");
            else if (topicManager.isTopic(topic)){
                Subscriber newsubscriber;
                newsubscriber = new SubscriberImpl(ClientSwing.this);
                topicManager.subscribe(topic, newsubscriber);              // Note: adding subscriber to the actual publishers list
                my_subscriptions.put(topic, newsubscriber);                // Note: adding subscriber to clientSwing subscribers list
                my_subscriptions_TextArea.append(topic + "\n");
                messages_TextArea.append(getTime() + "SYSTEM: Subscribed on topic '"+ topic + "̈́'.\n");
            }
            else
                messages_TextArea.append(getTime() + "SYSTEM: Topic '"+ topic + "̈́' does not exist.\n");
        }
    }

    class UnsubscribeHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String topic = getArg();
            try{
                if (topicManager.unsubscribe(topic,my_subscriptions.get(topic)) ){
                        my_subscriptions.remove(topic);
                        my_subscriptions_TextArea.setText(null);
                       for (String otherTopics : my_subscriptions.keySet()){
                             my_subscriptions_TextArea.append(otherTopics+"\n");
                       }
                }
                else{
                messages_TextArea.append(getTime() + "SYSTEM: Topic '"+ topic + "̈́' does not exist.\n");
                }
            }
            catch(Exception ex){
               System.out.println("DEBUG: Error, no active subscribers to remove.");
            }
        }
    }

    class postEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (publisher != null){
                String event = getArg();
                if (!event.isEmpty()){
                    publisher.publish(publisherTopic, event);
                    messages_TextArea.append(getTime() + "You published: " + event + "\n");
                }
            }
            else{
                messages_TextArea.append(getTime() + "SYSTEM: No publisher exist.\n");
            }
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
        }
    }
}
