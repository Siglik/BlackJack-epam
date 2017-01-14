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
