/**
 * 
 */
package app.ewarehouse.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.dto.DepositorResponse;
import app.ewarehouse.dto.DepositoryDetailsDto;
import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.Depositor;
import app.ewarehouse.entity.Status;

/**
 * Priyanka Singh
 */
public interface DepositorService {

	public BuyerDepositorAndWareHouseOperator save(String data);

	public Object takeAction(String buyer);

	public DepositorResponse getById(String id);

	public List<Depositor> getAll();

	public String deleteById(String id);

	public Page<BuyerDepositorResponse> getFilteredBuyers(Date fromDate, Date toDate, Status status, Pageable pageable);

	public Page<DepositorResponse> getFilteredDepositors(Date fromDate, Date toDate, Status status, Pageable pageable);

	public DepositoryDetailsDto getUserDataByEmailIdByDepositor(String email);

	public Page<BuyerDepositorAndWareHouseOperator> findByIntCreatedBy(Integer id, Pageable pageable);

	public BuyerDepositorAndWareHouseOperator getDepositorById(String id);

	public DepositoryDetailsDto getWareHouseOperatorDataByUserId(String vchWareHouseId);

	public Integer getDepositorCount(Integer createdBy);

	public Page<BuyerDepositorAndWareHouseOperator> getAllWareHouseList(Pageable pageable);

	public Page<BuyerDepositorAndWareHouseOperator> getAllWareHouseListById(String vchWareHouseId, Pageable pageable);



}
