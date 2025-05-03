package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.*;

public class TcpServer {
    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
    private final int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("TCP Echo Server started on 127.0.0.1:{}", port);

            while (Main.isRunning()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    logger.debug("TCP: New client connection accepted");
                    // Запускаем обработчик клиента в новом потоке
                    new Thread(new TcpClientHandler(clientSocket)).start();
                } catch (IOException e) {
                    if (!Main.isRunning()) {
                        break;
                    }
                    logger.error("TCP: Error accepting client connection", e);
                }
            }
        } catch (IOException e) {
            logger.error("TCP Server error", e);
        }
        logger.info("TCP Server stopped");
    }
}