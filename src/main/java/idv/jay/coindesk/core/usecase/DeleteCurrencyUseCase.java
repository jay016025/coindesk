package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.DeleteCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.DeleteCurrencyUseCase.OutputValues;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class DeleteCurrencyUseCase extends UseCase<InputValues, OutputValues> {

  private final CurrencyRepository currencyRepository;

  @Override
  public OutputValues execute(InputValues input) {
    validatedInputValues(input);
    currencyRepository.deleteById(input.getCode());
    return null;
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

  public static class OutputValues implements UseCase.OutputValues {}
}
