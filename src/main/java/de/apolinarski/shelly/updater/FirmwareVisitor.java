package de.apolinarski.shelly.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

@AllArgsConstructor
@Slf4j
public class FirmwareVisitor extends SimpleFileVisitor<Path> {

    @Getter
    private final DownloadedFirmware shellyFirmware;

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) {
        if(DiscoverQueryShellies.FIRMWARE_FILE_NAME.equals(file.getFileName().toString())) {
            Path parent = file.getParent();
            shellyFirmware.setAvailableFirmware(parent.getFileName().toString(),file.toFile());
            log.debug("Found firmware file for shelly type {}, version {}, file name is {}.",
                    shellyFirmware.getShellyType(), parent.getFileName(), file);
        }
        return CONTINUE;
    }

}
