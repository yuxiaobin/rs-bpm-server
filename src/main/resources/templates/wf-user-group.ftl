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
                <div style="position: absolute;right: 10px;"> <h3><a href="${base.contextPath}/wf">回首页</a></h3></div>
                <h1>Welcome, 用户组维护页面</h1>
            </div>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <table class="table">
                    <thead>
                    <tr>
                        <th width="10%">#</th>
                        <th width="20%">用户组名称</th>
                        <th>用户组成员</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="group in groupList"
                        ng-click="selectTableRow($event)"
                        ng-dblclick="viewGroupDetail(group.id,group.name)"
                            >
                        <td>{{$index+1}}</td>
                        <td>{{group.name}}</td>
                        <td>
                            <label ng-repeat="user in group.userInGroup">
                                {{user.name}},
                            </label>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/app-usergroup.js"></script>
</html>