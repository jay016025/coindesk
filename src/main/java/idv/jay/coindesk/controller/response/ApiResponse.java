package idv.jay.coindesk.controller.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
  private String message;
  private String errorCode;
  private T data;
}
