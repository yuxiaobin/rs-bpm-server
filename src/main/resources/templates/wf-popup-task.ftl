<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/docs.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/plugin/bootstrap-select.css">
    <style>
        .form-wrapper {
            padding: 40px 30px;
        }

    </style>
    <script>
        <#if taskData?exists>
            var taskData = ${taskData};
        </#if>
        var basePath = "${base.contextPath}";
    </script>
</head>
<body ng-app="taskApp" ng-controller="taskCtrl">
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideModal()"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="editTaskTitle">事务属性设置</h4>
</div>
<div class="modal-body">
    <ul class="nav nav-tabs">
        <li role="presentation" class="active"><a href="javascript:void(0)" onclick="changeTab(this,'updateTaskPropertiesForm')">事务定义</a></li>
        <li role="presentation" ng-hide="!showAssignerEdit"><a href="javascript:void(0)" onclick="changeTab(this,'updateTaskPropertiesForm2')">执行人员</a></li>
    </ul>
    <div class="row form-wrapper">
    <form id="updateTaskPropertiesForm" style="display: ''">
        <div class="row">
            <div class="col-xs-4">
                <div class="input-group">
                    <label for="txCode" class="control-label">事务编码:</label>
                    <input type="text" class="input-sm" id="txCode" ng-model="task.txCode" ng-disabled="isStartEndNode">
                </div>
                <label class="control-label">事务类型:</label>
                <select class="selectpicker" data-hide-disabled="true" data-live-search="false" ng-model="task.txType" ng-disabled="isStartEndNode">
                    <option ng-repeat="opt in txTypeOptions" value="{{opt.value}}">{{opt.descp}}</option>
                    <!--S:一般事务-->
                    <!--M:会签事务-->
                </select><br>
                <label for="buzStatus" class="control-label">业务状态:</label>
                <select id="buzStatus" class="selectpicker" data-hide-disabled="true" data-live-search="false" ng-model="task.buzStatus" ng-disabled="isStartEndNode">
                    <option ng-repeat="buzStatusOpt in buzStatusOptions" value="{{buzStatusOpt.value}}">{{buzStatusOpt.descp}}</option>
                </select><br>
                <label for="timeLimitTp" class="control-label">完成期限:</label>
                <input type="number" min="1" class="input-sm" id="durTime" style="width:80" ng-model="task.timeLimit" ng-disabled="isStartEndNode">
                <select id="timeLimitTp" class="selectpicker" data-hide-disabled="true" data-live-search="false" ng-model="task.timeLimitTp" ng-disabled="isStartEndNode">
                    <option value="M">分钟</option>
                    <option value="H">小时</option>
                    <option value="D">天</option>
                </select><br>
                <label for="alarmTime" class="control-label">预警提前期:</label>
                <input type="number" min="1" class="input-sm" aria-label="Text input with dropdown button" id="alarmTime" style="width:80" ng-model="task.alarmTime" ng-disabled="isStartEndNode">
                <select id="alarmTimeTp" class="selectpicker" data-hide-disabled="true" data-live-search="false" ng-model="task.alarmTimeTp" ng-disabled="isStartEndNode">
                    <option value="M">分钟</option>
                    <option value="H">小时</option>
                    <option value="D">天</option>
                </select><br>
            </div>
            <div class="col-xs-4">
                显示内容
                <textarea id="taskDescpDisp" rows="8" style="width:95%" ng-model="task.taskDescpDisp" ng-disabled="isEndNode"></textarea>
            </div>
            <div class="col-xs-4">
                事务说明
                <textarea id="taskDescp" rows="8" style="width:95%" ng-model="task.taskDescp" ng-disabled="isEndNode"></textarea>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-4">
                <div class="input-group">
                    <label for="moduleId" class="control-label">功能模块:</label>
                    <input type="text" class="input-sm" id="moduleId" ng-model="task.moduleId">
                </div>
            </div>
            <div class="col-xs-8">
                <label for="runParam" class="control-label">功能运行参数:</label>
                <input type="text" class="form-controller" id="runParam" width="80%" ng-model="task.runParam">
            </div>
        </div>
        <div class="row">
            <div class="bs-example" data-example-id="vertical-button-group">
                <div>事务选项</div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="isStartEndNode">
                        <input type="checkbox" class="btn btn-default" id="txChoicUpdData" ng-model="task.TX_CHOICES.AllowEdit" ng-disabled="isStartEndNode">
                        <label for="txChoicUpdData" style="font-weight: inherit;">允许修改数据</label>
                    </div>
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="isStartEndNode">
                        <input type="checkbox" class="btn btn-default" id="txChoicDelData" ng-model="task.TX_CHOICES.AllowDelete" ng-disabled="isStartEndNode">
                        <label for="txChoicDelData" style="font-weight: inherit;">允许删除数据</label>
                    </div>
                </div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="isStartEndNode">
                        <input type="checkbox" class="btn btn-default" id="txChoicAlwRej" ng-model="task.TX_CHOICES.AllowGoBack" ng-disabled="isStartEndNode">
                        <label for="txChoicAlwRej" style="font-weight: inherit;">允许退回</label>
                    </div>
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!task.TX_CHOICES.AllowGoBack">
                        <input type="checkbox" class="btn btn-default" id="txChoicRejCom" ng-model="task.TX_CHOICES.SignWhenGoBack" ng-disabled="!task.TX_CHOICES.AllowGoBack">
                        <label for="txChoicRejCom" style="font-weight: inherit;">退回时必须签署意见</label>
                    </div>
                </div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="isStartNode">
                        <input type="checkbox" class="btn btn-default" id="txChoicAlwRej2" ng-model="task.TX_CHOICES.AllowReCall" ng-disabled="isStartNode">
                        <label for="txChoicAlwRej2" style="font-weight: inherit;">允许撤回</label>
                    </div>
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!task.TX_CHOICES.AllowReCall">
                        <input type="checkbox" class="btn btn-default" id="txChoicRejCom2" ng-model="task.TX_CHOICES.SignWhenReCall" ng-disabled="!task.TX_CHOICES.AllowReCall">
                        <label for="txChoicRejCom2" style="font-weight: inherit;">撤回时必须签署意见</label>
                    </div>
                </div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="isStartEndNode">
                        <input type="checkbox" class="btn btn-default" id="txChoicAlwVeto" ng-model="task.TX_CHOICES.AllowVeto" ng-disabled="isStartEndNode">
                        <label for="txChoicAlwVeto" style="font-weight: inherit;">允许否决</label>
                    </div>
                    <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!task.TX_CHOICES.AllowVeto">
                        <input type="checkbox" class="btn btn-default" id="txChoicVetoCom" ng-model="task.TX_CHOICES.SignWhenVeto" ng-disabled="!task.TX_CHOICES.AllowVeto">
                        <label for="txChoicVetoCom" style="font-weight: inherit;">否决时必须签署意见</label>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-5">
                <div class="bs-example" data-example-id="vertical-button-group">
                    <div>事务处理选项</div>
                    <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                        <div class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpComOnCom" ng-model="task.TX_PR_CHOICES.SignWhenGo">
                            <label for="txpComOnCom" style="font-weight: inherit;">提交时必须签署意见</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyNextOnPro" ng-model="task.TX_PR_CHOICES.NoticeNextAfterGo" ng-click="selectNotify($event)">
                            <label for="txpNotifyNextOnPro" style="font-weight: inherit;">处理后通知下一步事务处理人</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyCreOnPro" ng-model="task.TX_PR_CHOICES.NoticeFirstAfterGo" ng-click="selectNotify($event)">
                            <label for="txpNotifyCreOnPro" style="font-weight: inherit;">处理后通知创建人</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyPreOnPro" ng-model="task.TX_PR_CHOICES.NoticePreviousAfterGo" ng-click="selectNotify($event)">
                            <label for="txpNotifyPreOnPro" style="font-weight: inherit;">处理后通知上一步事务处理人</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left"  ng-disabled="!csFlag">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyCsOnPro" ng-model="task.TX_PR_CHOICES.NoticeElseAfterGo" ng-disabled="!csFlag">
                            <label for="txpNotifyCsOnPro" style="font-weight: inherit;">处理后通知其他会签人</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!needNotifyFlag">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyMsg" ng-model="task.TX_PR_CHOICES.MsgAlert" ng-disabled="!needNotifyFlag">
                            <label for="txpNotifyMsg" style="font-weight: inherit;">消息提醒</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!needNotifyFlag">
                            <input type="checkbox" class="btn btn-default" id="txpNotifySms" ng-model="task.TX_PR_CHOICES.SmsAlert" ng-disabled="!needNotifyFlag">
                            <label for="txpNotifySms" style="font-weight: inherit;">短信提醒</label>
                        </div>

                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="bs-example" data-example-id="vertical-button-group">
                    <div>退回选项</div>
                    <div class="btn-group-vertical" role="group" aria-label="Vertical button group" ng-disabled="isStartEndNode || !task.TX_CHOICES.AllowGoBack">
                        <div class="btn btn-default" ng-disabled="isStartEndNode || !task.TX_CHOICES.AllowGoBack">
                            <input type="checkbox" class="btn btn-default" id="bkAlwPrev" ng-model="task.TX_BK_CHOICES.GoBackToPrevious" ng-disabled="isStartEndNode|| !task.TX_CHOICES.AllowGoBack">
                            <label for="bkAlwPrev" style="font-weight: inherit;">允许退回至上一步事务</label>
                        </div>
                        <div class="btn btn-default" ng-disabled="isStartEndNode || !task.TX_CHOICES.AllowGoBack">
                            <input type="checkbox" class="btn btn-default" id="bkAlwFirst" ng-model="task.TX_BK_CHOICES.GoBackToFirst" ng-disabled="isStartEndNode|| !task.TX_CHOICES.AllowGoBack">
                            <label for="bkAlwFirst" style="font-weight: inherit;">允许退回至第一步事务</label>
                        </div>
                    </div>
                </div>
                <div class="bs-example" data-example-id="vertical-button-group">
                    <div>会签选项</div>
                    <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                        <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!csFlag">
                            <input type="checkbox" class="btn btn-default" id="csAllCom" ng-model="task.SIGN_CHOICES.AllHandledThenGo" ng-click="csOptionSelect('ALL')" ng-disabled="!csFlag">
                            <label for="csAllCom" style="font-weight: inherit;">全部会签人员提交后流程提交下一步</label>
                        </div>
                        <div class="btn btn-default text-left" style="text-align:left" ng-disabled="!csFlag">
                            <input type="checkbox" class="btn btn-default" id="csLeastCom" ng-model="task.SIGN_CHOICES.PartHandledThenGo" ng-click="csOptionSelect('LEAST')" ng-disabled="!csFlag">
                            <label for="csLeastCom" style="font-weight: inherit;">
                                至少<input type="number" min="1" class="input-sm" style="width:55px" ng-model="task.SIGN_CHOICES.AtLeastHandled" ng-disabled="!csFlag">会签人员提交后流程提交
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <form id="updateTaskPropertiesForm2" style="display: none" ng-show="showAssignerEdit">
        <div class="col-xs-12">
            <table class="table table-striped" id="selectedAssignersTable">
                <thead><tr>
                    <th>人员类型</th>
                    <th>名称</th>
                    <th>选中模式</th>
                    <th>整体选择</th>
                    <th>执行条件</th>
                </tr></thead>
                <tbody>
                <tr ng-repeat="assign in assignerList" def-sel-mod="{{assign.defSelMod}}" assign-type="{{assign.assignTypeCode}}"
                        assign-id="{{assign.id}}" exe-conn="{{assign.exeConn}}" ng-click="selectAddedUserGroup($event)">
                    <td>{{assign.assignTypeDesc}}</td>
                    <td>{{assign.name}}</td>
                    <td>{{assign.defSelModTxt}}</td>
                    <td>
                        <input type="checkbox" ng-if="assign.checkFlag" checked="checked" ng-disabled="true">
                        <input type="checkbox" ng-if="!assign.checkFlag" ng-disabled="true">
                    </td>
                    <td>{{assign.exeConn}}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="col-xs-12">
            <div class="row">
                <div class="btn-group" role="group">
                    <div class="btn-group" role="group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false" ng-click="cancelAddUserGroup()">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true" id="addDropdownTxt">添加</span>
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a href="javascript:void(0)" ng-click="selectAddUserGroups('U',$event)">添加用户</a></li>
                            <li><a href="javascript:void(0)" ng-click="selectAddUserGroups('G',$event)">添加用户组</a></li>
                            <!--<li><a href="javascript:void(0)" ng-click="selectAddUserGroups('R',$event)">添加岗位</a></li>-->
                            <li><a href="javascript:void(0)" ng-click="selectAddUserGroups('C',$event)">添加自定义</a></li>
                        </ul>
                    </div>
                    <!--<button type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>修改
                    </button>-->
                    <button type="button" class="btn btn-default" ng-click="deleteAddedUserGroup()">
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除
                    </button>
                    <button type="button" class="btn btn-default" ng-click="saveUserGroup()">
                        <span class="glyphicon glyphicon-save" aria-hidden="true"></span>保存
                    </button>
                    <button type="button" class="btn btn-default" ng-click="cancelAddUserGroup()">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>放弃
                    </button>
                </div>
            </div>
        </div>
        <div class="col-xs-12 bs-example" id="addUserGroupId" style="display:none">
            <div class="row">
                <div class="col-xs-5" >
                    <select id="selectedUserGroup" class="selectpicker" multiple data-live-search="true"
                            ng-model="userGroupChoices.userGroupIds">
                        <option ng-repeat="item in userGroupOptList" value="{{item.id}}">{{item.name}}</option>
                    </select>
                </div>
                <div class="col-xs-5">
                    <label>选中模式：</label>
                    <select id="selectMode" class="selectpicker">
                        <option ng-repeat="opt in selectModeOptList" value="{{opt.value}}">{{opt.descp}}</option>
                    </select>
                </div>
                <div class="col-xs-2">
                    <div class="btn btn-default">
                        <input type="checkbox" class="btn btn-default" id="allSelected" ng-model="userGroupChoices.allSelected" >
                        <label for="allSelected" style="font-weight: inherit;">整体选择</label>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="input-group">
                        <span class="input-group-addon" >执行条件：</span>
                        <textarea class="form-control" id="exeConn" ng-model="userGroupChoices.exeConn"></textarea>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="btn btn-info" style="margin-left: 20px;" ng-repeat="funcVar in custFuncVars" ng-click="selectFuncVar(funcVar)">{{funcVar.varDescp}} - {{funcVar.varCode}}</div>
                </div>
            </div>
        </div>
    </form>
    </div>
    <div class="row">
        <div class="alert alert-success alert-dismissible col-md-6" role="alert" id="successMsg" style="display:none">
            <strong>保存成功</strong>
        </div>
    </div>
</div>

<div class="modal-footer">
    <button type="button" class="btn btn-primary" ng-click="updateTaskProperties()">确定</button>
    <button type="button" class="btn btn-default" onclick="hideModal()">取消</button>
</div>
</body>
<script src="${base.contextPath}/static/js/common.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/plugin/bootstrap-select.js"></script>
<script src="${base.contextPath}/static/js/app-task.js"></script>

</html>