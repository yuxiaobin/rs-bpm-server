<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/plugin/bootstrap-select.min.css">
    <script>
        var basePath = "${base.contextPath}";
        <#if groupId?exists>
        var groupId = "${groupId}";
	    </#if>
	    <#if groupName?exists>
		    var groupName = "${groupName}";
		</#if>
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
            <div class="panel panel-default">
				  <div class="panel-heading">用户组名称: ${groupName}</div>
				  <input type="hidden" name="groupId" value="${groupId}" id="groupId">
                <table class="table">
                    <thead>
                    <tr>
                        <th width="10%"><input type="checkbox" id="selectAll" onclick="selectAllRecords(this)"><label for="selectAll">全选</label></th>
                        <th width="20%">用户编号</th>
                        <th>用户名</th>
                    </tr>
                    </thead>
                    <tbody id="tableBody">
                    <tr ng-repeat="user in userList">
                        <td><input type="checkbox" value="{{user.id}}" id="user_{{user.id}}" class="record_check"></td>
                        <td>{{user.id}}</td>
                        <td>{{user.name}}</td>
                        <!--<td><label for="user_{{user.id}}">{{user.id}}</label></td>
                        <td><label for="user_{{user.id}}">{{user.name}}</label></td>-->
                    </tr>
                    </tbody>
                </table>
            </div>
            </div>
        </div>
        <div class="row">
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-default" ng-click="addUserPrepare();">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加用户
                </button>
                <button type="button" class="btn btn-default" ng-click="deleteUsers()">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除用户
                </button>
            </div>
        </div>
        <div class="row" ng-show="addFlag">
            <select class="selectpicker" data-hide-disabled="true" multiple data-live-search="true" id="selectAddUser" ng-model="selectedUsers">
                <optgroup ng-repeat="optGroup in optGroupList" label="{{optGroup.name}}">
                    <option ng-repeat="usInGp in optGroup.usersInGroup" value="{{usInGp.id}}">{{usInGp.name}}</option>
                </optgroup>
                <option ng-repeat="us in optUserList" value="{{us.id}}">{{us.name}}</option>
            </select>
            <button type="button" class="btn btn-default" ng-click="saveUser2Group()" ng-dbclick="">
                <span class="glyphicon glyphicon-save" aria-hidden="true"></span>保存
            </button>
            <button type="button" class="btn btn-default" ng-click="cancelAddUser()">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>放弃
            </button>
        </div>

    </div>

    <hr>
    <nav class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="form-group" align="center">
                <button type="button" class="btn btn-danger" onclick="showWarning4DeleteGroup()">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除该用户组
                </button>
                <button type="button" class="btn btn-default" ng-click="back2GroupIndex()" style="margin-left:30px">
                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>返回
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
                <h4 class="modal-title" id="myModalLabel"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span></h4>
            </div>
            <div class="modal-body" ng-model="confirmBody">
                确认要删除该用户组吗？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal" ng-click="deleteGroup()">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>确认删除
                </button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/plugin/bootstrap-select.min.js"></script>
<script src="${base.contextPath}/static/js/app-group.js"></script>
<script>
    function showWarning4DeleteGroup(){
        $('#myModal').modal({backdrop:true});
    }
</script>
</html>