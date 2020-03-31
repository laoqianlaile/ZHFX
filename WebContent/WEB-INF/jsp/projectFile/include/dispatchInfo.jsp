<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<script type="text/html" id="_dispatchInfoTempl">
		<div class="fb7-double fb7-first fb7-noshadow" id="report{0}">
			<div class="fb7-cont-page-book">
				<div class="fb7-book-nr">
					<h1 class="titleL">{1}</h1>
					<h1 class="titleL">{2}</h1>
					<h1 class="titleL">实施信息</h1>
					<table class="tableList">
						<tbody>
						<tr>
							<th width="120">实际开工时间</th>
							<td>{3}</td>
						</tr>
						<tr>
							<th>实际竣工时间</th>
							<td>{4}</td>
						</tr>
						<tr>
							<th>招投标方式</th>
							<td>{5}</td>
						</tr>
						<tr>
							<th>建设单位</th>
							<td>{6}</td>
						</tr>
						</tbody>
					</table>
					<h1 class="titleL">进度详细信息</h1>
					<table class="tableList">
						<tbody>
						<tr>
							<th width="120">报告期</th>
							<td>{7}</td>
						</tr>
						<tr>
							<th>形象进度</th>
							<td>{8}</td>
						</tr>
						<tr>
							<th>年度建设内容</th>
							<td>{9}</td>
						</tr>
						<tr>
							<th>问题及建议</th>
							<td>{10}</td>
						</tr>
						</tbody>
					</table>
				</div>
				<!-- begin number page  -->
				<div class="fb7-meta">
					<span class="fb7-num">{14}</span>
				</div>
			</div>
			<!-- end container page book -->
		</div>

		<div class="fb7-double fb7-second fb7-noshadow" >
			<!-- begin container page book -->
			<div class="fb7-cont-page-book">
				<!-- description for page-->
				<div class="fb7-book-nr">
					<h1 class="titleL">资金到位情况</h1>
					<div class="ofdiv1">
						<table class="tableList">
							<thead>
							<tr>
								<th width="120">资金类别</th>
								<th>总投资</th>
								<th>截至上一报告期<br>累计到位资金(万元)</th>
								<th>截至本报告期<br>累计到位资金(万元)</th>
							</tr>
							</thead>
							<tbody class="th_bold">
							{11}
							</tbody>
						</table>
					<h1 class="titleR">资金完成情况</h1>
						<table class="tableList">
							<thead>
							<tr>
								<th rowspan="2" width="120">资金类别</th>
								<th colspan="2">累计完成投资(万元)</th>
								<th colspan="2">累计支付情况(万元)</th>
							</tr>
							<tr>
								<th>截至上一报告期</th>
								<th>截至本报告期</th>
								<th>截至上一报告期</th>
								<th>截至本报告期</th>
							</tr>
							</thead>
							<tbody class="th_bold">
							{12}
							</tbody>
						</table>
					</div>
				</div>
				<!-- begin number page  -->
				<div class="fb7-meta">
					<span class="fb7-num">{15}</span>
				</div>
				<!-- end number page  -->
			</div>
			<!-- end container page book -->
		</div>

		<div class="fb7-double fb7-second fb7-noshadow" >
			<!-- begin container page book -->
			<div class="fb7-cont-page-book">
				<!-- description for page-->
				<div class="fb7-book-nr">
					<div class="ofdiv1">
						{13}
					</div>
				</div>
				<!-- begin number page  -->
				<div class="fb7-meta">
					<span class="fb7-num">{16}</span>
				</div>
				<!-- end number page  -->
			</div>
			<!-- end container page book -->
		</div>
	</script>
	
<script  type="text/html" id="_placeTemplate">
       <tr>
              <td>{0}</td>
              <td>{1}</td>
              <td>{2}</td>
              <td>{3}</td>
       </tr>
</script>
<script  type="text/html" id="_finishTemplate">
       <tr>
              <td>{0}</td>
              <td>{1}</td>
              <td>{2}</td>
              <td>{3}</td>
              <td>{4}</td>
       </tr>
</script>
<script  type="text/html" id="_dispatchImageInfo">
	<h1 class="titleL">调度图片/视频</h1>
		<table class="tableList">
			{0}
		</table>
</script>
<script  type="text/html" id="_imgTemplate">
       <tr>
              <td width="33%">{0}</td>
              <td width="33%" >{1}</td>
              <td width="33%">{2}</td>              
       </tr>
</script>	