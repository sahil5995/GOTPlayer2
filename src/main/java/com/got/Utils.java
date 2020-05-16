package com.got;

import javax.json.Json;

public class Utils {

    public static String addedField = "Added";
    public static String resultingNumber = "Resulting Number";

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
