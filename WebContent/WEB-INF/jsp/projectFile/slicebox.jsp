<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh-cn">
    <head>
        <title>图片查看器</title>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="Slicebox - 3D Image Slider with Fallback" />
        <meta name="keywords" content="jquery, css3, 3d, webkit, fallback, slider, css3, 3d transforms, slices, rotate, box, automatic" />
        <meta name="author" content="Pedro Botelho for Codrops" />

        <link rel="stylesheet" type="text/css" href="${ctx}/js/slice-box/css/demo.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/slice-box/css/slicebox.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/slice-box/css/custom.css" />
		<style>
		.top-banner {
			background-color: rgba(255, 255, 255, 0.55);
		}
		.top-banner a {
			color: #019135;
		}
		h1 {
			margin-top: 100px;
			font-family: 'Microsoft Yahei';
			font-size: 36px;
			color: #019157;
		}
		</style>
		<script type="text/javascript" src="${ctx}/js/slice-box/js/modernizr.custom.46884.js"></script>
		<script type="text/javascript" src="${ctx}/common/extend.js"></script>
	</head>
	<body>
		<div class="container">
			<div class="wrapper">

				<ul id="sb-slider" class="sb-slider">
				</ul>

				<div id="shadow" class="shadow"></div>

				<div id="nav-arrows" class="nav-arrows">
					<a href="#" title="下一张">Next</a>
					<a href="#" title="上一张">Previous</a>
				</div>

				<div id="nav-dots" class="nav-dots">
				</div>

			</div><!-- /wrapper -->

		<div class="footer-banner" style="width:728px; margin:30px auto"></div>
		</div>
		<script src="${ctx}/book/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/js/slice-box/js/jquery.slicebox.js"></script>
		<script type="text/javascript">
			$(function() {
                /**
                 *   得到URL中的值
                 * @param name
                 * @returns {null}
                 */
                function getQueryString(name)
                {
                    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
                    var r = window.location.search.substr(1).match(reg);
                    if(r!=null)return  unescape(r[2]); return null;
                }
                var imgCode=getQueryString("showImgCode");
                var _li="<li>{0}</li>";
                // 默认展现的第一个
                var _span0='<span class="nav-dot-current"></span>';
                // 默认展现第二个
                var _span='<span></span>';
                var _liContent="";
                var _spanContent="";
                var imgs=window.opener.document.getElementsByClassName("showImg"+imgCode);

                for(var i=0,len=imgs.length;i<len;i++){
                    _liContent=_liContent+ZHFX.fs(_li,imgs[i].innerHTML);
                    // 显示点
                    _spanContent=_spanContent+(i==0?_span0:_span);
                }

                // 添加对象内容框中
                $("#sb-slider").html(_liContent);
                $("#nav-dots").html(_spanContent);
				var Page = (function() {

					var $navArrows = $( '#nav-arrows' ).hide(),
						$navDots = $( '#nav-dots' ).hide(),
						$nav = $navDots.children( 'span' ),
						$shadow = $( '#shadow' ).hide(),
						slicebox = $( '#sb-slider' ).slicebox( {
                            cuboidsCount:1,
                            orientation:'h',
							onReady : function() {

								$navArrows.show();
								$navDots.show();
								$shadow.show();

							},
							onBeforeChange : function( pos ) {

								$nav.removeClass( 'nav-dot-current' );
								$nav.eq( pos ).addClass( 'nav-dot-current' );

							}
						} ),

						init = function() {

							initEvents();

						},
						initEvents = function() {

							// add navigation events
							$navArrows.children( ':first' ).on( 'click', function() {
								slicebox.next();
								return false;

							} );

							$navArrows.children( ':last' ).on( 'click', function() {

								slicebox.previous();
								return false;

							} );

							$nav.each( function( i ) {

								$( this ).on( 'click', function( event ) {

									var $dot = $( this );

									if( !slicebox.isActive() ) {

										$nav.removeClass( 'nav-dot-current' );
										$dot.addClass( 'nav-dot-current' );

									}

									slicebox.jump( i + 1 );
									return false;

								} );

							} );

						};

						return { init : init };
				})();

				// 初始书图片
				Page.init();
                $( '#sb-slider li img').height(550);
                $( '#sb-slider li img').width(750);

			});
		</script>

	</body>
</html>
