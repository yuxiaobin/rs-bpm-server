/**
 * Created by yuxiaobin on 2016/7/28.
 *
 * 模拟增加模块页面
 */
angular.module('app', [ ])
    .service('wfService', ['$http', '$q', function ($http, $q) {
        this.newModule = function(moduleName){
            var delay = $q.defer();
            var req = {
                method: 'POST',
                url: basePath+'/wfadmin/module',
                data:{moduleName:moduleName}
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
        this.getAllModules = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url:basePath+ '/wfadmin/modules/list'
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
    .controller('ctrl', ['$scope', 'wfService', function ($scope, wfService) {
        $scope.newModule = function () {
            console.log("newModule="+$scope.moduleName);
            if($scope.moduleName==undefined || $scope.moduleName==""){
                alert("Name is empty");
                return;
            }else{
                wfService.newModule($scope.moduleName).then(function(success){
                    if($scope.moduleList==undefined){
                        $scope.moduleList = []
                    }
                    $scope.moduleList[$scope.moduleList.length] = {modId:success.moduleId, name:success.moduleName};
                },function(fail){
                    console.error("newModule failed");
                });
            }
        };
        wfService.getAllModules().then(function(success){
            $scope.moduleList = success.records;
        },function(fail){
            console.error("getAllModules failed");
        });

    }])

;