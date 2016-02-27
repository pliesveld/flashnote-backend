
$( "form" ).submit(function( event ) {
    console.log( $( this ).serializeArray() );
    event.preventDefault();
});

$(document).ready(function() {console.log("document ready")})

function func_print_form()
{
    var formEle = $("#form_login").serializeArray();
    var formData = JSON.stringify(formEle);
    console.log( formData );
    alert(formData);

    $.ajax({
        type: "POST",
        url: "serverUrl",
        data: formData,
        success: function(){},
        dataType: "json",
        contentType : "application/json"
    });
}