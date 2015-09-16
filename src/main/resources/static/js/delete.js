$(function() {
    $('form').submit(function(e) {
        e.preventDefault();

        var vendor = $('[name=vendor]').val();

        $.post("/vendor/delete/" + vendor, function(result) {
            if (!result.contains("permission")) {
                alert("You have deleted the vendor " + vendor);
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
    })
});
