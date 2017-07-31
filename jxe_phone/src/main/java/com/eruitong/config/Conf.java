package com.eruitong.config;

public class Conf {

	/** 登陆的接口 **/
	public static String LoginUrl = "/pda/devLogin.action";

	/** 基础数据更新 **/
	public static String bsseUpdataUrl = "/pda/syncdata.action";
	/** 基础数据更新 **/
	public static String waybillUpdataUrl = "/pda/applyWaybillNoSequence.action";
	/** 根据单号查信息 **/
	public static String queryWaybillNo = "/pda/queryDeliverPrintListAction.action";
	
	/** 根据单号查信息 **///测试
	public static String queryWaybillNoTest = "/pda/queryDeliverPrintListTestAction.action";
	
	/** 查询预约派件 **/
	public static String querySubscribeWaybillNo = "/pda/queryPdaNotifyWaybillAction.action";
	
	/** 提交预约派件 **/
	public static String pushSubscribeWaybillNo = "/pda/createPdaNotiryWaybill.action";
	
	/** 签收订单 **/
	public static String SigninWaybill = "/pda/deliverSigninAction.action";

	/** 签收订单 **/
	public static String SigninWaybillTest = "/pda/pdaGatherSigninedActions.action";
	
	/** 签收订单 **/
	public static String SigninWaybillImageUpLoad = "/pda/signinWaybillImageUpLoadAction.action";

	/** 查询票数 **/
	public static String queryWaybillNumber = "/pda/queryInputWaybillsAction.action";

	/** 查询客户信息 **/
	public static String queryCustomer = "/pda/queryCustomer.action";

	/** 上传录单信息 **/
	public static String saveWaybill = "/pda/saveWaybill.action";
	/** 上传 派件信息 **/
	public static String deliverSigninAction = "/pda/deliverSigninAction.action";
	/** 查询订单 **/
	public static String queryDeliverPrintListAction = "/pda/queryDeliverPrintListAction.action";

	/** 查询代收货款 **/
	public static String queryGoodsPaymentWaybillAction = "/pda/queryGoodsPaymentWaybill.action";

	/** 代收货款登记 **/
	public static String enrolGoodsChargeFeeAction = "/pda/enrolGoodsChargeFee.action";

	/** 代收货款转款 **/
	public static String paymentGoodsChargeFeeAction = "/pda/paymentGoodsChargeFee.action";
	
	
	/** 查询运单清单数据 **/
	public static String queryWaybillAction = "/pda/queryEditWaybillAction.action";

	public static String updateWaybill = "/pda/updateWaybill.action";

	/** 修改 **/
	public static String updateIsUpdateAction = "/pda/updateIsUpdateAction.action";

	/** 根据单号查信息 **/
	//public static String querySignImage = "/pda/querySignImageAction.action";
	
	/**
	 * 查询派送费
	 */
	public static String queryDeliverCommission = "/pda/queryDeliverCommission.action";

	public static String queryGatherWaybillNumber = "/pda/queryGatherWaybillsAction.action";
	
	/**
	 * 附加值
	 */
	public static String createAddedValueAction = "/pda/createAddedValueAction.action";
	
	public static String queryProvisionTemplateAction = "/pda/queryProvisionTemplateAction.action";
	
	/**
	 * 总关注人数查询
	 */
	public static String findOpenMobileTotal = "/weixin/findOpenMobileTotal.action";
	
	
	
	/**
	 * 总关注人数查询
	 */
	public static String findAppraiseMobile = "/weixin/findAppraiseMobile.action";
	
	
	/**
	 * 巴枪操作记录
	 */
	public static String addBarRecode = "/pda/addBarRecodeAction.action";
	
	/**
	 * 收货量查询
	 */
	public static String queryConsignorPiecesNum = "/pda/queryConsignorPiecesNum.action";
	
	/**
	 * 收件提成查询
	 */
	public static String queryConsignorCommission = "/pda/queryConsignorCommission.action";



	
}
