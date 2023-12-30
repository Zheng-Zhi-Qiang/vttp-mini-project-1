let button = document.getElementById("edit_email");
let email_div = document.getElementById("email_group");
let url = "/user/changeEmail";

function changeEmail(){
    let email = document.getElementById("email").value;
    data = {
        email: email
    }

    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json", "Accept": "application/json"},
        body: JSON.stringify(data)
    })
    .then((response) => {
        email_div.innerHTML = `<p class="mb-0"><b>Email:</b> <span id="email">${email}</span></p>
            <button class="btn outline-0 p-0 ms-2" id="edit_email"><img src="/images/edit_icon.png" alt=""></button>`;
        button = document.getElementById("edit_email");
        button.addEventListener("click", changetoInput)
    })
}

function changetoInput(){
    let email = document.getElementById("email").innerText;
    email_div.innerHTML = `<p class="mb-0"><b>Email:</b> </p><input id="email" class="ms-1 form-control" type="email" value="${email}"/><button class="ms-2 btn btn-outline-dark" id="save_email">Save</button>`
    button = document.getElementById("save_email");
    button.addEventListener("click", changeEmail);
}

button.addEventListener("click", changetoInput);