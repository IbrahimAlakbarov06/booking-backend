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
    PassengerDto toEntity(Passenger dto);

    @Override
    List<PassengerDto> toEntity(List<Passenger> dtoList);

    @Override
    Passenger toDto(PassengerDto entity);

    @Override
    List<Passenger> toDto(List<PassengerDto> entityList);
}