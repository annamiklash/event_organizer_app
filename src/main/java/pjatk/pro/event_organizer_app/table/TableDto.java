package pjatk.pro.event_organizer_app.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDto<T> implements Serializable {

    private MetaDto meta;

    private List<T> items;


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaDto implements Serializable {

        private Long total;

        private Integer pageNo;

        private Integer pageSize;

        private String sortBy;
    }
}
