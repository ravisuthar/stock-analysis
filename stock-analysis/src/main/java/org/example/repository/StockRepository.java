package org.example.repository;

import org.example.model.Stock52WeekHigh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface StockRepository extends JpaRepository<Stock52WeekHigh, Long> {
    List<Stock52WeekHigh> findAll();

    List<Stock52WeekHigh> findByTradeDate(LocalDate today);
}


