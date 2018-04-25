/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import topicmanager.TopicManager;
import topicmanager.TopicManagerImpl;

/**
 *
 * @author juanluis
 */
public class The_system {
    public static void main(String[] args){
        final TopicManager topicManager = new TopicManagerImpl();
        
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                        ClientSwing client = new ClientSwing(topicManager);
                        client.clientName="Client 1";
                        client.createAndShowGUI();
                }
        });
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                        ClientSwing client = new ClientSwing(topicManager);
                        client.clientName="Client 2";
                        client.createAndShowGUI();
                }
        });
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                        ClientSwing client = new ClientSwing(topicManager);
                        client.clientName="Client 3";
                        client.createAndShowGUI();
                }
        });
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    ClientSwing client = new ClientSwing(topicManager);
                    client.clientName="Client 4";
                    client.createAndShowGUI();
                }
        });
    }
}