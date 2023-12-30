let check = document.getElementById("username_check");
let username_input = document.getElementById("username");
let submit = document.getElementById("submit");
let password_input = document.getElementById("password");
let confirm_password_input = document.getElementById("confirm_password");
let password_check = document.getElementById("password_check");
let email_input = document.getElementById("email");
let all_inputs = document.querySelectorAll(".form-control")
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
            checkFields();
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

function checkFields(){
    if (username_input.value != "" && confirm_password_input.value != "" && password_input.value != "" && email_input.value != ""){
        checkPassword();
    }
    else {
        submit.disabled = true;
    }
}

function checkPassword(){
    if (confirm_password_input.value == ""){
        password_check.innerText = "";
    }
    else {
        if (confirm_password_input.value != password_input.value){
            password_check.innerText = "Password does not match!";
            submit.disabled = true;
        }
        else {
            password_check.innerText = "";
            submit.disabled = false;
        }
    }
}

username_input.addEventListener("input", checkUsername);
confirm_password_input.addEventListener("input", checkPassword);
all_inputs.forEach(input => {
    input.addEventListener("input", checkFields);
});
