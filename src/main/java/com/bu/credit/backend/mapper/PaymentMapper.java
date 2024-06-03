package com.bu.credit.backend.mapper;

import com.bu.credit.backend.dto.request.PaymentRequestDto;
import com.bu.credit.backend.dto.response.PaymentResponseDto;
import com.bu.credit.backend.model.Debt;
import com.bu.credit.backend.model.Payment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

  List<PaymentResponseDto> toPaymentReponse(List<Payment> payments);

  @Mapping(target = "debt", source = "debtId", qualifiedByName = "debtIdToDebt")
  Payment toEntity(PaymentRequestDto paymentRequestDto);

  @Named("debtIdToDebt")
  default Debt debtIdToDebt(Long debtId) {
    if (debtId == null) {
      return null;
    }
    Debt debt = new Debt();
    debt.setId(debtId);
    return debt;
  }
}
