package sa.com.anb.poc.ms.exception;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

public class CustomJsonLayout extends JsonLayout {
    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        map.put("application", "kafka-ms");
        try {
            map.put("host", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
        }
    }
}