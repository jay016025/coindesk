package idv.jay.coindesk.gateway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.exception.ThirdPartyAPIException;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway.BPIData;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.BPI;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.Coin;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.Time;
import idv.jay.coindesk.gateway.dto.CoinDeskBPITransferData;
import idv.jay.coindesk.gateway.dto.CoinDeskBPITransferData.TransferCoin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("測試取得Coin Desk BPI資料 Gateway")
public class GetCoinDeskBPIDataGatewayTest {

  @Mock
  RestTemplate restTemplate;
  @Mock
  CurrencyRepository currencyRepository;
  @InjectMocks
  GetCoinDeskBPIDataGateway getCoinDeskBPIDataGateway;

  @Test
  @DisplayName("測試取得coin desk BPI資料")
  void testGetCoinDeskBPIData() {
    // Arrange
    CoinDeskBPIData matches = buildCoinDeskBPIData();
    Mockito.doReturn(ResponseEntity.of(Optional.of(matches))).when(restTemplate)
        .getForEntity(anyString(), any());

    // Ack
    CoinDeskBPIData coinDeskBPIData = getCoinDeskBPIDataGateway.getBPIDate();

    // Assert
    Assertions.assertEquals(matches, coinDeskBPIData);
  }

  @Test
  @DisplayName("測試取得第coin desk BPI資料錯誤")
  void testGetCoinDeskBPIDataError() {
    // Arrange
    Mockito.doReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
        .when(restTemplate)
        .getForEntity(anyString(), any());

    // Ack、Assert
    ThirdPartyAPIException thirdPartyAPIException = Assertions.assertThrows(
        ThirdPartyAPIException.class, () -> getCoinDeskBPIDataGateway.getBPIDate());

    Assertions.assertEquals("E005", thirdPartyAPIException.getCode());
  }

  @Test
  @DisplayName("測試轉換coin desk 資料")
  void testTransferCoinDeskData() {

    // Arrange
    CoinDeskBPIData matches = buildCoinDeskBPIData();
    Mockito.doReturn(ResponseEntity.of(Optional.of(matches))).when(restTemplate)
        .getForEntity(anyString(), any());

    Mockito.doReturn(Optional.ofNullable(buildCurrency("USD"))).when(currencyRepository)
        .findById(eq("USD"));
    Mockito.doReturn(Optional.ofNullable(buildCurrency("GBP"))).when(currencyRepository)
        .findById(eq("GBP"));
    Mockito.doReturn(Optional.ofNullable(buildCurrency("EUR"))).when(currencyRepository)
        .findById(eq("EUR"));

    CoinDeskBPITransferData expected = BuildExpectedTransferData();

    // Ack
    CoinDeskBPITransferData transferData = getCoinDeskBPIDataGateway.transferData();

    // Assert
    Mockito.verify(currencyRepository, Mockito.times(3)).findById(anyString());
    Assertions.assertAll(() -> {
      Assertions.assertEquals(expected.getUpdateTime(), transferData.getUpdateTime());
      for (int i = 0; i < expected.getTransferCoins().size(); i++) {
        Assertions.assertEquals(expected.getTransferCoins().get(i).getCode(),
            transferData.getTransferCoins().get(i).getCode());
        Assertions.assertEquals(expected.getTransferCoins().get(i).getCodeName(),
            transferData.getTransferCoins().get(i).getCodeName());
        Assertions.assertEquals(expected.getTransferCoins().get(i).getRate(),
            transferData.getTransferCoins().get(i).getRate());
      }
    });
  }

  private CoinDeskBPIData buildCoinDeskBPIData() {
    Time time = Time.builder()
        .updated("Nov 19, 2022 04:05:00 UTC")
        .updatedISO("2022-11-19T04:05:00+00:00")
        .updateduk("Nov 19, 2022 at 04:05 GMT").build();

    Coin coinOfUSD = Coin.builder().code("USD").symbol("&#36;").rate("16,614.9387")
        .description("United States Dollar")
        .rate_float(new BigDecimal(16614.9387).setScale(4, BigDecimal.ROUND_DOWN)).build();
    Coin coinOfGBP = Coin.builder().code("GBP").symbol("&pound;").rate("13,883.3098")
        .description("British Pound Sterling")
        .rate_float(new BigDecimal(13883.3098).setScale(4, BigDecimal.ROUND_DOWN)).build();
    Coin coinOfEUR = Coin.builder().code("EUR").symbol("&euro;").rate("16,185.3760")
        .description("Euro")
        .rate_float(new BigDecimal(16185.376).setScale(4, BigDecimal.ROUND_DOWN)).build();

    BPI bpi = BPI.builder().USD(coinOfUSD).GBP(coinOfGBP).EUR(coinOfEUR).build();

    CoinDeskBPIData coinDeskBPIData = CoinDeskBPIData.builder().time(time).disclaimer(
            "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org")
        .chartName("Bitcoin")
        .bpi(bpi).build();

    return coinDeskBPIData;
  }

  private Currency buildCurrency(String code) {
    Currency currency;
    switch (code) {
      case "USD":
        return Currency.builder().code("USD").currencyName("美金").build();
      case "GBP":
        return Currency.builder().code("GBP").currencyName("英鎊").build();
      case "EUR":
        return Currency.builder().code("EUR").currencyName("歐元").build();
    }
    return null;
  }

  private CoinDeskBPITransferData BuildExpectedTransferData() {
    List<TransferCoin> transferCoins = new ArrayList<>();
    transferCoins.add(
        TransferCoin.builder().code("USD").codeName("美金")
            .rate(new BigDecimal(16614.9387).setScale(4, BigDecimal.ROUND_DOWN)).build());
    transferCoins.add(
        TransferCoin.builder().code("GBP").codeName("英鎊")
            .rate(new BigDecimal(13883.3098).setScale(4, BigDecimal.ROUND_DOWN)).build());
    transferCoins.add(
        TransferCoin.builder().code("EUR").codeName("歐元")
            .rate(new BigDecimal(16185.376).setScale(4, BigDecimal.ROUND_DOWN)).build());

    CoinDeskBPITransferData expectedData = CoinDeskBPITransferData.builder()
        .updateTime("2022/11/19 04:05:00").transferCoins(transferCoins).build();

    return expectedData;
  }
}
