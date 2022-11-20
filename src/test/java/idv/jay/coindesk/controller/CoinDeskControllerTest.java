package idv.jay.coindesk.controller;

import idv.jay.coindesk.controller.request.CreateCurrencyRequest;
import idv.jay.coindesk.controller.request.UpdateCurrencyRequest;
import idv.jay.coindesk.controller.response.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("整合測試")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoinDeskControllerTest {

  public static final String TYPE_BPI_DATA = "BPI_DATA";
  public static final String TYPE_TRANSFER_BPI_DATA = "TRANSFER_BPI_DATA";
  @LocalServerPort
  int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  @DisplayName("測試取得BPI資料")
  @Disabled
  void testGetBPIData() {
    ResponseEntity<ApiResponse> forEntity = testRestTemplate.getForEntity(
        "/coinDesk/getAPIData/BPI_DATA", ApiResponse.class);

    Assertions.assertEquals(HttpStatus.OK, forEntity.getStatusCode().is2xxSuccessful());
    Assertions.assertNotNull(forEntity.getBody());
  }

  @Test
  @DisplayName("測試新增幣別")
  void testCreateCurrency() {
    CreateCurrencyRequest request = CreateCurrencyRequest.builder().code("USD").currencyName("美金")
        .build();
    ResponseEntity<ApiResponse> responseEntity = testRestTemplate.postForEntity(
        "/coinDesk/currency", request, ApiResponse.class);

    Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    Assertions.assertNotNull(responseEntity.getBody());
  }

  @Test
  @DisplayName("查詢幣別")
  void testQueryCurrency() {
    ResponseEntity<ApiResponse> forEntity = testRestTemplate.getForEntity("/coinDesk/currency/EUR",
        ApiResponse.class);

    Assertions.assertTrue(forEntity.getStatusCode().is2xxSuccessful());
    Assertions.assertNotNull(forEntity.getBody());
  }

  @Test
  @DisplayName("修改幣別")
  void testUpdateCurrency() {
    UpdateCurrencyRequest request = UpdateCurrencyRequest.builder().code("EUR").CurrencyName("英鎊")
        .build();
    ResponseEntity<ApiResponse> response = testRestTemplate.exchange(
        "http://localhost:" + port + "/coinDesk/currency",
        HttpMethod.PUT,
        new HttpEntity<>(request), ApiResponse.class);

    Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    Assertions.assertNotNull(response.getBody());
  }
  
  @Test
  @DisplayName("測試刪除幣別")
  void testDeleteCurrency() {
    ResponseEntity<Void> response = testRestTemplate.exchange(
        "http://localhost:" + port + "/coinDesk/currency/EUR", HttpMethod.DELETE, HttpEntity.EMPTY,
        Void.class);

    Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
  }
}
