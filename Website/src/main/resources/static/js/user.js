function togglePicForm(){
    let formCont = document.getElementById('pic-form-cont');
    if(formCont.style.display == 'none'){
        formCont.style.display = 'flex';
    }
    else {
        formCont.style.display = 'none';
        document.getElementById('report-desc').value = '';
    }
}