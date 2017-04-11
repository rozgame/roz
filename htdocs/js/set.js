// JavaScript Document

/*
JavaScript 借用自：
honeybee
http://www.honeybee-cd.com/
該頁面的 JS 檔案：
http://www.honeybee-cd.com/common/js/set.js
版權所有：
© honeybee
*/

$(document).ready(function(){
	
	//Menu
	$('.fade').children().hover(function() {
   	    //要素の兄弟要素全部にフェードエフェクト実行
        $(this).siblings().stop().fadeTo(300, 0.3);
		}, function() {
        $(this).siblings().stop().fadeTo(300, 1);
    });
	
	//order
	$('#order').hover(function() {
        $('#order img').fadeIn();
	}, function() {
		$('#order img').fadeOut()
    });
	
	//Product
	$('.proList').children().hover(function() {
   	    //要素の兄弟要素全部にフェードエフェクト実行
        $(this).siblings().stop().fadeTo(300, 0.2);
		}, function() {
        $(this).siblings().stop().fadeTo(300, 1);
    });
	
	//テーブル色設定
	$(function(){
     $("tr:odd").addClass("odd");
	});
	
	
	//カレンダー
    $(".tip_trigger").hover(function(){
        //class="tip_trigger"内からclass="tip"を探す
        tip = $(this).find('.tip');
        tip.show(); //表示
    }, function() {
        tip.hide(); //非表示   

    //ここからマウスムーブイベント
    }).mousemove(function(e) {
        tip.css({  top: 45, left: -150 });
    });

});