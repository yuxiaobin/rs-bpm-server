<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
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
    <div class="form-group">
    <form id="updateTaskPropertiesForm" style="display: ''">
        <div class="form-group">
            <label for="taskDescpId" class="control-label">Task Description:</label>
            <input type="text" class="form-control" id="taskDescpId">
            <input type="hidden" class="form-control" id="taskPgId">
            <input type="hidden" class="form-control" id="taskType">
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
                            <input type="text" class="form-control" aria-label="..." placeholder="filter" ng-model="userFilter">
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
                            <input type="text" class="form-control" aria-label="..." placeholder="filter" ng-model="groupFilter">
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
    <div class="form-group">
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