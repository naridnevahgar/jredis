package com.rs;

import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class JRedis {
    public static void main( String[] args ) {
        int port = 6379;

        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                System.out.println("Unrecognized port number. Starting with default, 6379");
            }
        }

        System.out.printf("Starting Redis Server on port, %s\n", port);

        try {
            final RedisServer redisServer = new RedisServer(port);
            redisServer.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> redisServer.stop()));

            Scanner scanner = new Scanner(System.in);
            System.out.println("Press Ctrl-C / type exit to quit");

            if ("exit".equalsIgnoreCase(scanner.next())) {
                redisServer.stop();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (redisServer.isActive()) {
                    redisServer.stop();
                }
            }));

        } catch (IOException iox) {
            System.out.printf("Unable to start server on port, %s\n", port);
        } finally {
            System.exit(0);
        }
    }
}
