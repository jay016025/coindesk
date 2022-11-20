package idv.jay.coindesk.core.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Currency {
  private String code;
  private String currencyName;

  public void updateCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }
}
