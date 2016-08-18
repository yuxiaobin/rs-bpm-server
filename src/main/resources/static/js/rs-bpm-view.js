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
        instance.fire("jsPlumbDemoNodeAdded", el);
    };

    var newNodeById = function(node_) {
        var d = document.createElement("div");
        d.className = "w "+node_.rsType;
        if("PEND"==node_.status){
            $(d).css("border","3px solid red");
        }else if("PROC"==node_.status){
            $(d).css("border","3px solid blue");
        }
        d.id = node_.pgId;
        if(node_.rsType==RS_TYPE_CONDITION){
            d.innerHTML = "<div class='task-descp'>"+node_.taskDescpDisp + "</div>";//<div class=\"ep\"></div>
        }else{
            d.innerHTML = node_.taskDescpDisp;// + "<div class=\"ep\"></div>"
        }
        d.style.left = node_.position.left + "px";
        d.style.top = node_.position.top + "px";
        instance.getContainer().appendChild(d);
        initNode(d);
        return d;
    };

    var moduleId = $("#moduleId").val();
    var url_ = basePath+"/wf/status?rsWfId="+rsWfId+"&instNum="+instNum+"&refMkid="+refMkid
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
                            newNodeById(node1);
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


