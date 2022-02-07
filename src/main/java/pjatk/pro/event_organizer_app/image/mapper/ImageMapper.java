package pjatk.pro.event_organizer_app.image.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.util.Base64Utils;
import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar;
import pjatk.pro.event_organizer_app.image.model.CateringImage;
import pjatk.pro.event_organizer_app.image.model.Image;
import pjatk.pro.event_organizer_app.image.model.LocationImage;
import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage;
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto;

@UtilityClass
public class ImageMapper {

    public LocationImage fromDtoToLocationImage(ImageDto imageDto) {
        return LocationImage.builder()
                .fileName(imageDto.getName())
                .build();
    }

    public OptionalServiceImage fromDtoToServiceImage(ImageDto imageDto) {
        return OptionalServiceImage.builder()
                .fileName(imageDto.getName())
                .build();
    }

    public CateringImage fromDtoToCateringImage(ImageDto imageDto) {
        return CateringImage.builder()
                .fileName(imageDto.getName())
                .build();
    }

    public CustomerAvatar fromDtoToCustomerAvatar(ImageDto imageDto) {
        return CustomerAvatar.builder()
                .fileName(imageDto.getName())
                .build();
    }

    public ImageDto toDto(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .name(image.getFileName())
                .encodedImage(Base64Utils.encodeToString(image.getImage()))
                .build();
    }
}
