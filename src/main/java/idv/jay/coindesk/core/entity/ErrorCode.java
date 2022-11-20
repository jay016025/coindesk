package idv.jay.coindesk.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  INPUT_ARGUMENT_IS_NULL_OR_BLANK("E001", "輸入參數空值"),
  THIRD_PARTY_DATA_NOT_FOUND("E002", "第三方API無資料回傳"),
  DATA_NOT_FOUND("W001", "查無資料"),
  UPDATE_BUT_DATA_NOT_FOUND("W002", "查無修改資料"), 
  INPUT_ARGUMENT_ERROR("E003", "輸入參數錯誤"), 
  THIRD_PARTY_API_DATA_NOT_FOUND("E004", "第三方API無資料回傳"), 
  THIRD_PARTY_API_ERROR("E005", "呼叫第三方API錯誤");
  private String errorCode;
  private String message;
}
