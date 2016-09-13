<html>
<head>
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/font-awesome.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/site.min.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/plugin/bootstrap-select.min.css">
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
            <li><a href="${base.contextPath}/inbox">待办事宜</a></li>
            <li><a href="${base.contextPath}/wf/modules/page">发起请求页面</a></li>
            <li  role="presentation" class="active"><a href="#">历史记录</a></li>
        </ul>
        <div class="row">
            <div class="col-md-12">
                <nav class="navbar navbar-default" role="navigation">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <a class="navbar-brand" href="javascript:void(0) ">工作流事务</a>
                        </div>
                        <form class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                                <select id="selectMyTx" class="selectpicker" ng-change="selectMyTxChange()" ng-model="selectMyTx">
                                    <option value="PEND">待我处理</option>
                                    <option value="DONE">我已处理</option>
                                    <option value="ALL">全部事务</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select id="wfOptionsId" class="selectpicker" ng-change="chooseOption(taskOption)" ng-model="taskOption">
                                    <option value="">请选择</option>
                                    <optgroup ng-repeat="optGroup in optGroupList">
                                        <!--<option ng-repeat="opt in optGroup.opts" value="{{opt.value}}">{{opt.descp}}-{{opt.disflag}}</option>-->
                                        <option ng-repeat="opt in optGroup.opts" value="{{opt.value}}" ng-if="!opt.disflag">
                                            {{opt.descp}}
                                        </option>
                                        <option ng-repeat="opt in optGroup.opts" value="{{opt.value}}" disabled  ng-if="opt.disflag">
                                            {{opt.descp}}
                                        </option>
                                    </optgroup>
                                </select>

                            </div>
                        </form>
                    </div>
                </nav>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>工作流状态</th>
                        <th>待处理人</th>
                        <th>工作流ID</th>
                        <th>实例号</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="taskv in taskvList"
                        rs-inst-num="{{taskv.instNum}}" rs-ref-mkid="{{taskv.refMkid}}" rs-wf-id="{{taskv.rsWfId}}"
                        ng-click="selectTableRow($event)">
                        <td>{{taskv.taskDescpDisp}}</td>
                        <td>{{taskv.taskOwner}}</td>
                        <td>{{taskv.refMkid}}</td>
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
</body>
<script src="${base.contextPath}/static/js/angular.js"></script>
<script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script src="${base.contextPath}/static/js/bootstrap.js"></script>
<script src="${base.contextPath}/static/js/plugin/bootstrap-select.min.js"></script>
<script src="${base.contextPath}/static/js/app-history.js"></script>
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