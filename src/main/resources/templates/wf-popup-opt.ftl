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
        <#if opt?exists>
            var opt = "${opt}";
        </#if>
    </script>
</head>
<body ng-app="taskApp" ng-controller="taskCtrl">
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideModal()"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="editTaskTitle">工作流提交</h4>
</div>
<div class="modal-body">
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