package com.rualone.app.main.dao;

import com.rualone.app.main.entity.Gugun;
import com.rualone.app.main.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GugunRepository extends JpaRepository<Gugun, Integer> {
    List<Gugun> findAllBySido(Sido sido);
    Optional<Gugun> findByGugunCodeAndSido(int gugunCode, Sido sido);
}