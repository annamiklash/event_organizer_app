package pjatk.pro.event_organizer_app.optional_service.validator;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import pjatk.pro.event_organizer_app.exceptions.ValidationException;
import pjatk.pro.event_organizer_app.optional_service.enums.OptionalServiceTypeEnum;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;

@UtilityClass
public class Validator {

    public void validateType(OptionalServiceDto dto) {

        final OptionalServiceTypeEnum type = OptionalServiceTypeEnum.valueOfLabel(dto.getType());

        switch (type) {
            case KIDS_PERFORMER:
                if (dto.getKidAgeFrom() == null || dto.getKidAgeTo() == null) {
                    throw new ValidationException("Kid performed should specify kid age range");
                }
                if (dto.getKidAgeFrom() > dto.getKidAgeTo()) {
                    throw new ValidationException("Kid performed should specify valid age range for kids");
                }
                if (dto.getKidPerformerType() == null) {
                    throw new ValidationException("Kid performer should specify the type of performance");
                }
                break;

            case INTERPRETER:
                if (CollectionUtils.isEmpty(dto.getTranslationLanguages())) {
                    throw new ValidationException("Translators must specify at least one language");
                }
                break;

            case MUSIC_BAND:
                if (dto.getMusicBandPeopleCount() == null || dto.getMusicBandPeopleCount() <= 0) {
                    throw new ValidationException("Translators must specify at least one language");
                }
                if (CollectionUtils.isEmpty(dto.getMusicStyle())) {
                    throw new ValidationException("Music related services must specify at least 1 style they perform in");
                }
                break;

            case MUSICIAN:
                if (StringUtils.isEmpty(dto.getInstrument())) {
                    throw new ValidationException("Musicians should specify an instrument they perform with");
                }
                if (CollectionUtils.isEmpty(dto.getMusicStyle())) {
                    throw new ValidationException("Music related services must specify at least 1 style they perform in");
                }
                break;

            case DJ:
            case SINGER:
                if (CollectionUtils.isEmpty(dto.getMusicStyle())) {
                    throw new ValidationException("Music related services must specify at least 1 style they perform in");
                }
                break;

            case OTHER:
                if (StringUtils.isEmpty(dto.getOtherType())) {
                    throw new ValidationException("Must describe what service they offer");
                }
                break;

            case HOST:
            default:
                break;
        }
    }
}
