let old_pw = document.getElementById("old_pw");
let new_pw = document.getElementById("new_pw");
let confirm_pw = document.getElementById("cfm_pw");
let all_inputs = document.querySelectorAll(".form-control");
let password_check = document.getElementById("password_check");
let message_div = document.getElementById("message");
let submit = document.getElementById("change");
let url = "/user/changePassword";

function clearInput(){
    all_inputs.forEach(input => {
        input.value = "";
    });
}

function checkFields(){
    if (old_pw.value != "" && new_pw.value != "" && confirm_pw.value != ""){
        checkPassword();
    }
    else {
        submit.disabled = true;
    }
}


function checkPassword(){
    if (confirm_pw.value == ""){
        password_check.innerText = "";
    }
    else {
        if (confirm_pw.value != new_pw.value){
            password_check.innerText = "Password does not match!";
            submit.disabled = true;
        }
        else {
            password_check.innerText = "";
            submit.disabled = false;
        }
    }
}

function displayResult(result){
    if (result.includes("incorrect")){
        if (message_div.classList.contains("available")){
            message_div.classList.remove("available");
        }
        message_div.classList.add("not_available");
        message_div.innerText = result;
    }
    else {
        if (message_div.classList.contains("not_available")){
            message_div.classList.remove("not_available");
        }
        message_div.classList.add("available");
        message_div.innerText = result;
    }
    setTimeout(() => {message_div.innerText = ""}, 10000);
}

function changePassword(){
    data = {
        oldpw: old_pw.value,
        newpw: new_pw.value
    }

    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((data) => {
        let result = data.result;
        if (!result.includes("incorrect")){
            clearInput();
        }
        displayResult(result);
    })
}

submit.addEventListener("click", changePassword);
confirm_pw.addEventListener("input", checkPassword);
all_inputs.forEach(input => {
    input.addEventListener("input", checkFields)
});
