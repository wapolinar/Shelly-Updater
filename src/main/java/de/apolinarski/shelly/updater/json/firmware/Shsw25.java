
package de.apolinarski.shelly.updater.json.firmware;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "version"
})
@Generated("jsonschema2pojo")
public class Shsw25 implements ShellyObject {

    @JsonProperty("url")
    private String url;
    @JsonProperty("version")
    private String version;
    @JsonProperty("beta_url")
    private String betaUrl;
    @JsonProperty("beta_ver")
    private String betaVersion;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @Override
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @Override
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @Override
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("beta_ver")
    public String getBetaVersion() {
        return betaVersion;
    }

    @JsonProperty("beta_ver")
    public void setBetaVersion(String betaVersion) {
        this.betaVersion = betaVersion;
    }

    @JsonProperty("beta_url")
    public String getBetaUrl() {
        return betaUrl;
    }

    @JsonProperty("beta_url")
    public void setBetaUrl(String betaUrl) {
        this.betaUrl = betaUrl;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
