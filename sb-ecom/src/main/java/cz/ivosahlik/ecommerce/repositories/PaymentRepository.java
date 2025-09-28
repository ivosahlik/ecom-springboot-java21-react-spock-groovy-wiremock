package cz.ivosahlik.ecommerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.ivosahlik.ecommerce.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
