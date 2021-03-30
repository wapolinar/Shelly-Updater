package de.apolinarski.shelly.updater;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.File;
import java.util.*;

public class DownloadedFirmware {

    @Getter
    @NonNull
    private final String shellyType;

    @NonNull
    private Map<String, File> availableFirmware = new HashMap<>();

    public DownloadedFirmware(String shellyType) {
        this.shellyType = shellyType;
    }

    @Nullable
    public File getFirmware(String version) {
        return availableFirmware.get(version);
    }

    @NonNull
    public List<String> getAvailableFirmwareVersions() {
        return new ArrayList<>(availableFirmware.keySet());
    }

    public void setAvailableFirmware(String version, File firmware) {
        availableFirmware.put(version, firmware);
    }

}
