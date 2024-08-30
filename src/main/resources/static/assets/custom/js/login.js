$(document).ready(function(){
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000", // Duration in milliseconds (5000ms = 5 seconds)
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    $(".btn.btn-primary.btn-block").on("click", function(){
        let username = $("#username").val();
        let password = $("#password").val();


        let userDetails = {
            uname: username,
            password: password,
            // active: true
        };

        $.ajax({
            url: "/api/login",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(userDetails),
            success: function (response) {
                console.log("data received successfully");
                toastr.success(response);
                setTimeout(function() {
                    window.location.href = "/product_master.html";
                }, 5000);
            },
            error: function (xhr) {
                toastr.error(xhr.responseText);
                setTimeout(function() {
                    window.location.href = "/index.html";
                }, 5000);
            }
        });
    });
});