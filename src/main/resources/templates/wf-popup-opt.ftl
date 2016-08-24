<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/docs.css">

    <script>
        var basePath = "${base.contextPath}";
        <#if rsWfId?exists>
            var rsWfId = "${rsWfId}";
        </#if>
        <#if instNum?exists>
            var instNum = "${instNum}";
        </#if>
        <#if refMkid?exists>
            var refMkid = "${refMkid}";
        </#if>
        <#if optCode?exists>
            var optCode = "${optCode}";
        </#if>
    </script>
</head>
<body ng-app="taskApp" ng-controller="taskCtrl">
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideModal()"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title">{{optTitle}}</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-xs-7">
            <div style="width: 100%;">
                <div class="panel panel-default">
                    <div class="panel-heading">1、请填写处理意见：</div>
                    <textarea rows="8" style="width:100%" id="comments"></textarea>
                </div>
            </div>
            <div style="width: 100%;">
                <div class="panel panel-default">
                    <div class="panel-heading">2、请选择下一步处理事务：</div>
                    <table class="table" id="taskTable">
                        <tr ng-repeat="task in taskList">
                            <td><input type="checkbox" value="{{task.taskId}}"></td>
                            <td>{{task.taskDescpDisp}}</td>
                        </tr>
                    </table>
                </div>
            </div>
            <div style="width: 100%;">
                <div class="panel panel-default">
                    <div class="panel-heading">已选择待处理人员</div>
                    <div class="panel-body">
                        <p style="word-break: break-all;" ng-bind="selectedAssigners"></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-5">
            <div class="panel panel-default">
                <div class="panel-heading"> 3、请选择下一步处理人员：</div>
                <table class="table" id="userTable">
                    <tr>
                        <td colspan="3">类型：用户</td>
                    </tr>
                    <tr ng-repeat="user in userList">
                        <td><input type="checkbox" value="{{user.id}}" rs-attr-name="{{user.name}}" ng-click="selectAssigner($event)"></td>
                        <td>{{user.id}}</td>
                        <td>{{user.name}}</td>
                    </tr>
                </table>
                <table class="table" id="groupTable">
                    <tr>
                        <td colspan="3">类型：用户组</td>
                    </tr>
                    <tr ng-repeat="group in groupList">
                        <td><input type="checkbox" value="{{group.id}}" rs-attr-name="{{group.usersInGroup}}" ng-click="selectGroups($event)"></td>
                        <td>{{group.id}}</td>
                        <td>{{group.name}}</td>
                    </tr>
                </table>
            </div>
            </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-primary" ng-click="submitTask()">确定</button>
    <button type="button" class="btn btn-default" onclick="hideModal()">取消</button>
</div>
</body>
<script src="${base.contextPath}/static/js/common.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/app-opt.js"></script>
<script>
    if(typeof(taskData)=='undefined'){
        console.log("taskData is undefined");
    }else{
        $("#taskDescpId").val(taskData.taskDescp);
        $("#taskPgId").val(taskData.taskPgId);
        $("#taskType").val(taskData.taskType);
    }


</script>
</html>