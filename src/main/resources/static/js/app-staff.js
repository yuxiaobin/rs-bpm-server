/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('app', [ ])
    .service('wfService', ['$http', '$q', function ($http, $q) {
        this.getAllModules = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/wf/modules/listall'
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

        this.triggerWF = function(rsWfId,modId){
            var delay = $q.defer();
            var parm = {rsWfId:rsWfId, moduleId:modId};
            var req = {
                method: 'POST',
                url: basePath+'/wf/start',
                data: JSON.stringify(parm),
                headers: { 'Content-Type': "application/json" }
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
        wfService.getAllModules().then(function(success){
            $scope.moduleList = success.records;
        },function(fail){
            console.error("getAllModules failed");
        });
        $scope.viewInbox = function(){
            $window.location.href = basePath+"/inbox";
        }
        $scope.triggerWF = function(rsWfId,modId){
            wfService.triggerWF(rsWfId,modId).then(function(success){
                alert("Start successfully");
            },function(fail){
                alert("Start failed");
            });
        }

    }]);