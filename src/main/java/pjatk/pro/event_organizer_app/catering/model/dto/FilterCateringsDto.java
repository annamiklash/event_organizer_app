package pjatk.pro.event_organizer_app.catering.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCateringsDto {

    private List<String> cuisines;

    private Integer minPrice;

    private Integer maxPrice;

    private String city;

    private String date;
}
