package de.apolinarski.shelly.updater.json.firmware;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public interface ShellyObject {

    public String getUrl();

    public void setUrl(String url);

    public String getVersion();

    public void setVersion(String version);

     public default String getBetaUrl() {
         return null;
     }

    public default void setBetaUrl(String url) {
         // empty body
    }

    public default String getBetaVersion() {
         return null;
    }

    public default void setBetaVersion(String version) {
         // empty body
    }

    public Map<String, Object> getAdditionalProperties();

    public void setAdditionalProperty(String name, Object value);

}
