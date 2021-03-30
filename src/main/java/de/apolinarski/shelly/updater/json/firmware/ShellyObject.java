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

    public Map<String, Object> getAdditionalProperties();

    public void setAdditionalProperty(String name, Object value);

}
