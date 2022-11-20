package idv.jay.coindesk.config;

import idv.jay.coindesk.controller.CoinDeskController;
import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway;
import idv.jay.coindesk.core.repository.CurrencyRepository;
import idv.jay.coindesk.core.usecase.CreateCurrencyUseCase;
import idv.jay.coindesk.core.usecase.DeleteCurrencyUseCase;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase;
import idv.jay.coindesk.core.usecase.QueryCurrencyUseCase;
import idv.jay.coindesk.core.usecase.UpdateCurrencyUseCase;
import idv.jay.coindesk.gateway.GetCoinDeskBPIDataGateway;
import idv.jay.coindesk.repository.JpaCurrencyRepository;
import idv.jay.coindesk.repository.impl.JpaCurrencyRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

  @Bean
  public CurrencyRepository currencyRepository() {
    return new JpaCurrencyRepositoryImpl();
  }
  
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  
  @Bean
  public GetBPIDataGateway getBPIDataGateway(RestTemplate restTemplate,
      CurrencyRepository currencyRepository) {
    return new GetCoinDeskBPIDataGateway(restTemplate, currencyRepository);
  }

  @Bean
  public GetBPICurrentPriceUseCase getBPICurrentPriceUseCase(GetBPIDataGateway getBPIDataGateway) {
    return new GetBPICurrentPriceUseCase(getBPIDataGateway);
  }

  @Bean
  public CreateCurrencyUseCase createCurrencyUseCase(CurrencyRepository currencyRepository) {
    return new CreateCurrencyUseCase(currencyRepository);
  }

  @Bean
  public QueryCurrencyUseCase queryCurrencyUseCase(CurrencyRepository currencyRepository) {
    return new QueryCurrencyUseCase(currencyRepository);
  }

  @Bean
  public UpdateCurrencyUseCase updateCurrencyUseCase(CurrencyRepository currencyRepository) {
    return new UpdateCurrencyUseCase(currencyRepository);
  }

  @Bean
  public DeleteCurrencyUseCase deleteCurrencyUseCase(CurrencyRepository currencyRepository) {
    return new DeleteCurrencyUseCase(currencyRepository);
  }
}
