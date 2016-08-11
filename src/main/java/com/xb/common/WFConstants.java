package com.xb.common;

public class WFConstants {
	
	public static class OptTypes{
		public static final String REQUEST = "RQ";
		public static final String APPROVE = "AP";
		public static final String REJECT = "RJ";
	}
	
	public static class WFStatus{
		public static final String IN_PROCESS = "I";
		public static final String DONE = "D";
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
