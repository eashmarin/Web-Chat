function makeOffline() {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("users-list").innerHTML = this.responseText;
        }
    }

    xhr.open('DELETE', 'chat/users_online', true);
    xhr.send();
}

function updateUsersOnline () {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange =function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("users-list").innerHTML = this.responseText;
        }
    }

    xhr.open('GET', 'chat/users_online', true);
    xhr.send();
}

function updateChat() {
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            if (this.responseText)
                document.getElementById("chat-block").insertAdjacentHTML("beforeend", xhr.responseText);
            return;
        }
    }

    xhr.open('PUT', 'chat', true);
    xhr.send();
}

function updatePage() {
    updateChat();
    updateUsersOnline();
}

function send() {
    let req = new XMLHttpRequest();
    let msg = document.getElementById('msg-input').value;

    req.open('POST', 'chat', true);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                document.getElementById("chat-block").insertAdjacentHTML("beforeend", req.responseText);
                document.getElementById("msg-input").value = "";
            }
        }
    }

    req.setRequestHeader('Content-type','multipart/form-data');

    req.send(msg);
}



function signUp() {
    window.location.href = '/signup';
}

function authorize() {
    let loginElement = document.getElementById("login");
    let passElement = document.getElementById("password");

    if (document.getElementById("invalid-input-hint")) {
        loginElement.removeAttribute("style");
        passElement.removeAttribute("style");
        document.getElementById("invalid-input-hint").remove();
    }

    let params = JSON.stringify({ "login": loginElement.value, "password": passElement.value});

    let req = new XMLHttpRequest();

    req.open('POST', 'add', true);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200)
                window.location.href = "/chat";

            if (req.status == 401) {
                loginElement.setAttribute("style", "border: 2px solid lightcoral");
                passElement.setAttribute("style", "border: 2px solid lightcoral");
                document.getElementById("outer-block").insertAdjacentHTML("beforeend", req.responseText);
            }
        }
    }

    req.setRequestHeader('Content-Type', 'application/json');

    req.send(params);
}



function SignUp() {
    let loginElement = document.getElementById("login");
    let passElement = document.getElementById("password");
    let repassElement = document.getElementById("re-password");

    if (document.getElementById("invalid-input-hint")) {
        loginElement.removeAttribute("style");
        passElement.removeAttribute("style");
        repassElement.removeAttribute("style");
        document.getElementById("invalid-input-hint").remove();
    }

    var req = new XMLHttpRequest();

    req.open("POST", "signup", true);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200)
                window.location.href = "/add";

            if (req.status == 401) {
                document.getElementById("outer-block").insertAdjacentHTML("beforeend", req.responseText);

                if (passElement.value != repassElement.value || passElement.value.length < 8) {
                    passElement.setAttribute("style", "border: 2px solid lightcoral");
                    repassElement.setAttribute("style", "border: 2px solid lightcoral");
                }
                else
                    document.getElementById("login").setAttribute("style", "border: 2px solid lightcoral");
            }
        }
    }

    req.setRequestHeader("Content-Type", "application/json")

    req.send(JSON.stringify({"login": login.value, "password": passElement.value, "rePassword": repassElement.value}));
}