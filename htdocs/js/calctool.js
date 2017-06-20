


function 	requestExamples()
{
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : '/Examples',
		dataType : "json",
		data : JSON.stringify({
			"query" : "*",
			"session" : "12345",
			"language" : "de"
		}),

		dataType : "json",

		success : function(data) {
			
			//alert('data; ' + JSON.stringify(data));
			
			var templateTitle = Handlebars.compile($("#example-title").html());
			var templateCommand = Handlebars.compile($("#example-command").html());

			for(var i = 0;i < data.examples.length;i++)
			{
   			    var example = data.examples[i];
				var title    = $(templateTitle({text : example.title }));
				$(".examples").append(title);
				
				for(var x = 0;x < example.commands.length;x++)
				{
					var cmd    = $(templateCommand({
						text : example.commands[x].description , 
						command: example.commands[x].command }));
					
					$(cmd).find("a").click(function(ev){
						ev.preventDefault();
						sendCommand($(this).text());
					});
					$(".examples").append(cmd);
				}
			}	
			
		},
		error : function(error) {
			alert('error; ' + JSON.stringify(error));
		}
	});
	
}

function sendCommand(command)
{
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : '/Math',
		dataType : "json",
		data : JSON.stringify({
			"command" : command,
			"session" : "12345"
		}),

		/*
		beforeSend : function(xhr) {
			// set header
			xhr.setRequestHeader("Authorization", "BEARER " + bearer);
		},
		*/
		dataType : "json",

		success : function(data) {
			
			//alert('data; ' + JSON.stringify(data));

			
			var templateResult = Handlebars.compile($("#text-result").html());
			
			var l = data.result.length
			for(var i = 0;i < l;i++)
			{
				var text    = $(templateResult({text : data.result[i].text , src: "data:image/jpeg;base64," + data.result[i].image64 }));
				$(".calctool-result").append(text);
			}
			
			//Scroll
			setTimeout(function(){
				$(".calctool-result")[0].scrollTop = $(".calctool-result")[0].scrollHeight;
			},100);
			
		},
		error : function(error) {
			alert('error; ' + JSON.stringify(error));
		}
	});

}


$(document).ready(function() {
	
	
	var input = $(".calctool").find("input");
	
	var button = $(".calctool").find("button");
	
	button.click(function(ev) {
		
		ev.preventDefault();
		sendCommand(input.val());
		input.attr("placeholder","");
		input.val("");
	
	});
	
	input.keypress(function(ev) {
	
		if(ev.which == 13) 
		{
			ev.preventDefault();
			sendCommand(input.val());
			input.attr("placeholder","");
			input.val("");			
		}
	});
	
	requestExamples();
	
});