/**
 *
 * @type {Array}
 */
var result_data = [];
var nodeList = result_data.tasks;
var connList = result_data.conns;
jsPlumb.ready(function () {

    $(".menu-task").draggable({helper: 'clone'});
    $(".rscontainer").droppable({
        drop: function(event, ui) {
            var trans4ContainerPos = transferPagePos2ContainerPos(event.pageX, event.pageY);
            var task_width = ui.draggable.context.clientWidth;
            var task_height = ui.draggable.context.clientHeight;
            var pos_x = trans4ContainerPos.x-task_width/2;
            var pos_y = trans4ContainerPos.y-task_height/2;
            var clone_div = $(ui.draggable).clone();
            var rs_type = clone_div.attr(RS_ATTR_TASK_TYPE);
            var userNode = {pgId:new Date().getTime(),rsType:rs_type,taskDescpDisp:clone_div.text(), position:{top:pos_y,left:pos_x},
                txType:"S",timeLimitTp:"H",alarmTimeTp:"H",timeLimit:24,
                SIGN_CHOICES:{AllHandledThenGo:true,AtLeastHandled:1},
                TX_CHOICES:{AllowGoBack:true,SignWhenGoBack:true,AllowVeto:true,SignWhenVeto:true,SignWhenReCall:true},
                TX_BK_CHOICES:{GoBackToPrevious:true}
            };
            newNodeById(userNode);
        }
    });
    var transferPagePos2ContainerPos = function(pageX_,pageY_){
        var container_offset = $(".rscontainer").offset();
        var transPos = {};
        transPos.x = pageX_ - container_offset.left;
        transPos.y = pageY_ - container_offset.top;
        return transPos;
    }
    var transferPagePos2ParentPos = function(pageX_,pageY_, target_){
        var container_offset = $(target_).offset();
        var transPos = {};
        transPos.x = pageX_ - container_offset.left;
        transPos.y = pageY_ - container_offset.top;
        return transPos;
    }

    // setup some defaults for jsPlumb.
    var instance = jsPlumb.getInstance({
        Endpoint: ["Dot", {radius: 2}],
        Connector:"StateMachine",
        HoverPaintStyle: {strokeStyle: "#1e8151", lineWidth: 2 },
        ConnectionOverlays: [
            [ "Arrow", {
                location: 1,
                id: "arrow",
                length: 14,
                foldback: 0.8
            } ],
            [ "Label", { label: "<i>"+CONNECTION_LABEL_NORMAL+"</i>", id: "label", cssClass: "aLabel" }]
        ],
        Container: "canvas"
    });

    window.jsp = instance;
    var canvas = document.getElementById("canvas");
    // bind a click listener to each connection; the connection is deleted. you could of course
    // just do this: jsPlumb.bind("click", jsPlumb.detach), but I wanted to make it clear what was
    // happening.
   /* instance.bind("dbclick", function (c) {
        console.log(c);
        instance.detach(c);
    });*/

    // this is the paint style for the connecting lines..
    var connectorPaintStyle = {
            lineWidth: 4,
            strokeStyle: "#61B7CF",
            joinstyle: "round",
            outlineColor: "white",
            outlineWidth: 2
        },
        connectorHoverStyle = {
            lineWidth: 4,
            strokeStyle: "#216477",
            outlineWidth: 2,
            outlineColor: "white"
        },
        endpointHoverStyle = {
            fillStyle: "#216477",
            strokeStyle: "#216477"
        };

    /**
     * double click connection to remove it.
     * @type {string}
     */
    var connectionClickTime = "";
    instance.bind("click", function (c) {
        if(connectionClickTime!="" && new Date().getTime()-connectionClickTime<200){
            var sourceNodeId = c.source.getAttribute("id");
            var sourceNodeType = c.source.getAttribute(RS_ATTR_TASK_TYPE);
            if(RS_TYPE_CONDITION==sourceNodeType){
                $("#condConnId").show();
            }else{
                $("#condConnId").hide();
            }
            var conn_nameDisp = c.getOverlay("label").getElement().firstChild.innerHTML;
            $("#sourceNodeId").val(sourceNodeId);
            $("#currConnId").val(c.getId());
            $("#conn_nameDisp").val(conn_nameDisp);
            var condResult = c.getParameter(CONNECTION_CON_VALUE);
            if(condResult=="true"){
                $("#condResultTrue").click();
            }else{
                $("#condResultFalse").click();
            }
            $('#condConnModal').modal({backdrop:false});
        }else{
            connectionClickTime = new Date().getTime();
        }
    });

    instance.bind("connection", function (info) {
        var connection_id = info.connection.id;
        var connection_label = info.connection.getOverlay("label").getElement();
        $(connection_label).attr("connection_id",info.connection.id)
            .contextMenu({
            menu: 'connMenu'
            },
            function(action, el, pos) {
                var id_ = $(el).attr("id");
                if (action == 'edit') {
                    editCondition(id_);//TODO 0825
                }
                else if (action == 'delete') {
                    var removeItem = $(el).attr("connection_id");
                    instance.detach(info);
                }
            });
        var con_source = info.source;
        var connJspId = info.connection.getId();
        var connArray = instance.getConnections({source:con_source});
        if($(con_source).hasClass(RS_TYPE_CONDITION)){
            if(connArray.length==1){
                connection_label.firstChild.innerHTML = CONNECTION_LABEL_TRUE;
                info.connection.setParameter(CONNECTION_CON_VALUE,"true");
            }else if(connArray.length==2){
                for(var i=0;i<2;++i){
                    var con_ = connArray[i];
                    if(con_.getId()!=connJspId){
                        var conResVal =  con_.getParameter(CONNECTION_CON_VALUE);
                        if("true"==conResVal){
                            connection_label.firstChild.innerHTML = CONNECTION_LABEL_FALSE;
                            info.connection.setParameter(CONNECTION_CON_VALUE,"false");
                        }else{
                            connection_label.firstChild.innerHTML = CONNECTION_LABEL_TRUE;
                            info.connection.setParameter(CONNECTION_CON_VALUE,"true");
                        }
                    }
                }
            }else{
                instance.detach(info);
            }
        }
        else{
            if(connArray.length>1){
                instance.detach(info);
            }
        }
    });
    // initialise element as connection targets and source.
    var initNode = function(el,type) {
        // initialise draggable elements.
        instance.draggable(el,{containment:"parent"});
        instance.makeSource(el, {
            filter: ".ep",
            anchor: "Continuous",
            connectorStyle:connectorPaintStyle,
            connector: [ "Flowchart", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true } ],
            hoverPaintStyle: endpointHoverStyle,
            connectorHoverStyle: connectorHoverStyle,
            maxConnections: 5,
            //onMaxConnections: function (info, e) { alert("Maximum connections (" + info.maxConnections + ") reached");  },
            overlays: [
                [ "Label", {
                    location: [0.5, 1.5],
                    label: "Drag",
                    cssClass: "endpointSourceLabel",
                    visible:false
                } ]
            ]
        });
        instance.makeTarget(el, {
            dropOptions: { hoverClass: "dragHover" },
            anchor: "Continuous",
            allowLoopback: false//Not allow to connect to itself
        });
        // this is not part of the core demo functionality; it is a means for the Toolkit edition's wrapped
        // version of this demo to find out about new nodes being added.
        instance.fire("jsPlumbDemoNodeAdded", el);
    };

    var newNodeById = function(node_) {
        var d = document.createElement("div");
        var taskType = node_.rsType;
        d.className = "w "+taskType;
        d.id = node_.pgId;
        d.style.left = node_.position.left + "px";
        d.style.top = node_.position.top + "px";
        $(d).attr("rs-data-id", node_.id)
            .attr(RS_ATTR_TASK_TYPE,taskType)
            .attr(RS_NODE_ATTR_DESCP, node_.taskDescp)
            .attr(RS_NODE_ATTR_DESCP_DISP, node_.taskDescpDisp);
        if(taskType==RS_TYPE_CONDITION){
            d.innerHTML = "<div class='task-descp'>"+node_.taskDescpDisp + "</div><div class=\"ep\"></div>";
            $(d).attr(RS_ATTR_COND_EXPRESSION,node_.condExp);
        }else{
            d.innerHTML = node_.taskDescpDisp + "<div class=\"ep\"></div>";
            $(d).attr(RS_ATTR_ASSIGNERS, JSON.stringify(node_.assigners))
                .attr(RS_ATTR_TX_CHOICES,JSON.stringify(node_.TX_CHOICES))
                .attr(RS_ATTR_TX_PR_CHOICES,JSON.stringify(node_.TX_PR_CHOICES))
                .attr(RS_ATTR_TX_BK_CHOICES,JSON.stringify(node_.TX_BK_CHOICES))
                .attr(RS_ATTR_SIGN_CHOICES,JSON.stringify(node_.SIGN_CHOICES))
                .attr("txCode",node_.txCode)
                .attr("txType",node_.txType)
                .attr("buzStatus",node_.buzStatus)
                .attr("timeLimit",node_.timeLimit)
                .attr("timeLimitTp",node_.timeLimitTp)
                .attr("alarmTime",node_.alarmTime)
                .attr("alarmTimeTp",node_.alarmTimeTp)
                .attr("moduleId",node_.moduleId)
                .attr("runParam",node_.runParam);
        }

        $(d).dblclick(function(){
            editTask($(this).attr("id"));
        });
        var menuId = "activityMenu"
        if(node_.rsType==RS_TYPE_START || node_.rsType==RS_TYPE_END ){
            menuId = "activityMenu2StartEnd"
        }
        $(d).contextMenu({
            menu: menuId
        }, function(action, el, pos) {
            var id_ = $(el).attr("id");
            if (action == 'edit') {
                editTask(id_);
            }
            else if (action == 'delete') {
                instance.remove(id_);
            }
        });
        instance.getContainer().appendChild(d);
        initNode(d);
        return d;
    };

    var initEmptyWF = function(){
        var startNode = {pgId:RS_TYPE_START,rsType:RS_TYPE_START,taskDescpDisp:"开始", position:{top:70,left:350},
            txCode:"0000",txType:"B",timeLimitTp:"H",alarmTimeTp:"H",
            TX_CHOICES:{AllowEdit:true,AllowDelete:true},
            TX_BK_CHOICES:{GoBackToPrevious:true},
            SIGN_CHOICES:{AllHandledThenGo :true}
        };
        var endNode = {rsType:RS_TYPE_END,rsType:RS_TYPE_END,taskDescpDisp:"结束", position:{top:370,left:350},
            txCode:"9999",txType:"E",timeLimitTp:"H",alarmTimeTp:"H",
            TX_CHOICES:{AllowEdit:false,AllowDelete:false},
            TX_BK_CHOICES:{GoBackToPrevious:true},
            SIGN_CHOICES:{AllHandledThenGo :true}
        };
        newNodeById(startNode);
        newNodeById(endNode);
    }
    var url_ = basePath+"/wfadmin/module/"+refMkid+"/wf";
    $.ajax({
            type: "GET",
            url: url_,
            success: function (msg) {
                result_data = msg;
                nodeList = result_data.tasks;
                connList = result_data.conns;
                instance.batch(function () {
                    if(typeof(nodeList)=="undefined"){
                        nodeList = [];
                    }
                    var nodeSize = nodeList.length;
                    if(nodeSize!=undefined && nodeSize!=0){
                        for(var i=0;i<nodeList.length;++i){
                            newNodeById(nodeList[i]);
                        }
                        for(var i=0;i<connList.length;++i){
                            var conn_data = connList[i];
                            var conn_ = instance.connect({ source: conn_data.source_id, target: conn_data.target_id });
                            conn_.getOverlay("label").label = "<i>"+conn_data.con_descp+"</i>";
                            conn_.setParameter(CONNECTION_CON_VALUE, conn_data.con_value);
                        }
                    }else{
                        initEmptyWF();
                    }
                });
            }
        }
    );
    jsPlumb.fire("jsPlumbDemoLoaded", instance);

    $(document).bind("contextmenu",function(e){
        return false;
    });

    $("#removeAll").on("click",function(){
        instance.empty("canvas");
    });

    $("#Save").on("click",function(){
        var save_data = {};
        var save_tasks = [];
        var save_conns = [];
        var curr_conns = instance.getConnections();
        for(var i=0;i<curr_conns.length;++i){
            var conn_ = curr_conns[i];
            var conn_json = {};
            conn_json.con_id = conn_.id;
            conn_json.con_descp = conn_.getOverlay("label").getElement().firstChild.innerHTML
            var connVal = conn_.getParameter(CONNECTION_CON_VALUE);
            if(typeof(connVal)=="undefined"){
                connVal = "";
            }
            conn_json.con_value = connVal;
            conn_json.source_id = conn_.source.id;
            conn_json.target_id = conn_.target.id;
            save_conns[save_conns.length] = conn_json;
        }
        $("#canvas .w").each(function(){
            var task_json = {};
            var jqObj = $(this);
            var pos_ = jqObj.position();
            task_json.id = jqObj.attr("id");
            task_json.rsType = jqObj.attr(RS_ATTR_TASK_TYPE);
            task_json.taskDescp = jqObj.attr(RS_NODE_ATTR_DESCP);
            task_json.taskDescpDisp = jqObj.attr(RS_NODE_ATTR_DESCP_DISP);
            if(task_json.rsType ==RS_TYPE_CONDITION){
                pos_.top = parseInt(pos_.top)+13;// add for rotation divide issue
                pos_.left = parseInt(pos_.left)+13;
                task_json.position = pos_;
                task_json.condExp = jqObj.attr(RS_ATTR_COND_EXPRESSION);
            }else{
                task_json.position = pos_;
                var assignerJSONStr = jqObj.attr(RS_ATTR_ASSIGNERS);
                if(assignerJSONStr==undefined || assignerJSONStr==""){
                    assignerJSONStr = "[]";
                }
                task_json.assigners = $.parseJSON(assignerJSONStr);
                task_json.txCode = jqObj.attr("txCode");
                task_json.txType = jqObj.attr("txType");
                task_json.buzStatus = jqObj.attr("buzStatus");
                var timeLimit = jqObj.attr("timeLimit");
                if(timeLimit!=""){
                    timeLimit = parseInt(timeLimit);
                }
                task_json.timeLimit = timeLimit;
                task_json.timeLimitTp = jqObj.attr("timeLimitTp");
                var alarmTime = jqObj.attr("alarmTime");
                if(alarmTime!=""){
                    alarmTime = parseInt(alarmTime);
                }
                task_json.alarmTime = alarmTime;
                task_json.alarmTimeTp = jqObj.attr("alarmTimeTp");
                task_json.moduleId = jqObj.attr("moduleId");
                task_json.runParam = jqObj.attr("runParam");

                var txChoicesStr = jqObj.attr(RS_ATTR_TX_CHOICES);
                if(txChoicesStr==undefined || txChoicesStr==""){
                    txChoicesStr = "{}";
                }
                task_json.TX_CHOICES = $.parseJSON(txChoicesStr);
                var txPrChoicesStr = jqObj.attr(RS_ATTR_TX_PR_CHOICES);
                if(txPrChoicesStr==undefined || txPrChoicesStr==""){
                    txPrChoicesStr = "{}";
                }
                task_json.TX_PR_CHOICES = $.parseJSON(txPrChoicesStr);
                var txBkChoicesStr = jqObj.attr(RS_ATTR_TX_BK_CHOICES);
                if(txBkChoicesStr==undefined || txBkChoicesStr==""){
                    txBkChoicesStr = "{}";
                }
                task_json.TX_BK_CHOICES = $.parseJSON(txBkChoicesStr);
                var signChoicesStr = jqObj.attr(RS_ATTR_SIGN_CHOICES);
                if(signChoicesStr==undefined || signChoicesStr==""){
                    signChoicesStr = "{}";
                }
                task_json.SIGN_CHOICES = $.parseJSON(signChoicesStr);
            }
            save_tasks[save_tasks.length] = task_json;
        });
        save_data.tasks = save_tasks;
        save_data.conns = save_conns;
        save_data.custFuncVarArray = custFuncVarArray;
        var json_str = JSON.stringify(save_data);
        console.log(json_str);
        $.ajax({
                type: "POST",
                url: basePath+"/wfadmin/module/"+refMkid+"/wf",
                data:json_str,
                headers: { 'Content-Type': "application/json" },
                success: function (msg) {
                    if(msg.return_code==-1){
                        alert("存在非法的变量："+msg.invalidVarCode);
                    }else{
                        alert("Save successfully");
                    }
                },
                error:function(err){
                    alert(err.message);
                }
            }
        );
    });

    $("#resetBtn").on("click",function(){
        $("#canvas .w").each(function(){
            if($(this).hasClass(RS_TYPE_START) || $(this).hasClass(RS_TYPE_END)){
                //start&end no need to remove
            }else{
                instance.remove($(this).attr("id"));
            }
        });
    });
    $("#backBtn").on("click",function(){
        window.location = basePath+"/wf";
    });
});

function editCondition(id){
    $('#condConnModal').modal({backdrop:false});
}
function cancelCondConnEdit(){
    $("#condConnModal").modal("hide");
}
function updateCondConnEdit(){
    var sourceNodeId = $("#sourceNodeId").val();
    var currConnId = $("#currConnId").val();
    var sourceNodeType = $("#"+sourceNodeId).attr(RS_ATTR_TASK_TYPE);
    var connArray = window.jsp.getConnections({source:sourceNodeId});

    var currConn = "";
    if(RS_TYPE_CONDITION==sourceNodeType){
        var rd_val = getRadioValue("condResult");
        for(var i=0;i<connArray.length;++i){
            var con_ = connArray[i];
            if(con_.getId()==currConnId){
                currConn = con_;
            }else{
                if(rd_val==con_.getParameter(CONNECTION_CON_VALUE)){
                    alert("判断条件冲突");
                    return;
                }
            }
        }
        currConn.setParameter(CONNECTION_CON_VALUE,rd_val);
        currConn.getOverlay("label").getElement().firstChild.innerHTML = $("#conn_nameDisp").val();
    }else{
        connArray[0].getOverlay("label").getElement().firstChild.innerHTML = $("#conn_nameDisp").val();
    }
    $("#condConnModal").modal("hide");

}

function getRadioValue(rdName){
    var obj=document.getElementsByName(rdName);
    var rd_val = "";
    for(i=0;i<obj.length;i++){
        if(obj[i].checked){
            rd_val = obj[i].value;
            break;
        }
    }
    return rd_val;
}