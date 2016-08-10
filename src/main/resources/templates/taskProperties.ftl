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
    <form id="updateTaskPropertiesForm" style="display: ''">
        <div class="form-group">
            <label for="taskDescpId" class="control-label">Task Description:</label>
            <input type="text" class="form-control" id="taskDescpId">
            <input type="hidden" class="form-control" id="taskPgId">
            <input type="hidden" class="form-control" id="taskType">
        </div>
    </form>
    <form id="updateTaskPropertiesForm2" style="display: none">
        <div class="form-group">
            <label class="control-label">Select User(s) or Group(s):</label>
        </div>
        <div class="form-group">
            <div class="row">
                    <div class="list-group col-xs-6">
                        <a class="list-group-item" id="selectAllUsersId" href="javascript:void(0)">
                            <div class="input-group">
                                  <span class="input-group-addon" title="Select All">
                                    <label for="allUsersCheckbox">Select All:</label>&nbsp;<input type="checkbox" id="allUsersCheckbox" aria-label="Select All" ng-click="selectAllUserChange()"><!--onclick="selectAllUserChange(this)" -->
                                  </span>
                                  <input type="text" class="form-control" aria-label="..." placeholder="filter" ng-model="userFilter">
                            </div>
                        </a>
                        <a href="javascript:void(0)" ng-repeat="user in userList | filter:userFilter" class="list-group-item {{user.extClass}}"  rs-data-usname="{{user.name}}"  rs-data-usid="{{user.id}}" ng-click="selectUser(user.id)"><!--onclick="selectUser(this)"-->
                            {{user.name}}
                        </a>
                    </div>
                    <div class="list-group col-xs-6">
                        <a class="list-group-item" id="selectAllGroupId" href="javascript:void(0)">
                            <div class="input-group">
                                  <span class="input-group-addon" title="Select All">
                                    <label for="allGroupsCheckbox">Select All:</label>&nbsp;<input type="checkbox" id="allGroupsCheckbox" aria-label="Select All" value="true" ng-click="selectAllGroupChange()">
                                  </span>
                                <input type="text" class="form-control" aria-label="..." placeholder="filter" ng-model="groupFilter">
                            </div>
                        </a>
                        <a href="javascript:void(0)"  ng-repeat="group in groupList | filter:groupFilter" class="list-group-item {{group.extClass}}" rs-data-gpname="{{group.groupName}}" rs-data-gpid="{{group.groupId}}" ng-click="selectGroup(group.groupId)">
                            {{group.groupName}}
                        </a>
                    </div>
            </div>
        </div>
    </form>
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