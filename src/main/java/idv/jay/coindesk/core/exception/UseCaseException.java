package idv.jay.coindesk.core.exception;

import idv.jay.coindesk.core.entity.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class UseCaseException extends RuntimeException{
  private final String code;
  private final String message;
  
  public UseCaseException(ErrorCode errorCode) {
    this.code = errorCode.getErrorCode();
    this.message = errorCode.getMessage();
  }
}
