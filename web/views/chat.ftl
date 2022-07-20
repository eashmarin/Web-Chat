<html>
    <head>
        <title>Chat</title>
        <meta charset="utf-8">
        <link rel="stylesheet" href="../styles/style.css">
    </head>
    <body>
        <script>
            window.onload = updatePage;
            setInterval(updatePage, 10000);

            window.onbeforeunload = makeOffline;


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
        </script>
        <div class="wrapper">
            <div class="chat-tools-container">
                <div class="chat-block" name="chat-block" id="chat-block">
                    <#if msgs??>
                        <#list msgs as msg>
                            <div class="other-msg-block">
                                <div class="msg-wrapper">
                                    <div class="other-msg-text">
                                        ${msg.getText()}
                                    </div>
                                    <div class="msg-info-bar">
                                        <div class="msg-author">
                                            ${msg.getAuthor()}
                                        </div>
                                        <div class="msg-date">
                                            ${msg.getDate()}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </#if>
                </div>
                <div class="msg-input-block">
                    <textarea class="msg-input" name="msg" style="width: 50%; height: 100%" id="msg-input"></textarea>
                    <button onclick="send()" class="msg-button">Send</button>
                </div>
            </div>
            <div class="users-list-container">
                <div class="users-list" id="users-list">

                </div>
            </div>
        </div>
    </body>
</html>