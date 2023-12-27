package visa.vttpminiproject1;

import java.util.Optional;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

public class Utils {
    public static final String BEAN_NEWSREDIS = "newsdb";
    public static final String BEAN_USERREDIS = "userdb";
    public static final String BEAN_TICKERREDIS = "stockdb";
    public static final String QUERY_RESOURCE = "https://www.alphavantage.co/query";
    public static final String FUNCTION_NEWS = "NEWS_SENTIMENT";
    public static final String FUNCTION_EARNINGS = "EARNINGS";
    public static final String FUNCTION_OVERVIEW = "OVERVIEW";
    public static final String FUNCTION_QUOTE = "GLOBAL_QUOTE";
    public static final String API_FUNCTION = "function";
    public static final String API_KEY = "apikey";

    // Stock news constants
    public static final String ATTR_FEED = "feed";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_NEWSIMG = "banner_image";
    public static final String ATTR_URL = "url";
    public static final String ATTR_SUMMARY = "summary";
    public static final String ATTR_SENTIMENT = "overall_sentiment_label";
    public static final String ATTR_DATETIME = "time_published";
    public static final String ATTR_MARKETNEWS = "financial_markets";
    public static final String ATTR_ECONOMY = "economy_macro";

    // Portfolio constants
    public static final String ATTR_TICKER = "ticker";
    public static final String ATTR_SYMBOL = "symbol";
    public static final String ATTR_COYNAME = "Name";
    public static final String ATTR_QUANTITYPURCHASED = "quantityPurchased";
    public static final String ATTR_COSTBASIS = "costBasis";
    public static final String ATTR_GLOBALQUOTE = "Global Quote";
    public static final String ATTR_LASTTRADED = "05. price";

    // User constants
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_USERAPIKEY = "apiKey";
    public static final String ATTR_PASSWORD = "password";

    // Stock data constants
    public static final String DATA_SYMBOL = "Symbol";
    public static final String DATA_NAME = "Name";
    public static final String DATA_DESCRIPTION = "Description";
    public static final String DATA_COUNTRY = "Country";
    public static final String DATA_SECTOR = "Sector";
    public static final String DATA_INDUSTRY = "Industry";
    public static final String DATA_MARKETCAP = "MarketCapitalization";
    public static final String DATA_EBITDA = "EBITDA";
    public static final String DATA_PE = "PERatio";
    public static final String DATA_PEG = "PEGRatio";
    public static final String DATA_BV = "BookValue";
    public static final String DATA_DIVYIELD = "DividendYield";
    public static final String DATA_EPS = "EPS";
    public static final String DATA_PROFITMARGIN = "ProfitMargin";
    public static final String DATA_OPERATINGMARGIN = "OperatingMarginTTM";
    public static final String DATA_QTREARNINGS_YOY = "QuarterlyEarningsGrowthYOY";
    public static final String DATA_QTRREVENUE_YOY = "QuarterlyRevenueGrowthYOY";
    public static final String DATA_FORWARDPE = "ForwardPE";
    public static final String DATA_PRICETOSALES = "PriceToSalesRatioTTM";
    public static final String DATA_PRICETOBOOK = "PriceToBookRatio";
    public static final String DATA_BETA = "Beta";
    public static final String DATA_OUTSTANDINGSHARES = "SharesOutstanding";

    public static Optional<ModelAndView> authenticated(HttpSession session) {
        String authenticated = (String) session.getAttribute("authenticated");
        if (authenticated == null || authenticated.equals("false")) {
            ModelAndView mav = new ModelAndView("redirect:/user/login");
            return Optional.of(mav);
        }
        return Optional.empty();
    }
}
