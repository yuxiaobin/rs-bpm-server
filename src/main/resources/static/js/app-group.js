/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('app', [ ])
    .service('groupService', ['$http', '$q', function ($http, $q) {
        this.getUserInGroup = function(groupId){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/group/users?groupId='+groupId
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
        this.getUser4Add = function(groupId){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/group/users/add?groupId='+groupId
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
        this.saveUser2Group = function(groupId,userIds){
            var delay = $q.defer();
            var req = {
                method: 'POST',
                data:{groupId:groupId,userIds:userIds},
                url: basePath+'/usergroup/group/users/add'
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

        this.deleteGroup = function(groupId){
            var delay = $q.defer();
            var req = {
                method: 'DELETE',
                url: basePath+'/usergroup/group?groupId='+groupId
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
        this.deleteUsers = function(groupId,userIds){
            var delay = $q.defer();
            var req = {
                method: 'POST',
                data:{groupId:groupId,userIds:userIds},
                url: basePath+'/usergroup/group/users/delete'
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
        this.updateGroupName = function(group){
            var delay = $q.defer();
            var req = {
                method: 'POST',
                data:group,
                url: basePath+'/usergroup/group/update'
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
    .controller('ctrl', ['$scope','$window','$timeout', 'groupService', function ($scope,$window,$timeout, groupService) {
        $scope.back2GroupIndex = function(){
            $window.location.href = basePath+"/usergroup";
        };

        $scope.addUserPrepare = function(){
            groupService.getUser4Add(groupId).then(function(succ){
                $scope.optGroupList = succ.groups;
                $scope.optUserList = succ.users;
                $("#selectAddUser").selectpicker('hide').selectpicker("destroy");
                $timeout(function(){
                    $("#selectAddUser").selectpicker('show');
                    $scope.addFlag = true;
                },100);
            });
        };

        $scope.cancelAddUser = function(){
            $scope.addFlag = false;
        }

        $scope.loadUserData4Group = function(){
            groupService.getUserInGroup(groupId).then(function(success){
                $scope.userList = success.records;
            },function(fail){
                console.error("getUserInGroup failed"+fail);
            });
        };

        $scope.loadUserData4Group();
        $scope.groupName = groupName;
        $scope.showUpdateSucc = false;
        $scope.showUpdateFail = false;

        $scope.updateGroupName = function(){
            var groupname = $scope.groupName.toString();
            if($.trim(groupname)==""){
                $scope.groupName = groupName;
            }else if(groupname == groupName){
                return;
            }else{
                var group = {groupId:groupId, groupName:groupname};
                groupService.updateGroupName(group).then(function(succ){
                    if(succ.result=="succ"){
                        $scope.showUpdateSucc = true;
                        groupName = groupname;
                        $timeout(function(){
                            $scope.showUpdateSucc = false;
                        },1500)
                    }else{
                        $scope.showUpdateFail = true;
                        $scope.groupName = groupName;
                        $timeout(function(){
                            $scope.showUpdateFail = false;
                        },1500)
                    }
                },function(fail){});
            }
        };

        $scope.saveUser2Group = function(){
            //selectedUsers result is an array
            if($scope.selectedUsers.toString()==""){
                alert("请选择添加的用户");
                return;
            }
            groupService.saveUser2Group(groupId,$scope.selectedUsers.toString() ).then(function(succ){
                $scope.loadUserData4Group();
                $scope.addUserPrepare();
                alert("添加成功");
            },function(fail){
                alert("添加失败");
            });
        };

        $scope.deleteGroup = function(){
            groupService.deleteGroup(groupId).then(function(succ){
                alert("删除成功");
                $window.location.href = basePath+"/usergroup";
            });
        }

        $scope.deleteUsers = function(){
            var checkboxArray = $("#tableBody input");
            var hasUserSelected = false;
            var selectedUserIds = "";
            for(var i=0;i<checkboxArray.length;++i){
                if($(checkboxArray[i]).is(":checked")){
                    hasUserSelected = true;
                    selectedUserIds+=$(checkboxArray[i]).val()+",";
                }
            }
            if(!hasUserSelected){
                alert("请选择用户");
                return;
            }
            groupService.deleteUsers(groupId, selectedUserIds).then(function(succ){
                $scope.loadUserData4Group();
                alert("删除用户成功");
            },function(fail){
                alert("删除用户失败");
            });
        }

    }])
   ;

selectUserTr = function(obj,event){
    $(obj).find("input").click();
};

selectAllRecords = function(obj){
    var checkboxArray = $("#tableBody input");
    if($(obj).is(":checked")){
        for(var i=0;i<checkboxArray.length;++i){
            if(!$(checkboxArray[i]).is(":checked")){
                $(checkboxArray[i]).click();
            }
        }
    }
    else{
        for(var i=0;i<checkboxArray.length;++i){
            if($(checkboxArray[i]).is(":checked")){
                $(checkboxArray[i]).click();
            }
        }
    }
}