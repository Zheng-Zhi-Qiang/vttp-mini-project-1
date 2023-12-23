let watchlist = document.getElementById("watchlist");
let button = document.getElementById("watchlist_change")
let ticker = document.getElementById("ticker").innerText;


function changeWatchlist(e) {
    let changeRequested = e.target.innerText;
    let url = "";
    if (changeRequested.includes("Add")){
        url = "/watchlist/add"
    }
    else {
        url = "/watchlist/remove"
    }
    
    let data = {
        ticker: ticker
    }

    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "text/html"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        let listString = data.watchlist.replace("[", "").replace("]", "");
        if (listString == ""){
            watchlist.innerHTML = "";
        }
        else {
            let listArray = listString.split(",");
            let respList = "";
            listArray.forEach(element => 
                respList = respList.concat(`<li class="nav-item ms-1 mb-0"><a href="/stock/${element}" class="nav-link active">${element}</a></li>`)
            );
            watchlist.innerHTML = respList;
        }

        if (changeRequested.includes("Add")){
            button.innerText = "Remove from Watchlist"
        }
        else {
            button.innerText = "Add to Watchlist"
        }
    })
}

button.addEventListener("click", changeWatchlist);