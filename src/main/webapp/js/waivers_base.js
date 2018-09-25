$("#owner_name").autocomplete({
    source: APPLICATION_URL + "OwnerService?format=json",
    minLength: 2,
		dataType:"json",
    delay: 100,
    select: function( event, ui ) {
        if(ui.item){
            $("#owner_id").val(ui.item.id);
        }
    }
		})
/**
		.data('ui-autocomplete')._renderItem = function (ul, item) {
        return $('<li>')
						.attr("data-value",item.value)
            .appendTo(ul);
    };
*/

