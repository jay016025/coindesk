package idv.jay.coindesk.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCurrencyRequest {
  private String code;
  private String currencyName;
}
