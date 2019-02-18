package com.example.fsae.a19telemetryapp;

import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpListener extends Thread {

    private static final int DATA_LENGTH = 15;
    private static final int PORT = 8018;
    private DatagramSocket socket;
    private static DatagramPacket udpPacket;
    private static byte[] data;
    private DataStorage dataStorage;

    public UdpListener(DataStorage dataStorage) {
        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
            socket.bind(new InetSocketAddress(PORT));
            this.dataStorage = dataStorage;
        } catch(Exception e) {
            Log.e("UdpListener Exception", e.toString(), e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                data = new byte[DATA_LENGTH];
                udpPacket = new DatagramPacket(data, data.length);
                socket.receive(udpPacket);
                dataStorage.storeData(udpPacket.getData());
            } catch (Exception e) {
                Log.e("udpListener", "receive timeout", e);
            }
        }
    }
}
