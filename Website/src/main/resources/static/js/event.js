// import "iw.js"

function toggleActionsDisplay(){
    if (document.getElementById("check-join").checked == true) {
        document.getElementById("acts-aft-join").style.display = "flex";
        document.getElementById("acts-aft-join").style.flexDirection = "column";
        document.getElementById("text-join").innerHTML = "Salir";
        document.getElementById("pbar-vacs-progress").style.width = "150px";
        let takenVacs = Number(document.getElementById("taken-vacs").innerText);
        takenVacs++;
        document.getElementById("taken-vacs").innerText = takenVacs;
    } 
    else {
        document.getElementById("acts-aft-join").style.display = "none";
        document.getElementById("text-join").innerHTML = "Unirse";
        document.getElementById("pbar-vacs-progress").style.width = "120px";
        let takenVacs = Number(document.getElementById("taken-vacs").innerText);
        takenVacs--;
        document.getElementById("taken-vacs").innerText = takenVacs;
    }
}

function toggleJoin(elem, eventId){
    if(elem.checked){
        document.getElementById("acts-aft-join").style.display = "flex";
        document.getElementById("acts-aft-join").style.flexDirection = "column";
        document.getElementById("text-join").innerHTML = "Salir";
        document.getElementById("pbar-vacs-progress").style.width = "150px";
        let takenVacs = Number(document.getElementById("taken-vacs").innerText);
        takenVacs++;
        document.getElementById("taken-vacs").innerText = takenVacs;
        let joined = true;
        go(`/event/${eventId}/userEvent`, "POST", {joined}, false)
            .then(d => console.log(d))
            .catch(e => console.log(e));
    }
    else {
        document.getElementById("acts-aft-join").style.display = "none";
        document.getElementById("text-join").innerHTML = "Unirse";
        document.getElementById("pbar-vacs-progress").style.width = "120px";
        let takenVacs = Number(document.getElementById("taken-vacs").innerText);
        takenVacs--;
        document.getElementById("taken-vacs").innerText = takenVacs;
        let joined = false;
        go(`/event/${eventId}/userEvent`, "POST", {joined}, false)
            .then(d => console.log(d))
            .catch(e => console.log(e));
    }
}

function toggleFav(elem, eventId){
    // console.log(elem)
    // let favI = document.getElementById("fav-i");
    let numf = document.getElementById("fav-n");
    // console.log("Toggle");
    if(elem.innerText == "❤️"){
        elem.innerText = "🤍";
        if (numf != null) {
            numf.innerText = Number(numf.innerText) - 1;
        }
        let fav = false;
        go(`/event/${eventId}/userEvent`, "POST", {fav}, false)
            .then(d => console.log(d))
            .catch(e => console.log(e));
    }
    else{
        elem.innerText = "❤️";
        if (numf != null) {
            numf.innerText = Number(numf.innerText) + 1;
        }
        let fav = true;
        go(`/event/${eventId}/userEvent`, "POST", {fav}, false)
            .then(d => console.log(d))
            .catch(e => console.log(e));
    }
}