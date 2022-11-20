package idv.jay.coindesk.share.mapper;

import idv.jay.coindesk.controller.request.CreateCurrencyRequest;
import idv.jay.coindesk.controller.request.UpdateCurrencyRequest;
import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase;
import idv.jay.coindesk.repository.entity.JpaCurrency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

  CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

  JpaCurrency mapToJpaEntity(Currency currency);

  Currency mapToCurrency(JpaCurrency jpaCurrency);

  CreateCurrencyUseCase.InputValues mapToCreateCurrencyInputValues(CreateCurrencyRequest request);
  UpdateCurrencyUseCase.InputValues mapToUpdateCurrencyInputValues(UpdateCurrencyRequest request);
}
