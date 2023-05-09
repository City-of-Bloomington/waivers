		</div>
	</main>
<script type="text/javascript" src="<s:property value='#application.url' />js/jquery-3.6.1.min.js"></script>
	<script type="text/javascript" src="<s:property value='#application.url' />js/jquery-ui.min-1.13.2.js"></script>

  <script>
	function log( message ) {
    $( "<div>" ).text( message ).prependTo( "#log" );
    $( "#log" ).scrollTop( 0 );
  }
  $( function() {
    var availableTags = [
      {id:"1",value:"ActionScript",label:"ActionScript"},
      {id:"2",value:"AppleScript",label:"AppleScript"},
      {id:"3",value:"Asp",label:"Asp"},
      {id:"4",value:"BASIC",label:"BASIC"},
      {id:"5",value:"C",label:"C"},
      {id:"6",value:"C++",label:"C++"},
      {id:"7",value:"Clojure",label:"Clojure"},
      {id:"8",value:"COBOL",label:"COBOL"},
      {id:"9",value:"ColdFusion",label:"ColdFusion"}
    ];
        $("#tags_id").autocomplete({
    	  source: availableTags,
	  		dataType:"json",
					cacheLength: 0,			
			select: function( event, ui ) {
        if(ui.item){
					log(ui.item.id);
        }
    }			
    })
	})
    $("#own_tag2").autocomplete({
      source: "OwnerService?format=json",
			select: function( event, ui ) {
        if(ui.item){
					log(ui.item.value);
        }
    }			
    });
	$("#own_tag").autocomplete({
    source: function(request, response){
			$.ajax({
				url: APPLICATION_URL+"OwnerService?",
				dataType:"json",
				cache:false,
				data:{
					term:request.term
				},
				success: function(data, status){
					response(data);
					log('success '+status);
					// $("#own_tag_id").val(ui.item.value);
				},
				error: function(xx, status, strError){
					log('error '+status+' '+strError);
					}
			});
		},
    minLength: 2,
		cacheLength: 0,
		delay: 100,
    select: function( event, ui ) {
        if(ui.item){
					log(val(ui.item.id));
        }
    }
    		});
	
  </script>		
</body>
</html>
