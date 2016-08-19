<!doctype html>
<html>
<head>
    <title>View Workflow</title>
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
        var module_task_flag = "hist";
        <#if latestFlag?exists>
            var latestFlag = "${latestFlag}";
        </#if>
        <#if wfId?exists>
            var wfId = "${wfId}";
        </#if>
    </script>
</head>
<body data-demo-id="statemachine" data-library="dom" class="home-template" ng-app="app">
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
<main class="packages-list-container" id="all-packages">
<div class="container">
    <div class="row">
        <div class="col-md-6">
        <ul class="nav nav-pills nav-justified">
            <li role="presentation" class="active"><a href="javascript:void(0)" onclick="viewWFTab(this,'wfMain')">Workflow Status</a></li>
            <li role="presentation"><a href="javascript:void(0)" onclick="viewWFTab(this,'wfHist')">View History</a></li>
        </ul>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
    <div class="jtk-demo-main" id="wfMain">
    <div class="rscontainer jtk-demo-canvas canvas-wide statemachine-demo jtk-surface jtk-surface-nopan " id="canvas">

    </div>

    <div>
        <!--<input type="button" value="Remove All" id="removeAll">-->
        <input type="button" class="btn btn-lg btn-primary" value="Start Workflow" id="startWf" style="display: none">
        <input type="button" class="btn btn-lg btn-primary" value="Request Again" id="reSubmitWf" style="display: none">
        <input type="button" class="btn btn-lg btn-primary" value="Approve" id="approveWf" style="display: none">
        <input type="button" class="btn btn-lg btn-primary" value="Reject" id="rejectWf" style="display: none">
    </div>
</div>
            </div>
        </div>
    <div class="row" style="display:none" id="wfHist" ng-controller="ctrl">
        <div class="col-md-9">
    <table class="table" style="width:75%;margin:auto">
        <thead>
        <tr>
            <th>#</th>
            <th>Operation User</th>
            <th>Operation Type</th>
            <th>Opt Time</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="hist in histList">
            <td>{{hist.optSeq}}</td>
            <td>{{hist.optUser}}</td>
            <td>{{hist.optType}}</td>
            <td>{{hist.createdDt | date:'yyyy-MM-dd hh:mm:ss'}}</td>
        </tr>
        </tbody>
    </table>
</div>
</div>
</div>
</main>

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
<script src="${base.contextPath}/static/js/jquery-ui.js"></script>
<!--  demo code -->
<script src="${base.contextPath}/static/js/rs-bpm-view.js"></script>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/app-staff.js"></script>
<!-- <script src="demo-list.js"></script>-->
<script>
    function viewWFTab(obj,selectedId){
       var li_ = $(obj).parent();
        li_.addClass("active");
        li_.siblings().removeClass("active");
        if(selectedId=="wfHist"){
            $("#wfMain").css("display","none");
            $("#wfHist").css("display","");
        }else{
            $("#wfHist").css("display","none");
            $("#wfMain").css("display","");
        }
    }

</script>
</body>
</html>
