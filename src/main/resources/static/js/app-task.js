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
                $timeout(function(){
                    var usersStr = taskData.assignUsers;
                    if(typeof(usersStr)!="undefined"){
                        var users = usersStr.split(",");
                        for(var i=0;i<users.length;++i){
                            angular.forEach($scope.userList, function (item, index) {
                                if(item.name==users[i]) {
                                    item.extClass = "active";
                                }
                            });
                        }
                    }
                    var groupsStr = taskData.assignGroups;
                    if(typeof(groupsStr)!="undefined"){
                        var groups = groupsStr.split(",");
                        for(var i=0;i<groups.length;++i){
                            angular.forEach($scope.groupList, function (item, index) {
                                if(item.groupName==groups[i]) {
                                    item.extClass = "active";
                                }
                            });
                        }
                    }
                },500);
            })
        });

        $scope.updateTaskProperties = function(){
            taskData.taskDescp = $("#taskDescpId").val();
            var assignedUsersStr = "";
            var assignedGroupsStr = "";
            $("#selectAllUsersId").siblings(".active").each(function(){
                assignedUsersStr+=$(this).attr("rs-data-usname")+",";
            });
            $("#selectAllGroupId").siblings(".active").each(function(){
                assignedGroupsStr+=$(this).attr("rs-data-gpname")+",";
            });
            taskData.assignUsers = assignedUsersStr;
            taskData.assignGroups = assignedGroupsStr;
            taskData.opt = "U";
            window.parent.postMessage(JSON.stringify(taskData), '*');
            $("#successMsg").css("display","");
        }

        $scope.deleteTask = function(){
            taskData.opt = "D";
            window.parent.postMessage(JSON.stringify(taskData), '*');
            $("#deleteTaskAlert").css("display","none");
            $("#successMsg").css("display","");
        }

        $scope.selectUser = function(user_id){
            var elm_ = $("#selectAllUsersId").siblings("[rs-data-usid='"+user_id+"']");
            if(elm_.hasClass("active")){
                angular.forEach($scope.userList, function (item, index) {
                    if(item.id==user_id){
                        item.extClass = "";
                    }
                });
            }else{
                angular.forEach($scope.userList, function (item, index) {
                    if(item.id==user_id) {
                        item.extClass = "active";
                    }
                });
            }
        }
        $scope.selectGroup = function(group_id){
            var elm_ = $("#selectAllGroupId").siblings("[rs-data-gpid='"+group_id+"']");
            if(elm_.hasClass("active")){
                angular.forEach($scope.groupList, function (item, index) {
                    if(item.groupId==group_id){
                        item.extClass = "";
                    }
                });
            }else{
                angular.forEach($scope.groupList, function (item, index) {
                    if(item.groupId==group_id) {
                        item.extClass = "active";
                    }
                });
            }
        }
        $scope.selectAllUserChange = function(){
            if($("#allUsersCheckbox").is(':checked')){
                angular.forEach($scope.userList, function (item, index) {
                    item.extClass = "active";
                });
            }else{
                angular.forEach($scope.userList, function (item, index) {
                    item.extClass = "";
                });
            }
        }
        $scope.selectAllGroupChange = function(){
            if($("#allGroupsCheckbox").is(':checked')){
                angular.forEach($scope.groupList, function (item, index) {
                    item.extClass = "active";
                });
            }else{
                angular.forEach($scope.groupList, function (item, index) {
                    item.extClass = "";
                });
            }
        }
    }]);

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

function hideModal(){
    var data_ = {};
    data_.opt="C";
    window.parent.postMessage(JSON.stringify(data_), '*');
}