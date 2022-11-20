package idv.jay.coindesk.core.exception;

import idv.jay.coindesk.core.entity.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class ThirdPartyAPIException extends RuntimeException{
  private final String code;
  private final String message;

  public ThirdPartyAPIException(ErrorCode errorCode) {
    this.code = errorCode.getErrorCode();
    this.message = errorCode.getMessage();
  }
}
