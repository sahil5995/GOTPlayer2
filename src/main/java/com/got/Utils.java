package com.got;

import javax.json.Json;

public class Utils {

    public static final String addedField = "Added";
    public static final String resultingNumber = "Resulting Number";

    public static final String GAME_OVER = "Game Over";
    public static final String YOU_WON = "You won the game";
    public static final String YOU_LOST = "You lost the game..!!";

    /**
     * This method converts the string into JSON message
     *
     * @param added  modified value
     * @param Number resulting number
     * @return json message
     */
    public static String getMessage(String added, String Number) {
        return Json.createObjectBuilder()
                .add(addedField, added)
                .add(resultingNumber, Number)
                .build()
                .toString();
    }
}
