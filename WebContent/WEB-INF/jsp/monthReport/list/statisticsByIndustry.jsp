<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<div class="book_page" id="industry_book_page">
		<div class="book_content">
			<h1 class="book_title" ><a name="LV2"></a><label id="statistics_time1" class="book_title"></label>审批、核准、备案项目分行业情况统计表</h1>
			<table class="table_list td_right" id="statistics_industry_table">
						<thead>
							<tr>
								<th width='200'>行业</th>
								<th width='90'>项目个数</th>
								<th width='90'>项目个数<br>同比</th>
								<th width='90'>总投资<br>(亿元)</th>
								<th width='90'>总投资<br>同比</th>
								<th width='90'>项目个数<br>占比</th>
								<th width='90'>总投资<br>占比</th> 
							</tr>
						</thead>

			</table>
		</div>
	</div>
	
	
	<script  type="text/html" id="_pageTemplate">
<div class="book_page">
		<div class="book_content">
			<table class="table_list td_right" id="statistics_industry_table_{0)}">
						<thead>
							<tr>
								<th width='200'>行业</th>
								<th width='90'>项目个数</th>
								<th width='90'>项目个数<br>同比</th>
								<th width='90'>总投资<br>(亿元)</th>
								<th width='90'>总投资<br>同比</th>
								<th width='90'>项目个数<br>占比</th>
								<th width='90'>总投资<br>占比</th> 
							</tr>
                                      {1}
						</thead>

			</table>
		</div>
	</div>
	</script>