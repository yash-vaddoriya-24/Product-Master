$(document).ready(function () {
    $("#description").on("input", validDesc);
    $("#discount").on("input", validDiscount);
    $(".selectpicker.form-control.typecombo").eq(0).on("input", validCategory);
    $(".selectpicker.form-control.typecombo").eq(1).on("input", validSubCategory);
    $("#price").on("input", validPrice);
    $("input[type='radio'][name='optradio']").on("change",  validRadioButton);
    $("input[type='checkbox'][name='option1']").on("change",  validCheckbox);
    showTableDetails();
    $.ajax({
        url: "/api/categories/product",
        method: "GET",
        success: function (data) {
            $(".selectpicker.form-control.typecombo").eq(0).attr("data-size", data.length);
            data.forEach(function (product) {
                $(".selectpicker.form-control.typecombo").eq(0).prepend("<option value='" + product + "'>" + product + "</option>");
            });
            $('.selectpicker.form-control.typecombo').eq(0).selectpicker('refresh');
        },
        error: function (error) {
            console.log(error);
        }
    });

    $("#closeBtn").on("click", function () {
        $(".addmodal").modal('hide');
        resetForm();
    })

    $('.selectpicker.form-control.typecombo').eq(0).on("change", function () {
        let selectedCat = $(this).val(); // Get the selected category value

        // Refresh the second selectpicker
        $('.selectpicker.form-control.typecombo').eq(1).selectpicker('refresh');

        // Call getSubCategories function with selected category value
        getSubCategories(selectedCat);
    });

    function getSubCategories(catName, callback) {
        $.ajax({
            url: "/api/subcategories/product",
            method: "GET",
            data: { catName: catName },
            success: function (response) {
                $(".selectpicker.form-control.typecombo").eq(1).attr("data-size", response.length);

                let optionsHtml = '';
                response.forEach(function (item) {
                    optionsHtml += '<option value="' + item + '">' + item + '</option>';
                });

                $('.selectpicker.form-control.typecombo').eq(1).html(optionsHtml);
                $('.selectpicker.form-control.typecombo').eq(1).selectpicker('refresh');
                if (callback && typeof callback === "function") {
                    callback();
                }
            },
            error: function (error) {
                console.log("Error fetching subcategories:", error);
            }
        });
    }
    function validDesc() {
        const desc = $("#description").val();
        if (desc.length < 20) {
            $("#descMessage").text("Description must contain at least 20 characters!");
            $("#descMessage").show();
            return false;
        } else {
            $("#descMessage").text("");
            $("#descMessage").hide();
            return true;
        }
    }
    function validCategory() {
        const category = $(".selectpicker.form-control.typecombo").eq(0).val();
        if (category === "") {
            $("#categoryMessage").text("Please select Category");
            $("#categoryMessage").show();
            return false;
        } else {
            $("#categoryMessage").text("");
            $("#categoryMessage").hide();
            return true;
        }
    }

    function validSubCategory() {
        const subCategory = $(".selectpicker.form-control.typecombo").eq(1).val();
        if (subCategory === "") {
            $("#subCategoryMessage").text("Please select SubCategory");
            $("#subCategoryMessage").show();
            return false;
        } else {
            $("#subCategoryMessage").text("");
            $("#subCategoryMessage").hide();
            return true;
        }
    }

    function validDiscount() {
        const discount = $("#discount").val();
        const regex = /^(100|[1-9]?[0-9])%$/;

        if (!regex.test(discount)) {
            $("#disMessage").text("Discount must be in proper format with % sign (e.g., 50%)");
            $("#disMessage").show();
            return false;
        } else {
            $("#disMessage").text("");
            $("#disMessage").hide();
            return true;
        }
    }
    function validPrice() {
        const price = $("#price").val();
        if (isNaN(price) || price <= 0) {
            $("#priceMessage").text("Price must be a positive number!");
            $("#priceMessage").show();
            return false;
        } else {
            $("#priceMessage").text("");
            $("#priceMessage").hide();
            return true;
        }
    }
    function validRadioButton() {
        if ($(`input[type="radio"][name="optradio"]:checked`).length === 0) {
            $("#radioMessage").text("Please select an option!");
            $("#radioMessage").show();
            return false;
        } else {
            $("#radioMessage").text("");
            $("#radioMessage").hide();
            return true;
        }
    }

    function validCheckbox() {
        if ($(`input[type="checkbox"][name="option1"]:checked`).length === 0) {
            $("#checkboxMessage").text("Please select at least one option!");
            $("#checkboxMessage").show();
            return false;
        } else {
            $("#checkboxMessage").text("");
            $("#checkboxMessage").hide();
            return true;
        }
    }


    function validateInfo(productDetails) {
        let validForm = true; // Assume form is initially valid

        // Check name
        if (productDetails.name.length === 0) {
            validForm = false;
        } else {
            const pattern = /[!@#$%^&*(),.?":{}|<>]/;
            if (productDetails.name.match(pattern)) {
                validForm = false;
            }
        }
        console.log(validForm);

        // Check description
        if (!validDesc()) {
            validForm = false;
        }
        console.log(validForm);

        // Check discount
        if (!validDiscount()) {
            validForm = false;
        }
        console.log(validForm);

        // Check category
        if (!validCategory()) {
            validForm = false;
        }
        console.log(validForm);

        // Check subcategory
        if (!validSubCategory()) {
            validForm = false;
        }
        console.log(validForm);

        // Check price
        if (!validPrice()) {
            validForm = false;
        }
        // Check other fields
        if (!validRadioButton()) {
            validForm = false;
        }
        console.log(validForm);

        // Check checkboxes (example group name: "features")
        if (!validCheckbox()) {
            validForm = false;
        }
        console.log(validForm);

        // Check other fields
        if (productDetails.warranty === "" ||
            productDetails.img === "" ||
            productDetails.manuDate === "" ||
            productDetails.serialNo === "" ||
            productDetails.validFrom === "" ||
            productDetails.validTo === "" ||
            productDetails.color === "") {
            validForm = false;
        }
        console.log(validForm);
        return validForm;
    }
    $(".close").on("click", function (){
        resetForm();
    })

    $("#saveBtn").on("click", function () {
        let productName = $("#pname").val().trim();

        let description = $("#description").val().trim();
        let category = $(".selectpicker.form-control.typecombo").eq(0).val();
        let subCategory = $(".selectpicker.form-control.typecombo").eq(1).val();
        let price = $("#price").val();
        let startDate = $("#start_date").val(); // Fixed selector
        let serialNo = $("#pserialNo").val();
        let warrantySupport = $("#warranty").val();
        let image = $("#img").val();
        let imgArr = image.split("\\");
        let FileName = imgArr.pop();
        console.log(FileName);
        let condition = $("input[name='optradio']:checked").val(); // Get value of checked radio button
        let colors = [];
        $("input[name='option1']:checked").each(function () {
            colors.push($(this).val());
        }); // Get values of checked checkboxes
        colors = colors.join();
        let discount = $("#discount").val();
        let validFrom = $("#validfrom_date").val();
        let validTo = $("#validto_date").val();

        if(!productName.match(/\S/) || !description.match(/\S/)){
            toastr.error("Please enter valid name or description");
            return;
        }
        if (productName === "" && description === "" && category === "" && price === "" && startDate === "" && serialNo === "" && warrantySupport === "" && FileName === "" && condition === "" && colors === "" && validFrom === "" && validTo === "" && discount === "") {
            toastr.error("All field required")
            return;
        }
        console.log("active");
        let isChecked = $("#active").prop("checked");
        let isActive = isChecked ? "true" : "false";
        const productDetails = {
            id: $("#productId").val(),
            name: productName,
            description: description,
            catName: category,
            subCatName: subCategory,
            price: price,
            manuDate: startDate,
            serialNo: serialNo,
            warranty: warrantySupport,
            img: FileName,
            condition: condition,
            color: colors, // Changed key to 'colors' to indicate multiple values
            discount: discount,
            validFrom: validFrom,
            validTo: validTo,
            active:isActive
        };
        console.log(productDetails);
        if (validateInfo(productDetails)) {
            $.ajax({
                url: "/api/addProducts",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(productDetails),
                success: function (response) {
                    toastr.success(response);
                    showTableDetails();
                    resetForm();
                    $(".addmodal").modal('hide');
                },
                error: function (xhr) {
                    toastr.error(xhr.responseText);
                    resetForm();
                }
            });
        } else {
            toastr.error("All field required");
        }
    });
    $(document).on("click", ".delete_alert", function () {
        let row = $(this).closest("tr");
        let productId = row.find('td:eq(3)').text();
        console.log(productId);
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
                        $.ajax({
                            url: "/api/deleteProduct",
                            method: "POST",
                            data: {productId: productId},
                            success: function (response) {
                                toastr.success(response);
                                showTableDetails();
                            },
                            error: function (xhr) {
                                toastr.error(xhr.responseText);
                            }
                        })
                    }
                },
                cancel: {
                    btnClass: 'btn-secondary btn-min-width',
                    action: function () {
                    }
                },
            }
        });
    });

    $(document).on("click", "#Editbtn", function () {
        // Show the form elements (assuming these are checkboxes with class 'form-check float-left')
        $(".form-check.float-left").css("display", "inline");

        // Find the closest <tr> element relative to the clicked button
        let row = $(this).closest("tr");
        console.log(row);

        // Retrieve the productId from the <td> at index 3
        let productId = row.find('td:eq(3)').text().trim();
        console.log(productId);

        // Make an AJAX call to fetch additional details based on productId
        $.ajax({
            url: "/api/getProductId", // Endpoint to fetch product details
            method: "POST",
            data: { productId: productId }, // Send productId as data
            success: function (response) {
                console.log(response);

                // Assuming response is an array with at least one object
                if (response.length > 0) {
                    // Update form fields with fetched data
                    $("#productId").val(response[0].id);
                    $("#pname").val(response[0].name);
                    $("#description").val(response[0].description);
                    // Set the value for selectpicker fields and refresh the selectpicker
                    $(".selectpicker.form-control.typecombo").eq(0).val(response[0].catName).selectpicker('refresh');
                    getSubCategories(response[0].catName, function() {
                        let subCatSelect = $(".selectpicker.form-control.typecombo").eq(1);
                        subCatSelect.val(response[0].subCatName); // Set the value
                        subCatSelect.selectpicker('refresh');
                    });
                    $("#price").val(response[0].price);
                    $("#start_date").val(response[0].manuDate);
                    $("#pserialNo").val(response[0].serialNo);
                    $("#warranty").val(response[0].warranty);
                    // $("#img").val(response[0].img);
                    $("input[name='optradio'][value='" + response[0].condition + "']").prop("checked", true);
                    let colors = response[0].color; // Assuming colors is an array of color names
                    $("input[name='option1']").each(function() {
                        if (colors.includes($(this).val())) {
                            $(this).prop("checked", true);
                        } else {
                            $(this).prop("checked", false);
                        }
                    });
                    $("#discount").val(response[0].discount);
                    $("#validfrom_date").val(response[0].validFrom);
                    $("#validto_date").val(response[0].validTo);
                    let isActive = response[0].active; // Should be true or false

                    $("#active").prop("checked", isActive);

                } else {
                    console.error("Empty response received or no matching product found.");
                    // Handle scenario where no data is returned or handle error as needed
                }
            },
            error: function (xhr, status, error) {
                console.error("Error occurred while fetching product details:", error);
                // Handle error scenario
            }
        });

        // resetForm();
    });

})

function resetForm() {
    $("#productFrom").trigger("reset");
    $('.selectpicker.form-control.typecombo').eq(0).val('').selectpicker('refresh');
    $('.selectpicker.form-control.typecombo').eq(1).val('').selectpicker('refresh');
    $("#productId").val("");
    $(".form-check.float-left").css("display", "none");
}


function showTableDetails() {
    const productTable = $('#product_table').DataTable({
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
        url: "/api/getProducts",
        method: "POST",
        success: function (data) {
            productTable.clear();

            data.forEach(category => {
                productTable.row.add([
                    `<td class="text-center">
                    <span data-toggle="modal" data-target=".addmodal">
                        <a id="Editbtn" class="" data-toggle="tooltip" data-placement="bottom" title="Edit" href="javascript:void(0);">
                            <i class="fas fa-edit m-r-5 text-success"></i>
                        </a>
                    </span>
                    <a class="delete_alert" data-toggle="tooltip" data-placement="bottom" title="Delete">
                        <i class="far fa-trash-alt text-danger"></i>
                    </a>
                </td>`,
                    `<td class="">
                    <span class="badge ${category.active ? 'badge-success' : 'badge-danger'}">${category.active ? 'Yes' : 'No'}</span>
                </td>`,
                    `<td class="text-center">
                    <a href="javascript:void(0)" data-toggle="popover" data-trigger="hover" data-html="true"
                       data-placement="right"
                       data-template='<div class="popover fade bs-popover-right" role="tooltip"><div class="arrow"></div><h3 class="popover-header p-0 border_radius6"></h3></div>'
                       data-title="<img src='/assets/custom/images/products/${category.img}' height='150' class='border_radius6'>">
                        <img src='/assets/custom/images/products/${category.img}' alt="" width="25" height="25">
                    </a>
                </td>`,
                    category.id,
                    category.name,
                    category.description,
                    category.catName,
                    category.price,
                    category.discount
                ]).draw();
            });
        },
        error: function (error) {
            toastr.error("Error occurred while fetching data");
            console.log(error);
        }
    });
}

$(".btn.btn-success.m-r-5").eq(0).on("click", function () {
    let table = document.getElementById("product_table");

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
