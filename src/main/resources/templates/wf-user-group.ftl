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
                        ng-dblclick="viewGroupDetail(group.id)"
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
    <hr>
    <nav class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="form-group" align="center">
                <button type="button" class="btn btn-primary" onclick="addGroupPrepare()">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加用户组
                </button>
            </div>
        </div>
    </nav>
</main>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="false" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel"><span class="glyphicon glyphicon-info-sign" aria-hidden="true">添加用户组</span></h4>
            </div>
            <div class="modal-body" ng-model="confirmBody">
                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon3">新增用户组名称：</span>
                    <input type="text" class="form-control" aria-describedby="basic-addon3" ng-model="groupName">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal" ng-click="addGroup()">
                    <span class="glyphicon glyphicon-save" aria-hidden="true"></span>确认添加
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/app-usergroup.js"></script>
<script>
    function addGroupPrepare(){
        $('#myModal').modal({backdrop:true});
    }

</script>
</html>