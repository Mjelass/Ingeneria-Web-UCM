function reportUser(userTarget, description = ""){
    
    go(`/user/${userTarget}/report`, "POST", {description}, false)
        .then(d => {console.log(d);})
        .catch(e => {console.log(e)
            alert("Something went wrong.");
            });
}