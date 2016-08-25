<!doctype html>
<html>
<head>
    <title>Define Workflow</title>
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
<body data-demo-id="statemachine" data-library="dom" class="home-template" ng-app="taskApp">
<header class="site-header">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>${moduleName}</h1>
                <input type="hidden" name="moduleId" value="${moduleId}" id="moduleId">
            </div>
            <div class="col-xs-6">
                <div style="position: absolute;right: 10px;"> <h3><a href="${base.contextPath}/wf">回首页</a></h3></div>
            </div>

        </div>
    </div>
</header>
<div class="jtk-demo-main">
    <!-- demo -->
    <div class="rsmenu">
        <div class="w menu-task" rs-data-type="user-task">事务</div>
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
     aria-labelledby="myModalLabel" aria-hidden="false" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <iframe style="zoom: 0.8;" height="700px" src="" frameBorder="0" width="99.6%"></iframe>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<ul id="connMenu" class="contextMenu">
    <li class=""><a href="#edit">Edit</a></li>
    <li class=""><a href="#delete">Delete</a></li>
</ul>
<ul id="activityMenu" class="contextMenu">
    <li class=""><a href="#edit">Edit</a></li>
    <li class=""><a href="#delete">Delete</a></li>
</ul>
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
<script src="${base.contextPath}/static/js/jquery.contextMenu.js"></script>
<!--  demo code -->
<script src="${base.contextPath}/static/js/common.js"></script>
<script src="${base.contextPath}/static/js/rs-bpm.js"></script>

<script>
    var parmJsonStr = "";
    function editTask(id_){
        var parmJson = {};
        var el = $("#"+id_);
        parmJson.taskPgId = el.attr("id");;
        parmJson.taskDescpDisp = el.text();
        var assignersStr = el.attr(RS_ATTR_ASSIGNERS);
        if(assignersStr==undefined || assignersStr==""){
            assignersStr = "[]";
        }
        parmJson.assigners = $.parseJSON(assignersStr);
        parmJson.taskType = el.attr(RS_ATTR_TASK_TYPE);
        var tx_choices_str = el.attr(RS_ATTR_TX_CHOICES);
        if(tx_choices_str == undefined || tx_choices_str==""){
            tx_choices_str = "{}";
        }
        parmJson.TX_CHOICES = $.parseJSON(tx_choices_str);
        var tx_pr_choices_str = el.attr(RS_ATTR_TX_PR_CHOICES);
        if(tx_pr_choices_str == undefined || tx_pr_choices_str==""){
            tx_pr_choices_str = "{}";
        }
        parmJson.TX_PR_CHOICES = $.parseJSON(tx_pr_choices_str);
        var tx_bk_choices_str = el.attr(RS_ATTR_TX_BK_CHOICES);
        if(tx_bk_choices_str == undefined || tx_bk_choices_str==""){
            tx_bk_choices_str = "{}";
        }
        parmJson.TX_BK_CHOICES = $.parseJSON(tx_bk_choices_str);
        var sign_choices_str = el.attr(RS_ATTR_SIGN_CHOICES);
        if(sign_choices_str == undefined || sign_choices_str==""){
            sign_choices_str = "{}";
        }
        parmJson.SIGN_CHOICES = $.parseJSON(sign_choices_str);
        parmJson.txCode		=el.attr("txCode");
        parmJson.txType		=el.attr("txType");
        parmJson.buzStatus	=el.attr("buzStatus");
        var timeLimit = el.attr("timeLimit");
        if(timeLimit!=""){
            timeLimit = parseInt(timeLimit);
        }
        parmJson.timeLimit	=timeLimit;
        parmJson.timeLimitTp=el.attr("timeLimitTp");
        var alarmTime = el.attr("alarmTime");
        if(alarmTime!=""){
            alarmTime = parseInt(alarmTime);
        }
        parmJson.alarmTime	=alarmTime;
        parmJson.alarmTimeTp=el.attr("alarmTimeTp");
        parmJson.moduleId	=el.attr("moduleId");
        parmJson.runParam	=el.attr("runParam");
        parmJson.taskDescp	=el.attr("taskDescp");
        parmJsonStr = JSON.stringify(parmJson);
        $('iframe').attr("src",basePath+"/wfadmin/task?taskData="+parmJsonStr);
        $('#myModal').modal({backdrop:false});
    }
    
    window.addEventListener('message', receiveMessage, false);
    function receiveMessage(evt) {
        console.log("got message from child page:"+evt.data);
        var taskData = $.parseJSON(evt.data);
        if(taskData.opt=="U"){
            $("#"+taskData.taskPgId).attr(RS_ATTR_ASSIGNERS, JSON.stringify(taskData.assigners))
                    .attr(RS_ATTR_TX_CHOICES,JSON.stringify(taskData.TX_CHOICES))
                    .attr(RS_ATTR_TX_PR_CHOICES,JSON.stringify(taskData.TX_PR_CHOICES))
                    .attr(RS_ATTR_TX_BK_CHOICES,JSON.stringify(taskData.TX_BK_CHOICES))
                    .attr(RS_ATTR_SIGN_CHOICES,JSON.stringify(taskData.SIGN_CHOICES))
                    .attr("txCode",taskData.txCode)
                    .attr("txType",taskData.txType)
                    .attr("buzStatus",taskData.buzStatus)
                    .attr("timeLimit",taskData.timeLimit)
                    .attr("timeLimitTp",taskData.timeLimitTp)
                    .attr("alarmTime",taskData.alarmTime)
                    .attr("alarmTimeTp",taskData.alarmTimeTp)
                    .attr("moduleId",taskData.moduleId)
                    .attr("runParam",taskData.runParam)
                    .attr("taskDescp",taskData.taskDescp)
                    .attr("taskDescpDisp",taskData.taskDescpDisp)
            ;
            if(RS_TYPE_CONDITION==taskData.taskType){
                $("#"+taskData.taskPgId +" .task-descp").html(taskData.taskDescpDisp);
            }else{
                $("#"+taskData.taskPgId)
                        .html(taskData.taskDescpDisp+"<div class=\"ep\"></div>");
            }
        }else if(taskData.opt=="D"){
            window.jsp.remove(taskData.taskPgId);
        }
        if(taskData.opt=="C"){
            $("#myModal").modal("hide");
        }else{
            setTimeout(function(){
                $("#myModal").modal("hide");
            },1000);
        }

    }
</script>
</body>
</html>
