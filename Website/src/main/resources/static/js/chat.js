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
    receive: (text) => {
        console.log(text['text']);
        console.log(text['senderId']);
        if (document.getElementById('userId').innerText != text['senderId']){
            updateChat(false, {'sender': text['sender'], 'chatId': text['event'],
                'text': text['text'], 'dateSent': text['dateSent']});
            
            if(currentChatId != 'ini'){
                scrollDownChat();
            }

            updateChatList(false, {'sender': text['sender'], 'chatId': text['event'],
                'text': text['text'], 'dateSent': text['dateSent']});
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

// TODO remove
function go(url, method, data = {}, json = true, headers = false) {
    let params = {
        method: method, // POST, GET, POST, PUT, DELETE, etc.
        headers: headers === false ? {
            "Content-Type": "application/json; charset=utf-8",
        } : headers,
        body: data instanceof FormData ? data : JSON.stringify(data)
    };
    if (method === "GET") {
        // GET requests cannot have body; I could URL-encode, but it would not be used here
        delete params.body;
    } else {
        params.headers["X-CSRF-TOKEN"] = config.csrf.value;
    }
    console.log("sending", url, params)
    return fetch(url, params)
        .then(response => {
            const r = response;
            if (r.ok) {
                return json? r.json().then(json => Promise.resolve(json)): r;
            } else {
                return r.text().then(text => Promise.reject({
                    url,
                    data: JSON.stringify(data),
                    status: r.status,
                    text
                }));
            }
        });
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

function updateChat(local, values) { // Actualiza la lista de mensaje del chat
    // local indica si el mensaje es del este usuario (true) o de un tercero.
    // values es un diccionario con los datos necesarios para actualizar.
    let msgCont = document.createElement('div');
    msgCont.classList.add('msg-container', 'w-100', 'd-flex');
    if(local) {
        msgCont.classList.add('justify-content-end');
    }

    let msgCard = document.createElement('div');
    msgCard.classList.add('card');
    if(!local) {
        msgCard.classList.add('bg-light');
    }
    else {
        msgCard.style.backgroundColor = 'var(--bs-gray-200)';
    }
    msgCard.style.width = 'fit-content';

    let msgCardBody = document.createElement('div');
    msgCardBody.classList.add('card-body', 'p-2');

    if(!local){
        let pTitle = document.createElement('p');
        pTitle.classList.add('card-title', 'mb-0', 'text-primary');
        pTitle.style.fontWeight = '500';
        pTitle.textContent = values['sender'];

        msgCardBody.appendChild(pTitle);
    }

    let pText = document.createElement('p');
    pText.classList.add('card-text', 'mb-1');
    pText.textContent = values['text'];

    let divDate = document.createElement('div');
    divDate.classList.add('d-flex', 'justify-content-between', 'gap-3');
    let pDate = document.createElement('p');
    pDate.classList.add('card-subtitle', 'text-muted');
    pDate.style.fontSize = '0.8em';
    pDate.textContent = local? formatDate(new Date()):
        formatDate(new Date(Date.parse(values['dateSent'])));
    divDate.appendChild(pDate);
    if(local) {
        let pReaded = document.createElement('p');
        pReaded.classList.add('card-subtitle');
        pReaded.style.fontSize = '0.8em';
        pReaded.textContent = '✅';
        divDate.appendChild(pReaded);
    }

    msgCardBody.appendChild(pText);
    msgCardBody.appendChild(divDate);
    msgCard.appendChild(msgCardBody);
    msgCont.appendChild(msgCard);
    let chat = document.querySelector('#chat-' + values['chatId'] + '>.chat-msgs');
    chat.appendChild(msgCont);    
}

function updateChatList(local, values){
    let chatB = document.getElementById('chat-b-' + values['chatId']);
    if(!chatB.querySelector('.card-text>strong')) {
        // Se debe crear de cero las secciones.
        let cardT = chatB.querySelector('.card-text');
        cardT.classList.add('text-nowrap', 'text-truncate');
        cardT.innerHTML = local? `<strong>Tú: </strong>`:
            `<strong>${values['sender']}: </strong>`;
        cardT.innerHTML += `<span>${values['text']}</span>`;

        let divDate = document.createElement('div');
        divDate.classList.add('date-cont', 'd-flex', 'justify-content-between', 'gap-3');
        let pDate = document.createElement('p');
        pDate.classList.add('date-sent', 'card-subtitle', 'text-muted');
        pDate.style.fontSize = '0.8em';
        pDate.textContent = local? formatDate(new Date()):
            formatDate(new Date(Date.parse(values['dateSent'])));
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
    else { // Se actualiza.
        chatB.querySelector('.card-text>strong').textContent = local? 
        'Tú: ': values['sender'] + ': ';
        chatB.querySelector('.card-text>span').textContent = values['text'];
        chatB.querySelector('.date-sent').textContent = local?
            formatDate(new Date()): formatDate(new Date(Date.parse(values['dateSent'])));
        if(!local) {
            chatB.querySelector('.date-cont').removeChild(chatB.querySelector('.read-icon'));
        }
        else if ((chatB.querySelector('.read-icon'))) {
            // Existe el elemento p que almacena el icono.

        }
        else { // Se debe crear el elemento
            let pReaded = document.createElement('p');
            pReaded.classList.add('read-icon', 'card-subtitle');
            pReaded.style.fontSize = '0.8em';
            pReaded.textContent = '✅';
            chatB.querySelector('.date-cont').appendChild(pReaded);
        }
    }
}

function addLocalMsg(text){
    let msgCont = document.createElement('div');
    msgCont.classList.add('msg-container', 'w-100', 'd-flex', 'justify-content-end');

    let msgCard = document.createElement('div');
    msgCard.classList.add('card');
    msgCard.style.width = 'fit-content';
    msgCard.style.backgroundColor = 'var(--bs-gray-200)';

    let msgCardBody = document.createElement('div');
    msgCardBody.classList.add('card-body', 'p-2');

    let pText = document.createElement('p');
    pText.classList.add('card-text', 'mb-1');
    pText.textContent = text;

    let divDate = document.createElement('div');
    divDate.classList.add('d-flex', 'justify-content-between', 'gap-3');
    let pDate = document.createElement('p');
    pDate.classList.add('card-subtitle', 'text-muted');
    pDate.style.fontSize = '0.8em';
    pDate.textContent = formatDate(new Date());
    let pReaded = document.createElement('p');
    pReaded.classList.add('card-subtitle');
    pReaded.style.fontSize = '0.8em';
    pReaded.textContent = '✅';
    divDate.appendChild(pDate);
    divDate.appendChild(pReaded);

    msgCardBody.appendChild(pText);
    msgCardBody.appendChild(divDate);
    msgCard.appendChild(msgCardBody);
    msgCont.appendChild(msgCard);
    let chat = document.querySelector('#chat-' + currentChatId + '>.chat-msgs');
    chat.appendChild(msgCont);
}

let currentChatId = 'ini';
function sendMsg(){
    let event = currentChatId;
    let text = document.getElementById('input-msg-' + currentChatId).value;
    document.getElementById('input-msg-' + currentChatId).value = '';
    go(`/chat/${event}/sendMsg`, "POST", {text}, false)
        .then(d => {console.log(d);
            updateChat(true, {'text': text, 'chatId': event});
            // addLocalMsg(text);
            scrollDownChat();

            updateChatList(true, {'text': text, 'chatId': event});
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
    console.log("Setting Current Chat Id: " + chatId);
}

function formatDate(date) {
    return `${('00' + date.getDate()).slice(-2)}-${('00' + (date.getMonth() + 1)).slice(-2)}-${date.getFullYear()}` +
    ` ${('00' + date.getHours()).slice(-2)}:${('00' + date.getMinutes()).slice(-2)}`;
}

function loadMoreMsgs(chatId, userId){
    let inputNPNum = document.getElementById('chat-' + chatId + '-page');
    let nextPageNum = Number(inputNPNum.value);
    let inputFMDate = document.getElementById('chat-' + chatId + '-date');
    let firstMsgDate = inputFMDate.value;

    go(`/chat/${chatId}/loadMsgs/${firstMsgDate}`, "POST", {}, false)
    .then(async d => {
        let resJSON = await d.json();
        if (resJSON['page-items'] > 0){
            let chatContent = document.querySelector('#chat-' + chatId + '>.chat-msgs');
            let scrollHBefore = chatContent.scrollHeight;
            let auxFMDate = inputFMDate.value;
            resJSON['msgs'].forEach(msg => {
                let refElem = document.querySelector('#chat-' + chatId + 
                    '>.chat-msgs>.msg-container');
                let msgCont = document.createElement('div');
                msgCont.classList.add(...['msg-container', 'w-100', 'd-flex']);
                if(userId == msg['senderId'])
                    msgCont.classList.add('justify-content-end');
                
                let msgCard = document.createElement('div');
                msgCard.classList.add(...userId == msg['senderId']? ['card']: 
                    ['card', 'bg-light']);
                msgCard.style.width = 'fit-content';
                if(userId == msg['senderId'])
                    msgCard.style.backgroundColor = 'var(--bs-gray-200)';

                let msgCBody = document.createElement('div');
                msgCBody.classList.add(...['card-body', 'p-2']);

                if(userId != msg['senderId']) {
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
                // console.log(msgDate);
                msgCDate.innerText = formatDate(msgDate);
                
                msgCDet.appendChild(msgCDate);
                if(userId == msg['senderId']) {
                    let readedCDate = document.createElement('p');
                    readedCDate.classList.add('card-subtitle');
                    readedCDate.innerText = '✅';
                    msgCDet.appendChild(readedCDate);
                }

                msgCBody.appendChild(msgCText);
                msgCBody.appendChild(msgCDet);

                msgCard.appendChild(msgCBody);
                msgCont.appendChild(msgCard);

                refElem.before(msgCont);
                // console.log(msg['text']);
                auxFMDate = msg['dateSent'];
            });
            inputFMDate.value = auxFMDate;
            chatContent.scrollTop = chatContent.scrollHeight - scrollHBefore;
            // inputNPNum.value = (nextPageNum + 1);
        }
        else{
            document.querySelector('#chat-' + chatId + 
            '>.chat-msgs>.load-msgs-cont').remove();
        }
        console.log(resJSON);
        // console.log(await d.json());
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

        // let p = document.querySelector("#nav-unread");
        // if (p) {
        //     go(`${config.rootUrl}/user/unread`, "GET").then(d => p.textContent = d.unread);
        // }
    } else {
        console.log("Not opening websocket: missing config", config)
    }

    // add your after-page-loaded JS code here; or even better, call 
    // 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
    //   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});