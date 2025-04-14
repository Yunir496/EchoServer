package com;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int TCP_PORT = 12345;
    private static final int UDP_PORT = 12346;
    private static final int THREADS_POOL_SIZE = 10;
    private static volatile boolean running = true;

    public static void main(String[] args) {
        // Создаём пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_POOL_SIZE);

        // Запускаем TCP сервер
        executor.submit(() -> new TcpServer(TCP_PORT).start());

        // Запускаем UDP сервер
        executor.submit(() -> new UdpServer(UDP_PORT).start());

        // Обработка Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;
            System.out.println("Shutting down servers...");
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("Some tasks did not terminate in time");
                }
            } catch (InterruptedException e) {
                System.out.println("Shutdown interrupted");
            }
        }));
    }

    public static boolean isRunning() {
        return running;
    }
}