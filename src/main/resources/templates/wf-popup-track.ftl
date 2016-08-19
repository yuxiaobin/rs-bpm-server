<!doctype html>
<html>
<head>
    <title>流程跟踪</title>
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
        <#if rsWfId?exists>
            var rsWfId = "${rsWfId}";
        </#if>
        <#if instNum?exists>
            var instNum = "${instNum}";
        </#if>
        <#if refMkid?exists>
            var refMkid = "${refMkid}";
        </#if>
    </script>
</head>
<body data-demo-id="statemachine" data-library="dom" class="home-template" ng-app="app">
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideModal()"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="editTaskTitle">流程跟踪</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-6">
        <ul class="nav nav-pills nav-justified">
            <li role="presentation" class="active"><a href="javascript:void(0)" onclick="viewWFTab(this,'wfMain')">流程图形</a></li>
            <li role="presentation"><a href="javascript:void(0)" onclick="viewWFTab(this,'wfHist')">流程日志</a></li>
        </ul>
        </div>
    </div>
    <div class="row">
        <div class="jtk-demo-main" id="wfMain" style="width:100%">
            <div class="rscontainer jtk-demo-canvas canvas-wide statemachine-demo jtk-surface jtk-surface-nopan " id="canvas" style="width:100%">

            </div>
        </div>
    </div>
    <div class="row" style="display:none" id="wfHist" ng-controller="ctrl">
        <div class="col-md-9">
        <table class="table" style="width:75%;margin:auto">
            <thead>
            <tr>
                <th>事务名称</th>
                <th>处理人</th>
                <th>处理</th>
                <th>开始时间</th>
                <th>要求完成时间</th>
                <th>实际完成时间</th>
                <th>待处理人</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="hist in histList">
                <td>{{hist.taskDescpDisp}}</td>
                <td>{{hist.optUser}}</td>
                <td>{{hist.optType}}</td>
                <td>{{hist.taskBegin | date:'yyyy-MM-dd hh:mm:ss'}}</td>
                <td>{{hist.taskEnd | date:'yyyy-MM-dd hh:mm:ss'}}</td>
                <td>{{hist.taskRend | date:'yyyy-MM-dd hh:mm:ss'}}</td>
                <td>{{hist.taskOwner}}</td>
            </tr>
            </tbody>
        </table>
        </div>
    </div>
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
<script src="${base.contextPath}/static/js/jquery-ui.js"></script>
<!--  demo code -->
<script src="${base.contextPath}/static/js/rs-bpm-view.js"></script>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/app-track.js"></script>
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
