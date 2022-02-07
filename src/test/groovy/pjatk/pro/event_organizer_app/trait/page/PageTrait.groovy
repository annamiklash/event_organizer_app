package pjatk.pro.event_organizer_app.trait.page

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import pjatk.pro.event_organizer_app.common.paginator.CustomPage

trait PageTrait {

    CustomPage fakePage = CustomPage.builder()
            .pageNo(0)
            .pageSize(10)
            .sortBy("id")
            .order('desc')
            .build()

    Pageable fakePaging = PageRequest.of(0, 10, Sort.by("id").descending())

}