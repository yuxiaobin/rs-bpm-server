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
        this.getInstHist = function(instId){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/wf/inst/hist/'+instId
            };
            $http(req)
                .success(function(data, status, headers, config){
                    delay.resolve(data);
                })
                .error(function(data, status, headers, config){
                    delay.reject(data);
                });
            return delay.promise;
        }
    }])
    .controller('ctrl', ['$scope','$window', 'wfService', function ($scope,$window, wfService) {
        if(angular.isUndefined(module_task_flag)){
            module_task_flag = "tasks";
        }
        if("modules"==module_task_flag){
            wfService.getAllModules().then(function(success){
                $scope.moduleList = success.records;
            },function(fail){
                console.error("getAllModules failed");
            });
        }
        if("tasks"==module_task_flag){
            wfService.getTaskInbox().then(function(success){
                $scope.taskvList = success.records;
            },function(fail){
                console.error("getAllModules failed");
            });
        }
        if("hist"==module_task_flag){
            if(angular.isUndefined(instId)){
                instId = "";
            }
            wfService.getInstHist(instId).then(function(succ){
                $scope.histList = succ.records;
            });
        }


        $scope.viewWfById = function(wfId){
            $window.location.href = basePath+"/wf/view/"+wfId;
        }

        $scope.viewInbox = function(){
            $window.location.href = basePath+"/wf/inbox";
        }
        $scope.viewModules = function(){
            $window.location.href = basePath+"/wf/modules/page";
        }

        $scope.viewTask = function(instId){
            $window.location.href = basePath+"/wf/history/"+instId;
        }

    }]);