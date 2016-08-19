/**
 * Created by yuxiaobin on 2016/8/5.
 *
 * Popup page for Edit Task
 */
var basePath = "";
angular.module('taskApp', [ ])
    .service('userGroupService',['$http', '$q', function ($http, $q) {
        this.getAllUsers = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/users'
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
        this.getAllGroups = function(){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/usergroup/groups'
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
    .controller('taskCtrl', ['$scope','$timeout', 'userGroupService', function ($scope,$timeout, userGroupService) {
        $scope.defSelModTxt_yes = "默认选中";
        $scope.defSelModTxt_no = "默认不选中";
        $scope.assignTypeDesc_user = "用户";
        $scope.assignTypeDesc_group = "用户组";
        $scope.assignTypeDesc_other = "其他";
        userGroupService.getAllUsers().then(function(success){
            $scope.userList = success.records;
            userGroupService.getAllGroups().then(function(success){
                $scope.groupList = success.records;
                if(taskData.assigners!=""){
                    $timeout(function(){
                        $scope.assignerList = taskData.assigners;
                        angular.forEach($scope.assignerList,function(item,index){
                            if(item.assignTypeCode=="U"){
                                item.assignTypeDesc = $scope.assignTypeDesc_user;
                            }else if(item.assignTypeCode=="G"){
                                item.assignTypeDesc =  $scope.assignTypeDesc_group;
                            } else{
                                item.assignTypeDesc = $scope.assignTypeDesc_other;
                            }
                            if(item.defSelMod=="Y"){
                                item.defSelModTxt = $scope.defSelModTxt_yes;

                            }else{
                                item.defSelModTxt = $scope.defSelModTxt_no;
                            }
                        });

                    },500);
                }
            })
        });
        $scope.task = taskData;
        $scope.TX_CHOICES = taskData.TX_CHOICES;//to avoid data convert issue when save

        $scope.updateTaskProperties = function(){
            taskData.taskDescp = $("#taskDescp").val();
            taskData.taskDescpDisp = $("#taskDescpDisp").val();
            var assigns = [];
            angular.forEach($scope.assignerList,function(item, index){
                var assigner = {};
                assigner.assignTypeDesc = item.assignTypeDesc;
                assigner.assignTypeCode = item.assignTypeCode;
                assigner.name = item.name;
                assigner.id = item.id;
                assigner.defSelModTxt = item.defSelModTxt;
                assigner.checkFlag = item.checkFlag;
               assigns.push(assigner);
            });
            taskData.assigners = assigns;
            //
            taskData.txCode = $scope.task.txCode;
            taskData.txType = $("#txType").attr("rs-attr-txType");
            taskData.buzStatus = $("#buzStatus").attr("rs-attr-buzStatus");
            taskData.timeLimit =  $scope.task.timeLimit;
            taskData.timeLimitTp = $("#timeLimitTp").attr("rs-attr-timeLimitTp");
            taskData.alarmTime = $scope.task.alarmTime;
            taskData.alarmTimeTp = $("#alarmTimeTp").attr("rs-attr-alarmTimeTp");
            taskData.moduleId = $scope.task.moduleId;
            taskData.runParam = $scope.task.runParam;
            taskData.TX_CHOICES = $scope.TX_CHOICES;
            taskData.TX_PR_CHOICES = $scope.task.TX_PR_CHOICES;
            taskData.TX_BK_CHOICES = $scope.task.TX_BK_CHOICES;
            taskData.SIGN_CHOICES = $scope.task.SIGN_CHOICES;
            taskData.opt = "U";
            window.parent.postMessage(JSON.stringify(taskData), '*');
            $("#successMsg").css("display","");
        }

        $scope.deleteTask = function(){
            taskData.opt = "D";
            window.parent.postMessage(JSON.stringify(taskData), '*');
            $("#deleteTaskAlert").css("display","none");
            $("#successMsg").css("display","");
        }

        $scope.selectUser = function(user_id){
            var elm_ = $("#selectAllUsersId").siblings("[rs-data-usid='"+user_id+"']");
            if(elm_.hasClass("active")){
                angular.forEach($scope.userList, function (item, index) {
                    if(item.id==user_id){
                        item.extClass = "";
                    }
                });
            }else{
                angular.forEach($scope.userList, function (item, index) {
                    if(item.id==user_id) {
                        item.extClass = "active";
                    }
                });
            }
        }
        $scope.selectGroup = function(group_id){
            var elm_ = $("#selectAllGroupId").siblings("[rs-data-gpid='"+group_id+"']");
            if(elm_.hasClass("active")){
                angular.forEach($scope.groupList, function (item, index) {
                    if(item.groupId==group_id){
                        item.extClass = "";
                    }
                });
            }else{
                angular.forEach($scope.groupList, function (item, index) {
                    if(item.groupId==group_id) {
                        item.extClass = "active";
                    }
                });
            }
        }
        $scope.selectAllUserChange = function(){
            if($("#allUsersCheckbox").is(':checked')){
                angular.forEach($scope.userList, function (item, index) {
                    item.extClass = "active";
                });
            }else{
                angular.forEach($scope.userList, function (item, index) {
                    item.extClass = "";
                });
            }
        }
        $scope.selectAllGroupChange = function(){
            if($("#allGroupsCheckbox").is(':checked')){
                angular.forEach($scope.groupList, function (item, index) {
                    item.extClass = "active";
                });
            }else{
                angular.forEach($scope.groupList, function (item, index) {
                    item.extClass = "";
                });
            }
        }

        $scope.saveUserGroup = function(){
            if(angular.isUndefined($scope.assignerList)){
                $scope.assignerList = [];
            }
            var assignListId = "";
            if($scope.ugflag=="U"){
                assignListId = "selectAllUsersId";
            }else if($scope.ugflag=="G"){
                assignListId = "selectAllGroupId";
            }else{
                assignListId = "selectAllGroupId";//TODO:
            }
            $("#"+assignListId).siblings(".active").each(function(){
                var assigner = {};
                if($scope.ugflag=="U"){
                    assigner.assignTypeDesc = "用户";
                }else if($scope.ugflag=="G"){
                    assigner.assignTypeDesc = "用户组";
                }else{
                    assigner.assignTypeDesc = "其他";
                }
                assigner.assignTypeCode = $scope.ugflag;
                assigner.name = $(this).attr("rs-data-asname");
                assigner.id = $(this).attr("rs-data-asid");
                assigner.defSelModTxt = $("#selModeTxt").text();
                assigner.defSelMod = $("#selModeTxt").attr("def-sel-mod");
                if($("#selAllFlag").is(':checked')){
                    assigner.checkFlag = true;
                }else{
                    assigner.checkFlag = false;
                }
                assigner.exeConn = $scope.exeConn;
                if(isAssignerSelected($scope.assignerList,assigner)){
                    console.log("same user/group already existed");
                }else{
                    $scope.assignerList[$scope.assignerList.length] = assigner;
                }
                $(this).removeClass("active");

            });
            $("#selectAllGroupId").siblings(".active").each(function(){
//                assignedGroupsStr+=$(this).attr("rs-data-gpname")+",";
            });
        }
        $scope.selectAddUserGroups = function(ugflag,evt){
            $scope.ugflag = ugflag;
            $("#addUserGroupId").css("display","");
            if("U"==ugflag){
                $("#showUsers").css("display","");
                $("#showGroups").css("display","none");
            }
            else if("G"==ugflag){
                $("#showGroups").css("display","");
                $("#showUsers").css("display","none");
            }
            $("#addDropdownTxt").text($(evt.target).text());
        }
        $scope.selectAssignerDefSelMode = function(evt){
            var sel_target = $(evt.target);
            $("#selModeTxt").text(sel_target.text()).attr("def-sel-mod",sel_target.attr("def-sel-mod"));
            sel_target.parent().addClass("active");
            sel_target.parent().siblings().removeClass("active");
        }

    }]);

function confirmDelete(){
    $("#deleteTaskAlert").css("display","");
}
function notDeleteTask(){
    $("#deleteTaskAlert").css("display","none");
}

function changeTab(obj,formId_){
    var li_ = $(obj).parent();
    li_.addClass("active");
    li_.siblings("li").removeClass("active");
    $("#"+formId_).show();
    $("#"+formId_).siblings("form").hide();
}

function hideModal(){
    var data_ = {};
    data_.opt="C";
    window.parent.postMessage(JSON.stringify(data_), '*');
}

function isAssignerSelected(assignerList, assigner){
    var asnId = assigner.id;
    var asnType = assigner.assignTypeCode;
    var existed = $.grep(assignerList,function(value) {
        return value.id == asnId && value.assignTypeCode==asnType;
    });
    if(existed==undefined || existed==""){
        return false;
    }
    return true;
}
