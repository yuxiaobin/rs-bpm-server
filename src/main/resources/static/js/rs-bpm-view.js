var RS_TYPE_START = "start-task";
var RS_TYPE_END = "end-task";
var RS_TYPE_CONDITION = "rs-cond-task";

var result_data = [];
var nodeList = [];
var connList = [];

jsPlumb.ready(function () {
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
            [ "Label", { label: "<i>Next</i>", id: "label", cssClass: "aLabel" }]
        ],
        Container: "canvas"
    });
    window.jsp = instance;
    var canvas = document.getElementById("canvas");
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
    // initialise element as connection targets and source.
    var initNode = function(el,type) {
        // initialise draggable elements.
//        instance.draggable(el,{containment:"parent"});
        instance.makeSource(el, {
            filter: ".ep",
            anchor: "Continuous",
            connectorStyle:connectorPaintStyle,
            connector: [ "Flowchart", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true } ],
            hoverPaintStyle: endpointHoverStyle,
            connectorHoverStyle: connectorHoverStyle,
            maxConnections: 2
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

    var newNodeById = function(id,type,descp, x, y, status) {
        var d = document.createElement("div");
        d.className = "w "+type;
        if("PEND"==status){
            $(d).css("border","3px solid red");
        }else if("PROC"==status){
            $(d).css("border","3px solid blue");
        }
        d.id = id;
        if(type==RS_TYPE_CONDITION){
            d.innerHTML = "<div class='task-descp'>"+descp + "</div><div class=\"ep\"></div>";
        }else{
            d.innerHTML = descp + "<div class=\"ep\"></div>";
        }
        d.style.left = x + "px";
        d.style.top = y + "px";
        instance.getContainer().appendChild(d);
        initNode(d);
        return d;
    };

    var moduleId = $("#moduleId").val();
    var url_ = basePath+"/wf/module/"+moduleId+"/wf/";
    if(typeof(histId)=="undefined" || histId==""){
        //no need to add wfId
        url_ = url_+"init";
    }else{
        url_ = basePath+"/wf/view/status/"+histId;
    }
    $.ajax(
        {
            type: "GET",
            url: url_,
            success: function (msg) {
                result_data = msg;
                nodeList = result_data.tasks;
                connList = result_data.conns;
                var hasPendingTask = false;
                instance.batch(function () {
                    if(typeof(nodeList)=="undefined"){
                        nodeList = [];
                    }
                    var nodeSize = nodeList.length;
                    if(nodeSize!=undefined && nodeSize!=0){
                        for(var i=0;i<nodeList.length;++i){
                            var node1 = nodeList[i];
                            newNodeById(node1.id, node1.rsType, node1.descp,node1.position.left,node1.position.top, node1.status);
                            if("PEND"==node1.status){
                                hasPendingTask =true;
                            }
                        }
                        for(var i=0;i<connList.length;++i){
                            var conn_data = connList[i];
                            var conn_ = instance.connect({ source: conn_data.source_id, target: conn_data.target_id });
                            conn_.getOverlay("label").label = "<i>"+conn_data.con_descp+"</i>"
                        }
                    }
                });
                var role_ = msg.role;
                if("staff"==role_){
                    if(typeof(latestFlag)=="undefined" || latestFlag=="false"){
                        $("#startWf").css("display","none")
                    }else {
                        $("#startWf").css("display", "")
                            .on("click", function () {
                                $.ajax({
                                    type: "POST",
                                    url: basePath + "/wf/start/" + moduleId,
                                    success: function (msg) {
                                        console.log("save wf successfully");
                                        alert(msg.message);
                                    },
                                    error: function (err) {
                                        alert(err);
                                    }
                                });
                            });
                    }
                    if(hasPendingTask){
                        $("#reSubmitWf").css("display", "")
                            .on("click", function () {
                                $.ajax({
                                    type: "POST",
                                    url: basePath + "/wf/history/" + histId,
                                    data:JSON.stringify({opt:"RQ"}),
                                    headers: { 'Content-Type': "application/json" },
                                    success: function (msg) {
                                        console.log("save wf successfully");
                                        alert(msg.message);
                                    },
                                    error: function (err) {
                                        alert(err);
                                    }
                                });
                            });
                    }

                }else if("manager"==role_){
                    $("#approveWf").css("display","")
                        .on("click",function(){
                            $.ajax({
                                type: "POST",
                                url: basePath+"/wf/history/"+histId,
                                data:JSON.stringify({opt:"AP"}),
                                headers: { 'Content-Type': "application/json" },
                                success: function (msg) {
                                    console.log("save wf successfully");
                                    alert(msg.message);
                                },
                                error:function(msg){
                                    alert(msg.message);
                                }
                            });
                        });
                    $("#rejectWf").css("display","")
                        .on("click",function(){
                            $.ajax({
                                type: "POST",
                                url: basePath+"/wf/history/"+histId,
                                data:JSON.stringify({opt:"RJ"}),
                                headers: { 'Content-Type': "application/json" },
                                success: function (msg) {
                                    console.log("save wf successfully");
                                    alert(msg.message);
                                },
                                error:function(msg){
                                    alert(msg.message);
                                }
                            });
                        });
                }
                $("#canvas .w").each(function(){
                    var id_ = $(this).attr("id");
                    instance.setElementDraggable(id_, false);
                });
            }
        }
    );
    jsPlumb.fire("jsPlumbDemoLoaded", instance);
    $(document).bind("contextmenu",function(e){
        return false;
    });
});


