package turing.edu.az.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.model.response.FlightDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper extends EntityMapper<FlightDto, Flight> {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Override
    FlightDto toEntity(Flight dto);

    @Override
    List<FlightDto> toEntity(List<Flight> dtoList);

    @Override
    Flight toDto(FlightDto entity);

    @Override
    List<Flight> toDto(List<FlightDto> entityList);
}
