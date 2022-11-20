package idv.jay.coindesk.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCurrencyRequest {
  private String code;
  private String CurrencyName;
}
