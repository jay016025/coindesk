package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase.OutputValues;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class UpdateCurrencyUseCase extends UseCase<InputValues, OutputValues> {

  private final CurrencyRepository currencyRepository;

  @Override
  public OutputValues execute(InputValues input) {
    validatedInputValues(input);
    final String code = input.getCode();
    final String currencyName = input.getCurrencyName();
    Currency current = currencyRepository.findById(code).orElseThrow(() -> new UseCaseException(
        ErrorCode.UPDATE_BUT_DATA_NOT_FOUND));
    current.updateCurrencyName(currencyName);
    return new OutputValues(currencyRepository.updateCurrency(current));
  }

  @Override
  protected void validatedInputValues(InputValues input) throws UseCaseException {
    final String code = input.getCode();
    final String currencyName = input.getCurrencyName();

    if (code == null || code.isEmpty()) {
      throw new UseCaseException(ErrorCode.INPUT_ARGUMENT_IS_NULL_OR_BLANK);
    }
    if (currencyName == null || currencyName.isEmpty()) {
      throw new UseCaseException(ErrorCode.INPUT_ARGUMENT_IS_NULL_OR_BLANK);
    }
  }

  @Value
  @RequiredArgsConstructor
  public static class InputValues implements UseCase.InputValues {

    private final String code;
    private final String currencyName;
  }

  @Value
  @AllArgsConstructor
  public static class OutputValues implements UseCase.OutputValues {

    private Currency currency;
  }
}
