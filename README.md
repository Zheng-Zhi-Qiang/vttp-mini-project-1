# StockSentry
StockSentry is a web application that aims to provide users with the latest financial market, economy, and stock news to enable users to make quick decisions.
The application is built using Springboot(Java), styled with CSS and vanilla Javascript (AJAX/jquery). The database used to power the application is Redis.
The application consumes an external API. You will need an API key from https://www.alphavantage.co/documentation/ in order to run the application. There is a free version of the key, but it is limited to 25 requests per day. You will need the premium version of the key since the application refreshes frequently to provide the latest news.
To use the application, you can create a docker image using the Dockerfile provided and deploy the image. The Dockerfile will have all the environment variables that needs to be set for the application to work.
