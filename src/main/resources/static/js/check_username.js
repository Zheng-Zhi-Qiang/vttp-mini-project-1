let check = document.getElementById("username_check");
let username_input = document.getElementById("username");
let submit = document.getElementById("submit");
const url = "/user/username";

function checkUsername(e) {
    let username = e.target.value;
    let data = {
        username: username
    }

    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        if (data.result.includes("available")){
            if (check.classList.contains("not_available")){
                check.classList.remove("not_available");
            }
            check.classList.add("available");
            submit.disabled = false;
        }
        else {
            if (check.classList.contains("available")){
                check.classList.remove("available");
            }
            check.classList.add("not_available");
            submit.disabled = true;
        }
        
        if (username == ""){
            check.innerText = "";
            submit.disabled = true;
        }
        else {
            check.innerText = data.result;
        }
    })
}

username_input.addEventListener("input", checkUsername);