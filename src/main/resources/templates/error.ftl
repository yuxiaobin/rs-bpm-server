<!doctype html>
<html>
<head>
    <title>Error</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="stylesheet" href="${base.contextPath}/static/css/bootstrap.css">
</head>
<body class="home-template">
<header class="site-header">
    <div class="container">
        <div class="row">
            <div class="col-xs-6">
                <h1>出错页面</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6">
                <h3>错误编码：${return_code}， 错误信息：${return_msg}</h3>
            </div>
        </div>
    </div>
</header>
</body>
</html>
