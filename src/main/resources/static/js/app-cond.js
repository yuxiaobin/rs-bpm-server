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
        $scope.custFuncVars = [];
        $scope.getFuncVars = function(refMkid){
            funcVarService.getFuncVars(refMkid).then(function(records){
                $scope.custFuncVars =records;
                if(typeof(taskData.custFuncVarArray)!='undefined'){
                    var funcVarArray = taskData.custFuncVarArray;
                    for(var i=0;i<funcVarArray.length;++i){
                        if(funcVarArray[i].varType=="V"){
                            $scope.custFuncVars[$scope.custFuncVars.length] = {varCode:funcVarArray[i].varCode,varDescp:funcVarArray[i].varDescp};
//                            $scope.custFuncVars[$scope.custFuncVars.length] = funcVarArray[i];//error due to $$hashCode
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

        $scope.selectFuncVar = function(funcVar){
            if(angular.isUndefined($scope.task.condExp)){
                $scope.task.condExp = "";
            }
            if($scope.task.condExp!=""){
                $scope.task.condExp += " and ";
            }
            $scope.task.condExp += funcVar.varCode+"==";
        }
    }])
   ;

function hideModal(){
    var data_ = {};
    data_.opt="C";
    window.parent.postMessage(JSON.stringify(data_), '*');
}