function changeName(){
  let el = document.getElementById("dropdownMenuButton1"); 
  let name = window.location.href.split('/');
  name = name[name.length-1].split('?')[0];
  switch (name) {
    case "allEvents":
      name = "Trips";
      break;
    case "blackListUser":
      name = "Blacklist";
      break;
    case "allReports":
      name = "Reports";
      break;
      case "disabledUsers":
      name = "Disabled Users";
      break;
    case "allUsers":
    default:
      name = "All users";
      break;
  }
  el.innerHTML = name;
}

function deleteForm(){
  let formCont = document.getElementById('delete-form-cont');
  if(formCont.style.display == 'none'){
      formCont.style.display = 'flex';
  }
  else {
      formCont.style.display = 'none';
      document.getElementById('delete-desc').value = '';
  }
}

function getReportings(id){
  // description = document.getElementById('report-desc').value;
  
  // return go(`/user/${id}/report`, "GET", false)
  document.getElementById("reportSpan").textContent="1";
}


// function computeAge(e, birthdate){
//   let date = new Date(birthdate);
//   let diff = Date.now() - date.getTime();
//   let age = new Date(diff).getUTCFullYear();
//   console.log(e);
//   return 'sette';
// }

// function deleteUser(id){
//   alert("User id: " + id);
//   go(`/user/${id}/deleteUser`, "POST", false)
//   .then(d => {console.log(d);
//     deleteForm();})
//   .catch(e => {console.log(e)
//       alert("Something went wrong.");
//       });
// }