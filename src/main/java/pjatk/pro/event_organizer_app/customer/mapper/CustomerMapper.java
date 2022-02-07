package pjatk.pro.event_organizer_app.customer.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.appproblem.mapper.AppProblemMapper;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.customer.guest.mapper.GuestMapper;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.image.mapper.ImageMapper;
import pjatk.pro.event_organizer_app.user.model.dto.CustomerUserRegistrationDto;
import pjatk.pro.event_organizer_app.user.model.dto.UserDto;

import java.util.stream.Collectors;

@UtilityClass
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        final CustomerDto dto = CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserDto.builder()
                        .id(customer.getId())
                        .email(customer.getEmail())
                        .type(customer.getType())
                        .createdAt(DateTimeUtil.fromLocalDateTimetoString(customer.getCreatedAt()))
                        .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(customer.getModifiedAt()))
                        .build())
                .build();


        if (customer.getAvatar() != null) {
            dto.setAvatar(ImageMapper.toDto(customer.getAvatar()));
        }

        return dto;
    }

    public CustomerDto toDtoWithGuests(Customer customer) {
        return CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(String.valueOf(customer.getPhoneNumber()))
                .birthdate(customer.getBirthdate().toString())
                .user(UserDto.builder()
                        .id(customer.getId())
                        .email(customer.getEmail())
                        .type(customer.getType())
                        .createdAt(DateTimeUtil.fromLocalDateTimetoString(customer.getCreatedAt()))
                        .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(customer.getModifiedAt()))
                        .build())
                .guests(customer.getGuests().stream()
                        .map(GuestMapper::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public CustomerDto toDtoWithProblems(Customer customer) {
        final CustomerDto dto = toDto(customer);
        final UserDto user = dto.getUser();

        user.setAppProblems(customer.getAppProblems()
                .stream()
                .map(AppProblemMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public CustomerDto toDtoWithDetail(Customer customer) {
        final CustomerDto dto = toDtoWithProblems(customer);

        dto.setGuests(customer.getGuests().stream()
                .map(GuestMapper::toDto)
                .collect(Collectors.toSet()));
        dto.setEvents(customer.getEvents().stream()
                .map(OrganizedEventMapper::toDto)
                .collect(Collectors.toSet()));

        if (customer.getAvatar() != null) {
            dto.setAvatar(ImageMapper.toDto(customer.getAvatar()));
        }

        return dto;
    }


    public Customer fromCustomerRegistrationDto(CustomerUserRegistrationDto dto) {
        return Customer.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthdate(DateTimeUtil.fromStringToLocalDate(dto.getBirthdate()))
                .isActive(true)
                .type(dto.getType())
                .phoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
                .build();
    }

    public Customer fromDto(CustomerDto customer) {
        return Customer.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .birthdate(DateTimeUtil.fromStringToFormattedDate(customer.getBirthdate()))
                .phoneNumber(Converter.convertPhoneNumberString(customer.getPhoneNumber()))
                .build();
    }

    public static CustomerDto toDtoWIthAvatar(Customer customer) {
        final CustomerDto dto = toDto(customer);
        dto.setAvatar(ImageMapper.toDto(customer.getAvatar()));

        return dto;
    }
}
