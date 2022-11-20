package idv.jay.coindesk.controller;

import idv.jay.coindesk.controller.response.ApiResponse;
import idv.jay.coindesk.core.exception.ThirdPartyAPIException;
import idv.jay.coindesk.core.exception.UseCaseException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "idv.jay.coindesk.controller")
public class CoinDeskControllerAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof ApiResponse) {
      return body;
    }

    ApiResponse<Object> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("");
    apiResponse.setData(body);

    return apiResponse;
  }

  @ExceptionHandler(value = UseCaseException.class)
  public ResponseEntity<ApiResponse> handleException(UseCaseException useCaseException) {
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage(useCaseException.getMessage());
    apiResponse.setErrorCode(useCaseException.getCode());
    return ResponseEntity.ok().body(apiResponse);
  }
  
  @ExceptionHandler(value = ThirdPartyAPIException.class)
  public ResponseEntity<ApiResponse> handleException(ThirdPartyAPIException thirdPartyAPIException) {
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage(thirdPartyAPIException.getMessage());
    apiResponse.setErrorCode(thirdPartyAPIException.getCode());
    return ResponseEntity.ok().body(apiResponse);
  }
}
