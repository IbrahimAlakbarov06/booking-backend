package turing.edu.az.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.model.response.BookingDto;
import turing.edu.az.booking.model.response.FlightDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDto, Booking> {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Override
    BookingDto toEntity(Booking dto);

    @Override
    List<BookingDto> toEntity(List<Booking> dtoList);

    @Override
    Booking toDto(BookingDto entity);

    @Override
    List<Booking> toDto(List<BookingDto> entityList);
}
