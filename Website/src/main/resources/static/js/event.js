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

function toggleFav(){
    let fav = document.getElementById("fav-i");
    let numf = document.getElementById("fav-n");
    console.log("Toggle");
    if(fav.innerText == "❤️"){
        fav.innerText = "🤍";
        numf.innerText = Number(numf.innerText) - 1;
        go("/login", "GET", {})
            .then(d => console.log("ok!"))
            .catch(e => console.log(e))
    }
    else{
        fav.innerText = "❤️";
        numf.innerText = Number(numf.innerText) + 1;
    }
}