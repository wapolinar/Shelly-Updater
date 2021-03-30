
package de.apolinarski.shelly.updater.json.firmware;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "version",
        "beta_url",
        "beta_ver"
})
@Generated("jsonschema2pojo")
public class Shem implements ShellyObject {

    @JsonProperty("url")
    private String url;
    @JsonProperty("version")
    private String version;
    @JsonProperty("beta_url")
    private String betaUrl;
    @JsonProperty("beta_ver")
    private String betaVer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("beta_url")
    public String getBetaUrl() {
        return betaUrl;
    }

    @JsonProperty("beta_url")
    public void setBetaUrl(String betaUrl) {
        this.betaUrl = betaUrl;
    }

    @JsonProperty("beta_ver")
    public String getBetaVer() {
        return betaVer;
    }

    @JsonProperty("beta_ver")
    public void setBetaVer(String betaVer) {
        this.betaVer = betaVer;
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
