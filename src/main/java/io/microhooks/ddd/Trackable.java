package io.microhooks.ddd;

import java.util.HashMap;
import java.util.Map;

public interface Trackable {
    Map<String, Object> trackedFields = new HashMap<>();

    default Map<String, Object> getTrackedFields(){
        System.out.println("Hello"); // for testing
        return trackedFields;
    }
}