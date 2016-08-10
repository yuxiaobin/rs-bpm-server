<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <script>
        var basePath = "${base.contextPath}";
    </script>
</head>
<body class="home-template" ng-app="app" ng-controller="ctrl">
<header class="site-header jumbotron">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <h1>Welcome, Manager Role</h1>
            </div>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <div class="list-group packages">
            <div class="row">
                <div class="col-md-12">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Task Descp</th>
                            <th>Operation User</th>
                            <th>Operation Seq</th>
                            <th>Assigned User(s)</th>
                            <th>Assigned Group(s)</th>
                            <th>Opt Time</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="taskv in taskvList">
                            <td>{{$index+1}}</td>
                            <td>{{taskv.task.taskDescp}}</td>
                            <td>{{taskv.instHist.optUser}}</td>
                            <td>{{taskv.instHist.optSeq}}</td>
                            <td>{{taskv.instHist.nextAssigner}}</td>
                            <td>{{taskv.task.assignGroups}}</td>
                            <td>{{taskv.instHist.createdDt | date:'yyyy-MM-dd hh:mm:ss'}}</td>
                            <!--{{taskv.instHist.status}}-->
                            <td><a href="" ng-click="viewTask(taskv.instHist.instId)">TODO</a></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/app-manager.js"></script>
</html>