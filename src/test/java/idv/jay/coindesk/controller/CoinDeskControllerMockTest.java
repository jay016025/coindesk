package idv.jay.coindesk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import idv.jay.coindesk.controller.request.CreateCurrencyRequest;
import idv.jay.coindesk.controller.request.UpdateCurrencyRequest;
import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.ThirdPartyAPIException;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase;
import idv.jay.coindesk.core.usecase.DeleteCurrencyUseCase;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.InputValues;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.OutputValues;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.BPI;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.Coin;
import idv.jay.coindesk.gateway.dto.CoinDeskBPIData.Time;
import idv.jay.coindesk.gateway.dto.CoinDeskBPITransferData;
import idv.jay.coindesk.gateway.dto.CoinDeskBPITransferData.TransferCoin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("controller mock測試")
@WebMvcTest(controllers = CoinDeskController.class)
public class CoinDeskControllerMockTest {

  public static final String TYPE_BPI_DATA = "BPI_DATA";
  public static final String TYPE_TRANSFER_BPI_DATA = "TRANSFER_BPI_DATA";

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  GetBPICurrentPriceUseCase getBPICurrentPriceUseCase;
  @MockBean
  CreateCurrencyUseCase createCurrencyUseCase;
  @MockBean
  QueryCurrencyUseCase queryCurrencyUseCase;
  @MockBean
  UpdateCurrencyUseCase updateCurrencyUseCase;
  @MockBean
  DeleteCurrencyUseCase deleteCurrencyUseCase;

  @Test
  @DisplayName("測試取得coin desk API資料")
  void testGetCoinDeskAPIData() throws Exception {

    // Arrange
    GetBPICurrentPriceUseCase.InputValues matches = new InputValues(TYPE_BPI_DATA);
    Mockito.doReturn(buildGetBPICurrentPriceUseCaseOutputValues(TYPE_BPI_DATA))
        .when(getBPICurrentPriceUseCase).execute(eq(matches));

    // Ack、Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/coinDesk/getAPIData/BPI_DATA"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").hasJsonPath())
        .andExpect(jsonPath("$.data").hasJsonPath())
        .andExpect(jsonPath("$.message").isEmpty())
        .andExpect(jsonPath("$.data").isNotEmpty());
  }

  @Test
  @DisplayName("測試取得coin desk API轉換資料")
  void testGetCoinDeskAPITransferData() throws Exception {
    // Arrange
    GetBPICurrentPriceUseCase.InputValues matches = new InputValues(TYPE_TRANSFER_BPI_DATA);
    Mockito.doReturn(buildGetBPICurrentPriceUseCaseOutputValues(TYPE_TRANSFER_BPI_DATA))
        .when(getBPICurrentPriceUseCase).execute(eq(matches));

    // Ack、Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/coinDesk/getAPIData/TRANSFER_BPI_DATA"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").hasJsonPath())
        .andExpect(jsonPath("$.data").hasJsonPath())
        .andExpect(jsonPath("$.message").isEmpty())
        .andExpect(jsonPath("$.data").isNotEmpty());
  }

  @Test
  @DisplayName("測試建立幣別")
  void testCreateCurrency() throws Exception {

    // Arrange
    CreateCurrencyRequest request =
        CreateCurrencyRequest.builder().code("USD").currencyName("美金").build();
    CreateCurrencyUseCase.OutputValues matches = new CreateCurrencyUseCase.OutputValues(
        Currency.builder().code("USD").currencyName("美金").build());
    Mockito.doReturn(matches).when(createCurrencyUseCase).execute(any());

    // Ack
    mockMvc.perform(MockMvcRequestBuilders.post("/coinDesk/currency")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").isEmpty())
        .andExpect(jsonPath("$.errorCode").isEmpty())
        .andExpect(jsonPath("$.data").isNotEmpty());
  }

  @Test
  @DisplayName("測試查詢幣別")
  void testQueryCurrency() throws Exception {

    // Arrange
    QueryCurrencyUseCase.OutputValues matches = new QueryCurrencyUseCase.OutputValues(
        Currency.builder().code("USD").currencyName("美金").build());
    QueryCurrencyUseCase.InputValues inputValues = new QueryCurrencyUseCase.InputValues("USD");
    Mockito.doReturn(matches).when(queryCurrencyUseCase).execute(eq(inputValues));

    // Ack
    mockMvc.perform(MockMvcRequestBuilders.get("/coinDesk/currency/USD"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").isEmpty())
        .andExpect(jsonPath("$.errorCode").isEmpty())
        .andExpect(jsonPath("$.data").isNotEmpty());
    Mockito.verify(queryCurrencyUseCase).execute(any());
  }

  @Test
  @DisplayName("測試修改幣別資訊")
  public void testUpdateCurrency() throws Exception {

    // Arrange
    UpdateCurrencyRequest request = UpdateCurrencyRequest.builder().code("GBP").CurrencyName("英鎊")
        .build();

    UpdateCurrencyUseCase.OutputValues matches = new UpdateCurrencyUseCase.OutputValues(
        Currency.builder().code("GBP").currencyName("英鎊").build());
    Mockito.doReturn(matches).when(updateCurrencyUseCase).execute(any());

    // Ack
    mockMvc.perform(
            MockMvcRequestBuilders.put("/coinDesk/currency").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.message").isEmpty())
        .andExpect(jsonPath("$.errorCode").isEmpty())
        .andExpect(jsonPath("$.data").isNotEmpty());
  }

  @Test
  @DisplayName("測試刪除幣別")
  void testDeleteCurrency() throws Exception {

    // Arrange
    DeleteCurrencyUseCase.InputValues inputValues = new DeleteCurrencyUseCase.InputValues("EUR");
    Mockito.doReturn(null).when(deleteCurrencyUseCase).execute(eq(inputValues));
    
    // Ack
    mockMvc.perform(MockMvcRequestBuilders.delete("/coinDesk/currency/EUR"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("測試應用程式出錯並顯示於訊息")
  void testApplicationHasExceptionShowOnMessage() throws Exception {
    // Arrange
    GetBPICurrentPriceUseCase.InputValues matches = new InputValues(TYPE_TRANSFER_BPI_DATA);
    Mockito.doThrow(new ThirdPartyAPIException(ErrorCode.THIRD_PARTY_API_ERROR))
        .when(getBPICurrentPriceUseCase).execute(eq(matches));

    // Ack、Assert
    mockMvc.perform(
            MockMvcRequestBuilders.get("/coinDesk/getAPIData/TRANSFER_BPI_DATA"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").isNotEmpty())
        .andExpect(jsonPath("$.errorCode").isNotEmpty())
        .andExpect(jsonPath("$.data").isEmpty());
  }

  private GetBPICurrentPriceUseCase.OutputValues buildGetBPICurrentPriceUseCaseOutputValues(
      String type) {
    if (TYPE_BPI_DATA.equals(type)) {
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

      return new GetBPICurrentPriceUseCase.OutputValues(coinDeskBPIData);
    } else {
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
      return new OutputValues(CoinDeskBPITransferData.builder().updateTime("2022/11/19 04:05:00")
          .transferCoins(transferCoins).build());
    }
  }
}
