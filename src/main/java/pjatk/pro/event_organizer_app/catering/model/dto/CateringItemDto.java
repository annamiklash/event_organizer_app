package pjatk.pro.event_organizer_app.catering.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringItemDto implements Serializable {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50, message
            = "The name should be between 1 and 50 characters")
    @Pattern(regexp = "^(?=\\s*\\S).*$")
    private String name;

    private String description;

    @NotBlank(message = "Price is mandatory")
    @Pattern(regexp = "^\\d+(.\\d{1,2})?$", message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String servingPrice;

    @NotNull(message = "Choose if vegan or not")
    private Boolean isVegan;

    @NotNull(message = "Choose if vegetarian or not")
    private Boolean isVegetarian;

    @NotNull(message = "Choose if gluten-free or not")
    private Boolean isGlutenFree;

    @NotBlank(message = "Choose Entree, Appetizer, Dessert, Soup or Salad")
    @Pattern(regexp = "^(Entree|Appetizer|Dessert|Soup|Salad)$")
    private String type;

    private String createdAt;

    private String modifiedAt;

    private CateringDto catering;
}
