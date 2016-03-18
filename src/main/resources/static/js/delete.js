$(function() {
    $('form').submit(function(e) {
        e.preventDefault();

        var vendor = $('[name=vendor]');

        var url = "/vendor/delete/" + vendor.val();
        var csrfData = { csrfToken: getCsrfTokenFromCookie() };

        $.post(url, csrfData, function(result) {
            if (!result.indexOf("permission") > -1) {
                alert("You have deleted the vendor " + vendor.val());
            } else {
                $("#status").text(result);
            }
        });

        vendor.val("");
    });

    $('#logout').click(function(e) {
        e.preventDefault();

        $.post("/service/logout", function(){
            window.location = window.location.href.split("/")[0] + "/index.html";
        });
    });

    function getCsrfTokenFromCookie() {
        var cookies = {};
        document.cookie.split(';').forEach(function(cookie) {
            var keyAndValue = cookie.trim().split(/=(.+)?/);
            cookies[keyAndValue[0]] = keyAndValue[1];
        });
        return cookies['csrfToken'];
    }
});
