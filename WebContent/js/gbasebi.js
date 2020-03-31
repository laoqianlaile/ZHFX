/*
 Based on ndef.parser, by Raphael Graf(r@undefined.ch)
 http://www.undefined.ch/mparser/index.html

 Ported to JavaScript and modified by Matthew Crumley (email@matthewcrumley.com, http://silentmatt.com/)

 You are free to use and modify this code in anyway you find useful. Please leave this comment in the code
 to acknowledge its original source. If you feel like it, I enjoy hearing about projects that use my code,
 but don't feel like you have to let me know or ask permission.
*/

//  Added by stlsmiths 6/13/2011
//  re-define Array.indexOf, because IE doesn't know it ...
//
//  from http://stellapower.net/content/javascript-support-and-arrayindexof-ie
	if (!Array.indexOf) {
		Array.prototype.indexOf = function (obj, start) {
			for (var i = (start || 0); i < this.length; i++) {
				if (this[i] === obj) {
					return i;
				}
			}
			return -1;
		}
	}
String.prototype.format=function()
	{
	  if(arguments.length==0) return this;
	  for(var s=this, i=0; i<arguments.length; i++)
		s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);
	  return s;
	};
var Parser = (function (scope) {
	function object(o) {
		function F() {}
		F.prototype = o;
		return new F();
	}
	var errorsList = [
			"Syntax error",        	//0
			"",    					//1
			"The expression is empty",       //2
			"Division by zero",              //3
			"Unexpected end of expression",  //4
			"The name '{0}' does not exist in the current context",   	//5
			"Syntax error - unprocessed lexemes remain",  				//6
			"( expected",         //7
			") expected",         //8
			"Field, method, or property is not found: '{0}'", 			//9
			"Operator '{0}' cannot be applied to operands of type '{1}' and type '{2}'",    //10
			"The function is not found: '{0}'",              			//11
			"No overload for method '{0}' takes '{1}' arguments",   	//12
			"The '{0}' function has invalid argument '{1}': cannot convert from '{2}' to '{3}'",   //13
			"The '{0}' function is not yet implemented",    	        //14
			"The '{0}' method has invalid argument '{1}': cannot convert from '{2}' to '{3}'",  //15
			"'{0}' does not contain a definition for '{1}'"];   		//16
	function throwError(code, msg){
		throw "error";
		//throw errorsList[code].format.call(this, msg);
	}

	function get_category(par)
		{
			var category = 0;
			if (typeof par == "string") category = 1;
			else if (typeof par == "number")
			{
				category = 2;
				if (parseInt(par) == par) category = 4;
			}
//			else if (par is uint) category = 4;
//			else if (par is int) category = 5;
			else if (par instanceof Date) category = 8;
			else if (typeof par == "boolean") category = 9;
			return category;
		}

	var GbiTypeCode = {
		Boolean:0,
		DateTime:1,
		Number:2,
		Int:3,
		UInt:4,
		String:5,
		Object:6
		
	};
	var GbiOperation = {
		op_Add:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);

			if (category1 <= 1 || category2 <= 1)
			{
				if (par1 == null) par1 = "";
				if (par2 == null) par2 = "";
				return String(par1) + String(par2);
			}
//			else if (category1 == 8 && category2 == 8)
//			{
//				return StiDateTime.fromTicks(StiDateTime(par1).ticks + StiDateTime(par2).ticks);
//			}
			else if (category1 >= 9 || category2 >= 9)
			{
					throwError(0);
				//throwError(10, "+", getQualifiedClassName(par1), getQualifiedClassName(par2));
			}
			return par1 + par2;
		},
		//#region Sub
		op_Sub:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);

			if (category1 <= 1 || category2 <= 1 || category1 >= 9 || category2 >= 9)
			{
				throwError(0);
				//throwError(10, "-", getQualifiedClassName(par1), getQualifiedClassName(par2));
			}
//			else if (category1 == 8 && category2 == 8)
//			{
//				return StiDateTime.fromTicks(StiDateTime(par1).ticks - StiDateTime(par2).ticks);
//			}
			return par1 - par2;
		},
		//#endregion

		//#region Mult
		op_Mult:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);

			if (category1 <= 1 || category2 <= 1 || category1 >= 8 || category2 >= 8)
			{throwError(0);
				//throwError(10, "*", getQualifiedClassName(par1), getQualifiedClassName(par2));
			}
			return par1 * par2;
		},
		//#endregion

		//#region Div
		op_Div:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);

			if (category1 <= 1 || category2 <= 1 || category1 >= 8 || category2 >= 8)
			{throwError(0);
				//throwError(10, "/", getQualifiedClassName(par1), getQualifiedClassName(par2));
			}
			return par1 / par2;
		},
		op_Mod:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 <= 1 || category2 <= 1 || category1 >= 8 || category2 >= 8)
			{throwError(0);
				//throwError(10, "%", getQualifiedClassName(par1), getQualifiedClassName(par2));
			}
			return par1 % par2;
		},
		//#endregion
		
		//#region Pow
		op_Pow:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 <= 1 || category2 <= 1 || category1 >= 8 || category2 >= 8)
			{throwError(0);
				//throwError(10, "^", getQualifiedClassName(par1), getQualifiedClassName(par2));
			}
			return Math.pow(par1, par2);
		},
		//#endregion

		//#region Neg
		op_Neg:function(par1)
		{
			var category = get_category(par1);
			
			if (category <= 1 || category >= 8)
			{throwError(0);
				//throwError(10, "Negative", getQualifiedClassName(par1));
			}
			return this.op_Mult(par1, -1);
		},
		//#region Neg
		op_Neg:function(par1)
		{
			var category = get_category(par1);
			
			if (category <= 1 || category >= 8)
			{
				throwError(10, "Negative", par1);
			}
			return this.op_Mult(par1, -1);
		},
		//#endregion
		
		//#region Not
		op_Not:function(par1)
		{
			var category = get_category(par1);
			
			if (category != 9)
			{
				throwError(10, "Not", par1);
			}
			return !par1;
		},
		//#region Cast
		op_Cast:function(par1, par2)
		{
			var typecode = par2;
			switch (typecode)
			{
				case GbiTypeCode.Boolean:
					return Boolean(par1);
//				case StiTypeCode.DateTime:
//					return StiDateTime(par1);
				case GbiTypeCode.Int:
					return parseInt(par1);
				case GbiTypeCode.UInt:
					return uint(par1);
				case GbiTypeCode.Number:
					return Number(par1);
				case GbiTypeCode.String:
					return String(par1);
				case GbiTypeCode.Object:
					return Object(par1);
			}
			return par1;
		},
		//#region CompareLeft
		op_CompareLeft:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 <= 1 || category2 <= 1 || category1 >= 9 || category2 >= 9)
			{
				throwError(10, "<", par1, par2);
			}
//			else if (category1 == 8 && category2 == 8)
//			{
//				return StiDateTime(par1).ticks < StiDateTime(par2).ticks; 
//			}
			return par1 < par2;
		},
		//#endregion
		
		//#region CompareLeftEqual
		op_CompareLeftEqual:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 <= 1 || category2 <= 1 || category1 >= 9 || category2 >= 9)
			{
				throwError(10, "<=", par1, par2);
			}
//			else if (category1 == 8 && category2 == 8)
//			{
//				return StiDateTime(par1).ticks <= StiDateTime(par2).ticks; 
//			}
			return par1 <= par2;
		},
		//#endregion
		
		//#region CompareRight
		op_CompareRight:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 <= 1 || category2 <= 1 || category1 >= 9 || category2 >= 9)
			{
				throwError(10, ">", par1, par2);
			}
//			else if (category1 == 8 && category2 == 8)
//			{
//				return StiDateTime(par1).ticks > StiDateTime(par2).ticks; 
//			}
			return par1 > par2;
		},
		//#endregion
		
		//#region CompareRightEqual
		op_CompareRightEqual:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 <= 1 || category2 <= 1 || category1 >= 9 || category2 >= 9)
			{
				throwError(10, ">=", par1, par2);
			}
//			else if (category1 == 8 && category2 == 8)
//			{
//				return StiDateTime(par1).ticks >= StiDateTime(par2).ticks; 
//			}
			return par1 >= par2;
		},
		//#region CompareEqual
		op_CompareEqual:function(par1, par2)
		{
			return par1 == par2;
		},
		//#endregion
		
		//#region CompareNotEqual
		op_CompareNotEqual:function(par1, par2)
		{
			return par1 != par2;
		},
		//#region Shl
		op_Shl:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 < 4 || category2 < 4 || category1 >= 8 || category2 >= 8)
			{
				throwError(10, "<<", par1, par2);
			}
			return par1 << par2;
		},
		//#endregion

		//#region Shr
		op_Shr:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 < 4 || category2 < 4 || category1 >= 8 || category2 >= 8)
			{
				throwError(10, ">>", par1, par2);
			}
			return par1 >> par2;
		},
		//#endregion

		//#region And
		op_And:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 < 4 || category1 == 8 || category2 < 4 || category2 == 8)
			{
				throwError(10, "&", par1, par2);
			}
			return par1 & par2;
		},
		//#endregion

		//#region Or
		op_Or:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 < 4 || category1 == 8 || category2 < 4 || category2 == 8)
			{
				throwError(10, "|", par1, par2);
			}
			return par1 | par2;
		},
		//#endregion

		//#region Xor
		op_Xor:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 < 4 || category1 == 8 || category2 < 4 || category2 == 8)
			{
				throwError(10, "^", par1, par2);
			}
			return par1 ^ par2;
		},
		//#endregion

		//#region And2
		op_And2:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 != 9 || category2 != 9)
			{
				throwError(10, "&&", par1, par2);
			}
			return par1 && par2;
		},
		//#endregion
		
		//#region Or2
		op_Or2:function(par1, par2)
		{
			var category1 = get_category(par1);
			var category2 = get_category(par2);
			
			if (category1 != 9 || category2 != 9)
			{
				throwError(10, "||", par1, par2);
			}
			return par1 || par2;
		}
	}
	var GbiFunctionType = {
		TotalsNameSpace:0,
		Count:1,
		CountDistinct:2,
		Avg:3,
		AvgD:4,
		AvgDate:5,
		AvgI:6,
		AvgTime:7,
		Max:8,
		MaxD:9,
		MaxDate:10,
		MaxI:11,
		MaxStr:12,
		MaxTime:13,
		Median:14,
		MedianD:15,
		MedianI:16,
		Min:17,
		MinD:18,
		MinDate:19,
		MinI:20,
		MinStr:21,
		MinTime:22,
		Mode:23,
		ModeD:24,
		ModeI:25,
		Sum:26,
		SumD:27,
		SumDistinct:28,
		SumI:29,
		SumTime:30,
		First:31,
		Last:32,

		cCount:41,
		cCountDistinct:42,
		cAvg:43,
		cAvgD:44,
		cAvgDate:45,
		cAvgI:46,
		cAvgTime:47,
		cMax:48,
		cMaxD:49,
		cMaxDate:50,
		cMaxI:51,
		cMaxStr:52,
		cMaxTime:53,
		cMedian:54,
		cMedianD:55,
		cMedianI:56,
		cMin:57,
		cMinD:58,
		cMinDate:59,
		cMinI:60,
		cMinStr:61,
		cMinTime:62,
		cMode:63,
		cModeD:64,
		cModeI:65,
		cSum:66,
		cSumD:67,
		cSumDistinct:68,
		cSumI:69,
		cSumTime:70,
		cFirst:71,
		cLast:72,

		rCount:81,
		rCountDistinct:82,
		rAvg:83,
		rAvgD:84,
		rAvgDate:85,
		rAvgI:86,
		rAvgTime:87,
		rMax:88,
		rMaxD:89,
		rMaxDate:90,
		rMaxI:91,
		rMaxStr:92,
		rMaxTime:93,
		rMedian:94,
		rMedianD:95,
		rMedianI:96,
		rMin:97,
		rMinD:98,
		rMinDate:99,
		rMinI:100,
		rMinStr:101,
		rMinTime:102,
		rMode:103,
		rModeD:104,
		rModeI:105,
		rSum:106,
		rSumD:107,
		rSumDistinct:108,
		rSumI:109,
		rSumTime:110,
		rFirst:111,
		rLast:112,

		iCount:201,
		iCountDistinct:202,
		iAvg:203,
		iAvgD:204,
		iAvgDate:205,
		iAvgI:206,
		iAvgTime:207,
		iMax:208,
		iMaxD:209,
		iMaxDate:210,
		iMaxI:211,
		iMaxStr:212,
		iMaxTime:213,
		iMedian:214,
		iMedianD:215,
		iMedianI:216,
		iMin:217,
		iMinD:218,
		iMinDate:219,
		iMinI:220,
		iMinStr:221,
		iMinTime:222,
		iMode:223,
		iModeD:224,
		iModeI:225,
		iSum:226,
		iSumD:227,
		iSumDistinct:228,
		iSumI:229,
		iSumTime:230,
		iFirst:231,
		iLast:232,

		ciCount:241,
		ciCountDistinct:242,
		ciAvg:243,
		ciAvgD:244,
		ciAvgDate:245,
		ciAvgI:246,
		ciAvgTime:247,
		ciMax:248,
		ciMaxD:249,
		ciMaxDate:250,
		ciMaxI:251,
		ciMaxStr:252,
		ciMaxTime:253,
		ciMedian:254,
		ciMedianD:255,
		ciMedianI:256,
		ciMin:257,
		ciMinD:258,
		ciMinDate:259,
		ciMinI:260,
		ciMinStr:261,
		ciMinTime:262,
		ciMode:263,
		ciModeD:264,
		ciModeI:265,
		ciSum:266,
		ciSumD:267,
		ciSumDistinct:268,
		ciSumI:269,
		ciSumTime:270,
		ciFirst:271,
		ciLast:272,

		riCount:281,
		riCountDistinct:282,
		riAvg:283,
		riAvgD:284,
		riAvgDate:285,
		riAvgI:286,
		riAvgTime:287,
		riMax:288,
		riMaxD:289,
		riMaxDate:290,
		riMaxI:291,
		riMaxStr:292,
		riMaxTime:293,
		riMedian:294,
		riMedianD:295,
		riMedianI:296,
		riMin:297,
		riMinD:298,
		riMinDate:299,
		riMinI:300,
		riMinStr:301,
		riMinTime:302,
		riMode:303,
		riModeD:304,
		riModeI:305,
		riSum:306,
		riSumD:307,
		riSumDistinct:308,
		riSumI:309,
		riSumTime:310,
		riFirst:311,
		riLast:312,

		Abs:420,
		Acos:421,
		Asin:422,
		Atan:423,
		Ceiling:424,
		Cos:425,
		Div:426,
		Exp:427,
		Floor:428,
		Log:429,
		Maximum:430,
		Minimum:431,
		Round:432,
		Sign:433,
		Sin:434,
		Sqrt:435,
		Tan:436,
		Truncate:437,

		DateDiff:451,
		DateSerial:452,
		Day:453,
		DayOfWeek:454,
		DayOfYear:455,
		Hour:456,
		Minute:457,
		Month:458,
		Second:459,
		TimeSerial:460,
		Year:461,
		ToDate:462,

		DateToStr:470,
		Insert:471,
		Length:472,
		Remove:473,
		Replace:474,
		Roman:475,
		Substring:476,
		ToCurrencyWords:477,
		ToLowerCase:478,
		ToProperCase:479,
		ToUpperCase:480,
		ToWords:481,
		Trim:482,
		TryParseDecimal:483,
		TryParseDouble:484,
		TryParseLong:485,


		IIF:500,
		ToString:501,
		Format:502,
		contains:function(name){
			return typeof this[name] !== 'undefined';
		}
	};
	var ParametersList = (function(){
				this.list = {
					contains:function(name){
						return typeof this[name] !== 'undefined';
					}
				};
				list[GbiFunctionType.CountDistinct] = 2,
				list[GbiFunctionType.Avg] = 2;
				list[GbiFunctionType.AvgD] = 2;
				list[GbiFunctionType.AvgDate] = 2;
				list[GbiFunctionType.AvgI] = 2;
				list[GbiFunctionType.AvgTime] = 2;
				list[GbiFunctionType.Max] = 2;
				list[GbiFunctionType.MaxD] = 2;
				list[GbiFunctionType.MaxDate] = 2;
				list[GbiFunctionType.MaxI] = 2;
				list[GbiFunctionType.MaxStr] = 2;
				list[GbiFunctionType.MaxTime] = 2;
				list[GbiFunctionType.Median] = 2;
				list[GbiFunctionType.MedianD] = 2;
				list[GbiFunctionType.MedianI] = 2;
				list[GbiFunctionType.Min] = 2;
				list[GbiFunctionType.MinD] = 2;
				list[GbiFunctionType.MinDate] = 2;
				list[GbiFunctionType.MinI] = 2;
				list[GbiFunctionType.MinStr] = 2;
				list[GbiFunctionType.MinTime] = 2;
				list[GbiFunctionType.Mode] = 2;
				list[GbiFunctionType.ModeD] = 2;
				list[GbiFunctionType.ModeI] = 2;
				list[GbiFunctionType.Sum] = 2;
				list[GbiFunctionType.SumD] = 2;
				list[GbiFunctionType.SumDistinct] = 2;
				list[GbiFunctionType.SumI] = 2;
				list[GbiFunctionType.SumTime] = 2;
				list[GbiFunctionType.First] = 2;
				list[GbiFunctionType.Last] = 2;

				list[GbiFunctionType.cCountDistinct] = 2;
				list[GbiFunctionType.cAvg] = 2;
				list[GbiFunctionType.cAvgD] = 2;
				list[GbiFunctionType.cAvgDate] = 2;
				list[GbiFunctionType.cAvgI] = 2;
				list[GbiFunctionType.cAvgTime] = 2;
				list[GbiFunctionType.cMax] = 2;
				list[GbiFunctionType.cMaxD] = 2;
				list[GbiFunctionType.cMaxDate] = 2;
				list[GbiFunctionType.cMaxI] = 2;
				list[GbiFunctionType.cMaxStr] = 2;
				list[GbiFunctionType.cMaxTime] = 2;
				list[GbiFunctionType.cMedian] = 2;
				list[GbiFunctionType.cMedianD] = 2;
				list[GbiFunctionType.cMedianI] = 2;
				list[GbiFunctionType.cMin] = 2;
				list[GbiFunctionType.cMinD] = 2;
				list[GbiFunctionType.cMinDate] = 2;
				list[GbiFunctionType.cMinI] = 2;
				list[GbiFunctionType.cMinStr] = 2;
				list[GbiFunctionType.cMinTime] = 2;
				list[GbiFunctionType.cMode] = 2;
				list[GbiFunctionType.cModeD] = 2;
				list[GbiFunctionType.cModeI] = 2;
				list[GbiFunctionType.cSum] = 2;
				list[GbiFunctionType.cSumD] = 2;
				list[GbiFunctionType.cSumDistinct] = 2;
				list[GbiFunctionType.cSumI] = 2;
				list[GbiFunctionType.cSumTime] = 2;
				list[GbiFunctionType.cFirst] = 2;
				list[GbiFunctionType.cLast] = 2;

				list[GbiFunctionType.rCountDistinct] = 2;
				list[GbiFunctionType.rAvg] = 2;
				list[GbiFunctionType.rAvgD] = 2;
				list[GbiFunctionType.rAvgDate] = 2;
				list[GbiFunctionType.rAvgI] = 2;
				list[GbiFunctionType.rAvgTime] = 2;
				list[GbiFunctionType.rMax] = 2;
				list[GbiFunctionType.rMaxD] = 2;
				list[GbiFunctionType.rMaxDate] = 2;
				list[GbiFunctionType.rMaxI] = 2;
				list[GbiFunctionType.rMaxStr] = 2;
				list[GbiFunctionType.rMaxTime] = 2;
				list[GbiFunctionType.rMedian] = 2;
				list[GbiFunctionType.rMedianD] = 2;
				list[GbiFunctionType.rMedianI] = 2;
				list[GbiFunctionType.rMin] = 2;
				list[GbiFunctionType.rMinD] = 2;
				list[GbiFunctionType.rMinDate] = 2;
				list[GbiFunctionType.rMinI] = 2;
				list[GbiFunctionType.rMinStr] = 2;
				list[GbiFunctionType.rMinTime] = 2;
				list[GbiFunctionType.rMode] = 2;
				list[GbiFunctionType.rModeD] = 2;
				list[GbiFunctionType.rModeI] = 2;
				list[GbiFunctionType.rSum] = 2;
				list[GbiFunctionType.rSumD] = 2;
				list[GbiFunctionType.rSumDistinct] = 2;
				list[GbiFunctionType.rSumI] = 2;
				list[GbiFunctionType.rSumTime] = 2;
				list[GbiFunctionType.rFirst] = 2;
				list[GbiFunctionType.rLast] = 2;

				list[GbiFunctionType.iCount] = 2;
				list[GbiFunctionType.iCountDistinct] = 2 | 4;
				list[GbiFunctionType.iAvg] = 2 | 4;
				list[GbiFunctionType.iAvgD] = 2 | 4;
				list[GbiFunctionType.iAvgDate] = 2 | 4;
				list[GbiFunctionType.iAvgI] = 2 | 4;
				list[GbiFunctionType.iAvgTime] = 2 | 4;
				list[GbiFunctionType.iMax] = 2 | 4;
				list[GbiFunctionType.iMaxD] = 2 | 4;
				list[GbiFunctionType.iMaxDate] = 2 | 4;
				list[GbiFunctionType.iMaxI] = 2 | 4;
				list[GbiFunctionType.iMaxStr] = 2 | 4;
				list[GbiFunctionType.iMaxTime] = 2 | 4;
				list[GbiFunctionType.iMedian] = 2 | 4;
				list[GbiFunctionType.iMedianD] = 2 | 4;
				list[GbiFunctionType.iMedianI] = 2 | 4;
				list[GbiFunctionType.iMin] = 2 | 4;
				list[GbiFunctionType.iMinD] = 2 | 4;
				list[GbiFunctionType.iMinDate] = 2 | 4;
				list[GbiFunctionType.iMinI] = 2 | 4;
				list[GbiFunctionType.iMinStr] = 2 | 4;
				list[GbiFunctionType.iMinTime] = 2 | 4;
				list[GbiFunctionType.iMode] = 2 | 4;
				list[GbiFunctionType.iModeD] = 2 | 4;
				list[GbiFunctionType.iModeI] = 2 | 4;
				list[GbiFunctionType.iSum] = 2 | 4;
				list[GbiFunctionType.iSumD] = 2 | 4;
				list[GbiFunctionType.iSumDistinct] = 2 | 4;
				list[GbiFunctionType.iSumI] = 2 | 4;
				list[GbiFunctionType.iSumTime] = 2 | 4;
				list[GbiFunctionType.iFirst] = 2 | 4;
				list[GbiFunctionType.iLast] = 2 | 4;

				list[GbiFunctionType.ciCount] = 2;
				list[GbiFunctionType.ciCountDistinct] = 2 | 4;
				list[GbiFunctionType.ciAvg] = 2 | 4;
				list[GbiFunctionType.ciAvgD] = 2 | 4;
				list[GbiFunctionType.ciAvgDate] = 2 | 4;
				list[GbiFunctionType.ciAvgI] = 2 | 4;
				list[GbiFunctionType.ciAvgTime] = 2 | 4;
				list[GbiFunctionType.ciMax] = 2 | 4;
				list[GbiFunctionType.ciMaxD] = 2 | 4;
				list[GbiFunctionType.ciMaxDate] = 2 | 4;
				list[GbiFunctionType.ciMaxI] = 2 | 4;
				list[GbiFunctionType.ciMaxStr] = 2 | 4;
				list[GbiFunctionType.ciMaxTime] = 2 | 4;
				list[GbiFunctionType.ciMedian] = 2 | 4;
				list[GbiFunctionType.ciMedianD] = 2 | 4;
				list[GbiFunctionType.ciMedianI] = 2 | 4;
				list[GbiFunctionType.ciMin] = 2 | 4;
				list[GbiFunctionType.ciMinD] = 2 | 4;
				list[GbiFunctionType.ciMinDate] = 2 | 4;
				list[GbiFunctionType.ciMinI] = 2 | 4;
				list[GbiFunctionType.ciMinStr] = 2 | 4;
				list[GbiFunctionType.ciMinTime] = 2 | 4;
				list[GbiFunctionType.ciMode] = 2 | 4;
				list[GbiFunctionType.ciModeD] = 2 | 4;
				list[GbiFunctionType.ciModeI] = 2 | 4;
				list[GbiFunctionType.ciSum] = 2 | 4;
				list[GbiFunctionType.ciSumD] = 2 | 4;
				list[GbiFunctionType.ciSumDistinct] = 2 | 4;
				list[GbiFunctionType.ciSumI] = 2 | 4;
				list[GbiFunctionType.ciSumTime] = 2 | 4;
				list[GbiFunctionType.ciFirst] = 2 | 4;
				list[GbiFunctionType.ciLast] = 2 | 4;

				list[GbiFunctionType.riCount] = 2;
				list[GbiFunctionType.riCountDistinct] = 2 | 4;
				list[GbiFunctionType.riAvg] = 2 | 4;
				list[GbiFunctionType.riAvgD] = 2 | 4;
				list[GbiFunctionType.riAvgDate] = 2 | 4;
				list[GbiFunctionType.riAvgI] = 2 | 4;
				list[GbiFunctionType.riAvgTime] = 2 | 4;
				list[GbiFunctionType.riMax] = 2 | 4;
				list[GbiFunctionType.riMaxD] = 2 | 4;
				list[GbiFunctionType.riMaxDate] = 2 | 4;
				list[GbiFunctionType.riMaxI] = 2 | 4;
				list[GbiFunctionType.riMaxStr] = 2 | 4;
				list[GbiFunctionType.riMaxTime] = 2 | 4;
				list[GbiFunctionType.riMedian] = 2 | 4;
				list[GbiFunctionType.riMedianD] = 2 | 4;
				list[GbiFunctionType.riMedianI] = 2 | 4;
				list[GbiFunctionType.riMin] = 2 | 4;
				list[GbiFunctionType.riMinD] = 2 | 4;
				list[GbiFunctionType.riMinDate] = 2 | 4;
				list[GbiFunctionType.riMinI] = 2 | 4;
				list[GbiFunctionType.riMinStr] = 2 | 4;
				list[GbiFunctionType.riMinTime] = 2 | 4;
				list[GbiFunctionType.riMode] = 2 | 4;
				list[GbiFunctionType.riModeD] = 2 | 4;
				list[GbiFunctionType.riModeI] = 2 | 4;
				list[GbiFunctionType.riSum] = 2 | 4;
				list[GbiFunctionType.riSumD] = 2 | 4;
				list[GbiFunctionType.riSumDistinct] = 2 | 4;
				list[GbiFunctionType.riSumI] = 2 | 4;
				list[GbiFunctionType.riSumTime] = 2 | 4;
				list[GbiFunctionType.riFirst] = 2 | 4;
				list[GbiFunctionType.riLast] = 2 | 4;
			return list;
	})();

	var GbiSystemVariableType = {
		contains:function(name){
			return typeof this[name] !== 'undefined';
		}
	};
	var GbiTokenType = {
			/** Empty token */
		Empty:0,

		/** Delimiter */
		Delimiter:1,

		/** Variable */
		Variable:2,

		/** SystemVariable */
		SystemVariable:3,

		/** DataSourceField */
		DataSourceField:4,

		/** BusinessObjectField */
		BusinessObjectField:5,

		/** Number */
		Number:6,

		/** Function (args) */
		Function:7,

		/** Method (parent + args) */
		Method:8,

		/** Property (parent) */
		Property:9,

		/** Component */
		Component:10,

		/** Cast */
		Cast:11,

		/** String */
		String:12,

		/** Dot . */
		Dot:13,

		/** Comma , */
		Comma:14,

		/** Colon : */
		Colon:15,

		/** SemiColon , */
		SemiColon:16,

		/** Shift to the left << */
		Shl:17,

		/** Shift to the right >> */
		Shr:18,

		/** Assign */
		Assign:19,

		/** Equal */
		Equal:20,

		/** NotEqual */
		NotEqual:21,

		/** LeftEqual */
		LeftEqual:22,

		/** Left */
		Left:23,

		/** RightEqual */
		RightEqual:24,

		/** Right */
		Right:25,

		/** Or */
		Or:26,

		/** And */
		And:27,

		/** Xor ^ */
		Xor:28,

		/** Double logical OR || */
		DoubleOr:29,

		/** Double logical AND && */
		DoubleAnd:30,

		/** Question */
		Question:32,

		/** Plus + */
		Plus:33,

		/** Minus - */
		Minus:34,

		/** Mult * */
		Mult:35,

		/** Div / */
		Div:36,

		/** Percent % */
		Percent:38,

		/** LParenthesis ( */
		LParenthesis:45,

		/** RParenthesis ) */
		RParenthesis:46,

		/** Identifier */
		Identifier:52,

		/** Logical NOT Token. */
		Not:53,

		/** Unknown */
		Unknown:54
	};
	var GbiTypeCode = {
		Boolean:0,
		DateTime:1,
		Number:2,
		Int:3,
		UInt:4,
		String:5,
		Object:6,
		contains:function(name){
			return typeof this[name] !== 'undefined';
		}
	};
	var GbiPropertyType = {
		Year:0,
		Month:1,
		Day:2,
		Hour:3,
		Minute:4,
		Second:5,
		Length:6,
		contains:function(name){
			return typeof this[name] !== 'undefined';
		}
	};
	var GbiMethodType = {
		Substring:1024 + 0,
		ToString:1024 + 1,
		ToLower:1024 + 2,
		ToUpper:1024 + 3,
		IndexOf:1024 + 4,
		StartsWith:1024 + 5,
		EndsWith:1024 + 6,
		MethodNameSpace:1024 + 7,
		contains:function(name){
			return typeof this[name] !== 'undefined';
		}
	};

	function GbiToken(type) {
		if(typeof type === 'undefined'){
			type = GbiTokenType.Empty;
		}
		this.type = type;
		this.value = "";
		this.valueObject = null;
	}

	function GbiAsmCommand(type, parameter1, parameter2){
		this.type = type;
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
	}
	var GbiAsmCommandType = {
	PushValue:0,
		PushVariable:1,
		PushSystemVariable:2,
		PushDataSourceField:3,
		PushBusinessObjectField:4,
		PushFunction:5,
		PushMethod:6,
		PushProperty:7,
		PushComponent:8,
		CopyToVariable:9,
		Add:10,
		Sub:11,
		Mult:12,
		Div:13,
		Mod:14,
		Power:15,
		Neg:16,
		Cast:17,
		Not:18,
		CompareLeft:19,
		CompareLeftEqual:20,
		CompareRight:21,
		CompareRightEqual:22,
		CompareEqual:23,
		CompareNotEqual:24,
		Shl:25,
		Shr:26,
		And:27,
		And2:28,
		Or:29,
		Or2:30,
		Xor:31
	}

	function Expression(tokens) {
		this.tokensList = tokens;
		this.tokenPos = 0;
		this.currentToken = null;
		this.asmList = [];
		this.customVariables = null;
	}
	Expression.prototype = {
		getVariables:function(){
			var variables = [];
			for(var i in this.tokensList){
				if(this.tokensList[i].type == GbiTokenType.Variable)
					variables.push(this.tokensList[i].valueObject);
			}
			return variables;
		},
		evaluate:function(customVariables){
			this.customVariables = customVariables;
			var result = {success:true};
			try{
				this.evalToken();
				result.data = this.executeAsm();
			}catch(e){
				result.success = false;
				result.data = e;
			}
			return result
		},
		evalToken:function(){
			this.tokenPos = 0;
			if (this.tokensList.length == 0)
			{
				throwError(2);  //пустое выражение
				return;
			}
			this.eval_exp0();
			if (this.tokenPos <= this.tokensList.length)
			{
				throwError(6);  // остаются необработанные лексемы
			}
		},
		executeAsm:function()
		{
			if (this.asmList == null || this.asmList.length == 0) return null;
			var stack = [];
			var argsList = null;
			var par1 = null;
			var par2 = null;
			var index = 0;
			for(var asmKey in this.asmList)
			{
				var asmCommand = this.asmList[asmKey];
				switch (asmCommand.type)
				{
					case GbiAsmCommandType.PushValue:
						stack.push(asmCommand.parameter1);
						break;
					case GbiAsmCommandType.PushVariable:
						if(typeof this.customVariables == 'object'){
							if(!this.customVariables || this.customVariables[asmCommand.parameter1] == undefined)
								throwError(0);
							stack.push(this.customVariables[asmCommand.parameter1]);
						}else if(typeof this.customVariables == 'function'){
							stack.push(this.customVariables.apply(this, [asmCommand.parameter1]));
						}
						
						break;
					case GbiAsmCommandType.PushSystemVariable:
						stack.push(get_systemVariable(asmCommand.parameter1));
						break;
					case GbiAsmCommandType.PushComponent:
						stack.push(asmCommand.parameter1);
						break;

					case GbiAsmCommandType.CopyToVariable:
						var variableValue = stack.pop();
						stack.push(variableValue);
						//variableManager.getVariableByName(asmCommand.parameter1).value = variableValue;
						break;

					case GbiAsmCommandType.PushFunction:
						argsList = [];
						for (index = 0; index < asmCommand.parameter2; index++)
						{
							argsList.push(stack.pop());
						}
						argsList.reverse();
						stack.push(this.call_func(asmCommand.parameter1, argsList));
						break;

					// case GbiAsmCommandType.PushMethod:
						// argsList = new GbiArrayList();
						// for (index = 0; index < asmCommand.parameter2; index++)
						// {
							// argsList.push(stack.pop());
						// }
						// argsList.reverse();
						// stack.push(call_method(asmCommand.parameter1, argsList));
						// break;

					// case GbiAsmCommandType.PushProperty:
						// argsList = new GbiArrayList();
						// argsList.push(stack.pop());
						// stack.push(call_property(asmCommand.parameter1, argsList));
						// break;


					case GbiAsmCommandType.Add:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Add(par1, par2));
						break;
					case GbiAsmCommandType.Sub:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Sub(par1, par2));
						break;

					case GbiAsmCommandType.Mult:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Mult(par1, par2));
						break;
					case GbiAsmCommandType.Div:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Div(par1, par2));
						break;
					case GbiAsmCommandType.Mod:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Mod(par1, par2));
						break;

					case GbiAsmCommandType.Power:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Pow(par1, par2));
						break;

					case GbiAsmCommandType.Neg:
						par1 = stack.pop();
						stack.push(GbiOperation.op_Neg(par1));
						break;

					case GbiAsmCommandType.Cast:
						par1 = stack.pop();
						par2 = asmCommand.parameter1;
						stack.push(GbiOperation.op_Cast(par1, par2));
						break;

					case GbiAsmCommandType.Not:
						par1 = stack.pop();
						stack.push(GbiOperation.op_Not(par1));
						break;

					case GbiAsmCommandType.CompareLeft:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_CompareLeft(par1, par2));
						break;
					case GbiAsmCommandType.CompareLeftEqual:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_CompareLeftEqual(par1, par2));
						break;
					case GbiAsmCommandType.CompareRight:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_CompareRight(par1, par2));
						break;
					case GbiAsmCommandType.CompareRightEqual:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_CompareRightEqual(par1, par2));
						break;

					case GbiAsmCommandType.CompareEqual:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_CompareEqual(par1, par2));
						break;
					case GbiAsmCommandType.CompareNotEqual:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_CompareNotEqual(par1, par2));
						break;

					case GbiAsmCommandType.Shl:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Shl(par1, par2));
						break;
					case GbiAsmCommandType.Shr:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Shr(par1, par2));
						break;

					case GbiAsmCommandType.And:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_And(par1, par2));
						break;
					case GbiAsmCommandType.Or:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Or(par1, par2));
						break;
					case GbiAsmCommandType.Xor:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Xor(par1, par2));
						break;

					case GbiAsmCommandType.And2:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_And2(par1, par2));
						break;
					case GbiAsmCommandType.Or2:
						par2 = stack.pop();
						par1 = stack.pop();
						stack.push(GbiOperation.op_Or2(par1, par2));
						break;

				}
			}
			return stack.pop();
		},
		call_func:function(name, argsList)
		{
			var category;
			var category2;
			switch (name)
			{

				//#region Math
				case GbiFunctionType.Abs:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
						throwError(0);
						//throwError(13, "Abs", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.abs(argsList[0]);

				case GbiFunctionType.Acos:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
						throwError(0);
						//throwError(13, "Acos", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.acos(argsList[0]);

				case GbiFunctionType.Asin:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
						throwError(0);
						//throwError(13, "Asin", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.asin(argsList[0]);

				case GbiFunctionType.Atan:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
					throwError(0);
						//throwError(13, "Atan", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.atan(argsList[0]);

				case GbiFunctionType.Ceiling:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
					throwError(0);
						//throwError(13, "Ceiling", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.ceil(argsList[0]);

				case GbiFunctionType.Cos:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
					throwError(0);
						//throwError(13, "Cos", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.cos(argsList[0]);

				case GbiFunctionType.Div:
					category = get_category(argsList[0]);
					category2 = get_category(argsList[1]);
					if (category <= 1 || category >= 8)
					{
					throwError(0);
						//throwError(13, "Div", "1", getQualifiedClassName(argsList[0]), "double");
					}
					else if (category2 <= 1 || category2 >= 8)
					{
					throwError(0);
						//throwError(13, "Div", "2", getQualifiedClassName(argsList[1]), "double");
					}
					else if (argsList.length == 3)
					{
						if (argsList[1] == 0) return argsList[2];
						return argsList[0] / argsList[1];
					}
					else if (argsList.length == 2)
					{
						return argsList[0] / argsList[1];
					}
					throwError(0);
					//throwError(12, "Div", argsList.length.toString());
					break;

				case GbiFunctionType.Exp:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Exp", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.exp(argsList[0]);

				case GbiFunctionType.Floor:
					category = get_category(argsList[0]);
					//if (category <= 1 || category >= 4)
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Floor", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.floor(argsList[0]);

				case GbiFunctionType.Log:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Log", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.log(argsList[0]);

				case GbiFunctionType.Maximum:
					if (argsList.length != 2) throwError(12, "Maximum", argsList.length.toString());
					category = get_category(argsList[0]);
					category2 = get_category(argsList[1]);
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Maximum", "1", getQualifiedClassName(argsList[0]), "double");
					}
					else if (category2 <= 1 || category2 >= 8)
					{throwError(0);
						//throwError(13, "Maximum", "2", getQualifiedClassName(argsList[1]), "double");
					}
					return Math.max(argsList[0], argsList[1]);
				case GbiFunctionType.Sin:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Sin", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.sin(argsList[0]);

				case GbiFunctionType.Sqrt:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Sqrt", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.sqrt(argsList[0]);

				case GbiFunctionType.Tan:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Tan", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.tan(argsList[0]);

				case GbiFunctionType.Truncate:
					category = get_category(argsList[0]);
					//if (category <= 1 || category >= 4)
					if (category <= 1 || category >= 8)
					{throwError(0);
						//throwError(13, "Truncate", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return int(argsList[0]);
				//#endregion


				case GbiFunctionType.DayOfWeek:
//					category = get_category(argsList[0]);
//					if (category != 8) throwError(13, "DayOfWeek", "1", getQualifiedClassName(argsList[0]), "DateTime");
//					if (argsList.length == 1) return Convert.ToDateTime(argsList[0]).DayOfWeek.toString();
//					else if (argsList.length == 2)
//					{
//						category = get_category(argsList[1]);
//						if (category != 9) throwError(13, "DayOfWeek", "2", getQualifiedClassName(argsList[2]), "Boolean");
//						return Convert.ToDateTime(argsList[0]).DayOfWeek.toString();                                        // !!! доделать
//					}
//					throwError(12, "DayOfWeek", argsList.length.toString());
					throwError(14, "DayOfWeek");
					break;

				case GbiFunctionType.DayOfYear:
//					category = get_category(argsList[0]);
//					if (category != 8) throwError(13, "DayOfYear", "1", getQualifiedClassName(argsList[0]), "DateTime");
//					if (argsList.length == 1) return Convert.ToDateTime(argsList[0]).DayOfYear.toString();
//					throwError(12, "DayOfYear", argsList.length.toString());
					throwError(14, "DayOfYear");
					break;

				//#region Strings
				case GbiFunctionType.DateToStr:
					throwError(14, "DateToStr");
					break;
//
				case GbiFunctionType.Length:
					if (argsList.length != 1) throwError(12, "Length", argsList.length.toString());
					if (argsList[0] == null) return 0;
					category = get_category(argsList[0]);
					if (category != 1) throwError(13, "Length", "1", getQualifiedClassName(argsList[0]), "string");
					return argsList[0].length;
				case GbiFunctionType.Roman:
					throwError(14, "Roman");
					break;


				case GbiFunctionType.ToCurrencyWords:
					throwError(14, "ToCurrencyWords");
					break;

				case GbiFunctionType.ToLowerCase:
					if (argsList.length != 1) throwError(12, "ToLowerCase", argsList.length.toString());
					category = get_category(argsList[0]);
					if (category != 1) throwError(13, "ToLowerCase", "1", getQualifiedClassName(argsList[0]), "string");
					return argsList[0].toLowerCase();

				case GbiFunctionType.ToProperCase:
//					if (argsList.length != 1) throwError(12, "ToProperCase", argsList.length.toString());
//					category = get_category(argsList[0]);
//					if (category != 1) throwError(13, "ToProperCase", "1", getQualifiedClassName(argsList[0]), "string");
//					return System.Globalization.CultureInfo.InvariantCulture.TextInfo.ToTitleCase(Convert.toString(argsList[0]).ToLowerInvariant());
					throwError(14, "ToProperCase");
					break;

				case GbiFunctionType.ToUpperCase:
					if (argsList.length != 1) throwError(12, "ToUpperCase", argsList.length.toString());
					category = get_category(argsList[0]);
					if (category != 1) throwError(13, "ToUpperCase", "1", getQualifiedClassName(argsList[0]), "string");
					return argsList[0].toUpperCase();
				case GbiFunctionType.Year:
					if (argsList.length != 1) throwError(12, "Year", argsList.length.toString());
					category = get_category(argsList[0]);
					if (category != 8) throwError(13, "Year", "1", getQualifiedClassName(argsList[0]), "DateTime");
					return (argsList[0]).getFullYear();
				case GbiFunctionType.ToDate:
					if (argsList.length != 1) throwError(12, "Year", argsList.length.toString());
					category = get_category(argsList[0]);
					if (category != 1) throwError(13, "Year", "1", getQualifiedClassName(argsList[0]), "DateTime");
					return new Date(argsList[0]);
				case GbiFunctionType.ToWords:
					throwError(14, "ToWords");
					break;

				case GbiFunctionType.TryParseDecimal:
					throwError(14, "TryParseDecimal");
					break;

				case GbiFunctionType.TryParseDouble:
					throwError(14, "TryParseDouble");
					break;

				case GbiFunctionType.TryParseLong:
					throwError(14, "TryParseLong");
					break;
				//#endregion
				
				case GbiFunctionType.IIF:
					return Boolean(argsList[0]) ? argsList[1] : argsList[2];
				case GbiFunctionType.Round:
					category = get_category(argsList[0]);
					if (category <= 1 || category >= 8)
					{
					throwError(0);
						//throwError(13, "Cos", "1", getQualifiedClassName(argsList[0]), "double");
					}
					return Math.round(argsList[0]);
//
			}
			return null;
		},
		eval_exp0:function()
		{
			this.get_token();
			this.eval_exp01();
		},
		get_token:function(){
			if (this.tokenPos < this.tokensList.length)
			{
				this.currentToken = this.tokensList[this.tokenPos];
			}
			else
			{
				this.currentToken = new GbiToken();
			}
			this.tokenPos++;
		},
		eval_exp01:function()
		{
			if (this.currentToken.type == GbiTokenType.Variable)
			{
				var variableToken = this.currentToken;
				this.get_token();
				if (this.currentToken.type != GbiTokenType.Assign)
				{
					this.tokenPos--;
					this.currentToken = this.tokensList[this.tokenPos - 1];
				}
				else
				{
					this.get_token();
					this.eval_exp1();
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.CopyToVariable, variableToken.value));
					return;
				}
			}
			this.eval_exp1();
		},
		eval_exp1:function()
		{
			this.eval_exp10();
			if (this.currentToken.type == GbiTokenType.Question)
			{
				this.get_token();
				this.eval_exp10();
				if (this.currentToken.type != GbiTokenType.Colon) throwError(0);  //cинтаксическая ошибка
				this.get_token();
				this.eval_exp10();
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushFunction, GbiFunctionType.IIF, 3));
			}
		},
		eval_exp10:function()
		{
			this.eval_exp11();
			while (this.currentToken.type == GbiTokenType.DoubleOr)
			{
				this.get_token();
				this.eval_exp11();
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Or2));
			}
		},
		eval_exp11:function()
		{
			this.eval_exp12();
			while (this.currentToken.type == GbiTokenType.DoubleAnd)
			{
				this.get_token();
				this.eval_exp12();
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.And2));
			}
		},

		//----------------------------------------
		// Бинарное ИЛИ
		//----------------------------------------
		eval_exp12:function()
		{
			this.eval_exp14();
			if (this.currentToken.type == GbiTokenType.Or)
			{
				this.get_token();
				this.eval_exp14();
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Or));
			}
		},
		eval_exp14:function()
		{
			this.eval_exp15();
			if (this.currentToken.type == GbiTokenType.Xor)
			{
				this.get_token();
				this.eval_exp15();
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Xor));
			}
		},

		//----------------------------------------
		// Бинарное И
		//----------------------------------------
		eval_exp15:function()
		{
			this.eval_exp16();
			if (this.currentToken.type == GbiTokenType.And)
			{
				this.get_token();
				this.eval_exp16();
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.And));
			}
		},


		//----------------------------------------
		// Equality (==, !=)
		//----------------------------------------
		eval_exp16:function()
		{
			this.eval_exp17();
			if (this.currentToken.type == GbiTokenType.Equal || this.currentToken.type == GbiTokenType.NotEqual)
			{
				var command = new GbiAsmCommand(GbiAsmCommandType.CompareEqual);
				if (this.currentToken.type == GbiTokenType.NotEqual) command.type = GbiAsmCommandType.CompareNotEqual;
				this.get_token();
				this.eval_exp17();
				this.asmList.push(command);
			}
		},

		//----------------------------------------
		// Relational and type testing (<, >, <=, >=, is, as)
		//----------------------------------------
		eval_exp17:function()
		{
			this.eval_exp18();
			if (this.currentToken.type == GbiTokenType.Left || this.currentToken.type == GbiTokenType.LeftEqual ||
				this.currentToken.type == GbiTokenType.Right || this.currentToken.type == GbiTokenType.RightEqual)
			{
				var command = null;
				if (this.currentToken.type == GbiTokenType.Left)         command = new GbiAsmCommand(GbiAsmCommandType.CompareLeft);
				if (this.currentToken.type == GbiTokenType.LeftEqual)    command = new GbiAsmCommand(GbiAsmCommandType.CompareLeftEqual);
				if (this.currentToken.type == GbiTokenType.Right)        command = new GbiAsmCommand(GbiAsmCommandType.CompareRight);
				if (this.currentToken.type == GbiTokenType.RightEqual)   command = new GbiAsmCommand(GbiAsmCommandType.CompareRightEqual);
				this.get_token();
				this.eval_exp18();
				this.asmList.push(command);
			}
		},


		//----------------------------------------
		// Shift (<<, >>)
		//----------------------------------------
		eval_exp18:function()
		{
			this.eval_exp2();
			if ((this.currentToken.type == GbiTokenType.Shl) || (this.currentToken.type == GbiTokenType.Shr))
			{
				var command = new GbiAsmCommand(GbiAsmCommandType.Shl);
				if (this.currentToken.type == GbiTokenType.Shr) command.type = GbiAsmCommandType.Shr;
				this.get_token();
				this.eval_exp2();
				this.asmList.push(command);
			}
		},
		eval_exp2:function()
		{
			this.eval_exp3();
			while ((this.currentToken.type == GbiTokenType.Plus) || (this.currentToken.type == GbiTokenType.Minus))
			{
				var operation = this.currentToken;
				this.get_token();
				this.eval_exp3();
				if (operation.type == GbiTokenType.Minus)
				{
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Sub));
				}
				else if (operation.type == GbiTokenType.Plus)
				{
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Add));
				}
			}
		},


		//----------------------------------------
		// Умножение или деление двух множителей
		//----------------------------------------
		eval_exp3:function()
		{
			this.eval_exp4();
			while (this.currentToken.type == GbiTokenType.Mult || this.currentToken.type == GbiTokenType.Div || this.currentToken.type == GbiTokenType.Percent)
			{
				var operation = this.currentToken;
				this.get_token();
				this.eval_exp4();
				if (operation.type == GbiTokenType.Mult)
				{
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Mult));
				}
				else if (operation.type == GbiTokenType.Div)
				{
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Div));
				}
				if (operation.type == GbiTokenType.Percent)
				{
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Mod));
				}
			}
		},


		//----------------------------------------
		// Возведение в степень
		//----------------------------------------
		eval_exp4:function()
		{
			this.eval_exp5();
			//if (this.currentToken.Type == StiTokenType.Xor)
			//{
			//    get_token();
			//    eval_exp4();
			//    asmList.Add(new StiAsmCommand(StiAsmCommandType.Power));
			//}
		},


		//----------------------------------------
		// Вычисление унарного + и -
		//----------------------------------------
		eval_exp5:function()
		{
			var command = null;
			if (this.currentToken.type == GbiTokenType.Plus || this.currentToken.type == GbiTokenType.Minus || this.currentToken.type == GbiTokenType.Not)
			{
				if (this.currentToken.type == GbiTokenType.Minus) command = new GbiAsmCommand(GbiAsmCommandType.Neg);
				if (this.currentToken.type == GbiTokenType.Not) command = new GbiAsmCommand(GbiAsmCommandType.Not);
				this.get_token();
			}
			this.eval_exp6();
			if (command != null)
			{
				this.asmList.push(command);
			}
		},


		//----------------------------------------
		// Обработка выражения в скобках
		//----------------------------------------
		eval_exp6:function()
		{
			if (this.currentToken.type == GbiTokenType.LParenthesis)
			{
				this.get_token();
				if (this.currentToken.type == GbiTokenType.Cast)
				{
					var typeCode = GbiTypeCode[currentToken.value];
					this.get_token();
					if (this.currentToken.type != GbiTokenType.RParenthesis)
					{
						throwError(8);  //несбалансированные скобки
					}
					this.get_token();
					this.eval_exp6();
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.Cast, typeCode));
				}
				else
				{
					this.eval_exp1();
					if (this.currentToken.type != GbiTokenType.RParenthesis)
					{
						throwError(8);  //несбалансированные скобки
					}
					this.get_token();
				}
			}
			else
			{
				this.eval_exp7();
			}
		},


		//----------------------------------------
		// Вычисление методов и свойств
		//----------------------------------------
		eval_exp7:function()
		{
			this.atom();
			if (this.currentToken.type == GbiTokenType.Dot)
			{
				this.get_token();
				this.eval_exp7();
			}
		},
		atom:function()
		{
			if (this.currentToken.type == GbiTokenType.Variable)
			{
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushVariable, this.currentToken.valueObject, this.currentToken.value));
				this.get_token();
				return;
			}
			else if (this.currentToken.type == GbiTokenType.SystemVariable)
			{
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushSystemVariable, GbiSystemVariableType[this.currentToken.value]));
				this.get_token();
				return;
			}
			else if (this.currentToken.type == GbiTokenType.Function)
			{
				var ffunction = this.currentToken;
				var functionType = GbiFunctionType[ffunction.value];
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushFunction, functionType, this.get_args_count(functionType)));
				this.get_token();
				return;
			}
			else if (this.currentToken.type == GbiTokenType.Method)
			{
				var method = this.currentToken;
				var methodType = GbiMethodType[method.value];
//				this.asmList.push(new StiAsmCommand(StiAsmCommandType.PushMethod, methodType, get_args_count(methodType) + 1));
				this.get_token();
				return;
			}
			else if (this.currentToken.type == GbiTokenType.Property)
			{
				var property = this.currentToken;
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushProperty, GbiPropertyType[property.value]));
					this.get_token();
					return;
			}
			else if (this.currentToken.type == GbiTokenType.DataSourceField)
			{
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushDataSourceField, this.currentToken.value));
				this.get_token();
				return;
			}
			else if (this.currentToken.type == GbiTokenType.BusinessObjectField)
			{
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushBusinessObjectField, this.currentToken.value));
				this.get_token();
				return;
			}
//			else if (this.currentToken.type == StiTokenType.Component)
//			{
//				this.asmList.push(new StiAsmCommand(StiAsmCommandType.PushComponent, componentsList[this.currentToken.value]));
//				this.get_token();
//				return;
//			}
			else if (this.currentToken.type == GbiTokenType.Number)
			{
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushValue, this.currentToken.valueObject));
				this.get_token();
				return;
			}
			else if (this.currentToken.type == GbiTokenType.String)
			{
				this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushValue, this.currentToken.valueObject));
				this.get_token();
				return;
			}
			else
			{
				if (this.currentToken.type == GbiTokenType.Empty)
				{
					throwError(4);  //неожиданный конец выражения
				}
				throwError(0);  //cинтаксическая ошибка
			}
		},
		get_args_count:function(name)
		{
			var args = this.get_args();
			var aggregateComponent = null;
			var newCommand = null;

			//If aggregateComponent is not specified, search it
			var func = name;
			if ((func == GbiFunctionType.Count) && (args.length == 0) ||
				(func >= GbiFunctionType.CountDistinct && func <= GbiFunctionType.Last ||
				 func >= GbiFunctionType.cCountDistinct && func <= GbiFunctionType.cLast ||
				 func >= GbiFunctionType.rCountDistinct && func <= GbiFunctionType.rLast) && (args.length == 1) ||
				(func == GbiFunctionType.iCount) && (args.length == 1) ||
				(func >= GbiFunctionType.iCountDistinct && func <= GbiFunctionType.iLast ||
				 func >= GbiFunctionType.ciCountDistinct && func <= GbiFunctionType.ciLast ||
				 func >= GbiFunctionType.riCountDistinct && func <= GbiFunctionType.riLast) && (args.length == 2))
			{
				aggregateComponent = component.getGroupHeaderBand();
				if (aggregateComponent == null) aggregateComponent = component.getDataBand();
				newCommand = new GbiArrayList();
				newCommand.add(new GbiAsmCommand(GbiAsmCommandType.PushComponent, aggregateComponent));
				args.insert(0, newCommand);
			}

			//изменяем aggregateComponent если надо
			if ((func >= GbiFunctionType.Count && func <= GbiFunctionType.Last) ||
				(func >= GbiFunctionType.cCount && func <= GbiFunctionType.cLast) ||
				(func >= GbiFunctionType.rCount && func <= GbiFunctionType.rLast) ||
				func == GbiFunctionType.cCount ||
				(func >= GbiFunctionType.iCount && func <= GbiFunctionType.iLast) ||
				(func >= GbiFunctionType.ciCount && func <= GbiFunctionType.ciLast) ||
				(func >= GbiFunctionType.riCount && func <= GbiFunctionType.riLast) ||
				func == GbiFunctionType.ciCount)
			{
				aggregateComponent = component.getGroupHeaderBand();
				if (aggregateComponent != null)
				{
					newCommand = new GbiArrayList();
					newCommand.add(new GbiAsmCommand(GbiAsmCommandType.PushComponent, aggregateComponent));
					args[0] = newCommand;
				}
			}
			var bitsValue = 0;
			if (ParametersList.contains(name)) bitsValue = ParametersList[name];
			var bitsCounter = 1;
			for (var arg in args)
			{
				if ((bitsValue & bitsCounter) > 0)
				{
					this.asmList.push(new GbiAsmCommand(GbiAsmCommandType.PushValue, arg));
				}
				else
				{
					this.asmList.concat(arg);
				}
				bitsCounter = bitsCounter << 1;
			}

			return args.length;
		},
		get_args:function()
		{
			var args = [];
			this.get_token();
			if (this.currentToken.type != GbiTokenType.LParenthesis) throwError(7);   //ожидается открывающая скобка
			this.get_token();
			if (this.currentToken.type == GbiTokenType.RParenthesis)
			{
				return args;
			}
			else
			{
				this.tokenPos--;
				this.currentToken = this.tokensList[this.tokenPos - 1];
			}

			//обработка списка значений
			var tempAsmList = this.asmList;
			do
			{
				asmList = [];
				this.eval_exp0();
				args.push(asmList);
			}
			while (this.currentToken.type == GbiTokenType.Comma);
			this.asmList = tempAsmList;

			if (this.currentToken.type != GbiTokenType.RParenthesis) throwError(8);   //ожидается закрывающая скобка
			return args;
		}
	};

	function add(a, b) {
		return Number(a) + Number(b);
	}
	function sub(a, b) {
		return a - b;
	}
	function mul(a, b) {
		return a * b;
	}
	function div(a, b) {
		return a / b;
	}
	function mod(a, b) {
		return a % b;
	}
	function concat(a, b) {
		return "" + a + b;
	}

	function neg(a) {
		return -a;
	}

	function random(a) {
		return Math.random() * (a || 1);
	}
	function fac(a) { //a!
		a = Math.floor(a);
		var b = a;
		while (a > 1) {
			b = b * (--a);
		}
		return b;
	}

	// TODO: use hypot that doesn't overflow
	function pyt(a, b) {
		return Math.sqrt(a * a + b * b);
	}

	function append(a, b) {
		if (Object.prototype.toString.call(a) != "[object Array]") {
			return [a, b];
		}
		a = a.slice();
		a.push(b);
		return a;
	}

	// add langyuezhang
	function contrary(a){
		var r;
		if(typeof a == 'string'){
			r = (a == 'true');
		}
		r = !a;
		return r;
	}
	function Parser() {
		this.success = false;
		this.errormsg = "";
		this.expression = "";

		this.pos = 0;

		this.tokennumber = 0;
		this.tokenprio = 0;
		this.tokenindex = 0;
		this.tmpprio = 0;

		this.ops1 = {
			"sin": Math.sin,
			"cos": Math.cos,
			"tan": Math.tan,
			"asin": Math.asin,
			"acos": Math.acos,
			"atan": Math.atan,
			"sqrt": Math.sqrt,
			"log": Math.log,
			"abs": Math.abs,
			"ceil": Math.ceil,
			"floor": Math.floor,
			"round": Math.round,
			"-": neg,
			"exp": Math.exp,
			//add langyuezhang
			"!":contrary
		};

		this.ops2 = {
			"+": add,
			"-": sub,
			"*": mul,
			"/": div,
			"%": mod,
			"^": Math.pow,
			",": append,
			"||": concat

		};

		this.functions = {
			"random": random,
			"fac": fac,
			"min": Math.min,
			"max": Math.max,
			"pyt": pyt,
			"pow": Math.pow,
			"atan2": Math.atan2
		};

		this.consts = {
			"E": Math.E,
			"PI": Math.PI,
			//add langyuezhang
			"true":true,
			"false":false
		};
	}

	Parser.parse = function (expr) {
		return new Parser().parse(expr);
	};

	Parser.execute = function(expr){
		return new Parser().execute(expr);
	}

	Parser.parseVariables = function(expr){
		var expression = new Parser().parse(expr);
		return expression.getVariables();
	}

	Parser.evaluate = function (expr, variables) {
		return Parser.parse(expr).evaluate(variables);
	};

	Parser.Expression = Expression;

	Parser.values = {
		sin: Math.sin,
		cos: Math.cos,
		tan: Math.tan,
		asin: Math.asin,
		acos: Math.acos,
		atan: Math.atan,
		sqrt: Math.sqrt,
		log: Math.log,
		abs: Math.abs,
		ceil: Math.ceil,
		floor: Math.floor,
		round: Math.round,
		random: random,
		fac: fac,
		exp: Math.exp,
		min: Math.min,
		max: Math.max,
		pyt: pyt,
		pow: Math.pow,
		atan2: Math.atan2,
		E: Math.E,
		PI: Math.PI
	};

	var PRIMARY      = 1 << 0;
	var OPERATOR     = 1 << 1;
	var FUNCTION     = 1 << 2;
	var LPAREN       = 1 << 3;
	var RPAREN       = 1 << 4;
	var COMMA        = 1 << 5;
	var SIGN         = 1 << 6;
	var CALL         = 1 << 7;
	var NULLARY_CALL = 1 << 8;

	Parser.prototype = {
		postProcessTokensList:function(tokensList){
			var newList = [];
			this.tokenPos = 0;
			while (this.tokenPos < tokensList.length)
			{
				var token = tokensList[this.tokenPos];
				this.tokenPos++;
				if (token.type == GbiTokenType.Identifier)
				{
					if ((newList.length > 0) && (newList[newList.length - 1].type == GbiTokenType.Dot))
					{
						if (GbiMethodType.contains(token.value))
						{
							token.type = GbiTokenType.Method;
						}
						else if (GbiPropertyType.contains(token.value))
						{
							token.type = GbiTokenType.Property;
						}
						else
						{
							throwError(9, token.value);
						}
					}

					else if (GbiTypeCode.contains(token.value))
					{
						token.type = GbiTokenType.Cast;
					}

//					else if (componentsList.contains(token.value))
//					{
//						token.type = StiTokenType.Component;
//					}

					else if (GbiFunctionType.contains(token.value))
					{
						if (GbiFunctionType[token.value] == GbiFunctionType.TotalsNameSpace)
						{
							if (tokenPos + 1 >= tokensList.length) throwError(4);
							token.value += "." + tokensList[tokenPos + 1].value;
							tokenPos += 2;
							if (!GbiFunctionType.contains(token.value)) throwError(11, token.value);
						}
						token.type = GbiTokenType.Function;
					}

					else if (GbiSystemVariableType.contains(token.value))
					{
						token.type = GbiTokenType.SystemVariable;
					}

					else if (token.value.toLowerCase() == "true" || token.value.toLowerCase() == "false")
					{
						if (token.value.toLowerCase() == "true")
							token.valueObject = true;
						else
							token.valueObject = false;
						token.type = GbiTokenType.Number;
					}
					else if (token.value.toLowerCase() == "null")
					{
						token.valueObject = null;
						token.type = GbiTokenType.Number;
					}

					//else if (variableManager.getVariableByName(token.value) != null)
					//{
					//	token.type = GbiTokenType.Variable;
					//}
//					else if (report.dictionary.variables.getByName(token.value) != null)
//					{
//						token.type = StiTokenType.Variable;
//					}
					else
					{
						throwError(5, token.value);
					}
				}

				newList.push(token);
			}

			return newList;
		},
		execute:function(inputExpression){
			var exp = this.parse(inputExpression);
			return exp.evaluate();
		},
		parse: function (inputExpression) {
			var tokensList = [];
			this.position = 0;
			this.expression = inputExpression;
			while(true){
				while (this.position < inputExpression.length && this.isWhiteSpace(inputExpression, this.position)) this.position++;
				if (this.position >= inputExpression.length) break;
				var token = null;
				var ch = inputExpression.charAt(this.position);
				var pos2 = 0;
				if (this.isLetter(ch)){
					pos2 = this.position + 1;
					while ((pos2 < inputExpression.length) && (this.isLetterOrDigit(inputExpression.charAt(pos2)) || inputExpression.charAt(pos2) == '_')) pos2++;
					token = new GbiToken();
					token.value = inputExpression.substr(this.position, pos2 - this.position);
					token.type = GbiTokenType.Identifier;
					this.position = pos2;
					tokensList.push(token);
					continue;
				}
				else if (this.isDigit(ch))
				{
					token = new GbiToken();
					token.type = GbiTokenType.Number;
					token.valueObject = this.scanNumber();
					tokensList.push(token);
					continue;
				}
				else if (ch == "'")
				{
					//#region "String"
					this.position++;
					pos2 = this.position;
					while (pos2 < inputExpression.length)
					{
						if (inputExpression.charAt(pos2) == "'") break;
						if (inputExpression.charAt(pos2) == '\\') pos2++;
						pos2++;
					}
					token = new GbiToken();
					token.type = GbiTokenType.String;
					token.valueObject = inputExpression.substr(this.position, pos2 - this.position);
					this.position = pos2 + 1;
					tokensList.push(token);
					continue;
					//#endregion
				}
				else if (ch == '"')
				{
					//#region "String"
					this.position++;
					pos2 = this.position;
					while (pos2 < inputExpression.length)
					{
						if (inputExpression.charAt(pos2) == '"') break;
						if (inputExpression.charAt(pos2) == '\\') pos2++;
						pos2++;
					}
					token = new GbiToken();
					token.type = GbiTokenType.Variable;
					token.valueObject = inputExpression.substr(this.position, pos2 - this.position);
					this.position = pos2 + 1;
					tokensList.push(token);
					continue;
					//#endregion
				}
				else
				{
					//#region Delimiters
					this.position++;
					var ch2 = "";
					if (this.position < inputExpression.length) ch2 = inputExpression.charAt(this.position);
					switch (ch)
					{
						case '.': token = new GbiToken(GbiTokenType.Dot);break;
						case '(': token = new GbiToken(GbiTokenType.LParenthesis);break;
						case ')': token = new GbiToken(GbiTokenType.RParenthesis);break;
						case '+': token = new GbiToken(GbiTokenType.Plus);break;
						case '-': token = new GbiToken(GbiTokenType.Minus);break;
						case '*': token = new GbiToken(GbiTokenType.Mult);break;
						case '/': token = new GbiToken(GbiTokenType.Div);break;
						case '%': token = new GbiToken(GbiTokenType.Percent);break;
						case '^': token = new GbiToken(GbiTokenType.Xor);break;
						case ',': token = new GbiToken(GbiTokenType.Comma);break;
						case ':': token = new GbiToken(GbiTokenType.Colon);break;
						case ';': token = new GbiToken(GbiTokenType.SemiColon);break;
						case '?': token = new GbiToken(GbiTokenType.Question);break;
						case '|':
							if (ch2 == '|')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.DoubleOr);
							}
							else token = new GbiToken(GbiTokenType.Or);
							break;
						case '&':
							if (ch2 == '&')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.DoubleAnd);
							}
							else token = new GbiToken(GbiTokenType.And);
							break;
						case '!':
							if (ch2 == '=')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.NotEqual);
							}
							else token = new GbiToken(GbiTokenType.Not);
							break;
						case '=':
							if (ch2 == '=')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.Equal);
							}
							else token = new GbiToken(GbiTokenType.Assign);
							break;
						case '<':
							if (ch2 == '<')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.Shl);
							}
							else if (ch2 == '=')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.LeftEqual);
							}
							else token = new GbiToken(GbiTokenType.Left);
							break;
						case '>':
							if (ch2 == '>')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.Shr);
							}
							else if (ch2 == '=')
							{
								this.position++;
								token = new GbiToken(GbiTokenType.RightEqual);
							}
							else token = new GbiToken(GbiTokenType.Right);
							break;
						default:
							token = new GbiToken(GbiTokenType.Unknown);
							token.valueObject = ch;
					}
					tokensList.push(token);
					continue;
				//#endregion
				}
			}
			tokensList = this.postProcessTokensList(tokensList);
			return new Expression(tokensList);
		},
		isDigit:function(ch){
			if ((code >= 48 && code <= 57) || code === 46)
				return true;
			return false;
		},
		isLetter: function (ch) {
			return ch.toUpperCase() !== ch.toLowerCase();
		},
		isLetterOrDigit:function(ch){
			return this.isLetter(ch) || this.isDigit(ch);
		},
		isWhiteSpace: function () {
			var code = this.expression.charCodeAt(this.position);
			if (code === 32 || code === 9 || code === 10 || code === 13) {
				this.pos++;
				return true;
			}
			return false;
		},
		isDigit:function(ch){
			ch = ch.charCodeAt();
			return (ch >= 48 && ch <= 57);
		},
		scanNumber:function(){
			var typecode = GbiTypeCode.Int;
			var posBegin = this.position;
			//integer part
			while (this.position != this.expression.length && this.isDigit(this.expression.charAt(this.position)))
			{
				this.position++;
			}
			if (this.position != this.expression.length && this.expression.charAt(this.position) == '.' &&
				this.position + 1 != this.expression.length && this.isDigit(this.expression.charAt(this.position + 1)))
			{
				//fractional part
				this.position++;
				while (this.position != this.expression.length && this.isDigit(this.expression.charAt(this.position)))
				{
					this.position++;
				}
				typecode = GbiTypeCode.Number;
			}
			var nm = this.expression.substr(posBegin, this.position - posBegin);
			//nm = nm.replace(".", System.Globalization.CultureInfo.CurrentCulture.NumberFormat.NumberDecimalSeparator);     !!!!!

			if (this.position != this.expression.length && this.isLetter(this.expression.charAt(this.position)))
			{
				//postfix
				posBegin = this.position;
				while (this.position != this.expression.length && this.isLetter(this.expression.charAt(this.position)))
				{
					this.position++;
				}
				var postfix = this.expression.substr(posBegin, this.position - posBegin).toLowerCase();
				if (postfix == "f") typecode = GbiTypeCode.Number;	//TypeCode.Single;
				if (postfix == "d") typecode = GbiTypeCode.Number;  //TypeCode.Double;
				if (postfix == "m") typecode = GbiTypeCode.Number;  //TypeCode.Decimal;
				if (postfix == "l") typecode = GbiTypeCode.Int;     //TypeCode.Int64;
				if (postfix == "u" || postfix == "ul" || postfix == "lu") typecode = GbiTypeCode.UInt;  //TypeCode.UInt64;
				
			}

			return this.convertChangeType(nm, typecode);
		},
		convertChangeType:function(st, typeCode){
			switch (typeCode)
			{
				case GbiTypeCode.Number:	return parseFloat(st); break;
				case GbiTypeCode.Int:		return parseInt(st); break;
				case GbiTypeCode.UInt:		return parseInt(st); break;
				//default: 					return st;
			}
			return st;
		}


	};

	scope.Parser = Parser;
	return Parser
})(typeof exports === 'undefined' ? {} : exports);
