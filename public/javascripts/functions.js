$(function() {	
	$('.sendDM').click(function() {
		var dm = $('#dm');
		
		if(dm.is(':visible')) {
			dm.slideUp(400);
		} else {
			dm.slideDown(400);
		}
	});	
	
	resize();
});

$(window).resize(function() {
	resize();
});

function resize() {
	$('.tweets').height($(window).height() - 200);
}