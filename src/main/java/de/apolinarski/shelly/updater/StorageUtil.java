package de.apolinarski.shelly.updater;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.apolinarski.shelly.updater.json.Shelly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class StorageUtil {

    private static final String CONFIG_FILE = "shellies.json";
    @NonNull
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public static List<Shelly> loadUtil() {
        try {
            return Arrays.asList(mapper.readValue(new BufferedInputStream(new FileInputStream(CONFIG_FILE)),
                    Shelly[].class));
        } catch(IOException e) {
            log.warn("Could not read shelly list: {}",e.getMessage());
            log.trace("Complete exception:",e);
            return Collections.emptyList();
        }
    }

    public static void saveUtil(List<Shelly> shellyList) throws IOException {
        mapper.writeValue(new BufferedOutputStream(new FileOutputStream(CONFIG_FILE)), shellyList);
    }


}
