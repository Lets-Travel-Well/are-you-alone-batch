package com.rualone.app.main.dao;

import com.rualone.app.main.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoRepository extends JpaRepository<Sido, Integer> {
    Sido findBySidoCode(int sidoCode);
}
