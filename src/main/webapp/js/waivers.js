clicked_button_id="";
var icons = {
    header:"ui-icon-circle-plus",
    activeHeader:"ui-icon-circle-minus"
};

$(".date").datepicker({
    nextText: "Next",
    prevText:"Prev",
    buttonText: "Pick Date",
    showOn: "both",
    navigationAsDateFormat: true,
    buttonImage: "/waivers/js/calendar.gif"
});
// auto complete for owners 
$("#entity_name").autocomplete({
    source: APPLICATION_URL + "EntityService?format=json",
    minLength: 3,
		dataType:"json",
    delay: 100,
    select: function( event, ui ) {
        if(ui.item){
            $("#entity_id").val(ui.item.id);						
        }
    }
		})

// accept digits only
// all fields with pin_nubmer class must be numbers
// since some browsers may not honor input type=number
// this is an extra step
$(".pin_number").keypress(function(event) {
		var key = event.charCode || event.keyCode || 0;
		if( !(key == 8                                // backspace
					|| key == 46                              // delete
					|| (key >= 35 && key <= 40)     // arrow keys/home/end
					|| (key >= 48 && key <= 57)     // numbers on keyboard
					)   
      ){
        event.preventDefault();     // Prevent character input
    }
});
// go to next field
$(".pin_number").keyup(function () {
    if (this.value.length == this.maxLength) {
        $(this).next('.pin_number').focus();
    }
});

$("#pin_id_7").keyup(function(){		
		if(($("#pin_id_1").val().length == 2)
			 && ($("#pin_id_2").val().length == 2)
			 && ($("#pin_id_3").val().length == 2)
			 && ($("#pin_id_4").val().length == 3)
			 && ($("#pin_id_5").val().length == 3)
			 && ($("#pin_id_6").val().length == 3)
			 && ($("#pin_id_7").val().length == 3)
			){
				var xx = $("#pin_id_1").val()+"-"+$("#pin_id_2").val()+"-"+$("#pin_id_3").val()+"-"+$("#pin_id_4").val()+"-"+$("#pin_id_5").val()+"."+$("#pin_id_6").val()+"-"+$(this).val();
				var xx2 = $("#pin_id_multiple").val();
				if(xx2.length > 0){
						xx2 += " ";
				}
				xx2 += xx;
				$("#pin_id_multiple").val(xx2);
				$(".pin_number").val(""); // reset to empty
		}
});
//
// accept digits only
//
$(".tax_number").keypress(function(event) {
		var key = event.charCode || event.keyCode || 0;
		if( !(key == 8                                // backspace
					|| key == 46                              // delete
					|| (key >= 35 && key <= 40)     // arrow keys/home/end
					|| (key >= 48 && key <= 57)     // numbers on keyboard
					)   
      ){
        event.preventDefault();     // Prevent character input
    }
});
// go to next field
$(".tax_number").keyup(function () {
    if (this.value.length == this.maxLength) {
        $(this).next('.tax_number').focus();
    }
});
$("#tax_id_3").keyup(function(){		
		if(($("#tax_id_1").val().length == 3)
			 && ($("#tax_id_2").val().length == 5)
			 && ($("#tax_id_3").val().length == 2)){
				var xx = $("#tax_id_1").val()+"-"+$("#tax_id_2").val()+"-"+$("#tax_id_3").val();
				var xx2 = $("#tax_id_multiple").val();
				if(xx2.length > 0){
						xx2 += " ";
				}
				xx2 += xx;
				$("#tax_id_multiple").val(xx2);
				$(".tax_number").val("");
		}
});

$("#selection_id").change(function() {
		$("#action2").val("refresh");
    $("#form_id").submit();
});

$("#a_disabled").attr('disabled','disabled');
$(document).on('click', 'a', function(e) {
    if ($(this).attr('disabled') == 'disabled') {
        e.preventDefault();
    }
});
//		.data('ui-autocomplete')._renderItem = function (ul, item) {
//        return $('<li>')
//						.attr("data-value",item.value)
//            .appendTo(ul);
//    };

jQuery(function ($) {
    var launcherClick = function(e)  {
            var openMenus   = $('.menuLinks.open'),
                menu        = $(e.target).siblings('.menuLinks');
            openMenus.removeClass('open');
            setTimeout(function() { openMenus.addClass('closed'); }, 300);

            menu.removeClass('closed');
            menu.   addClass('open');
            e.stopPropagation();
        },
        documentClick = function(e) {
            var openMenus   = $('.menuLinks.open');

            openMenus.removeClass('open');
            setTimeout(function() { openMenus.addClass('closed'); }, 300);
        };
    $('.menuLauncher').click(launcherClick);
    $(document       ).click(documentClick);
});
$(document).on("click","button", function (event) {
	clicked_button_id = event.target.id;
});

function doRefresh(){
		document.getElementById("action2").value="Refresh";		
		document.getElementById("form_id").submit();				
}
function checkTypeSelection(){
		var elm = document.getElementById("attach_type_id");
		var val = elm.options[elm.selectedIndex].value;
		if(val == "-1"){
				alert("You need to select document type");
				elm.focus();
				return false;
		}
		return true;
}
function verifyDelete(){
		return confirm("Are you sure you want to delete");
}
$('#show_info_button').click(function() {
    $('#show_info').hide();
    $('#hide_info').show();
		return false;
});
$('#hide_info_button').click(function() {
    $('#show_info').show();
    $('#hide_info').hide();
		return false;
});
function windowOpener(url, name, args) {
    if(typeof(popupWin) != "object" || popupWin.closed)  { 
        popupWin =  window.open(url, name, args); 
    } 
    else{ 
        popupWin.location.href = url; 
    }
		setTimeout(function(){popupWin.focus();},1000);
 }
