package pjatk.pro.event_organizer_app.customer.guest.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;
import pjatk.pro.event_organizer_app.customer.guest.model.dto.GuestDto;
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper;

@UtilityClass
public class GuestMapper {

    public GuestDto toDto(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .createdAt(String.valueOf(guest.getCreatedAt()))
                .modifiedAt(String.valueOf(guest.getModifiedAt()))
                .build();
    }


    public GuestDto toDtoWithCustomer(Guest guest) {
        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .createdAt(String.valueOf(guest.getCreatedAt()))
                .modifiedAt(String.valueOf(guest.getModifiedAt()))
                .customer(CustomerMapper.toDto(guest.getCustomer()))
                .build();
    }

    public Guest fromDto(GuestDto dto) {
        return Guest.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();

    }
}
