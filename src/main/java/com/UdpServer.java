package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.*;

public class UdpServer {
    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);
    private final int port;

    public UdpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (DatagramSocket udpSocket = new DatagramSocket(port)) {
            logger.info("UDP Echo Server started on 127.0.0.1:{}", port);
            byte[] buffer = new byte[1024];

            while (Main.isRunning()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    udpSocket.receive(packet);

                    // Логируем отправителя
                    String clientAddress = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                    logger.info("UDP: Received packet from {}, length: {}", clientAddress, packet.getLength());

                    // Отправляем данные обратно
                    DatagramPacket response = new DatagramPacket(
                            packet.getData(),
                            packet.getLength(),
                            packet.getAddress(),
                            packet.getPort()
                    );
                    udpSocket.send(response);
                    logger.debug("UDP: Sent response to {}", clientAddress);

                } catch (IOException e) {
                    if (!Main.isRunning()) {
                        break;
                    }
                    logger.error("UDP: Error handling packet", e);
                }
            }
        } catch (IOException e) {
            logger.error("UDP Server error", e);
        }
        logger.info("UDP Server stopped");
    }
}