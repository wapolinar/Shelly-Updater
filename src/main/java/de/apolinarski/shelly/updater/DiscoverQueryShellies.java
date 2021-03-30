package de.apolinarski.shelly.updater;

import de.apolinarski.shelly.updater.json.Shelly;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
public class DiscoverQueryShellies implements CommandLineRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        ShellyWebQuery query = new ShellyWebQuery();
        List<Shelly> shellyList = StorageUtil.loadUtil();
        if (shellyList.isEmpty()) {
            log.warn("Could not load configuration file, scanning network.");
            shellyList = loadShellies(query);
            StorageUtil.saveUtil(shellyList);
        } else {
            log.info("Loaded shellies from file:");
            for(Shelly s : shellyList) {
                log.info("Shelly ip: {}, type: {}, mac: {}, fw: {}, auth: {}", s.getIp(), s.getType(), s.getMac(),
                        s.getFw(), s.isAuth());
            }
        }
        context.close();
    }

    private List<Shelly> loadShellies(ShellyWebQuery query) {
        Enumeration<NetworkInterface> networks = null;
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
            return String.valueOf(bytes);
        }
    }
}
