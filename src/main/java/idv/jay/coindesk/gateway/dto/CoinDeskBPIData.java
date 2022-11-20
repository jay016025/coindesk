package idv.jay.coindesk.gateway.dto;

import idv.jay.coindesk.core.gateway.GetBPIDataGateway.BPIData;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinDeskBPIData implements BPIData {
  private Time time;
  private String disclaimer;
  private String chartName;
  private BPI bpi;
  
  public List<Coin> getCoins() {
    List<Coin> coins = new ArrayList<>();
    coins.add(this.bpi.USD);
    coins.add(this.bpi.GBP);
    coins.add(this.bpi.EUR);
    return coins;
  }
  
  public String getTransUpdateTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
    LocalDateTime dateTime = LocalDateTime.parse(this.time.updatedISO, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    return formatter.format(dateTime);
  }
  
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Time {
    private String updated;
    private String updatedISO;
    private String updateduk;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class BPI {
    private Coin USD;
    private Coin GBP;
    private Coin EUR;
  }
  
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Coin {
    private String code;
    private String symbol;
    private String rate;
    private String description;
    private BigDecimal rate_float;
  }
}
