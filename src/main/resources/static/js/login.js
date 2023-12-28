let all_inputs = document.querySelectorAll(".form-control")
let username_input = document.getElementById("username");
let password_input = document.getElementById("password");
let submit = document.getElementById("submit");

function checkFields(){
    if (username_input.value != "" && password_input.value != ""){
        submit.disabled = false;
    }
    else {
        submit.disabled = true;
    }
}

all_inputs.forEach(input => {
    input.addEventListener("input", checkFields);
});