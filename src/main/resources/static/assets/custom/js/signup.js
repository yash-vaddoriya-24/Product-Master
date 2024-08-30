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

    $("#confirmPass").parent().after("<div id='confirmMessage' class='alert'></div>");

    // Hide the message initially
    $("#confirmMessage").hide();
    $("#mailId").on("input", function () {
        let mailId = $(this).val();
        $(this).val(mailId.toLowerCase());
    });

    $("#confirmPass").on("input", function(){
        let password = $("#password").val();
        let confirmPassword = $("#confirmPass").val();

        if(confirmPassword.length === 0){
            $("#confirmMessage").hide();
        }
        else {
            if (confirmPassword !== password) {
                $("#confirmMessage").text("Password do not match");
                $("#confirmMessage").css("color", "red").show();
            } else {
                $("#confirmMessage").text("Password match!!");
                $("#confirmMessage").css("color", "green").show();
            }
        }
    });

    function validateEmail(email) {
        let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(email);
    }

    $(document).on( "click",".btn.btn-primary.btn-block", function(){
        let uname = $("#uname").val();
        let email = $("#mailId").val();
        let password = $("#password").val();
        let confirmPassword = $("#confirmPass").val();
        if(password !== confirmPassword){
            toastr.error("Password and confirmPassword do not match");
            return;
        }
        if(uname === "" || email === "" || password === "" || confirmPassword === ""){
            toastr.error("All Field required")
            return;
        }
        let userDetails= {
            uname:uname,
            email:email,
            password:password
        }

        console.log(JSON.stringify(userDetails));
        if(validateEmail(email)) {
            $.ajax({
                url: "signup/addUser",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(userDetails),
                success: function (response) {
                    console.log(response);
                    toastr.success(response);
                    setTimeout(function () {
                        window.location.replace("/index.html");
                    }, 5000);
                },
                error: function (xhr) {
                    console.log(xhr.responseText);
                    toastr.error(xhr.responseText);
                    setTimeout(function () {
                        window.location.replace("/index.html");
                    }, 5000);
                }
            })
        }else{
            toastr.error("Email must be in proper format");
            return;
        }
    })
})