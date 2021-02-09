package com.smartcontact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcontact.model.MyOrder;

@Repository
public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {
	public MyOrder findByOrderId(String orderId);

}
