package com.got.endpoint;


import com.got.Utils;
import com.got.endpoint.ChatClientEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Scanner;

@Component
public class Service {

    @Autowired
    private RetryTemplate retryTemplate;

    @Value("${serverendpoint}")
    private String serverEndPoint;

    private ChatClientEndpoint clientEndPoint = null;

    public void callServer() throws NumberFormatException, URISyntaxException {

        retryTemplate.execute(Context -> {
                    try {
                        clientEndPoint = new ChatClientEndpoint(new URI(serverEndPoint));
                        return clientEndPoint;
                    } catch (Exception e) {
                        System.out.println("Failed to connect to other Player..!! Will try to connect 5 times.");
                        throw new URISyntaxException("Other Player is not ready yet....2", "reason");
                    }
                },
                retryContext -> {
                    throw new URISyntaxException("System will Exit now", "Failed to connect to other player");
                });


        if (clientEndPoint != null) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Press 1 for automatic number generation.\nPress 2 for entering number of your choice.");
                int number = sc.nextInt();
                int gameInputNum = -1;
                if (number == 1) {
                    gameInputNum = new Random().nextInt(101);
                }
                if (number == 2) {
                    System.out.println("Enter the number to start the game..!!");
                    gameInputNum = sc.nextInt();
                }
                if (gameInputNum != -1)
                    clientEndPoint.sendMessage(Utils.getMessage("0", String.valueOf(gameInputNum)));
            }catch (Exception e){
                throw new NumberFormatException("Invalid input given");
            }
        }
    }
}
