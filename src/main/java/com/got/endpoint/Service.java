package com.got.endpoint;


import com.got.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Scanner;

@Slf4j
@Component
public class Service {

    @Autowired
    private RetryTemplate retryTemplate;

    @Value("${serverendpoint}")
    private String serverEndPoint;

    private PlayerClientEndpoint endpoint = null;

    /**
     * This method initiates the game by connecting to other player
     *
     * @throws NumberFormatException NumberFormatException
     * @throws URISyntaxException    URISyntaxException
     */
    public void connectAndStart() throws NumberFormatException, URISyntaxException {
        endpoint = connectWithPlayer();

        if (null != endpoint) {
            startTheGame();
        }
    }

    /**
     * This method takes the input from player for starting game
     */
    private void startTheGame() {
        try {
            Scanner sc = new Scanner(System.in);
            int input;
            do {
                System.out.println("Press 1 for automatic number generation.\nPress 2 for entering number of your choice.");
                input = sc.nextInt();
            } while (!isValid(input));

            int gameInputNum = inputUserChoise(sc, input);
            endpoint.sendMessage(Utils.getMessage("0", String.valueOf(gameInputNum)));
        } catch (Exception e) {
            throw new NumberFormatException("Invalid input given");
        }
    }

    /**
     * This method is used for generating input number
     *
     * @param sc Scanner
     * @param input input
     * @return integer number
     */
    private int inputUserChoise(Scanner sc, int input) {
        int startingNumber = 0;
        if (input == 1) {
            startingNumber = new Random().nextInt(101);
            System.out.println("Generated number:" + startingNumber);
        }
        if (input == 2) {
            System.out.println("Enter the number to start the game");
            startingNumber = sc.nextInt();
        }
        return startingNumber;
    }

    /**
     * This method is used for connecting with other player.
     * It will try 5 times to connect if it fails first time.
     *
     * @return PlayerClientEndpoint
     * @throws URISyntaxException URISyntaxException
     */
    private PlayerClientEndpoint connectWithPlayer() throws URISyntaxException {
        retryTemplate.execute(Context -> {
                    try {
                        endpoint = new PlayerClientEndpoint(new URI(serverEndPoint));
                        return endpoint;
                    } catch (Exception e) {
                        log.error("Failed to connect to other Player..!! Will try to connect 5 times.");
                        throw new URISyntaxException("Other Player is not ready yet....2", "reason");
                    }
                },
                retryContext -> {
                    throw new URISyntaxException("System will Exit now", "Failed to connect to other player");
                });
        return endpoint;
    }

    /**
     * This method checks if the user input is valid or not. Valid options are 1 or 2.
     *
     * @param userInput
     * @return boolean
     */
    private boolean isValid(int userInput) {
        if (userInput > 2 || userInput < 1) {
            System.out.println("Invalid input. Please try again.");
            return false;
        } else return true;
    }
}
