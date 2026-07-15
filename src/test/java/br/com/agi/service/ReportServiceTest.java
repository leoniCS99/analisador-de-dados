package br.com.agi.service;

import br.com.agi.model.Customer;
import br.com.agi.model.Report;
import br.com.agi.model.Sale;
import br.com.agi.model.SaleItem;
import br.com.agi.model.Salesman;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportServiceTest {

    private final ReportService reportService = new ReportService();

    @Test
    @DisplayName("Should generate report correctly")
    void t1() {

        List<Customer> customers = List.of(
                new Customer("1", "Jose da Silva", "Rural"),
                new Customer("2", "Eduardo Pereira", "Rural")
        );

        List<Salesman> salesmen = List.of(
                new Salesman("123", "Pedro", new BigDecimal("50000")),
                new Salesman("456", "Paulo", new BigDecimal("40000.99"))
        );

        Sale sale1 = new Sale(
                "10",
                List.of(
                        new SaleItem(1, 10, new BigDecimal("100")),
                        new SaleItem(2, 30, new BigDecimal("2.50")),
                        new SaleItem(3, 40, new BigDecimal("3.10"))
                ),
                "Pedro"
        );

        Sale sale2 = new Sale(
                "08",
                List.of(
                        new SaleItem(1, 34, new BigDecimal("10")),
                        new SaleItem(2, 33, new BigDecimal("1.50")),
                        new SaleItem(3, 40, new BigDecimal("0.10"))
                ),
                "Paulo"
        );

        Report report = reportService.generate(
                customers,
                salesmen,
                List.of(sale1, sale2)
        );

        assertEquals(2, report.getCustomerCount());
        assertEquals(2, report.getSalesmanCount());
        assertEquals("10", report.getMostExpensiveSaleId());
        assertEquals("Paulo", report.getWorstSalesman());

    }

    @Test
    @DisplayName("Should return N/A when there are no sales")
    void t2() {

        Report report = reportService.generate(
                List.of(
                        new Customer("1", "Jose", "Rural")
                ),
                List.of(
                        new Salesman("123", "Pedro", new BigDecimal("50000"))
                ),
                List.of()
        );

        assertEquals(1, report.getCustomerCount());
        assertEquals(1, report.getSalesmanCount());
        assertEquals("N/A", report.getMostExpensiveSaleId());
        assertEquals("N/A", report.getWorstSalesman());

    }

    @Test
    @DisplayName("Should identify the worst salesman using total sales")
    void t3() {

        Sale sale1 = new Sale(
                "01",
                List.of(
                        new SaleItem(1, 1, new BigDecimal("100"))
                ),
                "Pedro"
        );

        Sale sale2 = new Sale(
                "02",
                List.of(
                        new SaleItem(1, 1, new BigDecimal("300"))
                ),
                "Pedro"
        );

        Sale sale3 = new Sale(
                "03",
                List.of(
                        new SaleItem(1, 1, new BigDecimal("200"))
                ),
                "Paulo"
        );

        Report report = reportService.generate(
                List.of(),
                List.of(),
                List.of(sale1, sale2, sale3)
        );

        assertEquals("Paulo", report.getWorstSalesman());

    }

    @Test
    @DisplayName("Should identify the most expensive sale")
    void t4() {

        Sale sale1 = new Sale(
                "10",
                List.of(
                        new SaleItem(1, 1, new BigDecimal("100"))
                ),
                "Pedro"
        );

        Sale sale2 = new Sale(
                "20",
                List.of(
                        new SaleItem(1, 1, new BigDecimal("500"))
                ),
                "Paulo"
        );

        Report report = reportService.generate(
                List.of(),
                List.of(),
                List.of(sale1, sale2)
        );

        assertEquals("20", report.getMostExpensiveSaleId());

    }

}