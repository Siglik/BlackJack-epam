function deleteErrors() {
    let errors = document.getElementsByClassName('val-error');
    for (let i = 0; i < errors.length;) {
        errors[i].parentNode.removeChild(errors[i]);
    }
}

function showMessage(input) {
    let errorMessage = document.createElement('p');
    errorMessage.setAttribute('class', 'val-error');
    errorMessage.innerHTML = validationMessages[input.name];
    input.parentNode.appendChild(errorMessage);
}

function validate(form) {
    let valid = true;
    deleteErrors();
    inputs = form.getElementsByTagName('input');
    for (input of inputs) {
        if (input.name === 'passrepeat') {
            if (input.value !== form['new-password'].value) {
                showMessage(input);
                valid = false;
            }
        } else if (!input.checkValidity()) {
            showMessage(input);
            valid = false;
        }
    }
    if (valid){
    	form.submit();
    }
    return valid;
}

let password = document.getElementById("new-password")
, confirm_password = document.getElementById("passrepeat");

function validatePassword(){
	if(password.value != confirm_password.value) {
		confirm_password.setCustomValidity(validationMessages[confirm_password.name]);
	} else {
		confirm_password.setCustomValidity('');
	}
}

password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;
