package idv.jay.coindesk.core.usecase;

import idv.jay.coindesk.core.entity.ErrorCode;
import idv.jay.coindesk.core.exception.UseCaseException;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway.BPIData;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.InputValues;
import idv.jay.coindesk.core.usecase.GetBPICurrentPriceUseCase.OutputValues;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class GetBPICurrentPriceUseCase extends UseCase<InputValues, OutputValues>{

  public static final String TYPE_BPI_DATA = "BPI_DATA";
  public static final String TYPE_TRANSFER_BPI_DATA = "TRANSFER_BPI_DATA";
  private final GetBPIDataGateway getBPIDataAPI;

  @Override
  public OutputValues execute(InputValues input) {
    validatedInputValues(input);
    BPIData bpiData;
    if(TYPE_BPI_DATA.equals(input.getType())) {
      bpiData = getBPIDataAPI.getBPIDate();
    } else {
      bpiData = getBPIDataAPI.transferData();
    }
    
    return new OutputValues(bpiData);
  }

  @Override
  protected void validatedInputValues(InputValues input) throws UseCaseException {
    final String type = input.getType();
    if(!type.equals(TYPE_BPI_DATA) && !type.equals(TYPE_TRANSFER_BPI_DATA))
      throw new UseCaseException(ErrorCode.INPUT_ARGUMENT_ERROR);
  }
  
  @Value
  @RequiredArgsConstructor
  public static class InputValues implements UseCase.InputValues{
    private final String type;
  }
  
  @Value
  @RequiredArgsConstructor
  public static class OutputValues implements UseCase.OutputValues {
     private final BPIData bpiData;
  }
}
