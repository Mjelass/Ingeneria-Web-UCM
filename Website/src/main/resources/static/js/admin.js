function showUsers() {
    var x = document.getElementById("userlist");
    var y = document.getElementById("triplist");
    var z = document.getElementById("blacklist");
    x.style.display = "block";
    y.style.display = "none";
    z.style.display = "none";

  }

  function showTrips() {
    var x = document.getElementById("userlist");
    var y = document.getElementById("triplist");
    var z = document.getElementById("blacklist");
    x.style.display = "none";
    y.style.display = "block";
    z.style.display = "none";

  }

  function showBlacklist() {
    var x = document.getElementById("userlist");
    var y = document.getElementById("triplist");
    var z = document.getElementById("blacklist");
    x.style.display = "none";
    y.style.display = "none";
    z.style.display = "block";

  }