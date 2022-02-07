package pjatk.pro.event_organizer_app.location.mapper;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import pjatk.pro.event_organizer_app.address.mapper.AddressMapper;
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper;
import pjatk.pro.event_organizer_app.businesshours.mapper.BusinessHoursMapper;
import pjatk.pro.event_organizer_app.catering.mapper.CateringMapper;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.image.mapper.ImageMapper;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDescriptionItemDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;

import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class LocationMapper {

    public Location fromDto(LocationDto dto) {
        return Location.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
                .seatingCapacity(dto.getSeatingCapacity())
                .standingCapacity(dto.getStandingCapacity())
                .description(Converter.convertDescriptionsString(dto.getDescription()))
                .dailyRentCost(Converter.convertPriceString(dto.getDailyRentCost()))
                .sizeInSqMeters(dto.getSizeInSqMeters())
                .locationAddress(AddressMapper.fromDto(dto.getAddress()))
                .createdAt(Converter.fromStringToFormattedDateTime(dto.getCreatedAt()))
                .modifiedAt(Converter.fromStringToFormattedDateTime(dto.getModifiedAt()))
                .deletedAt(Converter.fromStringToFormattedDateTime(dto.getDeletedAt()))
                .build();
    }

    public LocationDto toDto(Location location) {
        final LocationDto dto = LocationDto.builder()
                .id(Math.toIntExact(location.getId()))
                .name(location.getName())
                .description(location.getDescription())
                .email(location.getEmail())
                .phoneNumber(String.valueOf(location.getPhoneNumber()))
                .seatingCapacity(location.getSeatingCapacity())
                .standingCapacity(location.getStandingCapacity())
                .dailyRentCost(String.valueOf(location.getDailyRentCost()))
                .sizeInSqMeters(location.getSizeInSqMeters())
                .createdAt(String.valueOf(location.getCreatedAt()))
                .modifiedAt(String.valueOf(location.getModifiedAt()))
                .deletedAt(String.valueOf(location.getDeletedAt()))
                .address(AddressMapper.toDto(location.getLocationAddress()))
                .build();

        if (location.getRating() != null) {
            dto.setRating(new Double(location.getRating()));
        }

        if (!CollectionUtils.isEmpty(location.getImages())) {
            dto.setImages(location.getImages().stream().map(ImageMapper::toDto).collect(Collectors.toList()));
        } else {
            dto.setImages(new ArrayList<>());
        }

        return dto;
    }

    public static LocationDto toDtoWithAvailability(Location location) {
        final LocationDto dto = toDto(location);
        dto.setLocationAvailability(location.getAvailability().stream()
                .map(AvailabilityMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static LocationDto toDtoWithDetailWithCaterings(Location location) {
        final LocationDto dto = toDto(location);

        dto.setCaterings(location.getCaterings().stream()
                .map(CateringMapper::toDto)
                .collect(Collectors.toSet()));

        dto.setDescriptions(location.getDescriptions().stream()
                .map(LocationDescriptionItemMapper::toDto)
                .map(LocationDescriptionItemDto::getId)
                .collect(Collectors.toSet()));

        dto.setLocationAvailability(location.getAvailability().stream()
                .map(AvailabilityMapper::toDto)
                .collect(Collectors.toList()));

        dto.setBusinessHours(location.getLocationBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static LocationDto toDtoWithDetail(Location location) {
        final LocationDto dto = toDto(location);

        dto.setDescriptions(location.getDescriptions().stream()
                .map(LocationDescriptionItemMapper::toDto)
                .map(LocationDescriptionItemDto::getId)
                .collect(Collectors.toSet()));

        dto.setLocationAvailability(location.getAvailability().stream()
                .map(AvailabilityMapper::toDto)
                .collect(Collectors.toList()));

        dto.setBusinessHours(location.getLocationBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
