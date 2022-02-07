package pjatk.pro.event_organizer_app.cateringforchosenevent.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.catering.model.CateringItem;
import pjatk.pro.event_organizer_app.catering.service.CateringItemService;
import pjatk.pro.event_organizer_app.cateringforchosenevent.mapper.CateringOrderChoiceMapper;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringOrderChoice;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringOrderChoiceDto;
import pjatk.pro.event_organizer_app.cateringforchosenevent.repository.CateringOrderChoiceRepository;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CateringOrderChoiceService {

    private final CateringOrderChoiceRepository cateringOrderChoiceRepository;
    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;
    private final CateringItemService cateringItemService;


    public ImmutableList<CateringOrderChoice> getAll(long cateringId, long reservationId) {
        return ImmutableList.copyOf(cateringOrderChoiceRepository.getAll(cateringId, reservationId));
    }

    public ImmutableList<CateringOrderChoice> getAll(long cateringId) {
        return ImmutableList.copyOf(cateringOrderChoiceRepository.getAll(cateringId));
    }

    public List<CateringOrderChoice> create(CateringOrderChoiceDto[] dtos, long reservationId) {

        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.get(reservationId);
        final List<CateringOrderChoice> result = new ArrayList<>();

        for (CateringOrderChoiceDto dto : dtos) {
            final CateringItem cateringItem = cateringItemService.get(dto.getItemId());
            final CateringOrderChoice orderChoice = CateringOrderChoiceMapper.fromDto(dto);
            orderChoice.setItem(cateringItem);
            orderChoice.setEventLocationCatering(catering);

            result.add(orderChoice);

        }
        cateringOrderChoiceRepository.saveAll(result);
        return result;
    }

    public CateringOrderChoice edit(CateringOrderChoiceDto dto, long orderChoiceId) {
        final CateringOrderChoice orderChoice = cateringOrderChoiceRepository.findWithDetail(orderChoiceId)
                .orElseThrow(() -> new NotFoundException("No caatering order with id " + orderChoiceId));

        if (orderChoice.getEventLocationCatering().isCateringOrderConfirmed()) {
            throw new ActionNotAllowedException("Cannot edit order after it was confirmed");
        }
        orderChoice.setAmount(dto.getAmount());

        cateringOrderChoiceRepository.save(orderChoice);
        return orderChoice;
    }

    public void delete(long id) {
        final CateringOrderChoice orderChoice = cateringOrderChoiceRepository.findWithDetail(id)
                .orElseThrow(() -> new NotFoundException("No caatering order with id " + id));

        if (orderChoice.getEventLocationCatering().isCateringOrderConfirmed()) {
            throw new ActionNotAllowedException("Cannot edit order adter it was confirmed");
        }
        cateringOrderChoiceRepository.delete(orderChoice);
    }


}


