package eu.europeana.api.commons_sb3.definitions.utils;

import java.util.HashMap;
import java.util.Map;

public class HeaderUtils {

    /**
     * This method parses prefer header in keys and values
     *
     * @param preferHeader prefer header sent in the request
     * @return map of prefer header keys and values
     */
    public static Map<String, String> parsePreferHeader(String preferHeader) {
        String[] headerParts = null;
        String[] contentParts = null;
        int KEY_POS = 0;
        int VALUE_POS = 1;

        Map<String, String> resMap = new HashMap<>();

        headerParts = preferHeader.split(";");
        for (String headerPart : headerParts) {
            contentParts = headerPart.split("=");
            resMap.put(contentParts[KEY_POS], contentParts[VALUE_POS]);
        }
        return resMap;
    }
}
