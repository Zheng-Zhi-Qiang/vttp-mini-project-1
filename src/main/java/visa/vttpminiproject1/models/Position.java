package visa.vttpminiproject1.models;

import static visa.vttpminiproject1.Utils.*;

import java.io.StringReader;
import java.text.DecimalFormat;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Position {
    private static final DecimalFormat decimalFormat = new DecimalFormat("$#,##0.00;$(#,##0.00)");

    @NotBlank(message = "This field is mandatory!")
    private String ticker;

    @Min(value = 1, message = "Minimum number of shares purchased must be 1!")
    @NotNull(message = "This field is mandatory!")
    private Integer quantityPurchased;

    @NotBlank(message = "This field is mandatory!")
    @Pattern(regexp = "^\\d*(\\.\\d{0,2})?$", message = "Please enter a valid number with maximum 2 decimal places!")
    private String costBasis;

    private String companyName;
    private Double lastTradedPrice;
    private Double marketValue;
    private String unrealisedProfit;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(Integer quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public String getCostBasis() {
        return costBasis;
    }

    public void setCostBasis(String costBasis) {
        this.costBasis = costBasis;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(Double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public String getUnrealisedProfit() {
        return unrealisedProfit;
    }

    public void setUnrealisedProfit(String unrealisedProfit) {
        this.unrealisedProfit = unrealisedProfit;
    }

    public static Position toPosition(String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();

        return toPosition(data);
    }

    public static Position toPosition(JsonObject data) {
        Position position = new Position();
        position.setTicker(data.getString(ATTR_TICKER));
        position.setQuantityPurchased(Integer.parseInt(data.getString(ATTR_QUANTITYPURCHASED)));
        position.setCompanyName(data.getString(ATTR_COYNAME));
        position.setCostBasis(data.getString(ATTR_COSTBASIS));
        return position;
    }

    public static String toJsonString(Position position) {
        String jsonString = Json.createObjectBuilder()
                .add(ATTR_TICKER, position.getTicker())
                .add(ATTR_COYNAME, position.getCompanyName())
                .add(ATTR_QUANTITYPURCHASED, position.getQuantityPurchased().toString())
                .add(ATTR_COSTBASIS, position.getCostBasis().toString())
                .build().toString();
        return jsonString;
    }

    public void calculateData() {
        marketValue = lastTradedPrice * quantityPurchased;
        Double profit = marketValue - (Double.parseDouble(costBasis) * quantityPurchased);
        unrealisedProfit = decimalFormat.format(profit);
    }
}