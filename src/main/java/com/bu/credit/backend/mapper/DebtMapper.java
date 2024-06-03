package com.bu.credit.backend.mapper;

import com.bu.credit.backend.dto.request.DebtRequestDto;
import com.bu.credit.backend.dto.response.DebtResponseDto;
import com.bu.credit.backend.model.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DebtMapper {

  DebtMapper INSTANCE = Mappers.getMapper(DebtMapper.class);

  @Mapping(target = "status", constant = "pending")
  Debt toEntity(DebtRequestDto debtRequestDto);

  List<DebtResponseDto> toDebtReponseList(List<Debt> debtList);

  DebtResponseDto toDebtReponse(Debt debt);
}
