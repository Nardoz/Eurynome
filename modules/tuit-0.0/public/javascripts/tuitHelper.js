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
});

$(window).resize(function() {
	resize();
});

function resize() {
	$('.tweets ul').height($(window).height() - 200);
}
