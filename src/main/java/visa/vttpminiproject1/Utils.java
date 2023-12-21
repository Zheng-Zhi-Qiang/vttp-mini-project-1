package visa.vttpminiproject1;
import java.util.Optional;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

public class Utils {
    public static final String BEAN_NEWSREDIS = "newsdb";
    public static final String BEAN_USERSREDIS = "userdb";
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


    public static Optional<ModelAndView> authenticated(HttpSession session){
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || authenticated.booleanValue() == false){
            ModelAndView mav = new ModelAndView("redirect:/user/login");
            return Optional.of(mav);
        }
        return Optional.empty();
    }
}
