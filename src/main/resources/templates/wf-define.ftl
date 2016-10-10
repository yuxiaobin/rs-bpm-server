<!doctype html>
<html>
<head>
    <title>Define Workflow</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="stylesheet" href="${base.contextPath}/static/css/jsPlumbToolkit-defaults.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/main.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/jsPlumbToolkit-demo.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/plugin/jquery.contextMenu.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/rs-bpm.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/theme/base/jquery.ui.all.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/properties.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/demo.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/plugin/bootstrap-select.css">
    <script>
        var basePath = "${base.contextPath}";
        <#if refMkid?exists>
            var refMkid = "${refMkid}";
        </#if>
    </script>
</head>
<body data-demo-id="statemachine" data-library="dom" class="container theme-showcase" >
<header class="site-header">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>功能模块ID：${refMkid}</h1>
            </div>
            <div class="col-xs-6">
                <div style="position: absolute;right: 10px;"> <h3><a href="${base.contextPath}/wf">回首页</a></h3></div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-5">
                <ul class="nav nav-pills nav-justified">
                    <li role="presentation" class="active"><a href="#" onclick="showWfDef(this)">工作流</a></li>
                    <li><a href="#" onclick="showCustVar(this)">自定义项</a></li>
                </ul>
            </div>
            <div class="col-xs-6" style="margin-left: 30px">
                <input type="button" class="btn btn-lg btn-primary" value="Save" id="Save">
                <input type="button" class="btn btn-lg btn-primary" value="Reset" id="resetBtn">
            </div>
        </div>
    </div>
</header>
<div class="jtk-demo-main" id="wfDefDiv">
    <!-- demo -->
    <div class="rsmenu">
        <div class="w menu-task" rs-data-type="user-task">事务</div>
        <div class="w menu-task rs-cond-task" rs-data-type="rs-cond-task"><div class="task-descp">条件节点</div></div>
    </div>
    <div class="rscontainer jtk-demo-canvas canvas-wide statemachine-demo jtk-surface jtk-surface-nopan " id="canvas">
    </div>

</div>
<div class="row" id="custVarDiv" ng-app="funcVarApp" ng-controller="ctrl" style="display: none">
    <div class="col-xs-12">
        <table class="table" id="funcVarTable"><!--table-striped-->
            <thead><tr>
                <th width="20%">类型</th>
                <th width="20%">编码</th>
                <th width="20%">名称</th>
                <th width="40%">表达式</th>
            </tr></thead>
            <tbody>
            <tr ng-repeat="funcVar in funcVarList" ng-click="editFuncVar($event,funcVar)">
                <td ng-if="funcVar.varType=='U'">自定义人员</td>
                <td ng-if="funcVar.varType=='V'">自定义变量</td>
                <td>{{funcVar.varCode}}</td>
                <td>{{funcVar.varDescp}}</td>
                <td>{{funcVar.varExpression}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="col-xs-12">
        <div class="row">
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-default" ng-click="addCustVar()">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>增加
                </button>
                <button type="button" class="btn btn-default" ng-click="deleteCustVar()">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除
                </button>
                <button type="button" class="btn btn-default" ng-click="saveCustVar()">
                    <span class="glyphicon glyphicon-save" aria-hidden="true"></span>保存
                </button>
                <button type="button" class="btn btn-default" ng-click="addCustVar()">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>放弃
                </button>
            </div>
        </div>
    </div>
    <div class="col-xs-12" id="funcVarEdit" style="padding-top:10px;display:none">
        <div class="row">
            <div class="col-xs-3" >
                <select id="selectVarType" class="selectpicker" ng-model="custVar.varType">
                    <option value="U">自定义人员</option>
                    <option value="V">自定义变量</option>
                </select>
            </div>
            <div class="col-xs-3">
                <div class="input-group">
                    <span class="input-group-addon" >编码：</span>
                    <input type="text" name="varCode" ng-model="custVar.varCode">
                </div>
            </div>
            <div class="col-xs-4">
                <div class="input-group">
                    <span class="input-group-addon" >名称：</span>
                    <input type="text" name="varDescp" ng-model="custVar.varDescp">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="input-group">
                    <span class="input-group-addon" >表达式：</span>
                    <textarea class="form-control" id="varExpression" ng-model="custVar.varExpression"></textarea>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">错误</h4>
                </div>
                <div class="modal-body">
                    <div class="alert alert-warning">{{funcVarError}}</div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">去修改</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="false" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <iframe style="zoom: 0.8;" height="700px" src="" frameBorder="0" width="99.6%"></iframe>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="modal fade" id="condConnModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">属性设置</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="input-group">
                            <span class="input-group-addon" >显示名称：</span>
                            <input type="text" class="form-control" id="conn_nameDisp">
                        </div>
                    </div>
                </div>
                <div class="row" id="condConnId">
                    <div class="col-xs-12">
                        <div class="bs-example input-group">
                        <!--<div>判断条件：</div>-->
                        <span class="input-group-addon" >判断条件：</span>
                        <div class="" role="group" aria-label="">
                            <div class="btn btn-default text-left" style="text-align:left">
                                <input type="radio" class="btn btn-default" name="condResult" id="condResultTrue" value="true">
                                <label for="condResultTrue" style="font-weight: inherit;">满足条件</label>
                            </div>
                            <div class="btn btn-default text-left" style="text-align:left">
                                <input type="radio" class="btn btn-default" name="condResult"  id="condResultFalse" value="false">
                                <label for="condResultFalse" style="font-weight: inherit;">不满足条件</label>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>

                <input type="hidden" name="sourceNodeId" id="sourceNodeId">
                <input type="hidden" name="currConnId" id="currConnId">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="updateCondConnEdit()">确定</button>
                <button type="button" class="btn btn-default" onclick="cancelCondConnEdit()">取消</button>
            </div>
        </div>
    </div>
</div>

<ul id="connMenu" class="contextMenu">
    <li class=""><a href="#edit">Edit</a></li>
    <li class=""><a href="#delete">Delete</a></li>
</ul>
<ul id="activityMenu" class="contextMenu">
    <li class=""><a href="#edit">Edit</a></li>
    <li class=""><a href="#delete">Delete</a></li>
</ul>
<ul id="activityMenu2StartEnd" class="contextMenu">
    <li class=""><a href="#edit">Edit</a></li>
</ul>
<!-- JS -->
<!-- support lib for bezier stuff -->
<script src="${base.contextPath}/static/js/lib/jsBezier-0.8.js"></script>
<!-- event adapter -->
<script src="${base.contextPath}/static/js/lib/mottle-0.7.2.js"></script>
<!-- geometry functions -->
<script src="${base.contextPath}/static/js/lib/biltong-0.3.js"></script>
<!-- drag -->
<script src="${base.contextPath}/static/js/lib/katavorio-0.17.0.js"></script>
<!-- jsplumb util -->
<script src="${base.contextPath}/static/js/lib/util.js"></script>
<script src="${base.contextPath}/static/js/lib/browser-util.js"></script>
<!-- main jsplumb engine -->
<script src="${base.contextPath}/static/js/lib/jsPlumb.js"></script>
<!-- base DOM adapter -->
<script src="${base.contextPath}/static/js/lib/dom-adapter.js"></script>
<script src="${base.contextPath}/static/js/lib/overlay-component.js"></script>
<!-- endpoint -->
<script src="${base.contextPath}/static/js/lib/endpoint.js"></script>
<!-- connection -->
<script src="${base.contextPath}/static/js/lib/connection.js"></script>
<!-- anchors -->
<script src="${base.contextPath}/static/js/lib/anchors.js"></script>
<!-- connectors, endpoint and overlays  -->
<script src="${base.contextPath}/static/js/lib/defaults.js"></script>
<!-- bezier connectors -->
<script src="${base.contextPath}/static/js/lib/connectors-bezier.js"></script>
<!-- state machine connectors -->
<script src="${base.contextPath}/static/js/lib/connectors-statemachine.js"></script>
<!-- flowchart connectors -->
<script src="${base.contextPath}/static/js/lib/connectors-flowchart.js"></script>
<!-- SVG renderer -->
<script src="${base.contextPath}/static/js/lib/renderers-svg.js"></script>

<!-- common adapter -->
<script src="${base.contextPath}/static/js/lib/base-library-adapter.js"></script>
<!-- no library jsPlumb adapter -->
<script src="${base.contextPath}/static/js/lib/dom.jsPlumb.js"></script>
<script src="${base.contextPath}/static/js/lib/bezier-editor.js"></script>
<!-- /JS -->

<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/jquery-ui.js"></script>
<!--<script src="${base.contextPath}/static/js/jquery.ui.dialog.js"></script>-->
<script src="${base.contextPath}/static/js/plugin/jquery.contextMenu.js"></script>
<!--  demo code -->
<script src="${base.contextPath}/static/js/common.js"></script>
<script src="${base.contextPath}/static/js/rs-bpm.js"></script>

<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/plugin/bootstrap-select.js"></script>
<script src="${base.contextPath}/static/js/app-wf.js"></script>

<script>
    var parmJsonStr = "";
    var custFuncVarArray;
    function editTask(id_){
        var parmJson = {};
        var el = $("#"+id_);
        parmJson.taskPgId = el.attr("id");;
        parmJson.taskDescpDisp = el.text();
        parmJson.taskDescp	=el.attr("taskDescp");
        parmJson.refMkid = refMkid;
        parmJson.taskType = el.attr(RS_ATTR_TASK_TYPE);
        if(RS_TYPE_CONDITION==parmJson.taskType){
            parmJson.custFuncVarArray = custFuncVarArray;
            parmJson.condExp = el.attr(RS_ATTR_COND_EXPRESSION);
            //TODO 1010: pass expression & display & description
        }else{
            var assignersStr = el.attr(RS_ATTR_ASSIGNERS);
            if(assignersStr==undefined || assignersStr==""){
                assignersStr = "[]";
            }
            parmJson.assigners = $.parseJSON(assignersStr);
            var tx_choices_str = el.attr(RS_ATTR_TX_CHOICES);
            if(tx_choices_str == undefined || tx_choices_str==""){
                tx_choices_str = "{}";
            }
            parmJson.TX_CHOICES = $.parseJSON(tx_choices_str);
            var tx_pr_choices_str = el.attr(RS_ATTR_TX_PR_CHOICES);
            if(tx_pr_choices_str == undefined || tx_pr_choices_str==""){
                tx_pr_choices_str = "{}";
            }
            parmJson.TX_PR_CHOICES = $.parseJSON(tx_pr_choices_str);
            var tx_bk_choices_str = el.attr(RS_ATTR_TX_BK_CHOICES);
            if(tx_bk_choices_str == undefined || tx_bk_choices_str==""){
                tx_bk_choices_str = "{}";
            }
            parmJson.TX_BK_CHOICES = $.parseJSON(tx_bk_choices_str);
            var sign_choices_str = el.attr(RS_ATTR_SIGN_CHOICES);
            if(sign_choices_str == undefined || sign_choices_str==""){
                sign_choices_str = "{}";
            }
            parmJson.SIGN_CHOICES = $.parseJSON(sign_choices_str);
            parmJson.txCode		=el.attr("txCode");
            parmJson.txType		=el.attr("txType");
            parmJson.buzStatus	=el.attr("buzStatus");
            var timeLimit = el.attr("timeLimit");
            if(timeLimit!=""){
                timeLimit = parseInt(timeLimit);
            }
            parmJson.timeLimit	=timeLimit;
            parmJson.timeLimitTp=el.attr("timeLimitTp");
            var alarmTime = el.attr("alarmTime");
            if(alarmTime!=""){
                alarmTime = parseInt(alarmTime);
            }
            parmJson.alarmTime	=alarmTime;
            parmJson.alarmTimeTp=el.attr("alarmTimeTp");
            parmJson.moduleId	=el.attr("moduleId");
            parmJson.runParam	=el.attr("runParam");
        }

        parmJsonStr = JSON.stringify(parmJson);
        $('iframe').attr("src",basePath+"/wfadmin/task?taskData="+parmJsonStr);
        $('#myModal').modal({backdrop:false});
    }
    
    window.addEventListener('message', receiveMessage, false);
    function receiveMessage(evt) {
        var taskData = $.parseJSON(evt.data);
        if(taskData.opt=="U"){
            if(taskData.taskType ==RS_TYPE_CONDITION ){
                $("#"+taskData.taskPgId).attr("taskDescp",taskData.taskDescp)
                        .attr("taskDescpDisp",taskData.taskDescpDisp)
                        .attr(RS_ATTR_COND_EXPRESSION,taskData.condExp);
                $("#"+taskData.taskPgId +" .task-descp").html(taskData.taskDescpDisp);
            }else{
                $("#"+taskData.taskPgId).attr(RS_ATTR_ASSIGNERS, JSON.stringify(taskData.assigners))
                        .attr(RS_ATTR_TX_CHOICES,JSON.stringify(taskData.TX_CHOICES))
                        .attr(RS_ATTR_TX_PR_CHOICES,JSON.stringify(taskData.TX_PR_CHOICES))
                        .attr(RS_ATTR_TX_BK_CHOICES,JSON.stringify(taskData.TX_BK_CHOICES))
                        .attr(RS_ATTR_SIGN_CHOICES,JSON.stringify(taskData.SIGN_CHOICES))
                        .attr("txCode",taskData.txCode)
                        .attr("txType",taskData.txType)
                        .attr("buzStatus",taskData.buzStatus)
                        .attr("timeLimit",taskData.timeLimit)
                        .attr("timeLimitTp",taskData.timeLimitTp)
                        .attr("alarmTime",taskData.alarmTime)
                        .attr("alarmTimeTp",taskData.alarmTimeTp)
                        .attr("moduleId",taskData.moduleId)
                        .attr("runParam",taskData.runParam)
                        .attr("taskDescp",taskData.taskDescp)
                        .attr("taskDescpDisp",taskData.taskDescpDisp)
                ;
                $("#"+taskData.taskPgId)
                        .html(taskData.taskDescpDisp+"<div class=\"ep\"></div>");
            }
        }else if(taskData.opt=="D"){
            window.jsp.remove(taskData.taskPgId);
        }
        if(taskData.opt=="C"){
            $("#myModal").modal("hide");
        }else{
            setTimeout(function(){
                $("#myModal").modal("hide");
            },1000);
        }

    }
</script>
</body>
</html>
