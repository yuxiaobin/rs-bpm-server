<!doctype html>
<html>
<head>
    <title>jsPlumb - state machine demonstration</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="stylesheet" href="${base.contextPath}/static/css/jsPlumbToolkit-defaults.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/main.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/jsPlumbToolkit-demo.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/jquery.contextMenu.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/rs-bpm.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/theme/base/jquery.ui.all.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/properties.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/demo.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <script>
        var basePath = "${base.contextPath}";
    </script>
</head>
<body data-demo-id="statemachine" data-library="dom" class="home-template">
<header class="site-header">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>${moduleName}</h1>
                <input type="hidden" name="moduleId" value="${moduleId}" id="moduleId">
            </div>
        </div>
    </div>
</header>
<div class="jtk-demo-main">
    <!-- demo -->
    <div class="rsmenu">
        <div class="w menu-task" rs-type="user-task"><label>User Task</label></div>
        <div class="w menu-task rs-cond-task" rs-type="user-task"><div class="task-descp">Condition Node</div></div>
    </div>
    <div class="rscontainer jtk-demo-canvas canvas-wide statemachine-demo jtk-surface jtk-surface-nopan " id="canvas">
    </div>
    <div>
        <!--<input type="button" value="Remove All" id="removeAll">-->
        <input type="button" class="btn btn-lg btn-primary" value="Save" id="Save">
        <input type="button" class="btn btn-lg btn-primary" value="Reset" id="resetBtn">
    </div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<!-- JS -->
<!-- support lib for bezier stuff -->
<script src="${base.contextPath}/static/js/lib/jsBezier-0.8.js"></script>
<!-- event adapter -->
<script src="${base.contextPath}/static/js/lib/mottle-0.7.2.js"></script>
<!-- geometry functions -->
<script src="${base.contextPath}/static/js/lib/biltong-0.3.js"></script>
<!-- drag -->
<script src="${base.contextPath}/static/js/lib/katavorio-0.17.0.js"></script>
<!-- jsplumb util -->
<script src="${base.contextPath}/static/js/lib/util.js"></script>
<script src="${base.contextPath}/static/js/lib/browser-util.js"></script>
<!-- main jsplumb engine -->
<script src="${base.contextPath}/static/js/lib/jsPlumb.js"></script>
<!-- base DOM adapter -->
<script src="${base.contextPath}/static/js/lib/dom-adapter.js"></script>
<script src="${base.contextPath}/static/js/lib/overlay-component.js"></script>
<!-- endpoint -->
<script src="${base.contextPath}/static/js/lib/endpoint.js"></script>
<!-- connection -->
<script src="${base.contextPath}/static/js/lib/connection.js"></script>
<!-- anchors -->
<script src="${base.contextPath}/static/js/lib/anchors.js"></script>
<!-- connectors, endpoint and overlays  -->
<script src="${base.contextPath}/static/js/lib/defaults.js"></script>
<!-- bezier connectors -->
<script src="${base.contextPath}/static/js/lib/connectors-bezier.js"></script>
<!-- state machine connectors -->
<script src="${base.contextPath}/static/js/lib/connectors-statemachine.js"></script>
<!-- flowchart connectors -->
<script src="${base.contextPath}/static/js/lib/connectors-flowchart.js"></script>
<!-- SVG renderer -->
<script src="${base.contextPath}/static/js/lib/renderers-svg.js"></script>

<!-- common adapter -->
<script src="${base.contextPath}/static/js/lib/base-library-adapter.js"></script>
<!-- no library jsPlumb adapter -->
<script src="${base.contextPath}/static/js/lib/dom.jsPlumb.js"></script>
<script src="${base.contextPath}/static/js/lib/bezier-editor.js"></script>
<!-- /JS -->

<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/jquery-ui.js"></script>
<!--<script src="${base.contextPath}/static/js/jquery.ui.dialog.js"></script>-->
<!--<script src="${base.contextPath}/static/js/jquery.contextMenu.js"></script>-->
<!--  demo code -->
<script src="${base.contextPath}/static/js/rs-bpm.js"></script>
<script src="${base.contextPath}/static/js/activity-edit/properties.js"></script>

<script>
    var parmJsonStr = "";
    function editTask(id_){
        var parmJson = {};
        var el = $("#"+id_);
        parmJson.taskPgId = el.attr("id");;
        parmJson.taskDescp = el.text();
        parmJsonStr = JSON.stringify(parmJson);
        $('#myModal').modal();
    }

    $('#myModal').on('show.bs.modal', function(e) {
        var $modal = $(this);
        $.ajax({
            cache: false,
            type: 'POST',
            url: basePath+"/wf/admin/task",
            data:parmJsonStr,
            headers: { 'Content-Type': "application/json" },
            success: function(data) {
                $modal.find('.modal-content').html(data);
            }
        });
    });
    $('#myModal').on('hide.bs.modal', function(){
        $(this).removeData('bs.modal');
    });

    function updateTaskProperties(){
        var taskPgId = $("#updateTaskPropertiesForm #taskPgId").eq(0).val();
        $("#"+taskPgId)
                .attr("rs-data-assigner",$("#updateTaskPropertiesForm #nextAssignerId").eq(0).val())
                .html($("#updateTaskPropertiesForm #taskDescpId").eq(0).val()+"<div class=\"ep\"></div>");
        $("#successMsg").css("display","");
        setTimeout(function(){
            $('#myModal').modal('hide');
        },1000);
    }
    function deleteTask(){
        var taskPgId = $("#updateTaskPropertiesForm #taskPgId").eq(0).val();
        window.jsp.remove(taskPgId);
        $('#myModal').modal('hide');
    }
    function confirmDelete(){
        $("#deleteTaskAlert").css("display","");
    }
    function notDeleteTask(){
        $("#deleteTaskAlert").css("display","none");
    }
</script>
</body>
</html>
