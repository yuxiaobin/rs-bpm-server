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
    }])
    .controller('ctrl', ['$scope','$window','$timeout', 'groupService', function ($scope,$window,$timeout, groupService) {
        groupService.getGroupsWithUsers().then(function(success){
            $scope.groupList = success.records;
        },function(fail){
            console.error("getGroupsWithUsers failed"+fail);
        });

        $scope.viewGroupDetail = function(groupId,groupName){
            $window.location.href = basePath+"/usergroup/group/edit?groupId="+groupId+"&groupName="+groupName;
        };

        $scope.selectTableRow = function(evt){
            $(evt.target).parents("tr").addClass("active").siblings().removeClass("active");
        };

    }])
   ;

