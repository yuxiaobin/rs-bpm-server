/**
 * Created by yuxiaobin on 2016/10/8.
 */
angular.module('conditionApp', [ ])
    .service('funcVarService',['$http', '$q', function ($http, $q) {
        this.getFuncVars = function(refMkid){
            var delay = $q.defer();
            var req = {
                method: 'POST',
                data:{refMkid:refMkid},
                url: basePath+'/wfadmin/funcvars'
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
    .controller('ctrl', ['$scope','$window','$timeout', 'funcVarService', function ($scope,$window,$timeout, funcVarService) {
        $scope.custUsers = [];
        $scope.custFuncVars = [];
        $scope.getFuncVars = function(refMkid){
            funcVarService.getFuncVars(refMkid).then(function(records){
                $scope.custFuncVars =records;
                if(typeof(taskData.funcVarArray)!='undefined'){
                    var funcVarArray = taskData.funcVarArray;
                    for(var i=0;i<funcVarArray.length;++i){
                        if(funcVarArray[i].varType=="U"){
                            $scope.custUsers[$scope.custUsers.length] = funcVarArray[i];
                        }else{
                            $scope.custFuncVars[$scope.custFuncVars.length] = funcVarArray[i];
                        }
                    }
                }
            })
        }
        $scope.getFuncVars(refMkid);
        $scope.task = taskData;

        $scope.updateTaskProperties = function(){
            taskData.taskDescp = $scope.task.taskDescp;
            taskData.taskDescpDisp = $scope.task.taskDescpDisp;
            taskData.condExp = $scope.task.condExp;
            taskData.opt = "U";
            window.parent.postMessage(JSON.stringify(taskData), '*');
            $("#successMsg").css("display","");
        }
    }])
   ;

function hideModal(){
    var data_ = {};
    data_.opt="C";
    window.parent.postMessage(JSON.stringify(data_), '*');
}