package turing.edu.az.booking.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.model.response.BookingDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDto, Booking> {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "passengerName", source = "passenger.fullName")
    @Mapping(target = "passengerEmail", source = "passenger.email")
    @Mapping(target = "origin", source = "flight.origin")
    @Mapping(target = "destination", source = "flight.destination")
    @Mapping(target = "flightDateTime", source = "flight.timestamp")
    BookingDto toDto(Booking entity);


    @Override
    List<BookingDto> toDto(List<Booking> entityList);

    @Override
    @Mapping(target = "flight.id", source = "flightId")
    @Mapping(target = "passenger", ignore = true)
    Booking toEntity(BookingDto dto);

    @Override
    List<Booking> toEntity(List<BookingDto> dtoList);
}