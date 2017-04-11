// JavaScript Document

// slider

$(window).load(function() {
  $('#slider').nivoSlider({
    effect : 'fade', /* エフェクトの種類 */
    animSpeed : 1000, /* エフェクトのスピード */
    pauseTime : 3500, /* 間隔の時間 */
    slices : 15, /* スライスエフェクトの数 */
    directionNav:true, //Next & Prev
	directionNavHide:true, //Only show on hover
	pauseOnHover:true, //Stop animation while hovering
	beforeChange: function(){},
	afterChange: function(){}
    });
});


$(document).ready(function(){
	
	$(function()
	{
	$('.scroll-pane').jScrollPane();
	});

});

