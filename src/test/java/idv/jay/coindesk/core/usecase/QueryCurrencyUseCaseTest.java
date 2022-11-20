package idv.jay.coindesk.core.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase.OutputValues;
import idv.jay.coindesk.core.repository.CurrencyRepository;
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
@DisplayName("測試查詢幣別使用案例")
public class QueryCurrencyUseCaseTest {
  
  @Mock
  CurrencyRepository currencyRepository;
  @InjectMocks
  QueryCurrencyUseCase queryCurrencyUseCase;
  
  @Test
  @DisplayName("測試查詢幣別")
  void testQueryCurrency() {
    
    // Arrange
    QueryCurrencyUseCase.InputValues inputValues = new InputValues("EUR");
    Currency matches = Currency.builder().code("EUR").currencyName("歐元").build();

    Mockito.doReturn(Optional.ofNullable(matches)).when(currencyRepository).findById(anyString());
    
    // Ack
    OutputValues outputValues = queryCurrencyUseCase.execute(inputValues);
    
    // Assert
    Mockito.verify(currencyRepository).findById(anyString());
    Assertions.assertEquals(matches, outputValues.getCurrency());
  }
  
  @Test
  @DisplayName("測試查詢幣別但找不到資料")
  void testQueryCurrencyButNoDataFound() {
    
    // Arrange
    QueryCurrencyUseCase.InputValues inputValues = new InputValues("USD");
    Mockito.doReturn(Optional.empty()).when(currencyRepository).findById(eq(inputValues.getCode()));
    
    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class,
        () -> queryCurrencyUseCase.execute(inputValues));
    Assertions.assertEquals("W001", useCaseException.getCode());
    Mockito.verify(currencyRepository).findById(anyString());
  }
  
  @Test
  @DisplayName("測試查詢幣別但輸入參數空值")
  void testQueryCurrencyButInputValuesHasBlank() {
    // Arrange
    QueryCurrencyUseCase.InputValues inputValues = new InputValues("");
    
    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class,
        () -> queryCurrencyUseCase.execute(inputValues));
    Assertions.assertEquals("E001", useCaseException.getCode());
  }
}
