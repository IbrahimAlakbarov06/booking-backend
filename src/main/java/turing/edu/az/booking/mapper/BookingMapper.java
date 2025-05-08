package turing.edu.az.booking.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.model.response.BookingDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDto, Booking> {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Override
    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "passengerName", source = "passenger.fullName")
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