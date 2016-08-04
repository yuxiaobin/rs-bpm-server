<html>
<head>
    <script>
        <#if taskPgId?exists>
            var taskPgId = "${taskPgId}";
        </#if>
        <#if taskDescp?exists>
            var taskDescp = "${taskDescp}";
        </#if>
    </script>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="exampleModalLabel">Edit Task</h4>
</div>
<div class="modal-body">
    <form>
        <div class="form-group">
            <label for="taskDescpId" class="control-label">Task Description:</label>
            <input type="text" class="form-control" id="taskDescpId">
            <input type="hidden" class="form-control" id="taskPgId">
        </div>
        <div class="form-group">
            <label for="nextAssignerId" class="control-label">Next Assigner</label>
            <textarea class="form-control" id="nextAssignerId"></textarea>
        </div>
    </form>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
    <button type="button" class="btn btn-primary">Save</button>
    <button type="button" class="btn btn-danger">Delete</button>
</div>
</body>
<!--
该页面会被模态框
<script src="/static/js/jquery-1.9.1.min.js"></script>
-->
<script>
   if(typeof(taskDescp)=='undefined'){
       console.log("taskDescp is undefined");
   }else{
       $("#taskDescpId").val(taskDescp);
       $("#taskPgId").val(taskPgId);
   }
</script>
</html>