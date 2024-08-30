$("#nameMessage").css("display", "none");
$("#descMessage").css("display", "none");
$(document).ready(function () {
    $.ajax({
        url: "/products/subcategory",
        type: "POST",
        success: function (response) {
            console.log(response);
            response.forEach(function (item) {
                $(".selectpicker.form-control.typecombo").prepend("<option value='" + item + "'>" + item + "</option>");
            });
            $('.selectpicker.form-control.typecombo').selectpicker('refresh');
        },
        error: function (error) {
            console.log(error);
        }

    })

    drawDataTable();
    resetForm();
    $(".addmodal").modal('show');

    $(document).on("click", ".EditBtn", function() {
        $("#updateBtn").css("display", "inline");
        $("#saveBtn").css("display", "none");
        $(".form-check.float-left").css("display", "inline");
        let row = $(this).closest("tr");
        console.log(row);

        // Set the value of the selectpicker
        $(".selectpicker.form-control.typecombo").val(row.find('td:eq(3)').text().trim()).selectpicker('refresh');
        $("#subcategory").val(row.find('td:eq(4)').text());
        $("#cat_desc").val(row.find('td:eq(5)').text());
        $("#active").prop("checked", true);

        $("#updateBtn").data('row', row);
    });


    $(document).on("click", "#updateBtn", function (event) {
        event.preventDefault();

        let row = $("#updateBtn").data('row');
        let cat_Id = row.find('td:eq(2)').text();
        if(cat_Id != null && $("#subcategory").val() != null && $("#cat_desc").val().length >= 20) {
            $.ajax({
                url: "/products/subcategory/update",
                method: "PUT",
                data: {
                    active: $("#active").is(":checked") ? 'true' : 'false',
                    subCategoryId: cat_Id,
                    categoryName: $('.selectpicker.form-control.typecombo').find(":selected").text(),
                    subCatName: $("#subcategory").val(),
                    subCatDesc: $("#cat_desc").val()
                },
                success: function (response) {
                    drawDataTable();
                    toastr.success("SubCategory updated successfully.");
                    $(".addmodal").modal('hide');
                    resetForm();
                },
                error: function (error) {
                    toastr.error("subCategory already exists.");
                }
            })
        }
    })
    $(document).on("click", ".delete_alert", function () {
        let row = $(this).closest('tr');
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
                            url: "/products/subcategory/delete",
                            method: "PUT",
                            data:{subcategoryId : row.find('td:eq(2)').text()},
                            success: function (response) {
                                drawDataTable();
                                console.log(response);
                                toastr.success(response);
                            },
                            error : function (error) {
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


    $("#closeBtn").on("click", function () {
        $(".addmodal").modal('hide');
        resetForm();
    });

    $(".close").on("click", function (){
        $(".addmodal").modal('hide');
        resetForm();
    });

    $("#subcategory").on("input", function () {
        if($(this).val().length > 0){
            $("#nameMessage").css("display", "inline");
        }
    });

    $("#cat_desc").on("input", function () {
        if($(this).val().length > 0 && $(this).val().length <= 20){
            $("#descMessage").css("display", "inline");
        }
        else{
            $("#descMessage").css("display", "none");
        }
    });

    $(".btn.btn-primary").on("click", function () {
        $("#updateBtn").css("display", "none");
        $("#saveBtn").css("display", "inline");
    })
})

function checkInputDesc(){
    return $("#cat_desc").val().length <= 20 && $("cat_desc").val() !== " ";
}

function resetForm() {
    // Reset the form
    document.getElementById("subCategoryForm").reset();

    // Reset the selectpicker to its default value and refresh it
    $(".selectpicker.form-control.typecombo").val('').selectpicker("refresh");
    $(".form-check.float-left").css("display", "none");
    // Hide the messages
    $("#nameMessage").css("display", "none");
    $("#descMessage").css("display", "none");
}
function validInfo(){
    let subcategory_name = $("#subcategory").val().trim();
    let subcategoryDescription = $("#cat_desc").val().trim();
    let categoryName = $(".selectpicker.form-control.typecombo").val();
    console.log(categoryName);
    if(categoryName === ""){
        document.getElementById("error-message").textContent = "Please select a Category";
        return false;
    }
    else{
        document.getElementById("error-message").textContent = "";
    }

    let isValid = true;
    console.log(subcategory_name);
    if(!subcategory_name.match(/\S/) || !subcategoryDescription.match(/\S/)){
        isValid = false;
        resetForm();
        toastr.error("Enter valid name or description");
    }

    if(checkInputDesc()){
        isValid = false;
    }
    const pattern = /[!@#$%^&*(),.?":{}|<>]/;
    if (subcategory_name.match(pattern)) {
        isValid = false;
    }
    return isValid;
}

function drawDataTable(){
    const table = $('#subcategory_table').DataTable({
        destroy: true,
        scrollResize: true,
        scrollX: true,
        // scrollY: 100,
        scrollCollapse: true,
        paging: true,
        // lengthChange: false,
        // scrollX: true,
        "bAutoWidth": true,
        // paging: true,
        "bLengthChange": true,
        // fixedColumns: true,
        "columnDefs": [{
            "targets": [0],
            "orderable": false,
            // "width": "2%",
        }],
        "pageLength": 15,

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
    $.ajax({
        url: "/api/subcategories",
        method: "GET",
        success: function (data) {
            console.log(data)
            table.clear();
            data.forEach(category => {
                table.row.add([
                    `<td class="text-center">
              <span data-toggle="modal" data-target=".addmodal">
                <a class="EditBtn" data-toggle="tooltip" data-placement="bottom" data-original-title="Edit"
                   href="javascript:void(0);">
                  <i class="fas fa-edit m-r-5 text-success"></i>
                </a>
              </span>

                    <a class="delete_alert" data-toggle="tooltip" data-placement="bottom" data-original-title="Delete"
                       href="javascript:void(0);">
                        <i class="far fa-trash-alt  text-danger"></i>
                    </a>
                </td>`,
                    `<td class="">
                        <span class="badge ${category.active ? 'badge-success' : 'badge-danger'}">${category.active ? 'Yes' : 'No'}</span>
                    </td>`,
                    category.subcategoryId,
                    category.categoryName,
                    category.subcategoryName,
                    category.subcategoryDescription
                ]).draw();
            })
        },
        error: function (error) {
            console.error('Error fetching categories:', error);
        }
    })
}
$(".btn.btn-success.m-r-5").eq(0).on("click", function () {
    let table = document.getElementById("subcategory_table");

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
        let actualData = JSON.stringify({header: headerData,data: bodyData});
        console.log(actualData);
        $.ajax({
            url:"/api/makeExcel",
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
    } else {
        console.error("Table with ID 'product_table' not found.");
    }
});