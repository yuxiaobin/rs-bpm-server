<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/docs.css">

    <script>
        var basePath = "${base.contextPath}";
        <#if instNum?exists>
            var instNum = "${instNum}";
        </#if>
        <#if refMkid?exists>
            var refMkid = "${refMkid}";
        </#if>
        <#if optCode?exists>
            var optCode = "${optCode}";
        </#if>
        <#if callbackUrl?exists>
            var callbackUrl = "${callbackUrl}";
        </#if>
        <#if userId?exists>
            var userId = "${userId}";
        </#if>
        <#if TX_PR_CHOICES?exists>
            var TX_PR_CHOICES = ${TX_PR_CHOICES};
        </#if>
    </script>
</head>
<body ng-app="taskApp" ng-controller="taskCtrl">
<div class="modal-header" ng-hide="fromRemote">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideModal()"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title">{{optTitle}}</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-xs-7">
            <div style="width: 100%;">
                <div class="panel panel-default">
                    <div class="panel-heading">1、请填写处理意见{{SignWhenGoDisp}}：</div>
                    <textarea rows="8" style="width:100%" id="comments" ng-model="comments"></textarea>
                </div>
            </div>
            <div style="width: 100%;">
                <div class="panel panel-default">
                    <div class="panel-heading">2、请选择下一步处理事务：</div>
                    <table class="table" id="taskTable">
                        <tr ng-repeat="task in taskList">
                            <td ng-switch="task.checkedFlag">
                                <input ng-switch-when="true" ng-checked="true" ng-disabled="true" type="checkbox" value="{{task.taskId}}">
                                <input ng-switch-default type="checkbox" value="{{task.taskId}}">
                            </td>
                            <td>{{task.taskDescpDisp}}</td>
                        </tr>
                    </table>
                </div>
            </div>
            <div style="width: 100%;">
                <div class="panel panel-default">
                    <div class="panel-heading">已选择待处理人员</div>
                    <div class="panel-body">
                        <p style="word-break: break-word;" ng-bind="selectedAssigners"></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-5" id="selectAssignerPanel">
            <div class="panel panel-default" style="max-height: 500px;overflow: auto">
                <div class="panel-heading"> 3、请选择下一步处理人员：</div>
                <div class="panel-heading" ng-show="actExecLabelFlag">
                    <div class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="actExecFlag" ng-model="actExecFlag" ng-click="selectNextAssignerType('actExecFlag')">
                        <label for="actExecFlag" style="font-weight: inherit;">实际执行人员</label>
                    </div>
                    <div class="btn btn-default text-left" style="text-align:left">
                        <input type="checkbox" class="btn btn-default" id="repickPeople" ng-model="repickPeople" ng-click="selectNextAssignerType('repickPeople')">
                        <label for="repickPeople" style="font-weight: inherit;">重新选择人员</label>
                    </div>
                </div>
                <div class="panel-body" ng-show="actExecFlag">
                    <span class="glyphicon glyphicon-minus" style="cursor: pointer;" aria-hidden="true" onclick="expandClick(this)">类型：用户</span>
                </div>
                <table class="table" id="actExecTable" style="max-height: 500px;overflow: auto" ng-show="actExecFlag">
                    <tr ng-repeat="actExer in actExecList">
                        <td>
                            <input type="checkbox" value="{{actExer.id}}" rs-attr-name="{{actExer.name}}" ng-checked="true" ng-click="selectActExecAssigner($event)">
                        </td>
                        <td>{{actExer.id}}</td>
                        <td>{{actExer.name}}</td>
                    </tr>
                </table>

                <div class="panel-body" ng-hide="actExecFlag">
                    <span class="glyphicon glyphicon-minus" style="cursor: pointer;" aria-hidden="true" onclick="expandClick(this)">类型：用户</span>
                </div>
                <table class="table" id="userTable" style="max-height: 500px;overflow: auto" ng-hide="actExecFlag">
                    <tr ng-repeat="user in userList">
                        <td ng-switch="user.defSelMod">
                            <input type="checkbox" ng-switch-when="1" ng-checked="true"  value="{{user.id}}" rs-attr-name="{{user.name}}" ng-click="selectAssigner($event)">
                            <input type="checkbox" ng-switch-when="2" ng-checked="true" ng-disabled="true" value="{{user.id}}" rs-attr-name="{{user.name}}" ng-click="selectAssigner($event)">
                            <input type="checkbox" ng-switch-default value="{{user.id}}" rs-attr-name="{{user.name}}" ng-click="selectAssigner($event)">
                        </td>
                        <!--<td>{{user.id}}</td>-->
                        <td>{{user.name}}</td>
                    </tr>
                </table>
                <div class="panel-body" ng-hide="actExecFlag">
                    <span class="glyphicon glyphicon-minus" style="cursor: pointer;" aria-hidden="true" onclick="expandClick(this)">类型：用户组</span>
                </div>
                <table class="table" id="groupTable" ng-hide="actExecFlag">
                    <tr ng-repeat="group in groupList">
                        <td ng-switch="group.defSelMod">
                            <input type="checkbox" ng-switch-when="1" ng-checked="true"  value="{{group.id}}" rs-attr-name="{{group.usersInGroup}}" ng-click="selectGroups($event)">
                            <input type="checkbox" ng-switch-when="2" ng-checked="true" ng-disabled="true"  value="{{group.id}}" rs-attr-name="{{group.usersInGroup}}" ng-click="selectGroups($event)">
                            <input type="checkbox" ng-switch-default value="{{group.id}}" rs-attr-name="{{group.usersInGroup}}" ng-click="selectGroups($event)">
                        </td>
                        <!--<td>{{group.id}}</td>-->
                        <td>{{group.name}}</td>
                    </tr>
                </table>
                <div class="panel-body" ng-hide="actExecFlag">
                    <span class="glyphicon glyphicon-minus" style="cursor: pointer;" aria-hidden="true" onclick="expandClick(this)">类型：自定义</span>
                </div>
                <table class="table" id="custUserTable" ng-hide="actExecFlag">
                    <tr ng-repeat="user in custUserList">
                        <td ng-switch="user.defSelMod">
                            <input type="checkbox" ng-switch-when="1" ng-checked="true"  value="{{user.id}}" rs-attr-name="{{user.usersInCust}}" ng-click="selectGroups($event)">
                            <input type="checkbox" ng-switch-when="2" ng-checked="true" ng-disabled="true" value="{{user.id}}" rs-attr-name="{{user.usersInCust}}" ng-click="selectGroups($event)">
                            <input type="checkbox" ng-switch-default value="{{user.id}}" rs-attr-name="{{user.usersInCust}}" ng-click="selectGroups($event)">
                        </td>
                        <!--<td>{{user.id}}</td>-->
                        <td>{{user.name}}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-primary" ng-click="submitTask()" ng-disabled="SignWhenGoDisp!=''&&comments==''">确定</button>
    <button type="button" class="btn btn-default" onclick="hideModal()">取消</button>
</div>
</body>
<script src="${base.contextPath}/static/js/common.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/app-opt.js"></script>
</html>