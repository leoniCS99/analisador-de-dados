package br.com.agi.service;

import br.com.agi.model.Customer;
import br.com.agi.model.Report;
import br.com.agi.model.Sale;
import br.com.agi.model.Salesman;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    public Report generate(
            List<Customer> customers,
            List<Salesman> salesmen,
            List<Sale> sales) {

        String mostExpensiveSaleId = sales.stream()
                .max(Comparator.comparing(Sale::getTotal))
                .map(Sale::getId)
                .orElse("N/A");

        Map<String, BigDecimal> salesBySalesman = new HashMap<>();

        for (Sale sale : sales) {

            salesBySalesman.merge(
                    sale.getSalesmanName(),
                    sale.getTotal(),
                    BigDecimal::add
            );

        }

        String worstSalesman = salesBySalesman.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        return new Report(
                customers.size(),
                salesmen.size(),
                mostExpensiveSaleId,
                worstSalesman
        );

    }

}
