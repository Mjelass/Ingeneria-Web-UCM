/**
 * WebSocket API, which only works once initialized
 */
const wsChat = {

    /**
     * Number of retries if connection fails
     */
    retries: 3,

    /**
     * Default action when message is received. 
     */
    receive: (msg) => {
        // Process only messages sent by a third user.
        if (document.getElementById('userId').innerText != msg['senderId']){
            // Check if scrollbar is full scrolled down
            let chatContent = document.querySelector('#chat-' + msg['event'] + '>.chat-msgs');
            console.log("top: " + chatContent.scrollTop + " . CH: " + chatContent.clientHeight + " . SH: " + chatContent.scrollHeight);
            let scroll = (chatContent.scrollTop + chatContent.clientHeight ) >= (chatContent.scrollHeight - 10);
            
            addNewMsgBottom(false, msg);

            if(currentChatId != 'ini' && scroll){
                scrollDownChat();
            }
            // TODO corregir
            updateChatList(false, msg);
        }
    },

    headers: { 'X-CSRF-TOKEN': config.csrf.value },

    /**
     * Attempts to establish communication with the specified
     * web-socket endpoint. If successfull, will call 
     */
    initialize: (endpoint, subs = []) => {
        try {
            wsChat.stompClient = Stomp.client(endpoint);
            wsChat.stompClient.reconnect_delay = 2000;
            // only works on modified stomp.js, not on original from mantainer's site
            wsChat.stompClient.reconnect_callback = () => wsChat.retries-- > 0;
            wsChat.stompClient.connect(wsChat.headers, () => {
                wsChat.connected = true;
                console.log('Connected to ', endpoint, ' - subscribing:');
                while (subs.length != 0) {
                    let sub = subs.pop();
                    console.log(` ... to ${sub} ...`)
                    wsChat.subscribe(sub);
                }
            });
            console.log("Connected to WS '" + endpoint + "'")
        } catch (e) {
            console.log("Error, connection to WS '" + endpoint + "' FAILED: ", e);
        }
    },

    subscribe: (sub) => {
        try {
            wsChat.stompClient.subscribe(sub,
                (m) => wsChat.receive(JSON.parse(m.body))); // fails if non-json received!
            console.log("Hopefully subscribed to " + sub);
        } catch (e) {
            console.log("Error, could not subscribe to " + sub, e);
        }
    }
}

function formatDate(date){
    let d = date.getDate().toString().padStart(2, '0');
    let m = (date.getMonth() + 1).toString().padStart(2, '0');
    let y = date.getFullYear().toString();
    let h = date.getHours().toString().padStart(2, '0');
    let min = date.getMinutes().toString().padStart(2, '0');

    return `${d}-${m}-${y} ${h}:${min}`;
}

function scrollDownChat() {
    let chatContent = document.querySelector('#chat-' + currentChatId + '>.chat-msgs');
    chatContent.scrollTop = chatContent.scrollHeight;
}

function createMessage(local, msg) {
    // local: if the msg was sent by a third person (false) or not (true).
    let msgCont = document.createElement('div');
    msgCont.classList.add(...['msg-container', 'w-100', 'd-flex']);
    if(local){
        msgCont.classList.add('justify-content-end');
    }
    
    let msgCard = document.createElement('div');
    msgCard.classList.add(...local? ['card']: ['card', 'bg-light']);
    msgCard.style.width = 'fit-content';
    if(local){
        msgCard.style.backgroundColor = 'var(--bs-gray-200)';
    }

    let msgCBody = document.createElement('div');
    msgCBody.classList.add(...['card-body', 'p-2']);

    if(!local) {
        let msgCSender = document.createElement('p');
        msgCSender.classList.add(...['card-title', 'mb-0', 'text-primary']);
        msgCSender.innerText = msg['sender'];
        msgCBody.appendChild(msgCSender);
    }

    let msgCText = document.createElement('p');
    msgCText.classList.add(...['card-text', 'mb-1']);
    msgCText.innerText = msg['text'];

    let msgCDet = document.createElement('div');
    msgCDet.classList.add(...['d-flex', 'justify-content-between', 'gap-3']);

    let msgCDate = document.createElement('p');
    msgCDate.classList.add(...['card-subtitle', 'text-muted']);
    let msgDate = new Date(msg['dateSent']);

    msgCDate.innerText = formatDate(msgDate);
    
    msgCDet.appendChild(msgCDate);
    if(local) {
        let readedCDate = document.createElement('p');
        readedCDate.classList.add('card-subtitle');
        readedCDate.innerText = '✅';
        msgCDet.appendChild(readedCDate);
    }

    msgCBody.appendChild(msgCText);
    msgCBody.appendChild(msgCDet);

    msgCard.appendChild(msgCBody);
    msgCont.appendChild(msgCard);

    return msgCont;
}

function addNewMsgTop(local, msg){
    let msgElem = createMessage(local, msg);
    let topMsg = document.querySelector('#chat-' + msg['event'] + 
        '>.chat-msgs>.msg-container');
    topMsg.before(msgElem);
}

function addNewMsgBottom(local, msg) {
    let msgElem = createMessage(local, msg);
    let bottomMsg = document.querySelector('#chat-' + msg['event'] + '>.chat-msgs');
    bottomMsg.appendChild(msgElem);
}

function updateChatList(local, msg){
    let chatB = document.getElementById('chat-b-' + msg['event']);
    if(!chatB.querySelector('.card-text>strong')) {
        // Create from zero.
        let cardT = chatB.querySelector('.card-text');
        cardT.classList.add('text-nowrap', 'text-truncate');
        cardT.innerHTML = local? `<strong>Tú: </strong>`:
            `<strong>${msg['sender']}: </strong>`;
        cardT.innerHTML += `<span>${msg['text']}</span>`;

        let divDate = document.createElement('div');
        divDate.classList.add('date-cont', 'd-flex', 'justify-content-between', 'gap-3');
        let pDate = document.createElement('p');
        pDate.classList.add('date-sent', 'card-subtitle', 'text-muted');
        pDate.style.fontSize = '0.8em';
        pDate.textContent = local? formatDate(new Date()):
            formatDate(new Date(Date.parse(msg['dateSent'])));
        divDate.appendChild(pDate);
        if(local) {
            let pReaded = document.createElement('p');
            pReaded.classList.add('read-icon', 'card-subtitle');
            pReaded.style.fontSize = '0.8em';
            pReaded.textContent = '✅';
            divDate.appendChild(pReaded);
        }
        chatB.querySelector('.card-body').appendChild(divDate);
    }
    else { // Update data.
        chatB.querySelector('.card-text>strong').textContent = local? 
        'Tú: ': msg['sender'] + ': ';
        chatB.querySelector('.card-text>span').textContent = msg['text'];
        chatB.querySelector('.date-sent').textContent = local?
            formatDate(new Date()): formatDate(new Date(Date.parse(msg['dateSent'])));
        if(!local) {
            if (chatB.querySelector('.read-icon')){
                chatB.querySelector('.date-cont').removeChild(chatB.querySelector('.read-icon'));
            }
        }
        else if ((chatB.querySelector('.read-icon'))) {
            // TODO para funcionalidad de lectura de mensajes.
            
        }
        else { // Create read icon element.
            let pReaded = document.createElement('p');
            pReaded.classList.add('read-icon', 'card-subtitle');
            pReaded.style.fontSize = '0.8em';
            pReaded.textContent = '✅';
            chatB.querySelector('.date-cont').appendChild(pReaded);
        }
    }
}

let currentChatId = 'ini';
function sendMsg(){
    let event = currentChatId;
    let text = document.getElementById('input-msg-' + currentChatId).value;
    document.getElementById('input-msg-' + currentChatId).value = '';
    go(`/chat/${event}/sendMsg`, "POST", {text}, false)
        .then(d => {
            console.log(d);

            addNewMsgBottom(true, {'text': text, 'event': event,
                'dateSent': (new Date()).toISOString()})

            scrollDownChat();

            updateChatList(true, {'text': text, 'event': event});
        })
        .catch(e => {console.log(e)
            alert("Something went wrong.");
        });
}

function setChat(chatId) {
    document.getElementById('chat-' + currentChatId).style.display = 'none';
    document.getElementById('chat-' + chatId).style.display = 'flex';
    currentChatId = chatId;
    scrollDownChat();
}

function loadMoreMsgs(chatId, userId){
    let inputFMDate = document.getElementById('chat-' + chatId + '-date');
    let firstMsgDate = inputFMDate.value;

    go(`/chat/${chatId}/loadMsgs/${firstMsgDate}`, "POST", {}, false)
    .then(async d => {
        let resJSON = await d.json(); // Convert body reponse to JSON
        if (resJSON['page-items'] > 0){ // There are still message to load.
            let chatContent = document.querySelector('#chat-' + chatId + '>.chat-msgs');
            let scrollHBefore = chatContent.scrollHeight;
            
            let auxFMDate = inputFMDate.value;
            resJSON['msgs'].forEach(msg => {
                addNewMsgTop(userId == msg['senderId'], msg);

                auxFMDate = msg['dateSent'];
            });
            // Update first message date.
            inputFMDate.value = auxFMDate;
            // scroll down to first new message added.
            chatContent.scrollTop = chatContent.scrollHeight - scrollHBefore;
        }
        else{
            document.querySelector('#chat-' + chatId + 
            '>.chat-msgs>.load-msgs-cont').remove();
        }
        console.log(resJSON);
        })
    .catch(e => {console.log(e);
        alert("Something went wrong.")});
}


/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
    if (config.socketUrl) {
        let chatList = document.getElementsByClassName('chat-button');
        let subs = [];
        for (let c of chatList){
            subs.push('/sendMsg/chat/' + c.id.replace('chat-b-', ''));
        }
        console.log(subs);
        // let subs = ["/sendMsg/chat/1"];
        wsChat.initialize(config.socketUrl, subs);
    } else {
        console.log("Not opening websocket: missing config", config)
    }

    // add your after-page-loaded JS code here; or even better, call 
    // 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
    //   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});