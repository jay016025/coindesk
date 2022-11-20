package idv.jay.coindesk.gateway.dto;

import idv.jay.coindesk.core.gateway.GetBPIDataGateway.BPIData;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class CoinDeskBPITransferData implements BPIData {
  private String updateTime;
  private List<TransferCoin> transferCoins;

  @Data
  @Builder
  public static class TransferCoin {
    private String code;
    private String codeName;
    private BigDecimal rate;
  }
}
