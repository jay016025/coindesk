package idv.jay.coindesk.core.usecase;

import static org.mockito.ArgumentMatchers.anyString;

import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.DeleteCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("測試刪除幣別使用案例")
public class DeleteCurrencyUseCaseTest {
  
  @Mock
  CurrencyRepository currencyRepository;
  
  @InjectMocks
  DeleteCurrencyUseCase deleteCurrencyUseCase;
  
  @Test
  @DisplayName("測試刪除幣別")
  void testDeleteCurrency() {
    
    // Arrange
    DeleteCurrencyUseCase.InputValues inputValues = new InputValues("EUR");
    
    // Ack
    deleteCurrencyUseCase.execute(inputValues);
    
    // Assert
    Mockito.verify(currencyRepository).deleteById(anyString());
  }
  
  @Test
  @DisplayName("測試刪除幣別但輸入參數有空值")
  void testDeleteCurrencyButInputValuesHasBlank() {
    
    // Arrange
    DeleteCurrencyUseCase.InputValues inputValues = new InputValues("");
    
    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class,
        () -> deleteCurrencyUseCase.execute(inputValues));
    Assertions.assertEquals("E001", useCaseException.getCode());
  }
}
