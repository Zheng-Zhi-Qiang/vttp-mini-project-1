let news_button = document.getElementById("news");
let financials_button = document.getElementById("financials");
let tab_content = document.getElementById("tab-content");
let news_url = "/data/news"
let financials_url = "/data/financials";
let data = {ticker:ticker};
let active = "News";
let refreshTimeout = 15000;

function toPercent(decimal){
    return ((decimal) * 100).toFixed(2) + '%';
}

function formatNumber(number){
    return Intl.NumberFormat("en-GB", {notation: "compact", compactDisplay: "short"}).format(number);
}

function formatData(data){
    data["Market Capitalization"] = formatNumber(data["Market Capitalization"]);
    data["EBITDA"] = formatNumber(data["EBITDA"]);
    data["Dividend Yield"] = toPercent(data["Dividend Yield"]);
    data["Profit Margin"] = toPercent(data["Profit Margin"]);
    data["Operating Margin"] = toPercent(data["Operating Margin"]);
    data["Quarterly Earnings Growth (YOY)"] = toPercent(data["Quarterly Earnings Growth (YOY)"]);
    data["Quarterly Revenue Growth (YOY)"] = toPercent(data["Quarterly Revenue Growth (YOY)"]);
    data["Outstanding Shares"] = formatNumber(data["Outstanding Shares"]);
    data["EPS"] = "$" + data["EPS"];
}

function getNews(){
    active = "News";
    fetch(news_url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        let news = "";
        data.forEach(element => {
            news = news.concat(`<div class="news mb-3 p-2 ${element.sentiment.toLowerCase()}">
            <div class="brief">
                <h6 class="brief-title">${element.title}</h6>
                <a href="${element.url}">Full Article</a>
            </div>
            <div class="ms-auto">
                <p class="m-0">Sentiment: <span>${element.sentiment}</span></p>
            </div>
        </div>`);
        });
        
        tab_content.innerHTML = news;
    })
}

function getFinancials(){
    active = "Financials";
    fetch(financials_url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        let financials = "";
        let count = 0;
        console.log(data);
        formatData(data);
        console.log(data);
        for (var key in data){
            count++;
            if (count % 2 == 0 && count != 0){
                financials = financials.concat(`<td class="text-start"><p class="m-2"><b>${key}:</b> ${data[key]}</p></td></tr>`);
            }
            else {
                if (count == data.length){
                    financials = financials.concat(`<tr><td class="text-start"><p class="m-2"><b>${key}:</b> ${data[key]}</p></td><td></td></tr>`);
                }
                else {
                    financials = financials.concat(`<tr><td class="text-start"><p class="m-2"><b>${key}:</b> ${data[key]}</p></td>`);
                }
            }
        };
        tab_content.innerHTML = `<table>${financials}</table>`;
    })
}

function periodicRefresh(){
    if (active == "News"){
        getNews();
    }
    setTimeout(periodicRefresh, refreshTimeout)
}

news_button.addEventListener("click", getNews);
financials_button.addEventListener("click", getFinancials)


document.addEventListener("DOMContentLoaded", function() {
    setTimeout(periodicRefresh, refreshTimeout);
});