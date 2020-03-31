<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- add css -->
    <link type="text/css" rel="stylesheet" href="${ctx}/book/css/style.css">
	<link type="text/css" rel="stylesheet" href="${ctx}/book/css/fonts.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/global.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/themes/css/layout.css" />
    
    <!-- add js -->
    <script src="${ctx}/book/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/js/base.js"></script>
    <script src="${ctx}/book/js/turn.js"></script>
    <script src="${ctx}/book/js/jquery.fullscreen.js"></script>
    <script src="${ctx}/book/js/jquery.address-1.6.min.js"></script>
    <%--正式编写工程的时候，这个样式去掉--%>
	<script src="${ctx}/book/js/onload_fail.js"></script>

	<style>
        html, body {
            margin: 0;
            padding: 0;
    		overflow:auto !important;
        }
    </style>
</head>
<body>
<jsp:include page="./../common/toolbar.jsp"></jsp:include>
<!-- BEGIN FLIPBOOK STRUCTURE -->
<div data-template="true" data-cat="book7" id="fb7-ajax">

    <!-- BEGIN HTML BOOK -->
    <div data-current="book7" class="fb7" id="fb7">
    
        <!-- preloader -->
        <div class="fb7-preloader">
            <div id="wBall_1" class="wBall">
                <div class="wInnerBall">
                </div>
            </div>
            <div id="wBall_2" class="wBall">
                <div class="wInnerBall">
                </div>
            </div>
            <div id="wBall_3" class="wBall">
                <div class="wInnerBall">
                </div>
            </div>
            <div id="wBall_4" class="wBall">
                <div class="wInnerBall">
                </div>
            </div>
            <div id="wBall_5" class="wBall">
                <div class="wInnerBall">
                </div>
            </div>
        </div>
    
        <!-- background for book -->
        <div class="fb7-bcg-book"></div>
      
        <!-- BEGIN CONTAINER BOOK -->
        <div id="fb7-container-book" style="background-image:url(${ctx}/book/images/zhfx_0-1.jpg)" class="fb7-double1 fb7-first fb7-noshadow" >
     
           <!-- BEGIN deep linking -->
           <section id="fb7-deeplinking">
             <ul>
                 <li data-address="page1" data-page="1"></li>
                 <li data-address="page2-3" data-page="2"></li>
                 <li data-address="page2-3" data-page="3"></li>
                 <li data-address="page4-5" data-page="4"></li>
                 <li data-address="page4-5" data-page="5"></li>
                 <li data-address="end" data-page="6"></li>
             </ul>
           </section>
           <!-- END deep linking -->
            
           <!-- BEGIN ABOUT -->
           <section id="fb7-about">
           </section>
           <!-- END ABOUT -->
            <!-- BEGIN PAGES -->
            <div id="fb7-book">
                
                <!-- BEGIN PAGE 1 -->
                <div style="background-image:url(${ctx}/book/images/zhfx_0-1.jpg)"  class="fb7-double fb7-second fb7-noshadow">
                    
                    <!-- begin container page book -->
                    <div class="fb7-cont-page-book">
                        
                        <!-- description for page -->
                        <div class="fb7-page-book">
                        </div>
                    </div>
                    <!-- end container page book -->
                    
                </div>
                <!-- END PAGE 1 -->
                
                <!-- BEGIN PAGE 2 -->
                <div style="background-image:url(${ctx}/book/images/zhfx_2-3.jpg)"  class="fb7-double fb7-first fb7-noshadow">
                    
                    <!-- begin container page book -->
                    <div class="fb7-cont-page-book">
                        
                        <!-- description for page  -->
                        <div class="fb7-page-book">
                        </div>
                        
                        <!-- begin number page -->
                        <div class="fb7-meta">
                            <span class="fb7-num">2</span>
                        </div>
                        <!-- end number page -->
                        
                    </div>
                    <!-- end container page book -->
                    
                </div>
                <!-- END PAGE 2 -->
                
                <!-- BEGIN PAGE 3 -->
                <div class="fb7-double fb7-second fb7-noshadow" style="background-image:url(${ctx}/book/images/zhfx_2-3.jpg)">
                    <!-- begin container page book -->
                    <div class="fb7-cont-page-book">
                           
                        <!-- description for page-->
                        <div class="fb7-page-book">
                            <div class="book_content book_right" style="top:70px;display:none;">
                                <h1 class="book_title">项目履历</h1>
                                <table class="tableList">
                                    <thead>
                                    <tr>
                                        <th>编制操作</th>
                                        <th>编制时间</th>
                                        <th>编制部门</th>
                                        <th>编制人员</th>
                                        <th>备注</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <th>项目入库</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>项目初审</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>项目纳入滚动计划</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>上报至市级</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>上报至省级</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>省级编制年度计划</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>省级上报年度计划</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>年度计划审核</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>年度计划下达</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <th>年度计划调度</th>
                                            <td>2016/1/23</td>
                                            <td>国家改委</td>
                                            <td></td>
                                            <td>目前调度到<br>第四报告期</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        
                        <!-- begin number page  -->
                        <div class="fb7-meta">
                            <span class="fb7-num">3</span>
                        </div>
                        <!-- end number page  -->
                        
                    </div>
                    <!-- end container page book -->
                
                </div>
                <!-- END PAGE 3 -->
                
                <!-- BEGIN PAGE 4-5 -->
                <div style="background-image:url(${ctx}/book/images/zhfx_4-5.jpg)" class="fb7-double fb7-first fb7-noshadow">
                    
                    <!-- begin container page book -->
                    <div class="fb7-cont-page-book">
                        
                        <!-- description for page -->
                        <div class="fb7-page-book">
                            <div class="book_content book_left" style="top:0px;display:none;">
                                <h1 class="book_title">基本信息</h1>
                                <table class="tableList">
                                    <tbody>
                                        <tr>
                                            <th>重大库编号</th>
                                            <td>2016-06-23-01-01-002146</td>
                                        </tr>
                                        <tr>
                                            <th>项目名称</th>
                                            <td>孙斌年度数据46</td>
                                        </tr>
                                        <tr>
                                            <th>建设性质</th>
                                            <td>新建</td>
                                        </tr>
                                        <tr>
                                            <th>项目（法人）单位</th>
                                            <td>高新技术产业处</td>
                                        </tr>
                                        <tr>
                                            <th>建设地点</th>
                                            <td>2016/1/23</td>
                                        </tr>
                                        <tr>
                                            <th>省级编制年度计划</th>
                                            <td>2016/1/23</td>
                                        </tr>
                                        <tr>
                                            <th>省级上报年度计划</th>
                                            <td>2016/1/23</td>
                                        </tr>
                                        <tr>
                                            <th>年度计划审核</th>
                                            <td>2016/1/23</td>
                                        </tr>
                                        <tr>
                                            <th>年度计划下达</th>
                                            <td>2016/1/23</td>
                                        </tr>
                                        <tr>
                                            <th>年度计划调度</th>
                                            <td>2016/1/23</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- begin number page  -->
                        <div class="fb7-meta">
                            <span class="fb7-num">4</span>
                        </div>
                    
                    </div>
                    <!-- end container page book -->
                    
                </div>
                     
                <div style="background-image:url(${ctx}/book/images/zhfx_4-5.jpg)" class="fb7-double fb7-second fb7-noshadow">
                    
                    <!-- begin container page book -->
                    <div class="fb7-cont-page-book">
                        
                        <!-- description for page -->
                        <div class="fb7-page-book">
                        </div>
                        <!-- begin number page  -->
                        <div class="fb7-meta">
                            <span class="fb7-num">5</span>
                        </div>
                    </div>
                    <!-- end container page book -->
                    
                </div>
                <!-- END PAGE 4-5 -->
                
                <!-- BEGIN PAGE end -->
                <div style="background-image:url(${ctx}/book/images/zhfx_0-1.jpg)" class="fb7-double fb7-first fb7-noshadow">
                    
                    <!-- begin container page book -->
                    <div class="fb7-cont-page-book">
                        
                        <!-- description for page-->
                        <div class="fb7-page-book">
                        </div>
                              
                    </div>
                    <!-- end container page book -->
                    
                </div>
                <!-- END PAGE end -->
            
            </div>
            <!-- END PAGES -->
            
            <!-- arrows -->
            <a class="fb7-nav-arrow prev"></a>
            <a class="fb7-nav-arrow next"></a>
            
            <!-- shadow -->
            <div class="fb7-shadow"></div>
    
        </div>
        <!-- END CONTAINER BOOK -->
    
        <!-- BEGIN FOOTER -->
        <div id="fb7-footer">
        
            <div class="fb7-bcg-tools"></div>
            <div class="fb7-menu" id="fb7-center">
                <ul>
                
                    <!-- margin left -->
                    <li></li>
                    
                    <!-- icon download -->
                     <!--<li>
                        <a title="Pdf File Or Zip" class="fb7-download" href="img/file.pdf"></a>
                    </li>-->
                    
                    <!-- icon_zoom_in -->
                    <li>
                        <a title="放大" class="fb7-zoom-in"></a>
                    </li>
                    
                    <!-- icon_zoom_out -->
                    <li>
                        <a title="缩小" class="fb7-zoom-out"></a>
                    </li>
                    
                    <!-- icon_zoom_auto -->
                    <li>
                        <a title="字体" class="fb7-zoom-auto"></a>
                    </li>
                    
                    <!-- icon_zoom_original -->
                    <li>
                        <a title="1:1" class="fb7-zoom-original"></a>
                    </li>
                    
                    <!-- icon_allpages -->
                    <!-- <li>
                        <a title="Ä¿Â¼ " class="fb7-show-all"></a>
                    </li> -->
                    
                    <!-- icon_home -->
                    <li>
                        <a title="返回主页" class="fb7-home"></a>
                    </li>
                    
                    <!-- icon fullscreen -->
                    <li>
                        <a title="全屏" class="fb7-fullscreen"></a>
                    </li>
                    
                    <!-- margin right -->
                    <li></li>
                    
                </ul>
            </div>
            
            <div class="fb7-menu" id="fb7-right">
                <ul>
                    <!-- icon page manager -->
                    <li class="fb7-goto">
                        <label for="fb7-page-number" id="fb7-label-page-number"></label>
                        <input type="text" id="fb7-page-number">
                        <button type="button">Go</button>
                    </li>
                </ul>
            </div>
        </div>
        <!-- END FOOTER -->
    
        <!-- BEGIN THUMBS -->
        <div id="fb7-all-pages" class="fb7-overlay">
    
            <section class="fb7-container-pages">
    
                <div id="fb7-menu-holder">
    
                    <ul id="fb7-slider">
                
                        <!-- PAGE 1 - THUMB -->
                        <li class="1">
                            <img alt="" src="${ctx}/book/images/1_.jpg">
                        </li>
                             
                        <!-- PAGE 2 - THUMB -->
                        <li class="2">
                            <img alt="" src="${ctx}/book/images/2_.jpg">
                        </li>
                             
                        <!-- PAGE 3 - THUMB -->
                        <li class="3">
                            <img alt="" src="${ctx}/book/images/3_.jpg">
                        </li>
                             
                        <!-- PAGE 4-5 - THUMB -->
                        <li class="5">
                            <img alt="" src="${ctx}/book/images/4_5_.jpg">
                        </li>
                             
                        <!-- PAGE 6 - THUMB -->
                        <li class="6">
                            <img alt="" src="${ctx}/book/images/6_.jpg">
                        </li>
                             
                        <!-- PAGE 7 - THUMB -->
                        <li class="7">
                            <img alt="" src="${ctx}/book/images/7_.jpg">
                        </li>
                             
                        <!-- PAGE 8 - THUMB -->
                        <li class="8">
                            <img alt="" src="${ctx}/book/images/8_.jpg">
                        </li>
                             
                        <!-- PAGE 9 - THUMB -->
                        <li class="9">
                            <img alt="" src="${ctx}/book/images/9_.jpg">
                        </li>
                             
                        <!-- PAGE 10S - THUMB -->
                        <li class="10">
                            <img alt="" src="${ctx}/book/images/end_.jpg">
                        </li>
          
                    </ul>
            
                </div>
    
            </section>
    
        </div>
        <!-- END THUMBS -->

    </div>
    <!-- END HTML BOOK -->
    
</div>
<!-- END FLIPBOOK STRUCTURE --> 

<!-- CONFIGURATION FLIPBOOK -->    
<script>
jQuery('#fb7-ajax').data('config',
    {
		"page_width":"640",
		"page_height":"920",
		"go_to_page":"Page",
		"gotopage_width":"45",
		"zoom_double_click":"1",
		"zoom_step":"0.06",
		"tooltip_visible":"true",
		"toolbar_visible":"true",
		"deeplinking_enabled":"true",
		"double_click_enabled":"true",
		"rtl":"false"
     })
</script>
</body>
</html>