package com.xb.common;

public class WFConstants {
	
	public static class FuncVarsParm{
		public static final String PARM_VAR_TYPE = "varType";
		public static final String PARM_VAR_CODE = "varCode";
		public static final String PARM_VAR_DESCP = "varDescp";
		public static final String PARAM_VAR_EXPRESSION = "varExpression";
	}
	
	public static class CustVarTypes{
		public static final String VAR_TYPE_USER = "U";
		public static final String VAR_TYPE_VAR = "V";
	}
	public static final String ASSIGNER_TYPE_CUST = "C";//定义工作流人员>选择“自定义人员”
	
	public static class OptTypes{
//		public static final String REQUEST = "RQ";
//		public static final String APPROVE = "AP";
		public static final String COMMIT = "C";//提交
		public static final String REJECT = "RJ";//退回
		public static final String VETO = "V";//否决
		public static final String FORWARD = "F";//转交
		public static final String RECALL = "RC";//撤回
		public static final String LET_ME_DO = "LMD";//我来处理
		public static final String DISPATCH = "DP";//流程调度
		public static final String TRACK = "TK";//流程跟踪
	}
	
	public static class OptTypesDescp{
//		public static final String REQUEST = "RQ";
//		public static final String APPROVE = "AP";
		public static final String COMMIT = "流程提交";//提交
		public static final String REJECT = "流程退回";//退回
		public static final String VETO = "流程否决";//否决
		public static final String FORWARD = "流程转交";//转交
		public static final String RECALL = "流程撤回";//撤回
		public static final String LET_ME_DO = "我来处理";//我来处理
		public static final String DISPATCH = "流程调度";//流程调度
		public static final String TRACK = "流程跟踪";//流程跟踪
	}
	
	
	public static class TxTypes{
		public static final String COUNTERSIGN = "M";
		public static final String NORMAL = "S";
	}
	
	public static class WFStatus{
		public static final String IN_PROCESS = "I";
		public static final String DONE = "D";
	}
	
	public static class ApiParams{
		public static final int STATUS_CODE_SUCC = 0;
		public static final int STATUS_CODE_INVALID = 1;
		public static final int STATUS_CODE_NO_RECORD = 2;
		public static final int STATUS_CODE_OPT_NOT_ALLOW = 3;
		public static final int STATUS_CODE_WF_NOT_DEFINED = 4;
		public static final int STATUS_CODE_FAIL = 9;
		
		public static final String STATUS_MSG_OPT_NOT_ALLOW = "该操作不被允许";
		
		public static final String RETURN_CODE = "return_code";
		public static final String RETURN_MSG = "return_msg";
		public static final String RETURN_WF_INST_NUM = "wfInstNum";
		public static final String RETURN_CURR_TASK_ID = "currTaskId";
		public static final String RETURN_TASK_OPTIONS = "taskOptions";
		public static final String RETURN_RECORDS = "records";
		public static final String RETURN_RECORDS_COUNT = "count";
		
		public static final String PARM_USER_ID = "userId";
		public static final String PARM_REFMK_ID = "refMkid";
		public static final String PARM_OPT_CODE = "optCode";
		public static final String PARM_INST_NUM = "wfInstNum";
		public static final String PARM_CALLBACK_URL = "callback_url";
	}
	
	public static class AssignType{
		public static final String USER = "U";
		public static final String GROUP = "G";
		public static final String CUST = "C";
	}
	
	public static class TaskSelectAllFlag{
		public static final String YES = "Y";
		public static final String NO = "N";
	}

	public static enum TaskTypes {
		S("S", "start-task"), E("E", "end-task"), U("U", "user-task"), C("C", "rs-cond-task");

		private String typeCode;
		private String typeDescp;

		TaskTypes(String code, String descp) {
			this.typeCode = code;
			this.typeDescp = descp;
		}

		public String getTypeCode() {
			return typeCode;
		}

		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}

		public String getTypeDescp() {
			return typeDescp;
		}

		public void setTypeDescp(String typeDescp) {
			this.typeDescp = typeDescp;
		}
	}
	
	public static TaskTypes parse(String descp){
		switch (descp) {
		case "start-task":
			return TaskTypes.S;
		case "end-task":
			return TaskTypes.E;
		case "user-task":
			return TaskTypes.U;
		case "rs-cond-task":
			return TaskTypes.C;
		default:
			return TaskTypes.U;
		}
	}
	
	public static String parse2Code(String descp){
		switch (descp) {
		case "start-task":
			return TaskTypes.S.getTypeCode();
		case "end-task":
			return TaskTypes.E.getTypeCode();
		case "user-task":
			return TaskTypes.U.getTypeCode();
		case "rs-cond-task":
			return TaskTypes.C.getTypeCode();
		default:
			return TaskTypes.U.getTypeCode();
		}
	}

}
