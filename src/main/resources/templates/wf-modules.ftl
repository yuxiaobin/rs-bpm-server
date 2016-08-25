<!--
    模拟增加模块页面@0819
  -->
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
                <h1>Welcome, Admin Role</h1>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="collapse navbar-collapse">
            <form class="navbar-form navbar-left">
                <div class="form-group" style="margin-top: auto;"><input type="text" class="form-control" ng-model="moduleName"  placeholder="Module Name"></div>
                <input type="button" class="btn btn-default" name="New Module"
                       value="New Module" ng-click="newModule()">
            </form>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <div class="list-group packages">
            <a class="package list-group-item" ng-repeat="module in moduleList" href="${base.contextPath}/wfadmin/define/{{module.modId}}" >
                <div class="row">
                    <div class="col-md-3">
                        <h4 class="package-name" ng-bind="module.name">Module Name</h4>
                    </div>
                    <div class="col-md-9 hidden-xs">
                        <p class="package-description">Workflow</p>
                    </div>
                    <!-- <div class="package-extra-info col-md-9 col-md-offset-3 col-xs-12">
                        <span><i class="fa fa-star"></i> 98666</span>
                    </div> -->
                </div>
            </a>
        </div>
    </div>
</main>

</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/app.js"></script>
</html>