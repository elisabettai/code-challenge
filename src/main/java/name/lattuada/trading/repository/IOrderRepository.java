package name.lattuada.trading.repository;

import name.lattuada.trading.model.EOrderType;
import name.lattuada.trading.model.entities.OrderEntity;
import name.lattuada.trading.model.entities.SecurityEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IOrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findBySecurityIdAndTypeAndFulfilled(SecurityEntity securityId, EOrderType type, Boolean fulfilled);

}
