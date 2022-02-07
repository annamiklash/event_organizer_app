package pjatk.pro.event_organizer_app.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterOptionalServiceDto {

    private String date;

    private String minPrice;

    private String maxPrice;

    private String type;

    private List<String> musicStyles;

    private Integer ageFrom;

    private Integer ageTo;

    private Integer bandPeopleCount;

    private List<String> languages;

    private String city;
}
