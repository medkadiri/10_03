package io.microhooks.eda.providers;

import java.io.IOException;

import io.microhooks.eda.Event;
import io.microhooks.eda.EventProducer;
import io.microhooks.util.JsonSerdes;

public class NullEventProducer<T, U> extends EventProducer<T, U> {

    @Override
    public void publish(Event<T, U> event, String stream) {
        try {
            System.out.println(new String(JsonSerdes.getSingleton().serialize(event.getKey())) + " " + new String(JsonSerdes.getSingleton().serialize(event.getLabeledPayload())) + " " + stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
