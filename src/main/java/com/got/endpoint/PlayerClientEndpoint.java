package com.got.endpoint;

import com.got.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;

@Slf4j
@ClientEndpoint
public class PlayerClientEndpoint {
    private Session session = null;

    private String gameOver = "Game Over";
    private String youWon = "You won the game";
    private String youLost = "You lost the game..!!";

    public PlayerClientEndpoint(URI endpointURI) throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider
                .getWebSocketContainer();
        container.connectToServer(this, endpointURI);
    }

    /**
     * Method for Connection open events.
     *
     * @param session the session which is opened.
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("Connected to other Player..!!");
        this.session = session;
    }

    /**
     * This method will be invoked when other player sends a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) throws InterruptedException, IOException {

        if (!message.contains(gameOver)) {

            log.info("Player1 sent:" + message);

            String number = getNumberFromJSON(message);

            int rawDigit = Integer.parseInt(number);
            int result = getDivisibleBy3(rawDigit);
            int addedValue = result - rawDigit;

            Thread.sleep(1000);

            int outputToSent = result / 3;

            if (outputToSent == 1) {
                log.info(youWon);
                System.out.println(youWon);
                this.session.getAsyncRemote().sendText(gameOver);
            } else {
                this.session.getAsyncRemote().sendText(Utils.getMessage(String.valueOf(addedValue), String.valueOf(outputToSent)));
            }
        } else {
            log.info(youLost);
            System.out.println(youLost);
        }
    }

    /**
     * This method is extracting number from JSON String
     *
     * @param message JSON message
     * @return extracted number value
     */
    private String getNumberFromJSON(String message) {
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        return jsonObject.getString(Utils.resultingNumber);
    }


    /**
     * This method modifies the input which is divisible by 3
     *
     * @param number whole number
     * @return int
     */
    private int getDivisibleBy3(int number) {
        return number % 3 == 0 ? number : ((number - 1) % 3 == 0 ? (number - 1) : number + 1);
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * Method for Connection close events.
     *
     * @param session the session which is getting closed.
     * @param reason  the reason for connection close
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        log.error(reason.getReasonPhrase());
        closeSessionAndExit();
    }

    /**
     * Method for receiving any errors.
     *
     * @param session   the session which is getting closed.
     * @param throwable the error which is thrown
     */
    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        log.error(throwable.getMessage());
        closeSessionAndExit();
    }

    private void closeSessionAndExit() throws IOException {
        this.session.close();
        System.exit(0);
    }
}