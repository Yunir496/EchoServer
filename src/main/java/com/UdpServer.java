package com;

import java.io.*;
import java.net.*;

public class UdpServer {
    private final int port;

    public UdpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (DatagramSocket udpSocket = new DatagramSocket(port)) {
            System.out.println("UDP Echo Server started on 127.0.0.1:" + port);
            byte[] buffer = new byte[1024];

            while (Main.isRunning()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    udpSocket.receive(packet);

                    // Логируем отправителя
                    String clientAddress = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                    System.out.println("UDP: Received packet from " + clientAddress + ", length: " + packet.getLength());

                    // Отправляем данные обратно
                    DatagramPacket response = new DatagramPacket(
                            packet.getData(),
                            packet.getLength(),
                            packet.getAddress(),
                            packet.getPort()
                    );
                    udpSocket.send(response);
                    System.out.println("UDP: Sent response to " + clientAddress);

                } catch (IOException e) {
                    if (!Main.isRunning()) {
                        break;
                    }
                    System.err.println("UDP: Error handling packet: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("UDP Server error: " + e.getMessage());
        }
        System.out.println("UDP Server stopped");
    }
}