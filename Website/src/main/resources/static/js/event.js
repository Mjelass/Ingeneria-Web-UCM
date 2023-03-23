// import "iw.js"

function updateElementsJoin(display, text, addition){
    document.getElementById("acts-aft-join").style.display = display;
    document.getElementById("acts-aft-join").style.flexDirection = "column";
    document.getElementById("text-join").innerHTML = text;
    let takenVacs = Number(document.getElementById("occupied").innerText);
    takenVacs += addition;
    document.getElementById("occupied").innerText = takenVacs;
    let capacity = Number(document.getElementById("capacity").innerText);
    document.getElementById("pbar-vacs-progress").style.width = ((takenVacs / capacity) * 200) + "px";
}

function toggleJoin(elem, eventId){
    if(elem.checked){
        let joined = true;
        go(`/event/${eventId}/userEvent`, "POST", {joined}, false)
            .then(d => {console.log(d);
                updateElementsJoin('flex', 'Salir', 1);})
            .catch(e => {//console.log(e)
                alert("Something went wrong.");
                elem.checked = false});
    }
    else {
        let joined = false;
        go(`/event/${eventId}/userEvent`, "POST", {joined}, false)
            .then(d => {console.log(d);
                updateElementsJoin('none', 'Unirse', -1);})
            .catch(e => {//console.log(e)
                alert("Something went wrong.");
                elem.checked = true});
    }
}

function updateElementsFav(icon, heart, num, addition){
    icon.innerText = heart;
    if (num != null) { // Search view case
        num.innerText = Number(num.innerText) + addition;
    }
}

function toggleFav(elem, eventId){
    let numf = document.getElementById("fav-n");
    if(elem.innerText == "❤️"){
        let fav = false;
        go(`/event/${eventId}/userEvent`, "POST", {fav}, false)
            .then(d => {console.log(d);
                updateElementsFav(elem, '🤍', numf, -1);})
            .catch(e => //console.log(e)
                alert("Something went wrong."));
    }
    else{
        let fav = true;
        go(`/event/${eventId}/userEvent`, "POST", {fav}, false)
            .then(d => {console.log(d);
                updateElementsFav(elem, '❤️', numf, 1);})
            .catch(e => //console.log(e)
                alert("Something went wrong."));
    }
}