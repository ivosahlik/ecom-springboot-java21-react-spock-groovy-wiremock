package cz.ivosahlik.ecommerce.repositories;

import cz.ivosahlik.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
