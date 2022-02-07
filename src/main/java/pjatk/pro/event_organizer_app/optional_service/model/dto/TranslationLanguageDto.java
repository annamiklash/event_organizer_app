package pjatk.pro.event_organizer_app.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationLanguageDto implements Serializable {

    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;
}
