package com.got.endpoint;

import com.got.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@Slf4j
@ClientEndpoint
public class PlayerClientEndpoint {
    private Session session = null;

    private GOTService service;

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
        service = new GOTService();
    }

    /**
     * This method will be invoked when other player sends a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) throws InterruptedException {

        if (!message.contains(Utils.GAME_OVER)) {
            log.info("Player1 sent:" + message);
            service.sendMessageToPlayer(message, session);
        } else {
            log.info(Utils.YOU_LOST);
            System.out.println(Utils.YOU_LOST);
        }
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