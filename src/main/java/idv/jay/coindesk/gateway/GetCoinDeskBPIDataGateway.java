package idv.jay.coindesk.gateway;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.ThirdPartyAPIException;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.Coin;
import idv.jay.coindesk.gateway.dto.CoinDeskBPITransferData;
import idv.jay.coindesk.gateway.dto.CoinDeskBPITransferData.TransferCoin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class GetCoinDeskBPIDataGateway implements GetBPIDataGateway {

  private final String URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
  private final RestTemplate restTemplate;
  private final CurrencyRepository currencyRepository;

  @Override
  public CoinDeskBPIData getBPIDate() {
    // call API
    ResponseEntity<CoinDeskBPIData> response = restTemplate.getForEntity(URL,
        CoinDeskBPIData.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new ThirdPartyAPIException(ErrorCode.THIRD_PARTY_API_ERROR);
    }

    if (!response.hasBody()) {
      throw new ThirdPartyAPIException(ErrorCode.THIRD_PARTY_API_DATA_NOT_FOUND);
    }
    return response.getBody();
  }

  @Override
  public CoinDeskBPITransferData transferData() {
    CoinDeskBPIData coinDeskBPIData = this.getBPIDate();
    List<Coin> coins = coinDeskBPIData.getCoins();

    List<TransferCoin> transferCoinList = coins.stream()
        .filter(e -> e.getCode() != null)
        .filter(e -> !e.getCode().isEmpty())
        .map(e -> {
          Optional<Currency> currency = currencyRepository.findById(e.getCode());
          return TransferCoin.builder().code(e.getCode())
              .codeName(currency.orElseGet(() -> Currency.builder().currencyName("")
                  .build()).getCurrencyName())
              .rate(e.getRate_float().setScale(4, BigDecimal.ROUND_DOWN)).build();
        })
        .collect(Collectors.toList());

    return CoinDeskBPITransferData.builder().updateTime(coinDeskBPIData.getTransUpdateTime())
        .transferCoins(transferCoinList)
        .build();
  }
}
