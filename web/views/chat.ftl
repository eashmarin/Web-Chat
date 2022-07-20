<html>
    <head>
        <title>Chat</title>
        <meta charset="utf-8">
        <link rel="stylesheet" href="../styles/style.css">
        <script type="text/javascript" src="../scripts/script.js"></script>
    </head>
    <body>
        <script>
            window.onload = updatePage;
            setInterval(updatePage, 10000);

            window.onbeforeunload = makeOffline;
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