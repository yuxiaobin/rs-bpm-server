/**
 * Created by yuxiaobin on 2016/8/5.
 */
var basePath = "";
angular.module('taskApp', [ ])
    .service('userGroupService',['$http', '$q', function ($http, $q) {
        this.getAllUsers = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/users'
            };
            $http(req)
                .success(function(data, status, headers, config){
                    delay.resolve(data);
                })
                .error(function(data, status, headers, config){
                    delay.reject(data);
                });
            return delay.promise;
        };
        this.getAllGroups = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/groups'
            };
            $http(req)
                .success(function(data, status, headers, config){
                    delay.resolve(data);
                })
                .error(function(data, status, headers, config){
                    delay.reject(data);
                });
            return delay.promise;
        };
    }])
    .controller('taskCtrl', ['$scope','$timeout', 'userGroupService', function ($scope,$timeout, userGroupService) {
        userGroupService.getAllUsers().then(function(success){
            $scope.userList = success.records;
            userGroupService.getAllGroups().then(function(success){
                $scope.groupList = success.records;
                console.log("get groupList done....");
                $timeout(function(){
                    renderAssigner(taskData.assignUsers, taskData.assignGroups);
                },500);
            })
        });


        $scope.updateTaskProperties = function(){
            taskData.taskDescp = $("#taskDescpId").val();
//            var assigners = {};
//            var assignedUsers = [];
            var assignedUsersStr = "";
//            var assignedGroups = [];
            var assignedGroupsStr = "";
            var sel_username_="";
            var sel_groupname_="";
            $("#selectAllUsersId").siblings(".active").each(function(){
                sel_username_ = $(this).attr("rs-data-usname");
//                assignedUsers.push(sel_username_);
                assignedUsersStr+=sel_username_+",";
            });
            $("#selectAllGroupId").siblings(".active").each(function(){
                sel_groupname_ = $(this).attr("rs-data-gpname");
//                assignedGroups.push(sel_groupname_);
                assignedGroupsStr+=sel_groupname_+",";
            });
//            assigners.users = assignedUsersStr;
//            assigners.groups = assignedGroupsStr;

            taskData.assignUsers = assignedUsersStr;
            taskData.assignGroups = assignedGroupsStr;
            taskData.opt = "U";
            window.parent.postMessage(JSON.stringify(taskData), '*');
            $("#successMsg").css("display","");
        }

        $scope.deleteTask = function(){
            taskData.opt = "D";
            window.parent.postMessage(JSON.stringify(taskData), '*');
        }
    }]);

var selectUser = function(obj){
    var item_ = $(obj);
    if(item_.hasClass("active")){
        item_.removeClass("active");
    }else{
        item_.addClass("active");
    }
}

var selectGroup = function(obj){
    var item_ = $(obj);
    if(item_.hasClass("active")){
        item_.removeClass("active");
    }else{
        item_.addClass("active");
    }
}

var selectAllUserChange = function(obj){
    if(obj.checked){
        $("#selectAllUsersId").siblings().addClass("active");
    }else{
        $("#selectAllUsersId").siblings().removeClass("active");
    }
}

var selectAllGroupChange = function(obj){
    if(obj.checked){
        $("#selectAllGroupId").siblings().addClass("active");
    }else{
        $("#selectAllGroupId").siblings().removeClass("active");
    }
}

/*function updateTaskProperties(){
    var taskPgId = $("#updateTaskPropertiesForm #taskPgId").eq(0).val();
    var taskType = $("#updateTaskPropertiesForm #taskType").eq(0).val();
    if(RS_TYPE_CONDITION==taskType){
        $("#"+taskPgId +" .task-descp").html($("#updateTaskPropertiesForm #taskDescpId").eq(0).val());
    }else{
        $("#"+taskPgId)
            .attr("rs-data-assigner",$("#updateTaskPropertiesForm #currAssignerId").eq(0).val())
            .html($("#updateTaskPropertiesForm #taskDescpId").eq(0).val()+"<div class=\"ep\"></div>");
    }

    $("#successMsg").css("display","");
    setTimeout(function(){
        $('#myModal').modal('hide');
    },1000);
}
function deleteTask(){
    var taskPgId = $("#updateTaskPropertiesForm #taskPgId").eq(0).val();
    window.jsp.remove(taskPgId);
    $('#myModal').modal('hide');
}
 */
function confirmDelete(){
    $("#deleteTaskAlert").css("display","");
}
function notDeleteTask(){
    $("#deleteTaskAlert").css("display","none");
}

function changeTab(obj,formId_){
    var li_ = $(obj).parent();
    li_.addClass("active");
    li_.siblings("li").removeClass("active");
    $("#"+formId_).show();
    $("#"+formId_).siblings("form").hide();
}

function renderAssigner(usersStr,groupsStr){
    if(typeof(usersStr)!="undefined"){
        var users = usersStr.split(",");
        for(var i=0;i<users.length;++i){
            var cond_ = "[rs-data-usname='"+users[i]+"']";
            $("#selectAllUsersId").siblings(cond_).addClass("active");
        }
    }
    if(typeof(groupsStr)!="undefined"){
        console.log("render selected groups:"+groupsStr);
        var groups = groupsStr.split(",");
        for(var i=0;i<groups.length;++i){
            var cond_ = "[rs-data-gpname='"+groups[i]+"']";
            $("#selectAllGroupId").siblings("[rs-data-gpname='"+groups[i]+"']").addClass("active")
        }
    }
}

function hideModal(){
    var data_ = {};
    data_.opt="C";
    window.parent.postMessage(JSON.stringify(data_), '*');
}