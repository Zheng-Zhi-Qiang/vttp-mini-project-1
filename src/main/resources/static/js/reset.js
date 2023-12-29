let email_input = document.getElementById("email");
let content_body = document.getElementById("content");
let error_div = document.getElementById("error");
let button = document.getElementById("submit");
let url = "/user/reset"


function resetPassword(){
    data = {
        email: email_input.value
    }
    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        let result = data.result;
        if (result.includes("sent")){
            content_body.innerHTML = `<p>${result}</p>`;
        }
        else {
            error_div.innerHTML = `<p class="ms-2 mb-0">${result}</p>`
        }
    })
}

button.addEventListener("click", resetPassword);