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
            var refMkid = taskData.refMkid;
        </#if>
        var basePath = "${base.contextPath}";
    </script>
</head>
<body ng-app="conditionApp" ng-controller="ctrl">
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideModal()"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="editTaskTitle">条件判断属性设置</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-xs-12 bs-example">
            <div class="row">
                <div class="col-xs-12">
                    <div class="input-group">
                        <span class="input-group-addon" >显示名称：</span>
                        <input type="text" class="form-control" id="nameDisp" ng-model="task.taskDescpDisp">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="input-group">
                        <span class="input-group-addon" >判断条件：</span>
                        <textarea class="form-control" id="condExp" ng-model="task.condExp"></textarea>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="btn btn-info" style="margin-left: 20px;" ng-repeat="funcVar in custFuncVars">{{funcVar.varDescp}} - {{funcVar.varCode}}</div>
                </div>
            </div>
            <div class="row" style="margin-top:10px">
                <div class="col-xs-12">
                    <div class="input-group">
                        <span class="input-group-addon" >判断说明：</span>
                        <textarea class="form-control" id="condDescp" ng-model="task.taskDescp"></textarea>
                    </div>
                </div>
            </div>
        </div>
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
<script src="${base.contextPath}/static/js/app-cond.js"></script>

</html>