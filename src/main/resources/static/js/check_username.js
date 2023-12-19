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
            submit.disabled = false;
        }
        else {
            submit.disabled = true;
        }
        check.innerText = data.result;
        console.log(data.result)
    })
}

username_input.addEventListener("input", checkUsername);