package pjatk.pro.event_organizer_app.business.service;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.address.service.AddressService;
import pjatk.pro.event_organizer_app.business.mapper.BusinessMapper;
import pjatk.pro.event_organizer_app.business.model.Business;
import pjatk.pro.event_organizer_app.business.model.dto.BusinessDto;
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.service.CateringService;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.enums.BusinessVerificationStatusEnum;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.exceptions.UserExistsException;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.service.LocationService;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService;
import pjatk.pro.event_organizer_app.security.password.PasswordEncoderSecurity;
import pjatk.pro.event_organizer_app.user.model.dto.BusinessUserRegistrationDto;
import pjatk.pro.event_organizer_app.user.service.UserService;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.exceptions.UserExistsException.ENUM.USER_EXISTS;

@Service
@AllArgsConstructor
@Slf4j
public class BusinessService {

    private final BusinessRepository businessRepository;

    private final AddressService addressService;

    private final UserService userService;

    private final LocationService locationService;

    private final CateringService cateringService;

    private final OptionalServiceService optionalServiceService;

    private final PasswordEncoderSecurity passwordEncoderSecurity;

    private final TimestampHelper timestampHelper;

    public ImmutableList<Business> list(CustomPage customPage) {
        final Pageable paging = PageableMapper.map(customPage);
        final Page<Business> page = businessRepository.findAll(paging);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    @Transactional(rollbackOn = Exception.class)
    public Business createBusinessAccount(BusinessUserRegistrationDto businessDto) {
        if (userService.userExists(businessDto.getEmail())) {
            throw new UserExistsException(USER_EXISTS);
        }
        final Address address = addressService.create(businessDto.getAddress());
        final Business business = BusinessMapper.fromBusinessUserRegistrationDto(businessDto);

        final String hashedPassword = passwordEncoderSecurity.bcryptEncryptor(businessDto.getPassword());
        business.setPassword(hashedPassword);

        business.setAddress(address);
        business.setCreatedAt(timestampHelper.now());
        business.setModifiedAt(timestampHelper.now());

        log.info("TRYING TO SAVE BUSINESS");
        businessRepository.save(business);

        return business;
    }

    public Business edit(long businessId, BusinessDto businessDto) {
        final Business business = businessRepository.getWithAddress(businessId)
                .orElseThrow(() -> new NotFoundException("Address with id " + businessId + " DOES NOT EXIST"));

        business.setBusinessName(businessDto.getBusinessName());
        business.setFirstName(businessDto.getFirstName());
        business.setLastName(businessDto.getLastName());
        business.setPhoneNumber(Converter.convertPhoneNumberString(businessDto.getPhoneNumber()));

        addressService.edit(business.getAddress().getId(), businessDto.getAddress());

        businessRepository.save(business);

        return business;
    }

    public Business getWithDetail(long businessId) {
        return businessRepository.getWithDetail(businessId)
                .orElseThrow(() -> new NotFoundException("Business with businessId " + businessId + " DOES NOT EXIST"));

    }

    public Business get(long businessId) {
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new NotFoundException("Business with businessId " + businessId + " DOES NOT EXIST"));
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(long businessId) {
        final Business businessToDelete = businessRepository.findAllBusinessInformation(businessId)
                .orElseThrow(() -> new NotFoundException("Business with businessId " + businessId + " DOES NOT EXIST"));

        final ImmutableSet<Location> locations = ImmutableSet.copyOf(locationService.getByBusinessId(businessId));

        final ImmutableSet<Catering> caterings =  ImmutableSet.copyOf(cateringService.getByBusinessId(businessId));

        final ImmutableSet<OptionalService> services =  ImmutableSet.copyOf(optionalServiceService.getByBusinessId(businessId));

        services
                .forEach(service -> optionalServiceService.delete(service.getId()));

        caterings
                .forEach(catering -> cateringService.delete(catering.getId()));

        locations
                .forEach(location -> locationService.delete(location.getId()));

        addressService.delete(businessToDelete.getAddress());
        businessRepository.delete(businessToDelete);
    }


    public Business verify(long id) {
        final Business business = get(id);
        business.setVerificationStatus(String.valueOf(BusinessVerificationStatusEnum.VERIFIED));

        businessRepository.save(business);
        return business;
    }


    private Business getWithAddress(long id) {
        return businessRepository.findByIdWithAddress(id)
                .orElseThrow(() -> new NotFoundException("Address with id " + id + " DOES NOT EXIST"));
    }
}
