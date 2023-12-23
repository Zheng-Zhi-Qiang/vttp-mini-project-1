package visa.vttpminiproject1.models;

import static visa.vttpminiproject1.Utils.*;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class StockData {
    private String ticker;
    private String name;
    private String description;
    private String country;
    private String sector;
    private String industry;
    private Long marketCap;
    private Long ebitda;
    private Double pe;
    private Double peg;
    private Double bookValue;
    private Double dividendYield;
    private Double eps;
    private Double profitMargin;
    private Double operatingMargin;
    private Double qtrEarningsGrowthYOY;
    private Double qtrRevenueGrowthYOY;
    private Double forwardpe;
    private Double priceToSales;
    private Double priceToBook;
    private Double beta;
    private Long sharesOutstanding;
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public String getIndustry() {
        return industry;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public Long getMarketCap() {
        return marketCap;
    }
    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }
    public Long getEbitda() {
        return ebitda;
    }
    public void setEbitda(Long ebitda) {
        this.ebitda = ebitda;
    }
    public Double getPe() {
        return pe;
    }
    public void setPe(Double pe) {
        this.pe = pe;
    }
    public Double getPeg() {
        return peg;
    }
    public void setPeg(Double peg) {
        this.peg = peg;
    }
    public Double getBookValue() {
        return bookValue;
    }
    public void setBookValue(Double bookValue) {
        this.bookValue = bookValue;
    }
    public Double getDividendYield() {
        return dividendYield;
    }
    public void setDividendYield(Double dividendYield) {
        this.dividendYield = dividendYield;
    }
    public Double getEps() {
        return eps;
    }
    public void setEps(Double eps) {
        this.eps = eps;
    }
    public Double getProfitMargin() {
        return profitMargin;
    }
    public void setProfitMargin(Double profitMargin) {
        this.profitMargin = profitMargin;
    }
    public Double getOperatingMargin() {
        return operatingMargin;
    }
    public void setOperatingMargin(Double operatingMargin) {
        this.operatingMargin = operatingMargin;
    }
    public Double getQtrEarningsGrowthYOY() {
        return qtrEarningsGrowthYOY;
    }
    public void setQtrEarningsGrowthYOY(Double qtrEarningsGrowthYOY) {
        this.qtrEarningsGrowthYOY = qtrEarningsGrowthYOY;
    }
    public Double getQtrRevenueGrowthYOY() {
        return qtrRevenueGrowthYOY;
    }
    public void setQtrRevenueGrowthYOY(Double qtrRevenueGrowthYOY) {
        this.qtrRevenueGrowthYOY = qtrRevenueGrowthYOY;
    }
    public Double getForwardpe() {
        return forwardpe;
    }
    public void setForwardpe(Double forwardpe) {
        this.forwardpe = forwardpe;
    }
    public Double getPriceToSales() {
        return priceToSales;
    }
    public void setPriceToSales(Double priceToSales) {
        this.priceToSales = priceToSales;
    }
    public Double getPriceToBook() {
        return priceToBook;
    }
    public void setPriceToBook(Double priceToBook) {
        this.priceToBook = priceToBook;
    }
    public Double getBeta() {
        return beta;
    }
    public void setBeta(Double beta) {
        this.beta = beta;
    }
    public Long getSharesOutstanding() {
        return sharesOutstanding;
    }
    public void setSharesOutstanding(Long sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public static StockData toStockData(String data){
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject obj = reader.readObject();
        return toStockData(obj);
    }
    
    public static StockData toStockData(JsonObject data){
        StockData stockData = new StockData();
        stockData.setTicker(data.getString(DATA_SYMBOL));
        stockData.setName(data.getString(DATA_NAME));
        stockData.setDescription(data.getString(DATA_DESCRIPTION));
        stockData.setCountry(data.getString(DATA_COUNTRY));
        stockData.setSector(data.getString(DATA_SECTOR));
        stockData.setIndustry(data.getString(DATA_INDUSTRY));
        stockData.setMarketCap(Long.parseLong(data.getString(DATA_MARKETCAP)));
        stockData.setEbitda(Long.parseLong(data.getString(DATA_EBITDA)));
        stockData.setPe(Double.parseDouble(data.getString(DATA_PE)));
        stockData.setPeg(Double.parseDouble(data.getString(DATA_PEG)));
        stockData.setBookValue(Double.parseDouble(data.getString(DATA_BV)));
        stockData.setDividendYield(Double.parseDouble(data.getString(DATA_DIVYIELD)));
        stockData.setEps(Double.parseDouble(data.getString(DATA_EPS)));
        stockData.setProfitMargin(Double.parseDouble(data.getString(DATA_PROFITMARGIN)));
        stockData.setOperatingMargin(Double.parseDouble(data.getString(DATA_OPERATINGMARGIN)));
        stockData.setQtrEarningsGrowthYOY(Double.parseDouble(data.getString(DATA_QTREARNINGS_YOY)));
        stockData.setQtrRevenueGrowthYOY(Double.parseDouble(data.getString(DATA_QTRREVENUE_YOY)));
        stockData.setForwardpe(Double.parseDouble(data.getString(DATA_FORWARDPE)));
        stockData.setPriceToSales(Double.parseDouble(data.getString(DATA_PRICETOSALES)));
        stockData.setPriceToBook(Double.parseDouble(data.getString(DATA_PRICETOBOOK)));
        stockData.setBeta(Double.parseDouble(data.getString(DATA_BETA)));
        stockData.setSharesOutstanding(Long.parseLong(data.getString(DATA_OUTSTANDINGSHARES)));
        return stockData;
    }
}
