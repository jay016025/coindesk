package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase.OutputValues;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class QueryCurrencyUseCase extends UseCase<InputValues, OutputValues> {

  private final CurrencyRepository currencyRepository;

  @Override
  public OutputValues execute(InputValues input) {
    validatedInputValues(input);
    Optional<Currency> optionalCurrency =
        currencyRepository.findById(input.getCode());
    return new OutputValues(optionalCurrency.
        orElseThrow(() -> new UseCaseException(ErrorCode.DATA_NOT_FOUND)));
  }

  @Override
  protected void validatedInputValues(InputValues input) throws UseCaseException {
    final String code = input.getCode();
    if (code == null || code.isEmpty()) {
      throw new UseCaseException(ErrorCode.INPUT_ARGUMENT_IS_NULL_OR_BLANK);
    }
  }

  @Value
  @RequiredArgsConstructor
  public static class InputValues implements UseCase.InputValues {

    private final String code;
  }

  @Value
  @AllArgsConstructor
  public static class OutputValues implements UseCase.OutputValues {

    private Currency currency;
  }
}
