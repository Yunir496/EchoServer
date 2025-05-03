package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int TCP_PORT = 12345;
    private static final int UDP_PORT = 12346;
    private static final int THREADS_POOL_SIZE = 10;
    private static volatile boolean running = true;

    public static void main(String[] args) {
        // Создаём пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_POOL_SIZE);
        logger.info("Starting Echo Server with TCP port {} and UDP port {}", TCP_PORT, UDP_PORT);

        // Запускаем TCP сервер
        executor.submit(() -> new TcpServer(TCP_PORT).start());

        // Запускаем UDP сервер
        executor.submit(() -> new UdpServer(UDP_PORT).start());

        // Обработка Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;
            logger.info("Shutting down servers...");
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    logger.warn("Some tasks did not terminate in time");
                }
            } catch (InterruptedException e) {
                logger.error("Shutdown interrupted", e);
            }
        }));
    }

    public static boolean isRunning() {
        return running;
    }
}