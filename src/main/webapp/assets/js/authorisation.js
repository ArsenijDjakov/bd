$(function () {
    $("#btnSubmitLoginForm").click(function () {
        var username = $("#userName").val();
        var password = $("#password").val();
        $.ajax({
            cache: true,
            type: "POST",
            url: "/login",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({"username": username, "password": password}),
            dataType: "json",
            beforeSend: function () {
                showSpinner("btnSubmitLoginForm");
            },
            error: function () {
                hideSpinner("btnSubmitLoginForm");
                $("#password").val('');
                var responseDiv = document.getElementById("loginResult");
                responseDiv.innerHTML = loginFailLabel;
                responseDiv.className = "alert alert-danger";
                responseDiv.style = "display: block";
            },
            success: function (response) {
                sessionStorage.setItem("loggedIn", true);
                sessionStorage.setItem("token", response.token);
                sessionStorage.setItem("username", response.username);
                sessionStorage.setItem("userEmail", response.email);
                window.location.href="/";
            }

        });
    });
});

$(function () {
    $("#btnSubmitRegistrationForm").click(function () {
        clearValidations();
        if(validateForm()===false){
            return;
        }
        var username = $("#userName").val();
        var password = $("#password").val();
        var secondPassword = $("#secondPassword").val();
        var email = $("#email").val();
        $.ajax({
            cache: true,
            type: "POST",
            url: "/registration",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({"username": username, "email": email, "password": password}),
            dataType: "json",
            beforeSend: function () {
                showSpinner("btnSubmitRegistrationForm");
            },
            error: function (data) {
                hideSpinner("btnSubmitRegistrationForm");
                $("#password").val('');
                $("#secondPassword").val('');
                var responseDiv = document.getElementById("registrationResult");
                responseDiv.innerHTML = data.responseJSON.message;
                responseDiv.className = "alert alert-danger";
                responseDiv.style = "display: block";
            },
            success: function () {
                sessionStorage.setItem("registration", true);
                window.location.href="/login";
            }

        });
    });
});

$(function () {
    $("#btnSubmitPasswordForm").click(function () {
        clearPasswordValidations();
        if(validatePasswordChangeForm()===false){
            return;
        }
        var oldPassword = $("#oldPassword").val();
        var newPassword = $("#newPassword").val();
        $.ajax({
            cache: true,
            type: "POST",
            data:{oldPassword: oldPassword, newPassword: newPassword},
            url: "/account/password",
            headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
            beforeSend: function () {
                showSpinner("btnSubmitPasswordForm");
            },
            error: function (data) {
                hideSpinner("btnSubmitPasswordForm");
                $("#oldPassword").val('');
                $("#newPassword").val('');
                $("#secondNewPassword").val('');
                var oldPasswordValidation = $("#invalidOldPassword");
                oldPasswordValidation.text(invalidPassword);
                oldPasswordValidation.show();
            },
            success: function () {
                cleanSession();
                window.location.href="/login";
            }
        });
    });
});

$(function () {
    $("#logout").click(function () {
        $.ajax({
            cache: true,
            type: "POST",
            url: "/logout",
            headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
            error: function (data) {
            },
            success: function () {
                cleanSession();
                window.location.href="/";
            }
        });
    });
});

function clearValidations(){
    $("#invalidUsername").hide();
    $("#invalidPassword").hide();
    $("#passwordNotMatch").hide();
    $("#invalidEmail").hide();
    $("#registrationResult").hide();
}

function validateForm () {
    var counter = 0;
    var usernameValidation = $("#invalidUsername");
    var passwordValidation = $("#invalidPassword");
    var secondPasswordValidation = $("#passwordNotMatch");
    var emailValidation = $("#invalidEmail");
    var username = $("#userName").val();
    if (username.length === 0) {
        usernameValidation.text(invalidUsername);
        usernameValidation.show();
        counter++;
    }
    var email = $("#email").val();
    const regex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
    if (!regex.test(email)) {
        emailValidation.text(invalidEmail);
        emailValidation.show();
        counter++;
    }
    var password = $("#password").val();
    if (password.length === 0) {
        passwordValidation.text(invalidPassword);
        passwordValidation.show();
        counter++;
    }
    var secondPassword = $("#secondPassword").val();
    if (secondPassword.length === 0 || secondPassword !== password) {
        secondPasswordValidation.text(passwordNotMatch);
        secondPasswordValidation.show();
        counter++;
    }
    return counter <= 0;

}

function validatePasswordChangeForm() {
    var counter = 0;
    var newPasswordValidation = $("#invalidNewPassword");
    var secondNewPasswordValidation = $("#passwordNotMatch");
    var password = $("#newPassword").val();
    if (password.length === 0) {
        newPasswordValidation.text(invalidPasswordFormat);
        newPasswordValidation.show();
        counter++;
    }
    var secondPassword = $("#secondNewPassword").val();
    if (secondPassword.length === 0 || secondPassword !== password) {
        secondNewPasswordValidation.text(passwordNotMatch);
        secondNewPasswordValidation.show();
        counter++;
    }
    return counter <= 0;

}

function clearPasswordValidations(){
    $("#invalidOldPassword").hide();
    $("#invalidNewPassword").hide();
    $("#passwordNotMatch").hide();
}

function showSpinner(id){
    $("#" + id).attr('disabled', true);
    $("#spinner").show();
}

function hideSpinner(id){
    $("#" + id).attr('disabled',false);
    $("#spinner").hide();
}

function checkLogin() {
    return sessionStorage.getItem("loggedIn") === 'true';
}

function cleanSession() {
    sessionStorage.removeItem("loggedIn");
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("currentUsername");
    sessionStorage.removeItem("currentUserEmail");
}