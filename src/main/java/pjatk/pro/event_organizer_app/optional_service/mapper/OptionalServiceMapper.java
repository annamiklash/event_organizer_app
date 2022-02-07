package pjatk.pro.event_organizer_app.optional_service.mapper;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import pjatk.pro.event_organizer_app.address.mapper.AddressMapper;
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper;
import pjatk.pro.event_organizer_app.businesshours.mapper.BusinessHoursMapper;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.image.mapper.ImageMapper;
import pjatk.pro.event_organizer_app.optional_service.enums.OptionalServiceTypeEnum;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.Interpreter;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.model.TranslationLanguage;
import pjatk.pro.event_organizer_app.optional_service.model.kidperformer.KidsPerformer;
import pjatk.pro.event_organizer_app.optional_service.model.music.DJ;
import pjatk.pro.event_organizer_app.optional_service.model.music.MusicBand;
import pjatk.pro.event_organizer_app.optional_service.model.music.Musician;
import pjatk.pro.event_organizer_app.optional_service.model.music.Singer;
import pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.MusicStyle;
import pjatk.pro.event_organizer_app.optional_service.model.other.Host;
import pjatk.pro.event_organizer_app.optional_service.model.other.OtherService;
import pjatk.pro.event_organizer_app.optional_service.validator.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class OptionalServiceMapper {

    public OptionalServiceDto toDto(OptionalService optionalService) {
        final OptionalServiceDto dto = OptionalServiceDto.builder()
                .id(optionalService.getId())
                .type(optionalService.getType())
                .alias(optionalService.getAlias())
                .firstName(optionalService.getFirstName())
                .lastName(optionalService.getLastName())
                .description(optionalService.getDescription())
                .serviceCost(String.valueOf(optionalService.getServiceCost()))
                .email(optionalService.getEmail())
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(optionalService.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(optionalService.getModifiedAt()))
                .deletedAt(DateTimeUtil.fromLocalDateTimetoString(optionalService.getDeletedAt()))
                .build();

        if (!CollectionUtils.isEmpty(optionalService.getImages())) {
            dto.setImages(optionalService.getImages().stream().map(ImageMapper::toDto).collect(Collectors.toList()));
        } else {
            dto.setImages(new ArrayList<>());
        }

        final OptionalServiceTypeEnum type = OptionalServiceTypeEnum.valueOfLabel(optionalService.getType());

        switch (type) {
            case KIDS_PERFORMER:
                dto.setKidAgeFrom(((KidsPerformer) optionalService).getAgeFrom());
                dto.setKidAgeTo(((KidsPerformer) optionalService).getAgeFrom());
                dto.setKidPerformerType(dto.getKidPerformerType());
                return dto;

            case INTERPRETER:
                dto.setTranslationLanguages(((Interpreter) optionalService).getLanguages()
                        .stream()
                        .map(TranslationLanguageMapper::toDto)
                        .collect(Collectors.toSet()));
                return dto;

            case MUSIC_BAND:
                dto.setMusicBandPeopleCount(((MusicBand) optionalService).getPeopleCount());
                dto.setMusicStyle(optionalService.getStyles()
                        .stream()
                        .map(MusicStyleMapper::toDto)
                        .collect(Collectors.toSet()));
                return dto;

            case MUSICIAN:
                dto.setMusicStyle(optionalService.getStyles()
                        .stream()
                        .map(MusicStyleMapper::toDto)
                        .collect(Collectors.toSet()));

                dto.setInstrument(((Musician) optionalService).getInstrument());
                return dto;

            case DJ:
            case SINGER:
                dto.setMusicStyle(optionalService.getStyles()
                        .stream()
                        .map(MusicStyleMapper::toDto)
                        .collect(Collectors.toSet()));
                return dto;

            case HOST:
            case OTHER:
            default:
                return dto;
        }
    }

    public OptionalService fromDto(OptionalServiceDto dto, List<MusicStyle> musicStyles, List<TranslationLanguage> translationLanguages) {
        Validator.validateType(dto);
        final OptionalServiceTypeEnum type = OptionalServiceTypeEnum.valueOfLabel(dto.getType());

        switch (type) {
            case KIDS_PERFORMER:
                return KidsPerformer.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .ageFrom(dto.getKidAgeFrom())
                        .ageTo(dto.getKidAgeTo())
                        .kidsPerformerType(dto.getKidPerformerType())
                        .build();

            case INTERPRETER:
                final Interpreter interpreter = Interpreter.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .languages(new HashSet<>())
                        .build();

                translationLanguages.forEach(interpreter::addLanguage);

                return interpreter;

            case MUSIC_BAND:
                final MusicBand musicBand = MusicBand.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .peopleCount(dto.getMusicBandPeopleCount())
                        .styles(new HashSet<>())
                        .build();

                musicStyles.forEach(musicBand::addMusicStyle);

                return musicBand;

            case MUSICIAN:
                final Musician musician = Musician.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .instrument(dto.getInstrument())
                        .styles(new HashSet<>())
                        .build();

                musicStyles.forEach(musician::addMusicStyle);

                return musician;

            case DJ:
                final DJ dj = DJ.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .styles(new HashSet<>())
                        .build();

                musicStyles.forEach(dj::addMusicStyle);
                return dj;


            case SINGER:
                final OptionalService singer = Singer.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .styles(new HashSet<>())
                        .build();

                musicStyles.forEach(singer::addMusicStyle);

                return singer;

            case HOST:
                return Host.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .build();

            case OTHER:
                return OtherService.builder()
                        .type(dto.getType())
                        .alias(dto.getAlias())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .description(dto.getDescription())
                        .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                        .email(dto.getEmail())
                        .otherType(dto.getOtherType())
                        .build();
            default:
                throw new ActionNotAllowedException("Invalid type");
        }
    }

    public OptionalServiceDto toDtoWithDetails(OptionalService optionalService) {
        final OptionalServiceDto dto = toDto(optionalService);
        dto.setAddress(AddressMapper.toDto(optionalService.getServiceAddress()));
//        dto.setBusiness(BusinessMapper.toDto(optionalService.getBusiness()));
        if (!CollectionUtils.isEmpty(optionalService.getImages())) {
            dto.setImages(optionalService.getImages().stream().map(ImageMapper::toDto).collect(Collectors.toList()));
        } else {
            dto.setImages(new ArrayList<>());
        }

        dto.setBusinessHours(optionalService.getOptionalServiceBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        dto.setServiceAvailability(optionalService.getAvailability().stream()
                .map(AvailabilityMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
