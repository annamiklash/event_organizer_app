package pjatk.pro.event_organizer_app.catering.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.catering.mapper.CateringItemMapper;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.model.CateringItem;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringItemDto;
import pjatk.pro.event_organizer_app.catering.repository.CateringItemRepository;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CateringItemService {

   private final CateringItemRepository cateringItemRepository;

   private final CateringService cateringService;

   private final TimestampHelper timestampHelper;


    public ImmutableList<CateringItem> listAllByCateringId(long cateringId) {
        final List<CateringItem> cateringItemList = cateringItemRepository.findAllByCatering_Id(cateringId);

        return ImmutableList.copyOf(cateringItemList);
    }

    public CateringItem get(long id) {
        final Optional<CateringItem> optionalCateringItem = cateringItemRepository.findById(id);
        if (optionalCateringItem.isPresent()) {
            return optionalCateringItem.get();
        }
        throw new NotFoundException("Catering item with id " + id + " DOES NOT EXIST");

    }

    public CateringItem create(CateringItemDto dto, long cateringId) {
        final Catering catering = cateringService.get(cateringId);

        final CateringItem cateringItem = CateringItemMapper.fromDto(dto);

        cateringItem.setCatering(catering);
        cateringItem.setCreatedAt(timestampHelper.now());
        cateringItem.setModifiedAt(timestampHelper.now());
        catering.setModifiedAt(timestampHelper.now());

        log.info("TRYING TO SAVE " + cateringItem);

        cateringItemRepository.save(cateringItem);

        return cateringItem;
    }

    public CateringItem edit(long cateringItemId, CateringItemDto dto) {

        final CateringItem cateringItem = get(cateringItemId);

        cateringItem.setItemType(dto.getType());
        cateringItem.setName(dto.getName());
        cateringItem.setDescription(dto.getDescription());
        cateringItem.setVegan(dto.getIsVegan());
        cateringItem.setVegetarian(dto.getIsVegetarian());
        cateringItem.setGlutenFree(dto.getIsGlutenFree());
        cateringItem.setServingPrice(Converter.convertPriceString(dto.getServingPrice()));
        cateringItem.setModifiedAt(timestampHelper.now());

        cateringItemRepository.save(cateringItem);

        log.info("UPDATED");

        return cateringItem;
    }

    public void delete(long id) {
        log.info("TRYING TO DELETE CATERING WITH ID " + id);
        cateringItemRepository.deleteById(id);
    }

}
