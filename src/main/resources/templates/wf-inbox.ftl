<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <script>
        var basePath = "${base.contextPath}";
        <#if userId?exists>
            var userId = "${userId}";
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
            </div>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <ul class="nav nav-pills nav-justified">
            <li role="presentation" class="active"><a href="#">待办事宜</a></li>
            <li><a href="/wf/modules/page">发起请求页面</a></li>
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
                    <tr ng-repeat="taskv in taskvList" ng-dblclick="viewAwtInMK(taskv.rsWfId, taskv.instNum, taskv.refMkid)">
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

</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/app-inbox.js"></script>
</html>