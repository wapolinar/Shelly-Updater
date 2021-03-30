package de.apolinarski.shelly.updater.json;

import lombok.Getter;
import lombok.Setter;

public class Shelly {

    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private boolean auth;

    @Getter
    @Setter
    private String fw;

    @Override
    public String toString() {
        return "Shelly{" +
                "ip='" + ip + '\'' +
                ", type='" + type + '\'' +
                ", fw='" + fw + '\'' +
                '}';
    }
}
