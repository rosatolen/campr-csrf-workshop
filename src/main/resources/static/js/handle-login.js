$(function() {
    $('form').submit(function(e) {
        e.preventDefault();

        var username = $('[name=username]');
        var password = $('[name=password]');

        var loginData = {
            username: username.val(),
            password : password.val()
        };

        $.post("/session", loginData, function(result) {
            if (result.indexOf("Sorry") > -1) {
                alert(result);
            } else {
                window.location = window.location.href.split("/")[0] + result;
            }
        });

        username.val("");
        password.val('');
    });
});
