package pjatk.pro.event_organizer_app.catering.mapper;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import pjatk.pro.event_organizer_app.address.mapper.AddressMapper;
import pjatk.pro.event_organizer_app.businesshours.mapper.BusinessHoursMapper;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.cuisine.mapper.CuisineMapper;
import pjatk.pro.event_organizer_app.image.mapper.ImageMapper;
import pjatk.pro.event_organizer_app.location.mapper.LocationMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CateringMapper {

    public static Catering fromDto(CateringDto dto) {

        final String requestServiceCost = dto.getServiceCost();
        log.info("COST STRING " + requestServiceCost);

        final BigDecimal serviceCost = Converter.convertPriceString(requestServiceCost);
        log.info("COST CONVERTED " + serviceCost);

        final String requestPhoneNumber = dto.getPhoneNumber();
        final BigInteger phoneNumber = Converter.convertPhoneNumberString(requestPhoneNumber);

        final String requestDescription = dto.getDescription();
        final String description = Converter.convertDescriptionsString(requestDescription);

        return Catering.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(phoneNumber)
                .serviceCost(serviceCost)
                .description(description)
                .createdAt(Converter.fromStringToFormattedDateTime(dto.getCreatedAt()))
                .modifiedAt(Converter.fromStringToFormattedDateTime(dto.getModifiedAt()))
                .deletedAt(Converter.fromStringToFormattedDateTime(dto.getDeletedAt()))
                .cateringAddress(AddressMapper.fromDto(dto.getAddress()))
                .offersOutsideCatering(dto.getOffersOutsideCatering())
                .build();
    }

    public CateringDto toDto(Catering catering) {
        final CateringDto dto = CateringDto.builder()
                .id(catering.getId())
                .name(catering.getName())
                .description(catering.getDescription())
                .email(catering.getEmail())
                .phoneNumber(String.valueOf(catering.getPhoneNumber()))
                .serviceCost(String.valueOf(catering.getServiceCost()))
                .createdAt(String.valueOf(catering.getCreatedAt()))
                .modifiedAt(String.valueOf(catering.getModifiedAt()))
                .deletedAt(String.valueOf(catering.getDeletedAt()))
                .address(AddressMapper.toDto(catering.getCateringAddress()))
                .offersOutsideCatering(catering.getOffersOutsideCatering())
                .cuisines(catering.getCuisines().stream()
                        .map(CuisineMapper::toDto)
                        .collect(Collectors.toList()))
                .build();

        if (!CollectionUtils.isEmpty(catering.getImages())) {
            dto.setImages(catering.getImages().stream().map(ImageMapper::toDto).collect(Collectors.toList()));
        } else {
            dto.setImages(new ArrayList<>());
        }

        return dto;

    }

    public static CateringDto toDtoWithDetailAndLocations(Catering catering) {
        final CateringDto dto = toDto(catering);

        dto.setCateringItems(catering.getCateringItems().stream()
                .map(CateringItemMapper::toDto)
                .collect(Collectors.toList()));

        dto.setBusinessHours(catering.getCateringBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        dto.setLocations(catering.getLocations().stream()
                .map(LocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static CateringDto toDtoWithDetail(Catering catering) {
        final CateringDto dto = toDto(catering);

        dto.setCateringItems(catering.getCateringItems().stream()
                .map(CateringItemMapper::toDto)
                .collect(Collectors.toList()));

        dto.setBusinessHours(catering.getCateringBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }


}
