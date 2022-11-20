package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase.OutputValues;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class CreateCurrencyUseCase extends UseCase<InputValues, OutputValues> {

  private final CurrencyRepository currencyRepository;

  @Override
  public OutputValues execute(InputValues input) {
    validatedInputValues(input);
    Currency currency = Currency.builder()
        .code(input.getCode()).currencyName(input.getCurrencyName()).build();
    currencyRepository.createCurrency(currency);
    return new OutputValues(currency);
  }

  @Value
  @RequiredArgsConstructor
  public static class InputValues implements UseCase.InputValues {
    final String code;
    final String currencyName;
  }

  @Value
  @AllArgsConstructor
  public static class OutputValues implements UseCase.OutputValues {  
    private Currency currency;
  }
  
  @Override
  protected void validatedInputValues(InputValues inputValues) throws UseCaseException{
    final String code = inputValues.getCode();
    final String currencyName = inputValues.getCurrencyName();
    
    if(code == null || code.isEmpty()) 
      throw new UseCaseException(ErrorCode.INPUT_ARGUMENT_IS_NULL_OR_BLANK);

    if(currencyName == null || currencyName.isEmpty())
      throw new UseCaseException(ErrorCode.INPUT_ARGUMENT_IS_NULL_OR_BLANK);
  }
}
