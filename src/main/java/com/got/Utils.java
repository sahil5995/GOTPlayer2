package com.got;

import javax.json.Json;

public class Utils {

    public static String getMessage(String added, String Number) {
        return Json.createObjectBuilder()
                .add("Added", added)
                .add("Resulting Number", Number)
                .build()
                .toString();
    }
}
