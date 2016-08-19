<!doctype html>
<html>
<head>
    <title>Welcome</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="stylesheet" href="${base.contextPath}/static/css/jquery.contextMenu.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <script>
        var basePath = "${base.contextPath}";
    </script>
    <script src="${base.contextPath}/static/js/jquery-1.9.1.min.js"></script>
</head>
<body data-demo-id="statemachine" data-library="dom" class="home-template">
<header class="site-header">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>Welcome</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6">
                <h3>Please select a Role</h3>
            </div>
        </div>
    </div>
</header>
<main>
    <div class="container">
        <div class="row">
            <ul class="nav nav-pills nav-justified">
                <li role="presentation" class="active"><a href="#" onclick="viewTab('staff')">Staff Role</a></li>
                <li><a href="#" onclick="viewTab('manager')">Manager Role</a></li>
                <li><a href="#" onclick="viewTab('admin')">Admin Role</a></li>
                <li><a href="#" onclick="viewTab('history')">History</a></li>
            </ul>
        </div>
        <div class="row tab" id="staffTab">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="#" onclick="viewWf('staff1')">staff1</a></li>
                <li><a href="#" onclick="viewWf('staff2')">staff2</a></li>
                <li><a href="#" onclick="viewWf('staff3')">staff3</a></li>
            </ul>
        </div>
        <div class="row tab" id="managerTab" style="display: none">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="#" onclick="viewWf('manager1')">manager1</a></li>
                <li><a href="#" onclick="viewWf('manager2')">manager2</a></li>
                <li><a href="#" onclick="viewWf('manager3')">manager3</a></li>
            </ul>
        </div>
        <div class="row tab" id="adminTab" style="display: none">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="#" onclick="viewWf('admin')">admin</a></li>
            </ul>
        </div>
        <div class="row tab" id="historyTab" style="display: none">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="#" onclick="viewHistory()">View history</a></li>
            </ul>
        </div>
    </div>

</main>


<!-- <script src="demo-list.js"></script>-->
</body>
<script type="text/javascript">
    function viewWf(userid){
        window.location = basePath +"/wf/"+userid;
    }

    function viewTab(role){
        $("#"+role+"Tab").css("display","")
                .siblings(".tab").hide();
    }

    function viewHistory(){

    }

</script>
</html>
