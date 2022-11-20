package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.InputValues;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.OutputValues;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("測試取得BPI資訊使用案例")
public class GetBPICurrentPriceUseCaseTest {

  public static final String TYPE_BPI_DATA = "BPI_DATA";
  public static final String TYPE_TRANSFER_BPI_DATA = "TRANSFER_BPI_DATA";

  @Mock
  GetBPIDataGateway getBPIDataAPI;
  @InjectMocks
  GetBPICurrentPriceUseCase getBPICurrentPriceUseCase;

  @Test
  @DisplayName("測試取得BPI資訊")
  void testGetBPICurrentPrice() {

    // Arrange
    GetBPICurrentPriceUseCase.InputValues inputValues = new InputValues(TYPE_BPI_DATA);
    CoinDeskBPIData matches = buildCoinDeskBPIData();

    Mockito.doReturn(matches).when(getBPIDataAPI).getBPIDate();

    // Ack
    OutputValues outputValues = getBPICurrentPriceUseCase.execute(inputValues);

    // Assert
    Mockito.verify(getBPIDataAPI).getBPIDate();
    Assertions.assertEquals(matches, outputValues.getBpiData());
  }

  @Test
  @DisplayName("測試取得轉換BPI資料")
  void testTransferBPIData() {

    // Arrange
    GetBPICurrentPriceUseCase.InputValues inputValues = new InputValues(TYPE_TRANSFER_BPI_DATA);
    CoinDeskBPITransferData matches = buildCoinDeskBPITransferData();
    Mockito.doReturn(matches).when(getBPIDataAPI).transferData();
    
    // Ack
    OutputValues outputValues = getBPICurrentPriceUseCase.execute(inputValues);
    
    // Assert
    Mockito.verify(getBPIDataAPI).transferData();
    Assertions.assertEquals(matches, outputValues.getBpiData());
  }
  
  @Test
  @DisplayName("測試取得BPI資訊但輸入值錯誤")
  void testGetBPICurrentPriceButInputValuesError() {
    
    // Arrange
    GetBPICurrentPriceUseCase.InputValues inputValues = new InputValues("ErrorType");
    
    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class,
        () -> getBPICurrentPriceUseCase.execute(inputValues));
    Assertions.assertEquals("E003", useCaseException.getCode());
  }

  private CoinDeskBPIData buildCoinDeskBPIData() {
    Time time = Time.builder()
        .updated("Nov 19, 2022 04:05:00 UTC")
        .updatedISO("2022-11-19T04:05:00+00:00")
        .updateduk("Nov 19, 2022 at 04:05 GMT").build();

    Coin coinOfUSD = Coin.builder().code("USD").symbol("&#36;").rate("16,614.9387")
        .description("United States Dollar").rate_float(new BigDecimal(16614.9387)).build();
    Coin coinOfGBP = Coin.builder().code("GBP").symbol("&pound;").rate("13,883.3098")
        .description("British Pound Sterling").rate_float(new BigDecimal(13883.3098)).build();
    Coin coinOfEUR = Coin.builder().code("EUR").symbol("&euro;").rate("16,185.3760")
        .description("Euro").rate_float(new BigDecimal(16185.376)).build();

    BPI bpi = BPI.builder().USD(coinOfUSD).GBP(coinOfGBP).EUR(coinOfEUR).build();

    CoinDeskBPIData coinDeskBPIData = CoinDeskBPIData.builder().time(time).disclaimer(
            "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org")
        .chartName("Bitcoin")
        .bpi(bpi).build();

    return coinDeskBPIData;
  }

  private CoinDeskBPITransferData buildCoinDeskBPITransferData() {
    TransferCoin transOfUSD = TransferCoin.builder()
        .code("USD").codeName("美金").rate(new BigDecimal(16614.9387)).build();
    TransferCoin transOfGBP = TransferCoin.builder()
        .code("GBP").codeName("英鎊").rate(new BigDecimal(13883.3098)).build();
    TransferCoin transOfEUR = TransferCoin.builder()
        .code("EUR").codeName("歐元").rate(new BigDecimal(16185.376)).build();

    List<TransferCoin> transferCoins = new ArrayList<>();
    transferCoins.add(transOfUSD);
    transferCoins.add(transOfGBP);
    transferCoins.add(transOfEUR);
    
    return CoinDeskBPITransferData.builder().updateTime("2022/11/19 04:05:00")
        .transferCoins(transferCoins).build();
  }
}
