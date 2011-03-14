var socket; 

$(function() {	
    resize();
    
	$('.sendDM').click(function() {
		var dm = $('#dm');
		
		if(dm.is(':visible')) {
			dm.slideUp(400);
		} else {
			dm.slideDown(400);
		}
	});	
    
    socket = new io.Socket(null, { port: 8080, rememberTransport: false });
    socket.connect();
        
    socket.on('message', function(obj) {    
        console.log(obj);
    });
});

$(window).resize(function() {
	resize();
});

function resize() {
	$('.tweets ul').height($(window).height() - 200);
}
