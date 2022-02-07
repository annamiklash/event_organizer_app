package pjatk.pro.event_organizer_app.customer.avatar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAvatarDto implements Serializable {

    private Long id;

    @Size(min = 1, max = 300, message = "Path is too long")
    @NotBlank(message = "Image path is mandatory")
    private String path;

    private byte[] image;

    private CustomerDto customer;
}
