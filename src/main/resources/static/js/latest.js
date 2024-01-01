let active = "General"
let news_container = document.getElementById("news");
let news_buttons = document.querySelectorAll(".news_button");
let url = "/news"
const refreshTimeout = 25000;

function getNews(){
    data = {
        type: active
    }
    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        let news = "";
        data.forEach(element => {
            news = news.concat(`<div class="news_container mb-3 ${element.sentiment.toLowerCase()}">
            <div class="img_container">
                <img src="${element.img == '' ? 'https://digitalfinger.id/wp-content/uploads/2019/12/no-image-available-icon-6.png': element.img}" alt="">
            </div>
            <div class="details_container">
                <h4>${element.title}</h4>
                <p>${element.summary}</p>
                <p>Sentiment: <span>${element.sentiment}</span></p>
                <a href="${element.url}">Full Article</a>
            </div>
        </div>`);
        });
        
        news_container.innerHTML = news;
    })
}

function periodicRefresh(){
    getNews();
    setTimeout(periodicRefresh, refreshTimeout)
}

document.addEventListener("DOMContentLoaded", function() {
    setTimeout(periodicRefresh, refreshTimeout);
});

news_buttons.forEach(button => {
    button.addEventListener("click", (e) => {
        active = e.target.innerText;
        getNews();
    });
});
