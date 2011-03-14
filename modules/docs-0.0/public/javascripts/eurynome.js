$(function() {
    resize();
});

$(window).resize(function() {
	resize();
});

function resize() {
	$('#container').height($(window).height() - 33);    
    $('#content').height($('#container').height() - 100);   
}
