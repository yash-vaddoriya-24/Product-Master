$("#nameMessage").css("display", "none");
$("#descMessage").css("display", "none");
$(".form-check.float-left").css("display", "none");
$(document).ready(function() {
    drawDataTable();
    resetForm();
    $(".addmodal").modal('show');

    $(document).on("click", "#Editbtn", function () {
        // Show the edit button and hide the save button
        $("#savebtn").css("display", "none");
        $("#updateBtn").css("display", "inline");

        $(".form-check.float-left").css("display", "inline");
        // Get the row data
        let row = $(this).closest('tr');
        let cat_Name = row.find('td:eq(3)').text();
        let cat_Desc = row.find('td:eq(4)').text();

        // Populate the form fields with the row data
        $("#category_name").val(cat_Name);
        $("#category_desc").val(cat_Desc);
        $("#active").prop("checked", true);

        // Store the row element in a data attribute for later use
        $("#updateBtn").data('row', row);

    });

    $(document).on("click", "#updateBtn", function (event) {
        event.preventDefault(); // Prevent the default form submission

        let row = $("#updateBtn").data('row');
        let cat_Id = row.find('td:eq(2)').text();
        if(cat_Id != null && $("#category_name").val() != null && $("#category_desc").val().length > 20) {
            $.ajax({
                url: '/api/categories/update',
                method: 'PUT',
                data: {
                    categoryId: cat_Id,
                    active: $("#active").is(":checked") ? 'true' : 'false',
                    cat_N: $("#category_name").val(),
                    cat_D: $("#category_desc").val()
                },
                success: function (response) {
                        row.find('td:eq(1)').html(`<span class="badge ${response.active ? 'badge-success' : 'badge-danger'}">${response.active ? 'Yes' : 'No'}</span>`);
                        row.find('td:eq(3)').text(response.name);
                        row.find('td:eq(4)').text(response.description);
                        toastr.success("Category updated successfully.");
                        resetForm();
                        $(".addmodal").modal('hide'); // Hide the modal

                },
                error: function (error) {
                    toastr.error("category already exists");
                }
            });
        }
        // else{
        //     $("#updateBtn").prop('disabled', true);
        //
        // }
    });

// Function to reset the form and button visibility

// Close button to hide the modal and reset the form
    $("#closebtn").on("click", function () {
        $(".addmodal").modal('hide');
        resetForm();
    });

    $(".close").on("click", function (){
        $(".addmodal").modal('hide');
        resetForm();
    })

    $(document).on('click', '.delete_alert', function () {
        let row = $(this).closest("tr");
        let categoryId = row.find('td:eq(2)').text();
        $.confirm({
            title: 'Record will be permenantly deleted !',
            content: 'You wont be able to undo the action.',
            theme: 'material',
            // icon: 'fas fa-exclamation-triangle',
            type: 'red',
            buttons: {
                delete: {
                    btnClass: 'btn-danger btn-min-width',
                    action: function () {
                        $.alert('Record deleted successfully!');
                        $.ajax({
                            url: '/api/categories/delete',
                            method: 'DELETE',
                            data:{categoryId:categoryId},
                            success: function (response){
                                drawDataTable();
                                console.log("Success:",response);
                                toastr.success(response);
                            },
                            error: function (error){
                                console.log("Error deleting category:" + error.message);
                                toastr.error(error);
                            }
                        });
                    }
                },
                cancel: {
                    btnClass: 'btn-secondary btn-min-width',
                    action: function () {
                    }
                },
            }
        })
    });
    function checkInputName(categoryName) {
        console.log("in check input")
        if (categoryName.length === 0) {
            $("#nameMessage").css("display", "none");
        } else {
            $("#nameMessage").css("display", "inline");
        }
    }

    function checkInputDesc(categoryDesc) {
        console.log("in check input")
        if (categoryDesc.length === 0 || categoryDesc.length > 20) {
            $("#descMessage").css("display", "none");
        }
        else{
            $("#descMessage").css("display", "inline");
        }
    }

    $("#category_name").on("input", function () {
        checkInputName($(this).val());
    });

    $("#category_desc").on("input", function () {
        checkInputDesc($(this).val());
    })

    $("#savebtn").on("click", function (event) {
        event.preventDefault();
        if (validateInfo()) {
            // Logic to handle the save action
            console.log("Form is valid");
        } else {
            console.log("Form is invalid");
        }
    });
});

function validateInfo() {
    let categoryName = document.getElementById("category_name").value.trim();
    let categoryDesc = document.getElementById("category_desc").value.trim();
    let isValid = true;
    // Perform final check and display message if necessary
    if(!categoryName.match(/\S/) && !categoryDesc.match(/\S/)){
        isValid = false;
        resetForm();
        toastr.error("Please enter valid name or description");
    }
    if(categoryDesc.length < 20){
        isValid = false;
    }
    const pattern = /^[A-Za-z ]+$/g;
    if (!categoryName.match(pattern)) {
        isValid = false;
    }
    return isValid;
}
function resetForm() {
    $("#category_name").val('');
    $("#category_desc").val('');
    $("#savebtn").css("display", "inline");
    $("#updateBtn").css("display", "none");
    $("#nameMessage").css("display", "none");
    $("#descMessage").css("display", "none");
    $(".form-check.float-left").css("display", "none");
}
function drawDataTable(){
    const table = $('#category_table').DataTable({
        destroy: true,
        scrollResize: true,
        scrollX: true,
        scrollCollapse: true,
        paging: true,
        bAutoWidth: true,
        bLengthChange: true,
        columnDefs: [{
            targets: [0],
            orderable: false,
        }],
        pageLength: 15,
        fixedColumns: {
            rightColumns: 0,
            leftColumns: 0
        },
        language: {
            paginate: {
                next: '<i class="fa fa-angle-double-right">',
                previous: '<i class="fa fa-angle-double-left">'
            }
        },
        dom:
            "<'row pl-0 pr-1'<'col-xl-9 col-lg-8 col-sm-8'pi><'col-xl-3 col-lg-4 col-sm-4'f>>" +
            "<'row pl-1 pr-1'<'col-xl-12 col-lg-12 col-sm-12'tr>>"
    });

    // Fetch data using AJAX
    $.ajax({
        url: '/api/categories',
        method: 'GET',
        success: function (data) {
            // Clear existing table data
            table.clear();

            // Add new rows to the table
            data.forEach(category => {
                table.row.add([
                    `<td class="text-center">
                        <span data-toggle="modal" data-target=".addmodal">
                            <a id = "Editbtn" class="" data-toggle="tooltip" data-placement="bottom" data-original-title="Edit" href="javascript:void(0);">
                                <i class="fas fa-edit m-r-5 text-success"></i>
                            </a>
                        </span>
                        <a class="delete_alert" data-toggle="tooltip" data-placement="bottom" data-original-title="Delete">
                            <i class="far fa-trash-alt text-danger"></i>
                        </a>
                    </td>`,
                    `<td class="">
                        <span class="badge ${category.active ? 'badge-success' : 'badge-danger'}">${category.active ? 'Yes' : 'No'}</span>
                    </td>`,
                    category.id,
                    category.name,
                    category.description
                ]).draw();

            });
        },
        error: function (error) {
            console.error('Error fetching categories:', error);
        }
    });
}

$(".btn.btn-success.m-r-5").eq(0).on("click", function () {
    let table = document.getElementById("category_table");

    if (table) {
        // Get the header row
        let header = table.querySelector("thead > tr");

        // Get the body rows
        let data = table.querySelectorAll("tbody > tr");

        // Extract and log header data
        let headerData = [];
        if (header) {
            headerData = Array.from(header.cells).map(cell => cell.textContent.trim()).filter(text => text !== 'Action' && text !== 'Image');
        }

        console.log("Headers:", headerData);

        // Log table body data
        let bodyData = [];
        data.forEach(row => {
            let rowData = Array.from(row.cells).map(cell => cell.textContent.trim()).filter(text => text !== '');
            bodyData.push(rowData);
        });
        console.log("Body Data:", bodyData);

        // Log the whole table element for reference
        console.log("Table Element:", table);

        // // let actualHeader = JSON.stringify(headerData);
        // let actualBody = JSON.stringify({ data: bodyData });
        // let headerBody = JSON.stringify({header: headerData});
        let actualData = JSON.stringify({header: headerData, data: bodyData});
        console.log(actualData);

        $.ajax({
            url: "/api/makeExcel",
            method: "POST",
            contentType: "application/json",
            data: actualData,
            success: function (response) {
                toastr.success(response);
                if (response === "Excel file created successfully") {
                    window.location.href = '/api/downloadExcel';
                }
            },
            error: function (xhr) {
                toastr.error(xhr.responseText);
            }
         });

    }else {
        console.error("Table with ID 'product_table' not found.");
    }

});


