package pjatk.pro.event_organizer_app.common.paginator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Builder
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CustomPage implements Serializable {

    private String sortBy;

    private String order;

    private Integer pageNo;

    private Integer pageSize;
}

