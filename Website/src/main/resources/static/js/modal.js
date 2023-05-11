function toggleModalForm(elementId){
    let formCont = document.getElementById(elementId);
    if(formCont.classList.contains('show')){
        formCont.classList.remove('show');
    }
    else {
        formCont.classList.add('show');
        // Se resetea el formulario contenido:
        document.querySelector('#' + elementId + '>.modal-form').reset();
    }
}