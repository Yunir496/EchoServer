package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.*;

public class TcpClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TcpClientHandler.class);
    private final Socket clientSocket;

    public TcpClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        // Логируем подключение клиента
        String clientAddress = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        logger.info("TCP: Client connected: {}", clientAddress);

        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Читаем данные от клиента и отправляем обратно
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
                logger.debug("TCP: Echoed {} bytes to {}", bytesRead, clientAddress);
            }
        } catch (IOException e) {
            logger.error("TCP: Client handling error for {}", clientAddress, e);
        } finally {
            try {
                clientSocket.close();
                logger.info("TCP: Client disconnected: {}", clientAddress);
            } catch (IOException e) {
                logger.error("TCP: Error closing client socket", e);
            }
        }
    }
}