package pjatk.pro.event_organizer_app.common.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;

@UtilityClass
public class PageableMapper {

    public Pageable map(CustomPage customPage) {
        final Sort orders = "desc".equalsIgnoreCase(customPage.getOrder())
                ? Sort.by(customPage.getSortBy()).descending()
                : Sort.by(customPage.getSortBy()).ascending();
        return PageRequest.of(
                customPage.getPageNo(),
                customPage.getPageSize(),
                orders
        );
    }
}
