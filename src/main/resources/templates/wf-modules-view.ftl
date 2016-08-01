<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <script>
        var basePath = "${base.contextPath}";
        var module_task_flag = "modules";
//        var role = "${role}";
    </script>

</head>
<body class="home-template" ng-app="app" ng-controller="ctrl">
<header class="site-header jumbotron">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>All Modules</h1>
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
            <a class="package list-group-item" ng-repeat="module in moduleList" href="" >
                <div class="row">
                    <div class="col-md-12">
                        <h4 class="package-name" >Module Name : {{module.name}}</h4>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Version</th>
                                <th>Workflow Name</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="wf in module.wfList">
                                <td>{{$index+1}}</td>
                                <td>{{wf.VERSION}}</td>
                                <td><a href="#" ng-click="viewWfById(wf.wfId)">Workflow Name</a></td>
                            </tr>
                            </tbody>
                        </table>
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