/**
 * Created by yuxiaobin on 2016/7/28.
 */
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
    }])
    .controller('ctrl', ['$scope','$window', 'inboxService', function ($scope,$window, inboxService) {
        inboxService.getTaskInbox().then(function(success){
            $scope.taskvList = success.records;
        },function(fail){
            console.error("getTaskInbox failed"+fail);
        });
        if(typeof(userId)!='undefined'){
            $scope.userId = userId;
        }

        $scope.viewAwtInMK = function(rsWfId, instNum, refMkid){
            $window.location.href = basePath+"/mk/task?rsWfId="+rsWfId+"&instNum="+instNum+"&refMkid="+refMkid;
        };

        window.reloadTask = function(){
            inboxService.getTaskInbox().then(function(success){
                $scope.taskvList = success.records;
            },function(fail){
                console.error("getTaskInbox failed"+fail);
            });
        }

        $scope.selectTableRow = function(evt){
            $(evt.target).parent().addClass("active").siblings().removeClass("active");
            $(evt.target).parent().contextMenu({
                menu: 'taskMenu'
            }, function(action, el, pos) {
                var id_ = $(el).attr("id");
                var rsWfId = $(el).attr("rs-wf-id");
                var instNum = $(el).attr("rs-inst-num");
                var refMkid = $(el).attr("rs-ref-mkid");
                var url_ = basePath+"/task/loadprocess?rsWfId="+rsWfId+"&instNum="+instNum+"&refMkid="+refMkid;
                if (action == 'commit') {
                    $('iframe').attr("src",url_+"&optCode=C");
                    $('#myModal').modal({backdrop:false});
                }
                else if (action == 'back') {
                    $('iframe').attr("src",url_+"&optCode=RJ");
                    $('#myModal').modal({backdrop:false});
                }
                else if (action == 'track') {
                    $('iframe').attr("src",url_+"&optCode=TRACK");
                    $('#myModal').modal({backdrop:true});
                }
            })
        }

    }]);