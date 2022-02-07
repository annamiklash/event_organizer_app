package pjatk.pro.event_organizer_app.image.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.customer.avatar.validator.ImageValidator;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@UtilityClass
public class ImageUtil {

    @SneakyThrows(IOException.class)
    public byte[] fromPathToByteArray(String path) {
        try {
            final File file = new File(path);
            ImageValidator.validateImageSize(file);

            return Files.readAllBytes(Paths.get(path));
        } catch (NoSuchFileException e) {
            throw new NotFoundException("File not found");
        }

    }
}
