package br.com.agi.model;
public class Report {

    private final int customerCount;
    private final int salesmanCount;
    private final String mostExpensiveSaleId;
    private final String worstSalesman;

    public Report(int customerCount,
                  int salesmanCount,
                  String mostExpensiveSaleId,
                  String worstSalesman) {

        this.customerCount = customerCount;
        this.salesmanCount = salesmanCount;
        this.mostExpensiveSaleId = mostExpensiveSaleId;
        this.worstSalesman = worstSalesman;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public int getSalesmanCount() {
        return salesmanCount;
    }

    public String getMostExpensiveSaleId() {
        return mostExpensiveSaleId;
    }

    public String getWorstSalesman() {
        return worstSalesman;
    }
}
