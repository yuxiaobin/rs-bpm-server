var RS_TYPE_START = "start-task";
var RS_TYPE_END = "end-task";
var RS_TYPE_USER = "user-task";
var RS_TYPE_CONDITION = "rs-cond-task";
var RS_ATTR_ASSIGNERS = "rs-data-assigners";
var RS_ATTR_TASK_TYPE = "rs-data-type";
var RS_ATTR_TX_CHOICES = "TX_CHOICES";
var RS_ATTR_TX_PR_CHOICES = "TX_PR_CHOICES";
var RS_ATTR_TX_BK_CHOICES = "TX_BK_CHOICES";
var RS_ATTR_SIGN_CHOICES = "SIGN_CHOICES";
var RS_ATTR_COND_EXPRESSION = "rs-data-cond-exp";

var RS_NODE_ATTR_DESCP = "taskDescp";
var RS_NODE_ATTR_DESCP_DISP = "taskDescpDisp";

var CONNECTION_LABEL_NORMAL = "Next";
var CONNECTION_LABEL_TRUE = "Yes";
var CONNECTION_LABEL_FALSE = "No";
var CONNECTION_CON_VALUE = "con_value";

var EXPRESSION_REG_EQUAL = /\b\s?=\s?\b/;//验证是否含有单个=号的表达式: a=1, 这种赋值的表达式是非法的，判断是否相等需要a==1
var EXPRESSION_REG_CN = /[》《！＝]+/;