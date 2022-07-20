<%--
  Created by IntelliJ IDEA.
  User: Evgeny
  Date: 7/4/2022
  Time: 7:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add</title>
        <link rel="stylesheet" href="../styles/style.css">
    </head>
    <body>
        <script>
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
        </script>
        <div class="outer-block">
            <div class="auth-block">
                <img src="images/user.png" width="64px" height="64px">
                <label>
                    <input type="text" placeholder="Login" id="login" class="auth-field"><br \>
                </label>

                <label>
                    <input type="password" placeholder="Password" id="password" class="auth-field"><br \>
                </label>

                <button class="auth-button" onclick="authorize()">SignIn</button>
                <button class="auth-button" onclick="signUp()">SignUp</button>
            </div>
        </div>
    </body>
</html>
