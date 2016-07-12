/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.vsm.xtension.remote;

import de.dfki.vsm.util.log.LOGConsoleLogger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick Gebhard
 *
 */
public class ReceiverThread extends Thread {
    private int mPort;

    private ReceiverExecutor mExecutor;

    private boolean mRunning = true;

    private DatagramSocket mSocket;

    // The singelton logger instance
    private final LOGConsoleLogger mLogger = LOGConsoleLogger.getInstance();

    public ReceiverThread(ReceiverExecutor executor, int port) {
        mExecutor = executor;
        mPort = port;
    }

    @Override
    public void run() {
        try {
            //Keep a mSocket open to listen to all the UDP trafic that is destined for this port
            mSocket = new DatagramSocket(mPort, InetAddress.getByName("0.0.0.0"));
            mSocket.setBroadcast(true);

            while (mRunning) {
                mLogger.message("Ready to receive messages ...");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                mSocket.receive(packet);

                // Read Packet
                byte[] realData = Arrays.copyOf(packet.getData(), packet.getLength());;

                String message = new String(packet.getData()).trim();
                mLogger.message("Message received " + message + " from " + packet.getAddress().getHostAddress());
                if (message.startsWith("VSMMessage#")) {
                    byte[] sendData = "VSMMessage#Received".getBytes();

                    mExecutor.setSceneFlowVariable(message.substring(message.indexOf("#") + 1));

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    mSocket.send(sendPacket);

                    mLogger.message("Sent confirmation to " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
           mLogger.message("Exception while receiving data ...");
        }
    }

    public  void stopServer() {
        mSocket.close();
        mRunning = false;
    }
}
