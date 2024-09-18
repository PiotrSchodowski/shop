package com.schodowski.shop.repository;

import com.schodowski.shop.repository.entity.CashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepo extends JpaRepository<CashEntity, Long>{

}
