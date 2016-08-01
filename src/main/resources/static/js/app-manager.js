/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('app', [ ])
    .service('wfService', ['$http', '$q', function ($http, $q) {
        this.getTaskInbox = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/wf/inbox/tasks'
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
    .controller('ctrl', ['$scope','$window', 'wfService', function ($scope,$window, wfService) {
        wfService.getTaskInbox().then(function(success){
            $scope.taskvList = success.records;
        },function(fail){
            console.log("getTaskInbox failed");
        });

        $scope.viewTask = function(histId){
            $window.location.href = basePath+"/wf/history/"+histId;
        }

    }]);