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
        <script type="text/javascript" src="../scripts/script.js"></script>
    </head>
    <body>
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
