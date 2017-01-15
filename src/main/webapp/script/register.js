$('form').on('submit', validateForm);

let formMessages = {
    "email": "Email can contain latin letters, digits, '.', '-' and '_' characters. Also it must contain '@' and '.'",
    "first-name": "Name must be at least 2 characters. You can use latin or cyrillyc letters.",
    "last-name": "Name must be at least 2 characters. You can use latin or cyrillyc letters.",
    "display-name": "Name must be at least 4 characters. You can use latin or cyrillyc letters, digits, '.', '-' and '_' characters. First and last characters must be a letter or digit ",
    "password": "Password can contain numbers and latin letters. Minimum length is 6 characters",
    "passrepeat": "Passwords are not same"
};

function deleteErrors() {
    let errors = document.getElementsByClassName('error');
    for (let i = 0; i < errors.length;) {
        errors[i].parentNode.removeChild(errors[i]);
    }
}

function showMessage(input) {
    let errorMessage = document.createElement('p');
    errorMessage.setAttribute('class', 'error');
    errorMessage.innerHTML = formMessages[input.name];
    input.parentNode.appendChild(errorMessage);
}

function validate(form) {
    let valid = true;
    deleteErrors();
    inputs = form.getElementsByTagName('input');
    for (input of inputs) {
        if (input.name === 'passrepeat') {
            if (input.value !== form['password'].value) {
                showMessage(input);
                valid = false;
            }
        } else if (!input.checkValidity()) {
            showMessage(input);
            valid = false;
        }
    }
    return valid;
}

function validateForm(event) {
    let valid = true;
    let form = document.forms['register'];
    inputs = form.getElementsByTagName('input');
    for (input of inputs) {
        if (input.name === 'passrepeat') {
            if (input.value !== form['password'].value) {
                valid = false;
            }
            valid = false;
        }
    }
    if (!valid) {
    	if(event.preventDefault){
            event.preventDefault();
        }else{
            event.returnValue = false;
        }
    }
    return valid;
}
