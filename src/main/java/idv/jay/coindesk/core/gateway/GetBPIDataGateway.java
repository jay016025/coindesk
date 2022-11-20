package idv.jay.coindesk.core.gateway;


import idv.jay.coindesk.core.entity.Currency;
import idv.jay.coindesk.core.gateway.GetBPIDataGateway.BPIData;
import java.util.List;
import java.util.Optional;

public interface GetBPIDataGateway<T extends BPIData> {

  BPIData getBPIDate();

  BPIData transferData();

  interface BPIData {}
}
