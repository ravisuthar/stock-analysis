package org.example.controller;

import org.example.model.Stock52WeekHigh;
import org.example.model.StockType;
import org.example.repository.StockRepository;
import org.example.repository.StockTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock")
public class StockController {


    private final StockRepository stockRepository;
    private final StockTypeRepository stockTypeRepository;

    @Autowired
    public StockController(StockRepository stockRepository, StockTypeRepository stockTypeRepository) {
        this.stockRepository = stockRepository;
        this.stockTypeRepository = stockTypeRepository;
    }

    @GetMapping("/stocks")
    public ResponseEntity<List<Stock52WeekHigh>> getAllStocks() {
        try {
            return new ResponseEntity<>(stockRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/newAddedToday")
    public ResponseEntity<List<Stock52WeekHigh>> getNewlyAddedToday() {
        try {
            Set<LocalDate> distinctTradeDates = stockRepository.findAll().stream().map(Stock52WeekHigh::getTradeDate).collect(Collectors.toSet());
            LocalDate lastDate = distinctTradeDates.stream().sorted(Comparator.reverseOrder()).skip(1).findFirst().get();
            LocalDate today = distinctTradeDates.stream().sorted(Comparator.reverseOrder()).findFirst().get();


            Map<String, String> stockType = stockTypeRepository.findAll().stream().collect(Collectors.toMap(StockType::getCompanyName, StockType::getIndex));


            // Step 1: Retrieve records added today
            List<Stock52WeekHigh> recordsAddedToday = stockRepository.findByTradeDate(today);

            // Step 2: Retrieve records from previous trade dates
            List<Stock52WeekHigh> recordsBeforeToday = stockRepository.findByTradeDate(lastDate);

            // Step 3: Identify new records added today and not present in previous trade dates
            List<Stock52WeekHigh> newRecords = recordsAddedToday.stream()
                    .filter(record -> recordsBeforeToday.stream().noneMatch(yesterdayRecord -> yesterdayRecord.getCompanyName().trim().equals(record.getCompanyName().trim())))
                    .map(s -> {
                        String index = stockType.get(s.getCompanyName());
                        s.setIndex(index);
                        return s;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(newRecords, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stockCount")
    public ResponseEntity<Map<String, Long>> stockCount() {
        try {
            List<Stock52WeekHigh> all = stockRepository.findAll();
            Map<String, Long> countMap = all.stream()
                    .collect(Collectors.groupingBy(Stock52WeekHigh::getCompanyName, Collectors.counting()));

            // Sort by trade date counts in descending order and get top 10
            Map<String, Long> top10Companies = countMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(50)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new ResponseEntity<>(top10Companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/freshAddedToday")
    public ResponseEntity<Set<Stock52WeekHigh>> freshAddedToday() {
        try {
            List<Stock52WeekHigh> all = stockRepository.findAll();
            Map<String, Long> countMap = all.stream()
                    .collect(Collectors.groupingBy(Stock52WeekHigh::getCompanyName, Collectors.counting()));

            Set<LocalDate> distinctTradeDates = stockRepository.findAll().stream().map(Stock52WeekHigh::getTradeDate).collect(Collectors.toSet());
            LocalDate today = distinctTradeDates.stream().sorted(Comparator.reverseOrder()).findFirst().get();

            List<Stock52WeekHigh> byTradeDate = stockRepository.findByTradeDate(today);

            Set<Stock52WeekHigh> collect = byTradeDate.stream()
                    .filter(trade -> countMap.containsKey(trade.getCompanyName()) && countMap.get(trade.getCompanyName()) == 1)
                    .sorted(Comparator.comparing(Stock52WeekHigh::getCompanyName))
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(collect, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/last2days")
    public ResponseEntity<Map<String, List<LocalDate>>> lastTwoDays() {
        try {

            Map<String, List<LocalDate>> all = stockRepository.findAll().stream().collect(Collectors.groupingBy(Stock52WeekHigh::getCompanyName, Collectors.mapping(Stock52WeekHigh::getTradeDate, Collectors.toList())));

            Set<LocalDate> distinctTradeDates = stockRepository.findAll().stream().map(Stock52WeekHigh::getTradeDate).collect(Collectors.toSet());
            List<LocalDate> twoDays = distinctTradeDates.stream().sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());

            Map<String, List<LocalDate>> result = all.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(twoDays.get(0)) &&  entry.getValue().contains(twoDays.get(1)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/last3days")
    public ResponseEntity<Map<String, List<LocalDate>>> lastThreeDays() {
        try {

            Map<String, List<LocalDate>> all = stockRepository.findAll().stream().collect(Collectors.groupingBy(Stock52WeekHigh::getCompanyName, Collectors.mapping(Stock52WeekHigh::getTradeDate, Collectors.toList())));

            Set<LocalDate> distinctTradeDates = stockRepository.findAll().stream().map(Stock52WeekHigh::getTradeDate).collect(Collectors.toSet());
            Set<LocalDate> threeDays = distinctTradeDates.stream().sorted(Comparator.reverseOrder()).limit(3).collect(Collectors.toSet());

            Map<String, List<LocalDate>> result = all.entrySet().stream().filter(entry -> threeDays.containsAll(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/last5days")
    public ResponseEntity<Map<String, List<LocalDate>>> lastFiveDays() {
        try {

            Map<String, List<LocalDate>> all = stockRepository.findAll().stream().collect(Collectors.groupingBy(Stock52WeekHigh::getCompanyName, Collectors.mapping(Stock52WeekHigh::getTradeDate, Collectors.toList())));

            Set<LocalDate> distinctTradeDates = stockRepository.findAll().stream().map(Stock52WeekHigh::getTradeDate).collect(Collectors.toSet());
            Set<LocalDate> fiveDays = distinctTradeDates.stream().sorted(Comparator.reverseOrder()).limit(5).collect(Collectors.toSet());

            Map<String, List<LocalDate>> result = all.entrySet().stream().filter(entry -> fiveDays.containsAll(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
