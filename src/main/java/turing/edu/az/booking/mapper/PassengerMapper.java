package turing.edu.az.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import turing.edu.az.booking.domain.entity.Passenger;
import turing.edu.az.booking.model.response.PassengerDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassengerMapper extends EntityMapper<PassengerDto, Passenger> {

    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);

    @Override
    Passenger toEntity(PassengerDto dto);

    @Override
    List<Passenger> toEntity(List<PassengerDto> dtoList);

    @Override
    PassengerDto toDto(Passenger entity);

    @Override
    List<PassengerDto> toDto(List<Passenger> entityList);
}