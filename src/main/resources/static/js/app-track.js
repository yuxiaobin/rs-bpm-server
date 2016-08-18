/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('app', [ ])
    .service('wfService', ['$http', '$q', function ($http, $q) {
        this.getInstHist = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+"/wf/hist?rsWfId="+rsWfId+"&instNum="+instNum+"&refMkid="+refMkid
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
        wfService.getInstHist().then(function(succ){
            $scope.histList = succ.records;
        });
    }]);

function hideModal(){
    var data_ = {};
    data_.opt="C";
    window.parent.postMessage(JSON.stringify(data_), '*');
}