package com.got;


import com.got.endpoint.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;

@SpringBootApplication
public class StartPlayer implements CommandLineRunner {

    @Autowired
    private Service service;

    public static void main(String[] args) {
        SpringApplication.run(StartPlayer.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            service.callServer();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
