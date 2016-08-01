<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    
</head>
<body class="home-template" ng-app="app" ng-controller="ctrl">
<header class="site-header jumbotron">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>New Module</h1>
                <!-- <p>
                    稳定、快速、免费的开源项目 CDN 服务<br>
                    <span class="package-amount">共收录了 <strong>2351</strong>
                        个开源项目
                    </span>
                </p> -->
                <form class="">
                    <div class="form-group">
                        <input type="text" class="form-control search clearable" ng-model="moduleName"
                               placeholder="Module Name">
                        <!-- <i class="fa fa-search"></i> -->
                    </div>
                    <div class="form-group">
                        <input type="button" class="btn-lg" name="New Module"
                               value="New Module" ng-click="newModule()">
                        <!-- <i class="fa fa-search"></i> -->
                    </div>
                </form>
            </div>
        </div>
    </div>
</header>

<main class="packages-list-container" id="all-packages">
    <div class="container">
        <div class="list-group packages">
            <a class="package list-group-item" ng-repeat="module in moduleList" href="/wf/admin/define/{{module.modId}}" >
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