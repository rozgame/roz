$(function() {
	$('#overlay').css('display', 'none');
	$('#bg, #siteWrap').css('display', 'block');
	$('#menuList li').hover(function() {
		$('a', this).addClass('selected');
		if(UserAgentGetSafari()) {
			$('> .subMenuList:not(:animated)', this).fadeIn(500);
		} else {
			$('> .subMenuList:not(:animated)', this).show(300);
		}
	}, function() {
		$('a', this).removeClass('selected');
		if(UserAgentGetSafari()) {
			$('> .subMenuList', this).fadeOut(100);
		} else {
			$('> .subMenuList', this).hide(100);
		}
		$('.charaImg').fadeOut(100);
	});
	$('#characterSubMenu li').hover(function() {
		$('.charaImg:not(:animated)', this).fadeIn(300);
	}, function() {
		$('.charaImg', this).fadeOut(100);
	});
	$('.disabled').click(function() {
		var msg = $(this).attr('title');
		$('#alert').append('<p>' + msg + '</p>').fadeIn(300).delay(500).fadeOut(300, function() {
			$('#alert p').remove();
		});
		return false;
	});
	$('#bannerLink li a').mouseover(function() {
		$(this).not(':animated').fadeTo(200, 0.5);
	}).mouseout(function() {
		$(this).fadeTo(100, 1);
	});
	$('.pageTopLink a').click(function() {
		$(window).scrollTo('#siteWrap', 500);
		return false;
	});
	if (navigator.userAgent.indexOf("MSIE") != -1) {
		$('h1 a img, .charaImg img').each(function() {
			if ($(this).attr('src').indexOf('.png') != -1) {
				$(this).css({
					'filter': 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' +
					$(this).attr('src') +
					'", sizingMethod="scale");'
				});
			}
		});
	}
});
$(window).scroll(function() {
	var scrollTop  = document.body.scrollTop || document.documentElement.scrollTop || document.scrollY || 0;
	if (scrollTop > 200) {
		$('.pageTopLink').fadeIn(300);
	}
	if (scrollTop < 200) {
		$('.pageTopLink').fadeOut(300);
	}
});
function UserAgentGetSafari() {
	if (navigator.userAgent.indexOf('Chrome') != -1) return false;
	if (navigator.userAgent.indexOf('Lunascape') != -1) return false;
	return (navigator.userAgent.indexOf('Safari') != -1);
}