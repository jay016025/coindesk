package idv.jay.coindesk.core.usecase;

import static org.mockito.ArgumentMatchers.eq;

import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase.InputValues;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase.OutputValues;
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
@DisplayName("測試建立幣別使用案例")
public class CreateCurrencyUseCaseTest {

  @Mock
  CurrencyRepository currencyRepository;

  @InjectMocks
  CreateCurrencyUseCase createCurrencyUseCase;

  @Test
  @DisplayName("測試建立幣別")
  void testCreateCurrency() {

    // Arrange
    CreateCurrencyUseCase.InputValues inputValues =
        new InputValues("USD", "美金");

    Currency expect = Currency.builder().code(inputValues.getCode())
        .currencyName(inputValues.getCurrencyName()).build();

    // Ack
    OutputValues outputValues = createCurrencyUseCase.execute(inputValues);

    // Assert
    Mockito.verify(currencyRepository).createCurrency(eq(expect));
    Assertions.assertEquals(expect, outputValues.getCurrency());
  }
  
  @Test
  @DisplayName("測試輸入參數有空值")
  void testInputValuesHasBlankValue() {
    
    // Arrange
    CreateCurrencyUseCase.InputValues inputValues = 
        new InputValues("", "英鎊");
    
    // Ack、Assert
    UseCaseException useCaseException = Assertions.assertThrows(UseCaseException.class, () ->
        createCurrencyUseCase.execute(inputValues));
    Assertions.assertEquals("E001", useCaseException.getCode());
  }
}
