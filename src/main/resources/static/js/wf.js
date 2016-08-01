/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('app', ['ngRoute','ngResource', 'ngSanitize'])
    .config(['$translateProvider','$routeProvider', function($translateProvider,$routeProvider){
        /*$routeProvider
            .when('/index',{
                templateUrl : 'company/comp-index.html',
                controller : 'companyCtrl'
            })
            .when('/publish',{
                templateUrl : 'company/comp-publish.html',
                controller : 'companyCtrl'
            })
            .when('/account',{
                templateUrl : 'company/comp-account.html',
                controller : 'companyCtrl'
            })
            .when('/manage',{
                templateUrl : 'company/comp-manage.html',
                controller : 'companyCtrl'
            })
            .when('/viewTender',{
                templateUrl : 'employer/employer-viewTender.html',
                controller : 'companyCtrl'
            })
            .otherwise({
                templateUrl : 'company/comp-index.html',
                controller : 'companyCtrl'
            });*/
    }])
.service('wfService',['$http','$q',function($http,$q){

    }])
.controller('ctrl',['$scope','wfService',function($scope,wfService){

    }]);