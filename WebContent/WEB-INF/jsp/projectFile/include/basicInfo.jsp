<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="fb7-double fb7-second fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">
		<!-- description for page-->
		<div class="fb7-book-nr">
			<h1 class="titleR">基本信息</h1>
			<%--<div class="ofdiv1">--%>
				<table class="tableList">
					<tr>
						<th style="width: 200px;">申报日期</th>
						<td id="STORE_TIME"></td>
					</tr>
					<tr>
						<th>审核目录分类</th>
						<td id="appDirTypeCode"></td>
					</tr>
					<tr>
						<th>审核目录</th>
						<td id="appDirCode"></td>
					</tr>
					<tr>
						<th>重大库编号</th>
						<td id="projectCode1"></td>
					</tr>
					<tr>
						<th>审批监管平台代码</th>
						<td id="projectCode2"></td>
					</tr>
					<tr>
						<th>项目名称</th>
						<td id="PRO_NAME"></td>
					</tr>
					<tr>
						<th>项目类型</th>
						<td id="PRO_TYPE"> </td>
					</tr>
					<tr>
						<th>建设性质</th>
						<td id="BUILD_NATURE"></td>
					</tr>
					<tr>
						<th>国别</th>
						<td id="COUNTRY"></td>
					</tr>
					<tr>
						<th>建设地点</th>
						<td id="BUILD_PLACE"></td>
					</tr>
					<tr>
						<th>建设地点详情</th>
						<td id="BUILD_PLACE_DETAIL"></td>
					</tr>
					<tr>
						<th>建设详细地址</th>
						<td id="BUILD_ADDRESS"></td>
					</tr>
					<tr>
						<th>国标行业</th>
						<td id="GB_INDUSTRY"> </td>
					</tr>
			</table>
			<%--</div>--%>
		</div>

		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">8</span>
		</div>
		<!-- end number page  -->
	</div>
	<!-- end container page book -->
</div>
<div class="fb7-double fb7-second fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">

		<!-- description for page-->
		<div class="fb7-book-nr">
			<h1 class="titleR">基本信息</h1>
			<%--<div class="ofdiv1">--%>
				<table class="tableList">
					<tr>
						<th style="width: 200px;">所属行业</th>
						<td id="INDUSTRY"></td>
					</tr>
					<tr>
						<th>总投资（万元）</th>
						<td id="INVESTMENT_TOTAL"></td>
					</tr>
					<tr>
						<th>拟开工年份</th>
						<td id="EXPECT_STARTYEAR"></td>
					</tr>
					<tr>
						<th>拟开工月份</th>
						<td id="EXPECT_NY"></td>
					</tr>
					<tr>
						<th>拟建成年份</th>
						<td id="EXPECT_ENDYEAR"></td>
					</tr>
					<tr>
						<th>主要建设规模</th>
						<td id="MAIN_BUILD_SCALE"></td>
					</tr>
					<tr>
						<th>量化建设规模</th>
						<td>
							<table class="tableList small">
								<thead>
								<tr>
									<th>类别</th>
									<th>数值</th>
								</tr>
								</thead>
								<tbody id="baseNum_table">
								<%--量化建设规模--%>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<th>*（年度）主要建设内容</th>
						<td id="MAIN_BUILD_CONTENT"></td>
					</tr>
					<tr>
						<th>备注</th>
						<td id="REMARK"></td>
					</tr>
					<tr>
						<th>入库依据</th>
						<td ></td>
					</tr>
				</table>
			<%--</div>--%>
		</div>

		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">9</span>
		</div>
		<!-- end number page  -->
	</div>
	<!-- end container page book -->
</div>

<div class="fb7-double fb7-first fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">

		<!-- description for page -->
		<div class="fb7-book-nr">
			<h1 class="titleL">入库依据</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">符合产业政策</th>
					<td id="PROPERTY_POLICY"></td>
				</tr>
				<tr>
					<th>符合规划</th>
					<td id="PLAN"></td>
				</tr>
				<tr>
					<th>符合重大战略</th>
					<td id="MAJOR_STRATEGY"></td>
				</tr>
				<tr>
					<th>符合政府投资方向</th>
					<td id="GOVERNMENT_INVEST_DIRECTION"></td>
				</tr>
				<tr>
					<th>下达单位</th>
					<td id="ORG_ISSUED"></td>
				</tr>
			</table>
			<h1 class="titleL">PPP项目信息</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">是否PPP</th>
					<td id="ISPPP"></td>
				</tr>
				<tr>
					<th>政府参与方式</th>
					<td id="GOVERNMENT_JOIN_TYPE"></td>
				</tr>
				<tr>
					<th>拟采用PPP<br>操作模式</th>
					<td id="EXPECT_PPP_OPERATE_MODE"></td>
				</tr>
			</table>
					</div>
		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">10</span>
		</div>
	</div>
	<!-- end container page book -->
</div>
<div class="fb7-double fb7-first fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">
		<!-- description for page -->
		<div class="fb7-book-nr">
			<h1 class="titleL">专项建设基金信息</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">专项类别</th>
					<td  id="SPECIAL_TYPE"></td>
				</tr>
				<tr>
					<th>回报方式</th>
					<td id="RETURN_METHOD"></td>
				</tr>
				<tr>
					<th>回报率（%）</th>
					<td id="RETURN_RATE"></td>
				</tr>
				<tr>
					<th>回报周期（年）</th>
					<td id="RETURN_PERIOD"></td>
				</tr>
				<tr>
					<th>拉动效果（1:x）</th>
					<td id="PULLING_EFFECT"></td>
				</tr>
				<tr>
					<th>建议银行</th>
					<td id="PROPOSED_BANK"></td>
				</tr>
			</table>
			<h1 class="titleL">一带一路</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">牵头单位</th>
					<td width='30%' id="ORG_LEAD"></td>
					<th style="width: 200px;">业主单位</th>
					<td id="ORG_OWNER"></td>
				</tr>
				<tr>
					<th>建设单位</th>
					<td id="ORG_INVEST"></td>
					<th>类型</th>
					<td id="ONE_ONE_TYPE"></td>
				</tr>
				<tr>
					<th>推进情况</th>
					<td colspan="3" id="ADVANCE_SITUATION"></td>
				</tr>
			</table>
					</div>
		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">11</span>
		</div>
	</div>
	<!-- end container page book -->
</div>
<div class="fb7-double fb7-first fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">
		<!-- description for page -->
		<div class="fb7-book-nr">
			<h1 class="titleL">项目责任人(PPP项目为政府方责任人)</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">姓名</th>
					<td id="PRO_USER_NAME"></td>
				</tr>
				<tr>
					<th>手机</th>
					<td id="PRO_USER_PHONE"></td>
				</tr>
				<tr>
					<th>固话</th>
					<td id="PRO_USER_TELEPHONE"></td>
				</tr>
				<tr>
					<th>邮箱</th>
					<td id="PRO_USER_MAILBOX"></td>
				</tr>
				<tr>
					<th>微信账号</th>
					<td id="PRO_USER_WXNUMBER"></td>
				</tr>
			</table>
			<h1 class="titleL">项目（法人）单位</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">项目（法人单位）</th>
					<td id="PRO_ORG"></td>
				</tr>
				<tr>
					<th>证照类型</th>
					<td  id="legalUnitType"></td>
				</tr>
				<tr>
					<th>证照号码</th>
					<td id="legalUnitNum"></td>
				</tr>
				<tr>
					<th>联系人</th>
					<td id="legalUnitContacts"></td>
				</tr>
				<tr>
					<th>联系电话</th>
					<td id="legalUnitTel"></td>
				</tr>
				<tr>
					<th>电子邮箱</th>
					<td id="legalUnitMail"></td>
				</tr>
			</table>
		</div>
		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">12</span>
		</div>
	</div>
	<!-- end container page book -->
</div>
<div class="fb7-double fb7-first fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">
		<!-- description for page -->
		<div class="fb7-book-nr">
			<h1 class="titleL">项目申报单位</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">项目申报单位</th>
					<td id="CREATE_DEPARTMENT_FULLNAME"></td>
				</tr>
				<tr>
					<th>证照类型</th>
					<td id="applyDeptType"></td>
				</tr>
				<tr>
					<th>证照号码</th>
					<td id="applyDeptNum"></td>
				</tr>
				<tr>
					<th>联系人</th>
					<td id="applyDeptContacts"></td>
				</tr>
				<tr>
					<th>联系电话</th>
					<td id="applyDeptTel"></td>
				</tr>
				<tr>
					<th>电子邮箱</th>
					<td id="applyDeptMail"></td>
				</tr>
			</table>
			<h1 class="titleL">项目联系人一</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">姓名</th>
					<td id="PRO_CONTACTOR_NAME1"></td>
				</tr>
				<tr>
					<th>手机</th>
					<td id="PRO_CONTACTOR_PHONE1"></td>
				</tr>
				<tr>
					<th>固话</th>
					<td id="PRO_CONTACTOR_TELEPHONE1"></td>
				</tr>
				<tr>
					<th>邮箱</th>
					<td id="PRO_CONTACTOR_MAILBOX1"></td>
				</tr>
				<tr>
					<th>微信账号</th>
					<td id="PRO_CONTACTOR_WXNUMBER1"></td>
				</tr>
			</table>
		</div>
		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">13</span>
		</div>
	</div>
	<!-- end container page book -->
</div>
<div class="fb7-double fb7-first fb7-noshadow">
	<!-- begin container page book -->
	<div class="fb7-cont-page-book">
		<!-- description for page -->
		<div class="fb7-book-nr">		
			<h1 class="titleL">项目联系人二</h1>
			<table class="tableList">
				<tr>
					<th style="width: 200px;">姓名</th>
					<td id="PRO_CONTACTOR_NAME2"></td>
				</tr>
				<tr>
					<th>手机</th>
					<td id="PRO_CONTACTOR_PHONE2"></td>
				</tr>
				<tr>
					<th>固话</th>
					<td id="PRO_CONTACTOR_TELEPHONE2"></td>
				</tr>
				<tr>
					<th>邮箱</th>
					<td id="PRO_CONTACTOR_MAILBOX2"></td>
				</tr>
				<tr>
					<th>微信账号</th>
					<td id="PRO_CONTACTOR_WXNUMBER2"></td>
				</tr>
			</table>
		</div>
		<!-- begin number page  -->
		<div class="fb7-meta">
			<span class="fb7-num">14</span>
		</div>
	</div>
	<!-- end container page book -->
</div>



