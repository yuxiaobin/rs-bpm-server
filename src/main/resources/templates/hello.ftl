<!doctype html>
<html>
<head>
    <title>jsPlumb - state machine demonstration</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="stylesheet" href="${base.contextPath}/static/css/jsPlumbToolkit-defaults.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/main.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/jsPlumbToolkit-demo.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/jquery.contextMenu.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/rs-bpm.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/theme/base/jquery.ui.all.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/properties.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/demo.css">
    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.min.css">
    <script>
        var basePath = "${base.contextPath}";
    </script>
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
                <li role="presentation" class="active"><a href="#" onclick="viewWf('staff')">Staff Role</a></li>
                <li><a href="#" onclick="viewWf('manager')">Manager Role</a></li>
                <li><a href="#" onclick="viewWf('admin')">Admin Role</a></li>
            </ul>
        </div>
    </div>

</main>


<!-- <script src="demo-list.js"></script>-->
</body>
<script type="text/javascript">
    function viewWf(roleName){
        window.location = basePath +"/wf/"+roleName;
    }

</script>
</html>
