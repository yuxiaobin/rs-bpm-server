<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <script>
        var basePath = "${base.contextPath}";
        var module_task_flag = "modules";
    </script>

</head>
<body class="home-template" ng-app="app" ng-controller="ctrl">
<header class="site-header jumbotron">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div style="position: absolute;right: 10px;"> <h3><a href="${base.contextPath}/wf">回首页</a></h3></div>
                <h1>Welcome, Staff Role</h1>
            </div>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <ul class="nav nav-pills nav-justified">
            <li role="presentation" class=""><a href="" ng-click="viewInbox()">Inbox</a></li>
            <li role="presentation" class="active"><a href="#">All Modules</a></li>
        </ul>
        <div class="list-group packages">
            <a class="package list-group-item">
                <div class="row">
                    <div class="col-md-3">
                        <h4 class="package-name">功能模块编号</h4>
                    </div>
                    <div class="col-md-9 hidden-xs">
                        <p class="package-description">点击发起工作流</p>
                    </div>
                </div>
            </a>
            <a class="package list-group-item" ng-repeat="module in moduleList" href="" >
                <div class="row">
                    <div class="col-md-3">
                        <h4 class="package-name" ng-bind="module.rsWfId"></h4>
                    </div>
                    <div class="col-md-9 hidden-xs">
                        <i href="#" ng-click="triggerWF(module.rsWfId)">发起</i>
                    </div>
                </div>
            </a>

        </div>
    </div>

</main>

</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/app-staff.js"></script>
</html>