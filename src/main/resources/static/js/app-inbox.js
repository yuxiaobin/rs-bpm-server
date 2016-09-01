/**
 * Created by yuxiaobin on 2016/7/28.
 */
var option_codes = {};//TODO:
angular.module('app', [ ])
    .service('inboxService', ['$http', '$q', function ($http, $q) {
        this.getTaskInbox = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/inbox/tasks'
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
        this.getTaskOptions = function(rsWfId,instNum){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/task/options/nogroup?rsWfId='+rsWfId+"&instNum="+instNum
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
    .controller('ctrl', ['$scope','$window','$timeout', 'inboxService', function ($scope,$window,$timeout, inboxService) {
        $scope.getTaskInbox = function(){
            inboxService.getTaskInbox().then(function(success){
                $scope.taskvList = success.records;
            },function(fail){
                console.error("getTaskInbox failed"+fail);
            });
        };
        $scope.getTaskInbox();
        if(typeof(userId)!='undefined'){
            $scope.userId = userId;
        }

        $scope.viewAwtInMK = function(rsWfId, instNum, refMkid){
            $window.location.href = basePath+"/mk/task?rsWfId="+rsWfId+"&instNum="+instNum+"&refMkid="+refMkid;
        };

        window.reloadTask = function(){
            $scope.getTaskInbox();
        };
        $scope.optGroupList =[{"opts":[{"disflag":true,"value":"C","descp":"流程提交"},{"disflag":true,"value":"RJ","descp":"流程退回"},{"disflag":true,"value":"V","descp":"流程否决"}]},{"opts":[{"disflag":true,"value":"F","descp":"流程转交"},{"disflag":true,"value":"RC","descp":"流程撤回"},{"disflag":true,"value":"LMD","descp":"我来处理"}]},{"opts":[{"disflag":true,"value":"DP","descp":"流程调度"},{"disflag":true,"value":"TK","descp":"流程跟踪"}]}]

        $scope.chooseOption = function(val_){
            if(angular.isUndefined($scope.selectedRcdInstNum) || $scope.selectedRcdInstNum==""){
                alert("请选择一条记录");
                $scope.taskOption = "";
                $('#wfOptionsId').selectpicker('val', '');
                return;
            }
            var instNum = $scope.selectedRcdInstNum;
            var refMkid = $scope.refMkid;
            var rsWfId = $scope.rsWfId;
            var url_ = basePath+"/task/loadprocess?rsWfId="+rsWfId+"&instNum="+instNum+"&refMkid="+refMkid;
            if (val_ == 'C') {
                $('iframe').attr("src",url_+"&optCode=C");
                $('#myModal').modal({backdrop:false});
            }
            else if (val_ == 'RJ') {
                $('iframe').attr("src",url_+"&optCode=RJ");
                $('#myModal').modal({backdrop:false});
            }
            else if (val_ == 'TK') {
                $('iframe').attr("src",url_+"&optCode=TK");
                $('#myModal').modal({backdrop:true});
            }
            $scope.taskOption = "";
            $('#wfOptionsId').selectpicker('val', '');
            $scope.selectedRcdInstNum = "";
        };
        $scope.selectTableRowInbox = function(evt){
            $(evt.target).parent().addClass("active").siblings().removeClass("active");
        };
        $scope.selectTableRow = function(evt){
            $(evt.target).parent().addClass("active").siblings().removeClass("active");
            var tr_ = $(evt.target).parent();
            $scope.selectedRcdInstNum = tr_.attr("rs-inst-num");
            $scope.refMkid = tr_.attr("rs-ref-mkid");
            $scope.rsWfId = tr_.attr("rs-wf-id");
            inboxService.getTaskOptions($scope.rsWfId, $scope.selectedRcdInstNum).then(
                function(optArray){
                    $scope.optGroupList = $.grep($scope.optGroupList,function(value){
                        value.opts = $.grep(value.opts,function(val){
                            for(i=0;i<optArray.length;++i){
                                if(val.value==optArray[i].value){
                                    val.disflag = optArray[i].disflag;
                                    break;
                                }
                            }
                            return true;
                        });
                        return true;
                    });
                    $("#wfOptionsId").selectpicker('hide').selectpicker("destroy");
                    setTimeout(function(){
                        $("#wfOptionsId").selectpicker('show');
                    },100);
                },function(fail){
                    alert("获取数据异常");
                }
            );
        }

    }])
   ;
