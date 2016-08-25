<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/jquery.contextMenu.css">
    <script>
        var basePath = "${base.contextPath}";
        <#if userId?exists>
            var userId = "${userId}";
        </#if>
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
<body class="home-template" ng-app="app" ng-controller="ctrl">
<header class="site-header jumbotron">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div style="position: absolute;right: 10px;"> <h3><a href="${base.contextPath}/wf">回首页</a></h3></div>
                <h1>Welcome, {{userId}}</h1>
                <p>模拟业务功能页面的待办事宜：
                    <#if rsWfId?exists>
                        rsWfId=${rsWfId},
                    </#if>
                    <#if instNum?exists>
                        instNum= ${instNum},
                    </#if>
                    <#if refMkid?exists>
                        refMkid= ${refMkid},
                    </#if>
                </p>
            </div>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <ul class="nav nav-pills nav-justified">
            <li role="presentation" class="active"><a href="#">Inbox</a></li>
        </ul>

        <div class="row">
            <div class="col-md-12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>状态名称</th>
                        <th>主题</th>
                        <th>事务收到时间</th>
                        <th>要求完成时间</th>
                        <th>上步处理人</th>
                        <th>待处理人</th>
                        <th>已处理人</th>
                        <th>创建人</th>
                        <th>工作流</th>
                        <th>实例号</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="taskv in taskvList" ng-click="selectTableRow($event)" rs-wf-id="{{taskv.rsWfId}}" rs-inst-num="{{taskv.instNum}}" rs-ref-mkid="{{taskv.refMkid}}">
                        <td>{{taskv.taskDescpDisp}}</td>
                        <td>{{taskv.awtTitle}}</td>
                        <td>{{taskv.awtBegin | date:'yyyy-MM-dd hh:mm:ss'}}</td>
                        <td>{{taskv.awtEnd | date:'yyyy-MM-dd hh:mm:ss'}}</td>
                        <td> - </td>
                        <td>{{taskv.assignerId}}</td>
                        <td> - </td>
                        <td>{{taskv.createdBy}}</td>
                        <td>{{taskv.rsWfId}}</td>
                        <td>{{taskv.instNum}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="false" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <iframe style="zoom: 0.8;" height="700px" src="" frameBorder="0" width="99.6%"></iframe>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<ul id="taskMenu" class="contextMenu">
    <li class=""><a href="#commit">流程提交</a></li>
    <li class=""><a href="#back">流程退回</a></li>
    <li class=""><a href="#veto">流程否决</a></li>
    <li class=""><a href="#forward">流程转交</a></li>
    <li class=""><a href="#redo">流程撤回</a></li>
    <li class=""><a href="#handme">我来处理</a></li>
    <li class=""><a href="#transfer">流程调度</a></li>
    <li class=""><a href="#track">流程跟踪</a></li>
</ul>
</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/jquery.contextMenu.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/app-inbox.js"></script>
<script>
    window.addEventListener('message', receiveMessage, false);
    function receiveMessage(evt) {
        console.log("got message from child page:"+evt.data);
        var recvData = $.parseJSON(evt.data);
        if(recvData.opt=="C"){
            $("#myModal").modal("hide");
        }
    }

    $('#myModal').on('hide.bs.modal', function (e) {
        window.reloadTask();
    });
</script>
</html>