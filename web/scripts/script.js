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
    var msg = document.getElementById('msg-input').value;
    //console.log("5");

    req.open('POST', 'chat', true);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                //console.log(req.responseXML.getElementById("msg-1").innerHTML);
                //console.log(req.responseText.g);
                document.getElementById("chat-block").insertAdjacentHTML("beforeend", req.responseText);
                document.getElementById("msg-input").value = "";
            }
            else {
                // Возникла ошибка HTTP
                alert("HTTP error: " + req.status);
            }
        }
    }

    req.setRequestHeader('Content-type','application/xml');

    console.log(msg);
    req.send(msg);
}



function signUp() {
    window.location.href = '/signup';
}

function authorize() {
    let login = document.getElementById("login").value;
    let password = document.getElementById("password").value;

    let params = JSON.stringify({ "login": login, "password": password});

    let req = new XMLHttpRequest();

    req.open('POST', 'add', true);

    req.onreadystatechange = function () {
        if (req.readyState == 4 && req.status == 200) {
            if (req.responseText == "0") {
                window.location.href = "/chat";     //TODO: replace with POST request
            }
            else
                console.log(req.responseText);
        }
    }

    req.setRequestHeader('Content-Type', 'application/json');

    req.send(params);
}



function SignUp() {
    var login = document.getElementById("login").value;
    var password = document.getElementById("password").value;
    var rePassword = document.getElementById("re-password").value;

    var req = new XMLHttpRequest();

    req.open("POST", "signup", true);

    req.onreadystatechange = function () {
        if (req.readyState == 4 && req.status == 200) {
            if (req.responseText == '0')
                window.location.href = "/add";
            else {
                console.log(req.responseText);
            }
        }
    }

    req.setRequestHeader("Content-Type", "application/json")

    req.send(JSON.stringify({"login": login, "password": password, "rePassword": rePassword}));
}