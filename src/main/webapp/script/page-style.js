function sidebarHeight() {
	var browserWidth = $(window).width();
	var containerHeight = $('.main-content').height();

	if (browserWidth < 800) {
		$('.sidebar').removeAttr('style');
	} else {
		$('.sidebar').height(containerHeight);
	}
}

// Run this function
sidebarHeight();

$(function() {
	const $menubutton = $('#show-menu');
	const $nav = $('.left-menu');
	
	$menubutton.on('click', function() {
		$nav.slideToggle();
	});

	// Run this function when page is resized
	$(window).resize(function() {
		$('.sidebar').removeAttr('style');
		sidebarHeight();
		if ($(window).width() > 800) {
			$nav.show();
		}
		if ($(window).width() <= 800) {
			$nav.hide();
		}
	});
});

window.onclick = function(event) {

	if (!event.target.matches('#lang-btn')) {
		var dropdowns = document.getElementsByClassName("dropdown-content");
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
	}
	
	if ($(window).width() <= 800 && !event.target.matches('#show-menu')) {
		$('.left-menu').hide();
	}
}

$("#lang-btn").click(function() {
	document.getElementById("lang-menu").classList.toggle("show");
})