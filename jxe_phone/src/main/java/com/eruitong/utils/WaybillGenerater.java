package com.eruitong.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaybillGenerater {

	
	/**
	 * @param bno
	 * @return boolean
	 *//*
	public static boolean validateExpressWaybillNo(String bno) {
		if (bno == null ||
			bno.length() != 12 || bno.startsWith("00")) {
			return false;
		}
		char c = genCheckCode(bno.substring(3, 11));
		char c1 = getCheckCodeOther(bno.substring(0, 11));
		if(c == bno.charAt(11) || c1 == bno.charAt(11)){
			if(IWaybillConstant.INNER_EXPRESS_PRE.equals(bno.substring(0, 3)) || IWaybillConstant.SINGLE_PRE_BILLNO.equals(bno.substring(0, 3))){
				return true;
			}
			if(Integer.valueOf(bno.substring(3, 11)) > 34050000){
				return true;
			}
		}
		return false;
	}
*/	
	/**
	 * @param bno
	 * @return boolean
	 */
	public static boolean validateWaybillNo(String bno) {
		if (bno == null ||
				bno.length() != 12 || bno.startsWith("00")) {
				return false;
			}
			char c = genCheckCode(bno.substring(3, 11));
			return c == bno.charAt(11);
		}
	
	/**
	 * 校验子单号
	 * @param bno
	 * @return
	 */
	public static boolean validateChildWaybillNo(String bno) {
		if (bno == null ||
				bno.length() != 12 || !bno.startsWith("00")) {
				return false;
			}
			char c = genCheckCode(bno.substring(3, 11));
			return c == bno.charAt(11);
		}
	
	/**
	 * 单号印刷错误,临时添加验证代码;
	 * @param js 
	 *        参数为运单号的前11位;
	 *        
	 * 具体算法 前11位数字;
	 * 1、奇数位之和乘以3加上偶数位之和除以10的余数；
     * 2、当余数为0时则校验码为0；当余数不为0时校验码为 10-余数;
	 * @return
	 */
	private static char getCheckCodeOther(String js) {
		if (js == null || js.length() != 11) {
			return ' ';
		}
		char CB = ' ';
		char[] CBS = js.toCharArray();
		int JS = 0;
		int OS = 0;
		for (int i = 0; i < CBS.length; i++) {
			if (i % 2 == 0) {
				// 计算奇数位之和
				JS += Integer.parseInt(String.valueOf(CBS[i]));
			} else {
				// 计算偶数位之和
				OS += Integer.parseInt(String.valueOf(CBS[i]));
			}
		}
		
		int m = 10 - (JS * 3 + OS) % 10;
		
		//如果余数等于10 则返回0;
		CB = String.valueOf(m == 10 ? 0 : m).charAt(0);
		return CB;
	}

	/**
	 * @param js1
	 *        029 00000001 9 参数为除去前三位前缀和最后一位校验位
	 * @return
	 */
	private static char genCheckCode(String js) {
		if (js == null || js.length() != 8){
			return ' ';
		}

		char CB;
		int P0, P1, P2, P3, P4, P5, P6, P7;
		int A0, A1, A2, A3, A4, A5, A6, A7;
		int Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7;
		char s0, s1, s2, s3, s4, s5, s6, s7;

		s0 = js.charAt(7);
		s1 = js.charAt(6);
		s2 = js.charAt(5);
		s3 = js.charAt(4);
		s4 = js.charAt(3);
		s5 = js.charAt(2);
		s6 = js.charAt(1);
		s7 = js.charAt(0);

		A0 = s0 - '0';
		A1 = s1 - '0';
		A2 = s2 - '0';
		A3 = s3 - '0';
		A4 = s4 - '0';
		A5 = s5 - '0';
		A6 = s6 - '0';
		A7 = s7 - '0';
	
		P0 = Math.abs(~A0) * A0;
		P1 = Math.abs(~A1) * A1;
		P2 = Math.abs(~A2) * A2;
		P3 = Math.abs(~A3) * A3;
		P4 = Math.abs(~A4) * A4;
		P5 = Math.abs(~A5) * A5;
		P6 = Math.abs(~A6) * A6;
		P7 = Math.abs(~A7) * A7;

		Q0 = (P0 / 10) + (P0 - 10 * (P0 / 10));
		Q1 = (P1 / 10) + (P1 - 10 * (P1 / 10));
		Q2 = (P2 / 10) + (P2 - 10 * (P2 / 10));
		Q3 = (P3 / 10) + (P3 - 10 * (P3 / 10));
		Q4 = (P4 / 10) + (P4 - 10 * (P4 / 10));
		Q5 = (P5 / 10) + (P5 - 10 * (P5 / 10));
		Q6 = (P6 / 10) + (P6 - 10 * (P6 / 10));
		Q7 = (P7 / 10) + (P7 - 10 * (P7 / 10));

		int Q = Q0 + Q1 + Q2 + Q3 + Q4 + Q5 + Q6 + Q7;
		int m = ((Q / 10) + 1) * 10;
		m = (m - Q) - 10 * ((m - Q) / 10);

		CB = String.valueOf(m).charAt(0);
		//实现秦楚新干线物流的新算法
		/*if ("qinchu".equals(CompanyContext.getInstance().getCompanyCode())){
			//定义参与运算的数值
			final char VP = '1'; 
			//相减后取绝对值,转换为char返回
			return (char)('0' + Math.abs(CB-VP));
		}*/
		return CB;
	}

	public static String getWaybillNo(String suffix, String billSeq){
		int len = billSeq.length();
		if (len < 8){
			StringBuilder builder = new StringBuilder();
			for (int i=0; i<8-len; i++){
				builder.append("0");
			}
			builder.append(billSeq);
			billSeq = builder.toString();
		}
		return new StringBuilder().append(suffix).append(billSeq).append(genCheckCode(billSeq)).toString();
	}

	private static void outputToFile(String file, String content){
		BufferedWriter outputFile = null;
		try {
			outputFile = new BufferedWriter(new FileWriter(file));
			String[] st = content.split(",");
			for (String s : st){
				outputFile.write(s);
				outputFile.newLine();
			}
			outputFile.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally{
			if (outputFile != null){
				try {
					outputFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 生成运单号列表
	 * @param suffix
	 * @param baseSeq
	 * @param count
	 * @return
	 */
	public static List<String> generaterWaybillNos(String suffix, Long baseSeq, int count){
		List<String> billNoList = new ArrayList<String>(count);
		for (Long billSeq = baseSeq; billSeq < baseSeq + count; billSeq++) {
			String billNo = getWaybillNo(suffix, String.valueOf(billSeq));
			billNoList.add(billNo);
		}
		return billNoList;
	}
	
	
	public static void main(String[] args){
		/**
		 * 运单号生成历史记录：需修改baseSeq和count参数
		 * 1. 2010-06-19 029 33300001---33310000 (共10000)
		 * 2. 2010-09-27 029 33310001---33320000 (共10000)
		 * 3. 2010-09-27 029 33320001---33330000 (共10000)
		 * 4. 2010-11-04 029 33330001---33350000 (共20000)
		 * 5. 2010-12-09 029 33350001---33400000 (共50000)
		 * 6. 2010-12-15 029 33400001---33450000 (共50000)赵亮要的10W包括上次的5W。
		 * 7. 2011-06-23 029 33450001---33600000 (共150000)
		 * 8. 2011-09-24 029 33600001---33610000 (共10000)
		 * 9. 2011-10-24 029 33610001---33810000 (共200000)孙娜20W份母单号
		 * 10.2012-05-24 029 33810001---33860000 (共 50000)孙娜5W份母单号
		 * 11.2012-08-16 029 33860001---33910000 (共 50000)陈翠5W份母单号
		 * 
		 * 12.2012-12-04 029 33910001---33960000 (共 50000)刘伟伟5W份母单号
		 * 13.2013-05-06 029 34000001---34050000 (共 50000)毛艳茹5W份母单号
		 * 14.2013-08-06 029 34050001---34100000 (共 50000)毛艳茹5W份母单号
		 * 15.2013-09-13 029 34100001---34200000 (共 100000)刘总10W份母单号
		 * 16.2013-12-05 029 34300001---34400000 (共 100000)邓清磊10W份母单号
		 * 16.2013-12-05 029 34400001---34450000 (共  50000)杨丽10W份母单号
		 * 
		 * 子单号：002
		 * 1. 2010-12-09 002 00000001---00025000 (共25000)
		 * 2. 2011-03-31 002 00025001---00045000
		 * 3. 2011-08-03 002 00045001---00065000 (共20000)王伍峰要 2W 子件。
		 * 4. 2012-03-06 002 00065001---00105000 (共40000)孙  娜要 4W 子件
		 * 
		 * 内部件:888
		 * 1. 2012-02-22 888 33300001---33320000 (共20000)孙娜
		 * 
		 * 快递签回单号：123 回单号
		 * 1. 2012-03-06 123 33300001---33320000 (共20000)孙娜要 2W 快递回单号
		 */
		
		Long baseSeq = 34400001L;
		List<String> billNoList = generaterWaybillNos("029", baseSeq, 50000);
		StringBuilder builder = new StringBuilder(); 
		for (String billNo : billNoList){
			System.out.println(billNo + "  " + validateWaybillNo(billNo));
			builder.append(billNo).append(",");
		}
		
		outputToFile("c:/WaybillNoList_2013-12-31.txt", builder.toString());
	}
	
	/**
	 * @param bno
	 * @return boolean
	 */
	public static boolean validateCarCode(String bno) {
		if (bno == null || bno.length() != 12 || !bno.startsWith("333")) {
			return false;
		}
		char c = genCheckCode(bno.substring(3, 11));
		return c == bno.charAt(11);
	}
}