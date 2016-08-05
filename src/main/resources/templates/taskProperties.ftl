<html>
<head>
    <script>
        <#if taskData?exists>
            var taskData = ${taskData};
        </#if>
    </script>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="exampleModalLabel">Edit Task</h4>
</div>
<div class="modal-body">
    <form id="updateTaskPropertiesForm">
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
    <div class="form-group">
        <div class="alert alert-success alert-dismissible col-md-6" role="alert" id="successMsg" style="display:none">
            <strong>Success!</strong> Saved successfully!.
        </div>
        <div class="alert alert-danger alert-dismissible col-md-6" role="alert" id="deleteTaskAlert" style="display:none">
            <p><strong>Confirm to delete?</strong></p>
            <p> <button type="button" class="btn btn-danger" onclick="deleteTask()">Yes</button> <button type="button" class="btn btn-default" onclick="notDeleteTask()">No</button> </p>
        </div>
    </div>
</div>

<div class="modal-footer">
    <button type="button" class="btn btn-danger" onclick="confirmDelete()">Delete</button>

    <button type="button" class="btn btn-primary" onclick="updateTaskProperties()">Save</button>
</div>
</body>
<!--
该页面会被模态框
<script src="/static/js/jquery-1.9.1.min.js"></script>
-->
<script>
   if(typeof(taskData)=='undefined'){
       console.log("taskData is undefined");
   }else{
       $("#taskDescpId").val(taskData.taskDescp);
       $("#taskPgId").val(taskData.taskPgId);
   }
</script>
</html>