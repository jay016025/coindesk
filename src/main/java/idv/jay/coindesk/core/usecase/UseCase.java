package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.exception.UseCaseException;

public abstract class UseCase<I extends UseCase.InputValues, O extends UseCase.OutputValues> {

  public abstract O execute(I input);
  
  protected abstract void validatedInputValues(I input) throws UseCaseException;
  
  public interface InputValues {}

  public interface OutputValues {}
}
