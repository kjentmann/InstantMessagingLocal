/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.JTextArea;
import main.ClientSwing;

/**
 *
 * @author juanluis
 */
public class SubscriberImpl implements Subscriber {

        private JTextArea messages_TextArea;
        private JTextArea my_subscriptions_TextArea;
        private Map<String,Subscriber> my_subscriptions;

        public SubscriberImpl(ClientSwing clientSwing) {
                this.messages_TextArea = clientSwing.messages_TextArea;
                this.my_subscriptions_TextArea = clientSwing.my_subscriptions_TextArea;
                this.my_subscriptions = clientSwing.my_subscriptions;
        }
        private static String getTime() {
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
            return sdfTime.format(new Date());
        }
        public void onClose(String topic, String cause) {
                if(cause.equals("PUBLISHER")) {
                        messages_TextArea.append(getTime()+"INFO: Publishers on '"+topic+"' have ended\n");
                        my_subscriptions.remove(topic);
                        
                        my_subscriptions_TextArea.setText(null);
                        for (String topic2 : this.my_subscriptions.keySet())
                            my_subscriptions_TextArea.append(topic2+"\n");
                }
                else if(cause.equals("SUBSCRIBER")) {
                        messages_TextArea.append(getTime()+"INFO: Subscription on '"+topic+"' ends...\n");
                }
        }

        public void onEvent(String topic, String event) {
                  messages_TextArea.append(getTime() + topic+": "+event+"\n");
        }
    }
