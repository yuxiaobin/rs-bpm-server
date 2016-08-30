/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('app', [ ])
    .service('groupService', ['$http', '$q', function ($http, $q) {
        this.getGroupsWithUsers = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/groupsWithUsers'
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
        this.addGroup = function(group){
            var delay = $q.defer();
            var req = {
                method: 'POST',
                data:group,
                url: basePath+'/usergroup/group'
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
        $scope.getGroups = function(){
            groupService.getGroupsWithUsers().then(function(success){
                $scope.groupList = success.records;
            },function(fail){
                console.error("getGroupsWithUsers failed"+fail);
            });
        };
        $scope.getGroups();
        $scope.viewGroupDetail = function(groupId){
            $window.location.href = basePath+"/usergroup/group/edit?groupId="+groupId;
        };

        $scope.selectTableRow = function(evt){
            $(evt.target).parents("tr").addClass("active").siblings().removeClass("active");
        };

        $scope.addGroup = function(){
            if(angular.isUndefined($scope.groupName) || $.trim($scope.groupName.toString())==""){
                alert("请输入用户组名称");
                return;
            }
            var group = {};
            group.groupName = $scope.groupName;
            groupService.addGroup(group).then(function(succ){
                if(succ.result=="succ"){
                    $scope.getGroups();
                    alert("添加成功");
                }else{
                    alert("相同的用户组已存在");
                }
            },function(fail){
                alert("添加失败");
            });
        }
    }])
   ;

