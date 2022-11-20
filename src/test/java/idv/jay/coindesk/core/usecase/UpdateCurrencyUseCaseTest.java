package idv.jay.coindesk.core.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase.OutputValues;
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
@DisplayName("測試修改幣別使用案例")
public class UpdateCurrencyUseCaseTest {

  @Mock
  CurrencyRepository currencyRepository;
  @InjectMocks
  UpdateCurrencyUseCase updateCurrencyUseCase;

  @Test
  @DisplayName("測試修改幣別")
  void testUpdateCurrency() {

    // Arrange
    UpdateCurrencyUseCase.InputValues inputValues = new InputValues("USD", "美金");
    Currency matches = Currency.builder().code(inputValues.getCode()).currencyName("台幣").build();
    Currency expected = Currency.builder().code(inputValues.getCode()).currencyName("美金").build();

    Mockito.doReturn(Optional.ofNullable(matches)).when(currencyRepository)
        .findById(eq(inputValues.getCode()));

    Mockito.doReturn(expected).when(currencyRepository).updateCurrency(eq(matches));

    // Ack
    OutputValues outputValues = updateCurrencyUseCase.execute(inputValues);

    // Assert
    Mockito.verify(currencyRepository).findById(anyString());
    Mockito.verify(currencyRepository).updateCurrency(any());
    Assertions.assertEquals(expected, outputValues.getCurrency());
  }

  @Test
  @DisplayName("測試修改幣別但找不到資料")
  void testUpdateCurrencyButDataNotFound() {

    // Arrange
    UpdateCurrencyUseCase.InputValues inputValues = new InputValues("GBP", "英鎊");
    
    Mockito.doReturn(Optional.empty())
        .when(currencyRepository).findById(eq(inputValues.getCode()));

    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class,
        () -> updateCurrencyUseCase.execute(inputValues));
    Assertions.assertEquals("W002", useCaseException.getCode());
    Mockito.verify(currencyRepository).findById(anyString());
  }
  
  @Test
  @DisplayName("測試修改幣別但輸入參數有空值")
  void testUpdateCurrencyButInputValuesHasBlank() {
    
    // Arrange
    UpdateCurrencyUseCase.InputValues inputValues = new InputValues("USD", "");
    
    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class,
        () -> updateCurrencyUseCase.execute(inputValues));
    
    Assertions.assertEquals("E001", useCaseException.getCode());
  }
}
