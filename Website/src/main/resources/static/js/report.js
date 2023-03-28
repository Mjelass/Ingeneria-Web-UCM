function reportUser(userTarget, description = ""){
    description = document.getElementById('report-desc').value;
    go(`/user/${userTarget}/report`, "POST", {description}, false)
        .then(d => {console.log(d);
            toggleReportForm();})
        .catch(e => {console.log(e)
            alert("Something went wrong.");
            });
}