package idv.jay.coindesk.controller;

import idv.jay.coindesk.controller.request.CreateCurrencyRequest;
import idv.jay.coindesk.controller.request.UpdateCurrencyRequest;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase;
import idv.jay.coindesk.core.usecase.DeleteCurrencyUseCase;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.InputValues;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.OutputValues;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase;
import idv.jay.coindesk.share.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coinDesk")
@RequiredArgsConstructor
public class CoinDeskController {

  private final GetBPICurrentPriceUseCase getBPICurrentPriceUseCase;

  private final CreateCurrencyUseCase createCurrencyUseCase;
  
  private final QueryCurrencyUseCase queryCurrencyUseCase;
  
  private final UpdateCurrencyUseCase updateCurrencyUseCase;
  
  private final DeleteCurrencyUseCase deleteCurrencyUseCase;

  @GetMapping("getAPIData/{type}")
  public OutputValues getAPIData(@PathVariable String type) {
    return getBPICurrentPriceUseCase.execute(new InputValues(type));
  }

  @PostMapping("currency")
  public CreateCurrencyUseCase.OutputValues createCurrency(CreateCurrencyRequest request) {
    CreateCurrencyUseCase.InputValues inputValues = 
        CurrencyMapper.INSTANCE.mapToCreateCurrencyInputValues(request);
    return createCurrencyUseCase.execute(inputValues);
  }
  
  @GetMapping("currency/{code}")
  public QueryCurrencyUseCase.OutputValues queryCurrency(@PathVariable String code) {
    return queryCurrencyUseCase.execute(new QueryCurrencyUseCase.InputValues(code));
  }
  
  @PutMapping("currency")
  public UpdateCurrencyUseCase.OutputValues updateCurrency(UpdateCurrencyRequest request) {
    UpdateCurrencyUseCase.InputValues inputValues = CurrencyMapper.INSTANCE.mapToUpdateCurrencyInputValues(
        request);
    return updateCurrencyUseCase.execute(inputValues);
  }
  
  @DeleteMapping("currency/{code}")
  public ResponseEntity<Void> deleteCurrency(@PathVariable String code) {
    deleteCurrencyUseCase.execute(new DeleteCurrencyUseCase.InputValues(code));
    return ResponseEntity.ok().body(null);
  }
}
