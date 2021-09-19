package de.apolinarski.shelly.updater;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.apolinarski.shelly.updater.firmware.DownloadedFirmware;
import de.apolinarski.shelly.updater.firmware.FirmwareDownloader;
import de.apolinarski.shelly.updater.firmware.FirmwareVisitor;
import de.apolinarski.shelly.updater.json.Shelly;
import de.apolinarski.shelly.updater.json.ShellyUpdating;
import de.apolinarski.shelly.updater.json.firmware.AvailableFirmware;
import de.apolinarski.shelly.updater.json.firmware.Data;
import de.apolinarski.shelly.updater.json.firmware.ShellyObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Component
public class DiscoverQueryShellies implements CommandLineRunner {

    public static final String FIRMWARE_FILE_NAME = "firmware.zip";
    private static final String FIRMWARE_DIR = "firmware";
    private static final String INPUT_IGNORE = "i";
    private static final String INPUT_QUIT = "q";
    private static final String PARAM_LOAD_SHELLIES="--load-shellies";

    private final Set<String> shellyTypes = new HashSet<>();

    @Value("${firmware.download.wait.minutes}")
    private int minutes;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private ShellyWebQuery query;

    @Override
    public void run(String... args) throws Exception {
        boolean loadShellies = false;
        for(String arg: args) {
            if(PARAM_LOAD_SHELLIES.equalsIgnoreCase(arg)) {
                loadShellies = true;
            }
        }
        List<Shelly> shellyList = StorageUtil.loadUtil();
        File fwMainDir = new File(FIRMWARE_DIR);
        fwMainDir.mkdir();
        if (shellyList.isEmpty() || loadShellies) {
            log.warn("Could not load configuration file, scanning network.");
            shellyList = loadShellies();
            StorageUtil.saveUtil(shellyList);
        } else {
            log.info("Loaded shellies from file:");
            for(Shelly s : shellyList) {
                shellyTypes.add(s.getType());
                File dir = new File(fwMainDir, s.getType());
                if(!dir.exists()) {
                    dir.mkdir();
                }

                log.info("Shelly ip: {}, type: {}, mac: {}, fw: {}, auth: {}", s.getIp(), s.getType(), s.getMac(),
                        s.getFw(), s.isAuth());
            }
        }
        log.info("Retrieving available firmware from shelly cloud");
        AvailableFirmware firmware = query.getFirmwareInformation();
        if(firmware.isIsok()) {
            Data d = firmware.getData();
            for(Method m : Data.class.getMethods()) {
                JsonProperty property = m.getAnnotation(JsonProperty.class);
                if(property != null && shellyTypes.contains(property.value()) && m.getName().startsWith("get")) {
                    log.debug("Method is {}", m.getName());
                    ShellyObject o = (ShellyObject) m.invoke(d);
                    String fw = extractFirmware(o.getVersion());
                    File fwDir = new File(fwMainDir, property.value() + "/" + fw);
                    fwDir.mkdirs();
                    File firmwareZip = new File(fwDir, FIRMWARE_FILE_NAME);
                    if(!firmwareZip.exists()) {
                        downloadFirmware(o, firmwareZip);
                    }
                }
            }
        }
        Map<String, DownloadedFirmware> availableFirmware = new HashMap<>();
        for(String s : shellyTypes) {
            DownloadedFirmware fw = new DownloadedFirmware(s);
            availableFirmware.put(s, fw);
        }
        for(DownloadedFirmware fw : availableFirmware.values()) {
            FirmwareVisitor visitor = new FirmwareVisitor(fw);
            Files.walkFileTree(fwMainDir.toPath().resolve(fw.getShellyType()), visitor);
        }
SHELLY: for(Shelly s : shellyList) {
            updateFwInfo(s);
            DownloadedFirmware fw = availableFirmware.get(s.getType());
            if(fw == null) {
                log.warn("No firmware available for {}, continuing with next shelly.", s);
                continue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Available firmware for ")
                    .append(s)
                    .append(":\n");
            int counter = 1;
            for(String availableFw : fw.getAvailableFirmwareVersions()) {
                sb.append(counter++)
                        .append(") ")
                        .append(availableFw)
                        .append('\n');
            }
            sb.append("Or press \"i\" to ignore this shelly or \"q\" to quit.\n");
            System.out.println(sb.toString());
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String input = scanner.next();
                if (INPUT_IGNORE.equals(input)) {
                    continue SHELLY;
                }
                if (INPUT_QUIT.equals(input)) {
                    waitAndCloseContext();
                    return;
                }
                int option;
                try {
                    option = Integer.parseInt(input);
                    updateShelly(query, s, fw.getAvailableFirmwareVersions().get(option-1));
                    break;
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Invalid command: " + input);
                }
            }
        }
        StorageUtil.saveUtil(shellyList);
        waitAndCloseContext();
    }

    private void updateFwInfo(Shelly s) {
        Shelly newShellyInfo = query.createRequest(s.getIp());
        s.setFw(newShellyInfo.getFw());
    }

    private void waitAndCloseContext() throws InterruptedException {
        log.info("Waiting for the shellies to download the firmware file. Waiting {} minutes", minutes);
        Thread.sleep(minutes * 1000 * 60);
        context.close();
    }

    private void updateShelly(ShellyWebQuery query, Shelly shelly, String firmwareVersion) {
        log.info("Request update for shelly {} with version {}", shelly, firmwareVersion);
        ShellyUpdating updateResponse = query.requestFirmwareUpdate(shelly, firmwareVersion);
        log.info("Shelly answer is: {}",updateResponse.getStatus());
    }

    private void downloadFirmware(ShellyObject o, File firmwareZip) {
        if(!firmwareZip.exists()) {
            FirmwareDownloader downloader = new FirmwareDownloader(o.getUrl(), firmwareZip.toPath());
            downloader.downloadFile();
        } else {
            log.info("Firmware file for version {} does already exist.", o.getVersion());
        }
    }

    private String extractFirmware(String version) {
        int stringStart = version.indexOf('/');
        int stringEnd = version.indexOf('-', stringStart);
        if(stringEnd == -1) {
            stringEnd = version.indexOf('@', stringStart);
        }
        stringStart++;
        String result =  version.substring(stringStart, stringEnd);
        log.debug("Current version is: {}", result);
        return result;
    }

    private List<Shelly> loadShellies() {
        Enumeration<NetworkInterface> networks;
        try {
            networks = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.warn("Could not retrieve network interfaces.", e);
            return Collections.emptyList();
        }
        NetworkInterface network;
        List<Shelly> shellyList = new ArrayList<>();
        while (networks.hasMoreElements()) {
            network = networks.nextElement();
            Inet4Address ownAddress;
            for (InterfaceAddress address : network.getInterfaceAddresses()) {
                if (address.getAddress() instanceof Inet4Address) {
                    Inet4Address inetaddress = (Inet4Address) address.getAddress();
                    ownAddress = inetaddress;
                    SupportedPrefixes prefix = SupportedPrefixes.getSupportedPrefix(address.getNetworkPrefixLength());
                    if (prefix == SupportedPrefixes.PREFIX_32) {
                        continue;
                    } else if (prefix == SupportedPrefixes.PREFIX_24) {
                        byte[] inetBytes = inetaddress.getAddress();
                        inetBytes[3] = 0;
                        try {
                            log.info("Searching network: {}/{}", Inet4Address.getByAddress(inetBytes).getHostAddress(), prefix);
                        } catch (UnknownHostException e) {
                            log.warn("Could not retrieve network address, not using this network: {}", e.getMessage());
                            continue;
                        }
                        final int startNumber = Byte.MIN_VALUE;
                        final int numberOfSteps = Byte.MAX_VALUE - startNumber - 2;
                        final int showSteps = numberOfSteps / 5;
                        int counter = 0;
                        for (int currentByte = startNumber; currentByte <= Byte.MAX_VALUE; currentByte++) {
                            if(currentByte == 0 || currentByte == -1) {
                                continue; // ignore .0 and .255
                            }
                            inetBytes[3] = (byte) currentByte;
                            counter++;
                            try {
                                String currentIp = Inet4Address.getByAddress(inetBytes).getHostAddress();
                                if (ownAddress.getHostAddress().equals(currentIp)) {
                                    continue;
                                }

                                Shelly currentShelly = query.createRequest(currentIp);
                                if (currentShelly.getType() != null) {
                                    shellyList.add(currentShelly);
                                    log.info("Shelly ip: {} type: {}, mac: {}, fw: {}, auth: {}", currentIp,
                                            currentShelly.getType(), currentShelly.getMac(), currentShelly.getFw(),
                                            currentShelly.isAuth());
                                }
                                if(counter % showSteps == 0) {
                                    log.info("Searched {}/{}", counter, numberOfSteps);
                                }
                            } catch (UnknownHostException e) {
                                log.warn("Could not create ip for host: {}", e.getMessage());
                                continue;
                            }
                        }
                    } else { //TODO: Add other prefixes
                        log.info("Prefix {} is not supported, yet.", prefix);
                        continue;
                    }
//                    byte[] addressBytes = inetaddress.getAddress();
//                    int ipAddress = ((0xFF & addressBytes[0]) << 24) | ((0xFF & addressBytes[1]) << 16) |
//                            ((0xFF & addressBytes[2]) << 8) | (0xFF & addressBytes[3]);
//                    byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(ipAddress).array();
                }
            }
        }
        return shellyList;
    }

    @AllArgsConstructor
    private enum SupportedPrefixes {
        PREFIX_8(1), PREFIX_16(2), PREFIX_24(3), PREFIX_32(4);

        private final int bytes;

        public static SupportedPrefixes getSupportedPrefix(int prefix) {
            switch (prefix) {
                case 8:
                    return PREFIX_8;
                case 16:
                    return PREFIX_16;
                case 24:
                    return PREFIX_24;
                case 32:
                    return PREFIX_32;
                default:
                    log.warn("Illegal prefix {} returning {}.", prefix, PREFIX_32);
                    return PREFIX_32;
            }
        }

        @Override
        public String toString() {
            switch(this) {
                case PREFIX_8:
                    return String.valueOf(8);
                case PREFIX_16:
                    return String.valueOf(16);
                case PREFIX_24:
                    return String.valueOf(24);
                case PREFIX_32:
                    return String.valueOf(32);
                default:
                    log.error("Illegal prefix: {}", this.name());
                    return this.name();
            }
        }
    }
}
