package visa.vttpminiproject1;

public class Utils {
    public static final String BEAN_REDIS = "redisdb";
    public static final String QUERY_RESOURCE = "https://www.alphavantage.co/query";
    public static final String FUNCTION_NEWS = "NEWS_SENTIMENT";
    public static final String FUNCTION_EARNINGS = "EARNINGS";
    
    // Stock news constants
    public static final String ATTR_FEED = "feed";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_URL = "url";
    public static final String ATTR_SUMMARY = "summary";
    public static final String ATTR_SENTIMENT = "overall_sentiment_label";

    //Portfolio constants
    public static final String ATTR_TICKER = "ticker";
    public static final String ATTR_QUANTITYPURCHASED = "quantityPurchased";
    public static final String ATTR_COSTBASIS = "costBasis";
}
