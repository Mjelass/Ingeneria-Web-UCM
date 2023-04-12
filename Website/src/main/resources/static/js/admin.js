// $(".dropdown-menu li a").click(function(){
  
//     $(".btn:first-child").html($(this).text()+'<span class="dropdownBtnName"></span>');
    
//   });

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