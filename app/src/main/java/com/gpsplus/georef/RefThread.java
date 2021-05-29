package com.gpsplus.georef;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class RefThread implements Runnable{

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final LocationDTO trueLocationDTO;
    private Socket socket;

    public RefThread(double longitude, double latitude, double altitude) {
        trueLocationDTO = new LocationDTO(latitude, longitude, altitude);
    }

    @Override
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(RefFragment.SERVER_IP);
            socket = new Socket(serverAddr, RefFragment.SERVER_PORT);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(trueLocationDTO);

            while (!Thread.currentThread().isInterrupted()) {}
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void sendLocation(final LocationDTO location) {
        new Thread(() -> {
            try {
                if (null != socket) {
                    output.writeObject(location);
                    output.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public LocationDTO getTrueLocationDTO() {
        return trueLocationDTO;
    }

}
