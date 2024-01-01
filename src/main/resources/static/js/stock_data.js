google.charts.load('current', {'packages':['corechart']});

let news_button = document.getElementById("news");
let financials_button = document.getElementById("financials");
let earnings_button = document.getElementById("earnings")
let tab_content = document.getElementById("tab-content");
let news_url = "/data/news"
let financials_url = "/data/financials";
let earnings_url = "/data/earnings";
let data = {ticker:ticker};
let active = "News";
let refreshTimeout = 25000;

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
    data["EPS"] = "$" + data["EPS"].toFixed(2);
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



function drawChart() {
    active = "Earnings";
    fetch(earnings_url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        if (!data.result.includes("success")){
            tab_content.innerText = data.result;
        }
        else {
            let dataArray = data.data;
            dataArray.forEach(array => {
                array[1] = parseFloat(array[1]);
                array[2] = parseFloat(array[2]);
            });
            finalArray = [['Fiscal Quarter Ending', 'Expected Earnings', 'Reported Earnings'], ...dataArray.reverse()];
            var data = google.visualization.arrayToDataTable(finalArray);
        
            var options = {
                hAxis: {title: 'Fiscal Quarter Ending'},
                animation: {startup: true, easing:'in'},
                legend: 'automatic'
            };
        
            var chart = new google.visualization.ScatterChart(tab_content);
        
            chart.draw(data, options);
        }
    })
}

news_button.addEventListener("click", getNews);
financials_button.addEventListener("click", getFinancials);
earnings_button.addEventListener("click", drawChart);


document.addEventListener("DOMContentLoaded", function() {
    setTimeout(periodicRefresh, refreshTimeout);
});

