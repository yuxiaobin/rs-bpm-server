/**
 * Created by yuxiaobin on 2016/10/8.
 */
angular.module('funcVarApp', [ ])
    .service('wfService', ['$http', '$q', function ($http, $q) {
        this.getCustFuncVars = function(refMkid,version){
            var delay = $q.defer();
            var data = {refMkid:refMkid};
            if(typeof(version)!='undefined'){
                data.version = version;
            }
            var req = {
                method: 'POST',
                data:data,
                url: basePath+'/wfadmin/custvars'
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
    .controller('ctrl', ['$scope','$window','$timeout', 'wfService', function ($scope,$window,$timeout, wfService) {
        $scope.getCustFuncVars = function(refMkid){
            wfService.getCustFuncVars(refMkid).then(function(success){
                $scope.funcVarList = success;
                custFuncVarArray = $scope.funcVarList;
            },function(fail){
                console.error("getTaskInbox failed"+fail);
            });
        };
        $scope.getCustFuncVars(refMkid);
        $scope.getFuncVars = function(refMkid){
            wfService.getFuncVars(refMkid).then(function(succ){
                $scope.funcVars = succ;
            })
        };
        $scope.getFuncVars(refMkid);
        $scope.selectFuncVar = function(funcVar){
            if(angular.isUndefined($scope.custVar.varExpression)){
                $scope.custVar.varExpression = "";
            }
            if($scope.custVar.varExpression!=""){
                $scope.custVar.varExpression += " and ";
            }
            $scope.custVar.varExpression += ":"+funcVar.varCode+"=";
        }

        $scope.saveCustVar = function(){
            if(angular.isUndefined($scope.custVar.varCode)||$scope.custVar.varCode==""
                ||angular.isUndefined($scope.custVar.varDescp)|| $scope.custVar.varDescp==""){
                $scope.funcVarError = "名称和编码必须指定";
                $('#messageModal').modal({backdrop:true});
                $timeout(function(){
                    $("#messageModal").modal("hide");
                },2000);
                return;
            }
            var edit_index = -1;
            $("#funcVarTable tr").each(function(index){
                if($(this).hasClass("active")){
                    edit_index = index-1;//first tr is <thead>
                }
            });
            if(edit_index==-1){//add
                var isContained = false;
                for(var i=0;i<$scope.funcVarList.length;++i){
                    if($scope.funcVarList[i].varCode==$scope.custVar.varCode){
                        $scope.funcVarList[i] = $scope.custVar;
                        isContained = true;
                        break;
                    }
                }
                if(!isContained){
                    $scope.funcVarList[$scope.funcVarList.length] = $scope.custVar;
                }
            }else{//edit
                var varCodePrev = $scope.funcVarList[edit_index].varCode;
                for(var i=0;i<$scope.funcVarList.length;++i){
                    if(i!=edit_index && $scope.funcVarList[i].varCode==$scope.custVar.varCode){
                        $scope.funcVarError = "相同的编码已存在";
                        $('#messageModal').modal({backdrop:true});
                        $timeout(function(){
                            $("#messageModal").modal("hide");
                        },2000);
                        return;
                    }
                }
                if(varCodePrev!=$scope.custVar.varCode){
                    //check if varCodePrev is used
                    var isValidVarCode = true;
                    $("#canvas .w").each(function(){
                        var jqObj = $(this);
                        var assignerJSONStr = jqObj.attr(RS_ATTR_ASSIGNERS);
                        if(assignerJSONStr==undefined || assignerJSONStr==""){
                            assignerJSONStr = "[]";
                        }
                        var assignersJSON = $.parseJSON(assignerJSONStr);
                        var existingUsingOldCode = $.grep(assignersJSON,function(value){
                            if(value.id==varCodePrev){
                                return true;
                            }
                            return false;
                        })
                        if(existingUsingOldCode.length!=0){
                            alert("该编码已经被使用，无法修改");
                            isValidVarCode = false;
                        }
                    });
                    if(!isValidVarCode){
                        return;
                    }
                }
                $scope.funcVarList[edit_index] = $scope.custVar;
                $("#funcVarTable tr").eq(edit_index+1).removeClass("active");
            }
            custFuncVarArray = $scope.funcVarList;
            $scope.addCustVar();
        };
        $scope.custVar = {varType:"U"};
        $scope.addCustVar = function(){
            $scope.custVar = {varType:"U"};
            $("#funcVarTable tr").removeClass("active");
            $("#selectVarType").selectpicker('hide').selectpicker("destroy");
            setTimeout(function(){
                $("#selectVarType").selectpicker('show').selectpicker('val', "U");
            },100);
            $("#funcVarEdit").show();
        }
        $scope.editFuncVar = function(evt,funcVar){
            var selectedTr = $(evt.target).parent();
            selectedTr.addClass("active").siblings().removeClass("active");
            angular.copy(funcVar,$scope.custVar);
        }
        $scope.deleteCustVar = function(){
            if(angular.isUndefined($scope.funcVarList)){
                return;
            }
            var del_index = -1;
            $("#funcVarTable tr").each(function(index){
                if($(this).hasClass("active")){
                    del_index = index-1;//first tr is <thead>
                }
            });
            if(del_index==-1){
                return;
            }
            $scope.funcVarList = $.grep($scope.funcVarList, function(value, index){
                if(index==del_index){
                    return false;
                }
                return true;
            })
            $scope.addCustVar();
        }

    }])
   ;

function showWfDef(obj){
    $("#wfDefDiv").show();
    $("#custVarDiv").hide();
    $(obj).parent().addClass("active").siblings().removeClass("active");
}
function showCustVar(obj){
    $("#custVarDiv").show();
    $("#wfDefDiv").hide();
    $(obj).parent().addClass("active").siblings().removeClass("active");
}