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

<#if own_msg??>
    <div class="msg-block">
        <div class="msg-wrapper">
            <div class="msg-text">
                ${own_msg.getText()}
            </div>
            <div class="msg-info-bar">
                <div class="msg-author">
                    ${own_msg.getAuthor()}
                </div>
                <div class="msg-date">
                    ${own_msg.getDate()}
                </div>
            </div>
        </div>
    </div>
</#if>

<#if other_msg??>
    <div class="other-msg-block">
        <div class="msg-wrapper">
            <div class="other-msg-text">
                ${other_msg.getText()}
            </div>
            <div class="msg-info-bar">
                <div class="msg-author">
                    ${other_msg.getAuthor()}
                </div>
                <div class="msg-date">
                    ${other_msg.getDate()}
                </div>
            </div>
        </div>
    </div>
</#if>