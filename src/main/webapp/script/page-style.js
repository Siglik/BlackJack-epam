function sidebarHeight(){
  var browserWidth = $(window).width();
  var containerHeight = $('.main-content').height();

  if( browserWidth < 768 ){
    $('.sidebar').removeAttr('style');
  } else {
    $('.sidebar').height(containerHeight);
  }
}

// Run this function
sidebarHeight();

// Run this function when page is resized
$(window).resize(function(){
  $('.sidebar').removeAttr('style');
  sidebarHeight();
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
}

$("#lang-btn").click(function() {
	document.getElementById("lang-menu").classList.toggle("show");
})