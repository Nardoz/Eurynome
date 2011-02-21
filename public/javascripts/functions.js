$(function() {	
	$('.sendDM').click(function() {
		var dm = $('#dm');
		
		if(dm.is(':visible')) {
			dm.slideUp(400);
		} else {
			dm.slideDown(400);
		}
	});	
	
	$('.tweets').height($(window).height() - 200);
	
	$(window).resize(function() {
		$('.tweets').height($(window).height() - 200);
	});
});