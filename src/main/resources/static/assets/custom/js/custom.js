
/* Hide Loading Box (Preloader) */
function handlePreloader() {
    if($('.preloader').length){
        $('.preloader').delay(500).fadeOut(500);
    }
}

/* When document is loading, do */
$(window).on('load', function() {
    handlePreloader();
});

$(document).ready(() => {
    $(".fade").removeClass("fade");
});
$('.delete_alert').on('click', function() {
    $.confirm({
        title: 'Record will be permenantly deleted !',
        content: 'You wont be able to undo the action.',
        theme: 'material',
        // icon: 'fas fa-exclamation-triangle',
        type: 'red',
        buttons: {
            delete: {
                btnClass: 'btn-danger btn-min-width',
                action: function(){
                    $.alert('Record deleted successfully!');
                }
            },
            cancel: {
                btnClass: 'btn-secondary btn-min-width',
                action: function () { }
            },

        }
});
});


$("#toastr-success").click(function () {
    $.toast({
        heading: 'Success!',
        text: 'Data Save Successfully.',
        position: 'top-right',
        loaderBg: '#5ba035',
        icon: 'success',
        hideAfter: 3000,
        stack: 1
    });
});


// $('[data-toggle="tooltip"]').tooltip(
//     {
//     container:'body'
// }
// );

// $(document).on({mouseover: function () {
//     if($(this).length>0)
//         $(this).tooltip("show");
// },
// mouseout: function () {
//     if($(this).length>0)
//         $(this).tooltip("dispose");
// }
// }, `[data-toggle="tooltip"]`);

$(document).on({
    mouseover: function () {
        if($(this).length>0)
            $(this).popover("show");
    },
    mouseout: function () {
        if($(this).length>0)
            $(this).popover("dispose");
    }
    }, `[data-toggle="popover"]`);

    $(document).ready(function(){
        $('.modal-dialog-centered').draggable({
            handle: ".modal-header"
        });
    
        // $('.modalcentered').draggable({
        //     handle: ".modal-header"
        // });
    });

 $(document).on("click","body",function(){
     if(!$("body").hasClass("menu-hide"))
        $(".menu-toggle").click();
}); 
    