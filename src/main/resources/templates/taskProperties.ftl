<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/docs.css">
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
    <h4 class="modal-title" id="editTaskTitle">Edit Task</h4>
</div>
<div class="modal-body">
    <ul class="nav nav-tabs">
        <li role="presentation" class="active"><a href="javascript:void(0)" onclick="changeTab(this,'updateTaskPropertiesForm')">Task Define</a></li>
        <li role="presentation"><a href="javascript:void(0)" onclick="changeTab(this,'updateTaskPropertiesForm2')">Assigner Define</a></li>
    </ul>
    <div class="row form-wrapper">
    <form id="updateTaskPropertiesForm" style="display: ''">
        <div class="row">
            <div class="col-xs-4">
                <div class="input-group">
                    <label for="txCode" class="control-label">事务编码:</label>
                    <input type="text" class="input-sm" id="txCode" ng-model="task.txCode">
                </div>
                <div class="input-group">
                    <label class="control-label">事务类型:</label>
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span aria-hidden="true" id="txType" rs-attr-txType="S">一般事务</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li rs-attr-txType="S"><a href="javascript:void(0)" >一般事务</a></li>
                        <li rs-attr-txType="M"><a href="javascript:void(0)" >会签事务</a></li>
                    </ul>
                </div>
                <div class="input-group">
                    <label for="buzStatus" class="control-label">业务状态:</label>
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span aria-hidden="true" id="buzStatus" rs-attr-buzStatus="I">初始</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li rs-attr-buzStatus="I"><a href="javascript:void(0)" >初始</a></li>
                        <li rs-attr-buzStatus="H"><a href="javascript:void(0)" >会签</a></li>
                        <li rs-attr-buzStatus="F"><a href="javascript:void(0)" >确认</a></li>
                        <li rs-attr-buzStatus="C"><a href="javascript:void(0)" >完成</a></li>
                    </ul>
                </div>
                <div class="input-group">
                    <label for="timeLimitTp" class="control-label">完成期限:</label>
                    <input type="text" class="input-sm" id="durTime" style="width:80" ng-model="task.timeLimit">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span aria-hidden="true" id="timeLimitTp" rs-attr-timeLimitTp="H">小时</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li rs-attr-timeLimitTp="M"><a href="javascript:void(0)" >分钟</a></li>
                        <li rs-attr-timeLimitTp="H"><a href="javascript:void(0)" >小时</a></li>
                        <li rs-attr-timeLimitTp="D"><a href="javascript:void(0)" >天</a></li>
                    </ul>
                </div>
                <div class="input-group">
                    <label for="alarmTime" class="control-label">预警提前期:</label>
                    <input class="input-sm" aria-label="Text input with dropdown button" id="alarmTime" style="width:80" ng-model="task.alarmTime">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span aria-hidden="true" id="alarmTimeTp" rs-attr-alarmTimeTp="H">小时</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li rs-attr-alarmTimeTp="M"><a href="javascript:void(0)" >分钟</a></li>
                        <li rs-attr-alarmTimeTp="H"><a href="javascript:void(0)" >小时</a></li>
                        <li rs-attr-alarmTimeTp="D"><a href="javascript:void(0)" >天</a></li>
                    </ul>
                    </div>
                </div>
            <div class="col-xs-4">
                显示内容
                <textarea id="taskDescpDisp" rows="8" style="width:95%" ng-model="task.taskDescpDisp"></textarea>
            </div>
            <div class="col-xs-4">
                事务说明
                <textarea id="taskDescp" rows="8" style="width:95%" ng-model="task.taskDescp"></textarea>
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
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicUpdData" ng-model="TX_CHOICES.AllowEdit">
                        <label for="txChoicUpdData" style="font-weight: inherit;">允许修改数据</label>
                    </button>
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicDelData" ng-model="TX_CHOICES.AllowDelete">
                        <label for="txChoicDelData" style="font-weight: inherit;">允许删除数据</label>
                    </button>
                </div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicAlwRej" ng-model="TX_CHOICES.AllowGoBack">
                        <label for="txChoicAlwRej" style="font-weight: inherit;">允许退回</label>
                    </button>
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicRejCom" ng-model="TX_CHOICES.SignWhenGoBack">
                        <label for="txChoicRejCom" style="font-weight: inherit;">退回时必须签署意见</label>
                    </button>
                </div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicAlwRej2" ng-model="TX_CHOICES.AllowReCall">
                        <label for="txChoicAlwRej2" style="font-weight: inherit;">允许撤回</label>
                    </button>
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicRejCom2" ng-model="TX_CHOICES.SignWhenReCall">
                        <label for="txChoicRejCom2" style="font-weight: inherit;">撤回时必须签署意见</label>
                    </button>
                </div>
                <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicAlwVeto" ng-model="TX_CHOICES.AllowVeto">
                        <label for="txChoicAlwVeto" style="font-weight: inherit;">允许否决</label>
                    </button>
                    <button type="button" class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="txChoicVetoCom" ng-model="TX_CHOICES.SignWhenVeto">
                        <label for="txChoicVetoCom" style="font-weight: inherit;">否决时必须签署意见</label>
                    </button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-5">
                <div class="bs-example" data-example-id="vertical-button-group">
                    <div>事务处理选项</div>
                    <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpComOnCom" ng-model="task.TX_PR_CHOICES.SignWhenGo">
                            <label for="txpComOnCom" style="font-weight: inherit;">提交时必须签署意见</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyNextOnPro" ng-model="task.TX_PR_CHOICES.NoticeNextAfterGo">
                            <label for="txpNotifyNextOnPro" style="font-weight: inherit;">处理后通知下一步事务处理人</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyCreOnPro" ng-model="task.TX_PR_CHOICES.NoticeFirstAfterGo">
                            <label for="txpNotifyCreOnPro" style="font-weight: inherit;">处理后通知创建人</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyPreOnPro" ng-model="task.TX_PR_CHOICES.NoticePreviousAfterGo">
                            <label for="txpNotifyPreOnPro" style="font-weight: inherit;">处理后通知上一步事务处理人</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyCsOnPro" ng-model="task.TX_PR_CHOICES.NoticeElseAfterGo">
                            <label for="txpNotifyCsOnPro" style="font-weight: inherit;">处理后通知其他会签人</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifyMsg" ng-model="task.TX_PR_CHOICES.MsgAlert">
                            <label for="txpNotifyMsg" style="font-weight: inherit;">消息提醒</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="txpNotifySms" ng-model="task.TX_PR_CHOICES.SmsAlert">
                            <label for="txpNotifySms" style="font-weight: inherit;">短信提醒</label>
                        </button>

                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="bs-example" data-example-id="vertical-button-group">
                    <div>退回选项</div>
                    <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                        <button type="button" class="btn btn-default">
                            <input type="checkbox" class="btn btn-default" id="bkAlwPrev" ng-model="task.TX_BK_CHOICES.GoBackToPrevious">
                            <label for="bkAlwPrev" style="font-weight: inherit;">允许退回至上一步事务</label>
                        </button>
                        <button type="button" class="btn btn-default">
                            <input type="checkbox" class="btn btn-default" id="bkAlwFirst" ng-model="task.TX_BK_CHOICES.GoBackToFirst">
                            <label for="bkAlwFirst" style="font-weight: inherit;">允许退回至第一步事务</label>
                        </button>
                    </div>
                </div>
                <div class="bs-example" data-example-id="vertical-button-group">
                    <div>会签选项</div>
                    <div class="btn-group-vertical" role="group" aria-label="Vertical button group">
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="csAllCom" ng-model="task.SIGN_CHOICES.AllHandledThenGo">
                            <label for="csAllCom" style="font-weight: inherit;">全部会签人员提交后流程提交下一步</label>
                        </button>
                        <button type="button" class="btn btn-default text-left" style="text-align:left">
                            <input type="checkbox" class="btn btn-default" id="csLeastCom" ng-model="task.SIGN_CHOICES.PartHandledThenGo">
                            <label for="csLeastCom" style="font-weight: inherit;">
                                至少<input type="text" class="input-sm" style="width:30px" ng-model="task.SIGN_CHOICES.AtLeastHandled">会签人员提交后流程提交
                            </label>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <form id="updateTaskPropertiesForm2" style="display: none">
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
                <tr ng-repeat="assign in assignerList" def-sel-mod="{{assign.defSelMod}}" assign-type="{{assign.assignTypeCode}}" assign-id="{{assign.id}}">
                    <td>{{assign.assignTypeDesc}}</td>
                    <td>{{assign.name}}</td>
                    <td>{{assign.defSelModTxt}}</td>
                    <td><input type="checkbox" ng-checked="{{assign.checkFlag}}"></td>
                    <td>{{assign.exeConn}}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="col-xs-12">
            <div class="row">
                <div class="btn-group" role="group" aria-label="...">
                    <div class="btn-group" role="group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true" id="addDropdownTxt">添加</span>
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a href="javascript:void(0)" ng-click="selectAddUserGroups('U',$event)">添加用户</a></li>
                            <li><a href="javascript:void(0)" ng-click="selectAddUserGroups('G',$event)">添加用户组</a></li>
                            <li><a href="javascript:void(0)">添加。。。</a></li>
                        </ul>
                    </div>
                    <button type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>修改
                    </button>
                    <button type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除
                    </button>
                    <button type="button" class="btn btn-default" ng-click="saveUserGroup()">
                        <span class="glyphicon glyphicon-save" aria-hidden="true"></span>保存
                    </button>
                    <button type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>放弃
                    </button>
                </div>
            </div>
            <div class="row" id="addUserGroupId" style="display:none">
                <div class="list-group col-xs-6" style="max-height: 500px;overflow: auto;display:none" id="showUsers">
                    <a class="list-group-item" id="selectAllUsersId" href="javascript:void(0)">
                        <div class="input-group">
                                  <span class="input-group-addon" title="Select All">
                                    <label for="allUsersCheckbox">Select All:</label>&nbsp;<input type="checkbox" id="allUsersCheckbox" aria-label="Select All" ng-click="selectAllUserChange()"><!--onclick="selectAllUserChange(this)" -->
                                  </span>
                            <input type="text" class="input-sm" aria-label="..." placeholder="filter" ng-model="userFilter">
                        </div>
                    </a>
                    <a href="javascript:void(0)" ng-repeat="user in userList | filter:userFilter" class="list-group-item {{user.extClass}}"  rs-data-asname="{{user.name}}"  rs-data-asid="{{user.id}}" ng-click="selectUser(user.id)"><!--onclick="selectUser(this)"-->
                        {{user.name}}
                    </a>
                </div>
                <div class="list-group col-xs-6" style="max-height: 500px;overflow: auto;display:none" id="showGroups">
                    <a class="list-group-item" id="selectAllGroupId" href="javascript:void(0)">
                        <div class="input-group">
                              <span class="input-group-addon" title="Select All">
                                <label for="allGroupsCheckbox">Select All:</label>&nbsp;<input type="checkbox" id="allGroupsCheckbox" aria-label="Select All" value="true" ng-click="selectAllGroupChange()">
                              </span>
                            <input type="text" class="input-sm" aria-label="..." placeholder="filter" ng-model="groupFilter">
                        </div>
                    </a>
                    <a href="javascript:void(0)"  ng-repeat="group in groupList | filter:groupFilter" class="list-group-item {{group.extClass}}" rs-data-asname="{{group.groupName}}" rs-data-asid="{{group.groupId}}" ng-click="selectGroup(group.groupId)">
                        {{group.groupName}}
                    </a>
                </div>
                <div class="list-group col-xs-6">
                    <div class="input-group">
                        <span class="input-group-addon" id="basic-addon1">选中模式：</span>
                        <div class="dropdown">
                            <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                <span id="selModeTxt">{{defSelModTxt_no}}</span>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                <li><a href="#" ng-click="selectAssignerDefSelMode($event)" def-sel-mod="N">{{defSelModTxt_no}}</a></li>
                                <li><a href="#" ng-click="selectAssignerDefSelMode($event)" def-sel-mod="Y">{{defSelModTxt_yes}}</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="input-group">
                        <span class="input-group-addon">
                            <input type="checkbox" aria-label="..." id="selAllFlag">
                        </span>
                        整体选择
                    </div>
                    <div class="input-group">
                        <span class="input-group-addon" >执行条件：</span>
                        <textarea class="form-control" id="exeConn" ng-model="exeConn"></textarea>
                    </div>
                </div>
            </div>
        </div>
    </form>
    </div>
    <div class="row">
        <div class="alert alert-success alert-dismissible col-md-6" role="alert" id="successMsg" style="display:none">
            <strong>Success!</strong> Update successfully!.
        </div>
        <div class="alert alert-danger alert-dismissible col-md-6" role="alert" id="deleteTaskAlert" style="display:none">
            <p><strong>Confirm to delete?</strong></p>
            <p> <button type="button" class="btn btn-danger" ng-click="deleteTask()">Yes</button> <button type="button" class="btn btn-default" onclick="notDeleteTask()">No</button> </p>
        </div>
    </div>
</div>

<div class="modal-footer">
    <button type="button" class="btn btn-danger" id="deleteBtn" onclick="confirmDelete()">Delete</button>

    <button type="button" class="btn btn-primary" ng-click="updateTaskProperties()">Save</button>
</div>
</body>
<script src="${base.contextPath}/static/js/common.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/app-task.js"></script>
<script>
   if(typeof(taskData)=='undefined'){
       console.log("taskData is undefined");
   }else{
       $("#taskDescpId").val(taskData.taskDescp);
       $("#taskPgId").val(taskData.taskPgId);
       $("#taskType").val(taskData.taskType);

       if(RS_TYPE_USER==taskData.taskType){
           $("#nextAssignerDisplay").show();
       }else{
           $("#nextAssignerDisplay").hide();
       }
       if(RS_TYPE_START == taskData.taskType || RS_TYPE_END == taskData.taskType){
           $("#deleteBtn").hide();
       }
   }


</script>
</html>