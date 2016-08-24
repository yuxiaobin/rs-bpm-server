/**
 * Created by yuxiaobin on 2016/8/5.
 *
 * Popup page for Edit Task
 */
var basePath = "";
var addUserGroupTypes = {ADD_USER:{CODE:"U",DESCP:"添加用户"}, ADD_GROUP:{TYPE:"G",DESCP:"添加用户组"}};
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

        if(angular.isUndefined( $scope.userGroupChoices)){
            $scope.userGroupChoices = {};
        }
        $scope.selectModeOptList = [{value:0,descp:"默认不选中"},{value:1,descp:"默认选中，允许取消"},{value:2,descp:"默认选中，不允许取消"}];
        userGroupService.getAllUsers().then(function(success){
            $scope.userList = success.records;
            userGroupService.getAllGroups().then(function(success){
                $scope.groupList = success.records;
                if(taskData.assigners!=""){
                    $timeout(function(){
                        $scope.assignerList = taskData.assigners;
                        angular.forEach($scope.assignerList,function(item,index){
                            if(item.assignTypeCode==addUserGroupTypes.ADD_USER.CODE){
                                item.assignTypeDesc = $scope.assignTypeDesc_user;
                            }else if(item.assignTypeCode==addUserGroupTypes.ADD_GROUP.CODE){
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
        if(taskData.taskType==RS_TYPE_START || taskData.taskType==RS_TYPE_END){
            $scope.showAssignerEdit = false;
            $scope.isStartEndNode = true;
            if(taskData.taskType==RS_TYPE_START){
                $scope.isStartNode = true;
                $scope.isEndNode = false;
                $scope.task.txType = "B";
                $scope.task.buzStatus = "I";
                if(angular.isUndefined($scope.TX_CHOICES)){
                    $scope.TX_CHOICES = {};
                }
                $scope.TX_CHOICES.AllowEdit = true;
                $scope.TX_CHOICES.AllowDelete = true;
            }else{
                $scope.isStartNode = false;
                $scope.isEndNode = true;
                $scope.task.txType = "E";
                $scope.task.buzStatus = "C";
                if(angular.isUndefined($scope.TX_CHOICES)){
                    $scope.TX_CHOICES = {};
                }
                $scope.TX_CHOICES.AllowEdit = false;
                $scope.TX_CHOICES.AllowDelete = false;
            }
            $scope.task.timeLimitTp = "H";
            $scope.task.alarmTimeTp = "H";
            $scope.task.SIGN_CHOICES.AllHandledThenGo = true;
            $scope.txTypeOptions = [{value:"B",descp:"开始"},{value:"E",descp:"结束"}]
        }else{
            $scope.showAssignerEdit = true;
            $scope.isStartEndNode = false;
            $scope.txTypeOptions = [{value:"S",descp:"一般事务"},{value:"M",descp:"会签事务"}]
        }


        if($scope.task.TX_PR_CHOICES.NoticeNextAfterGo || $scope.task.TX_PR_CHOICES.NoticeFirstAfterGo || $scope.task.TX_PR_CHOICES.NoticePreviousAfterGo || $scope.task.TX_PR_CHOICES.NoticeElseAfterGo){
            $scope.needNotifyFlag = true;
        }
        /**
         * Click Save button to save task properties
         */
        $scope.updateTaskProperties = function(){
            if(angular.isUndefined($scope.task.TX_PR_CHOICES)){
                $scope.task.TX_PR_CHOICES = {};
            }
            if($scope.task.TX_PR_CHOICES.NoticeNextAfterGo
                || $scope.task.TX_PR_CHOICES.NoticeFirstAfterGo
                || $scope.task.TX_PR_CHOICES.NoticePreviousAfterGo
                || $scope.task.TX_PR_CHOICES.NoticeElseAfterGo
                ){
                if(!$scope.task.TX_PR_CHOICES.MsgAlert && !$scope.task.TX_PR_CHOICES.SmsAlert){
                    alert("维护了处理后通知人，请维护通知方式！");
                    return;
                }
            }
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
                assigner.exeConn = item.exeConn;
               assigns.push(assigner);
            });
            taskData.assigners = assigns;
            taskData.txCode = $scope.task.txCode;
            taskData.txType = $scope.task.txType;
            taskData.buzStatus = $scope.task.buzStatus;
            taskData.timeLimit =  $scope.task.timeLimit;
            taskData.timeLimitTp =  $scope.task.timeLimitTp;
            taskData.alarmTime = $scope.task.alarmTime;
            taskData.alarmTimeTp =  $scope.task.alarmTimeTp;
            taskData.moduleId = $scope.task.moduleId;
            taskData.runParam = $scope.task.runParam;
            taskData.TX_CHOICES = $scope.TX_CHOICES;
            if(!taskData.TX_CHOICES.AllowGoBack){
                taskData.TX_CHOICES.SignWhenGoBack = false;
            }
            if(!taskData.TX_CHOICES.AllowReCall){
                taskData.TX_CHOICES.SignWhenReCall = false;
            }
            if(!taskData.TX_CHOICES.AllowVeto){
                taskData.TX_CHOICES.SignWhenVeto = false;
            }
            taskData.TX_PR_CHOICES = $scope.task.TX_PR_CHOICES;
            taskData.TX_BK_CHOICES = $scope.task.TX_BK_CHOICES;
            taskData.SIGN_CHOICES = $scope.task.SIGN_CHOICES;
            taskData.opt = addUserGroupTypes.ADD_USER.CODE;
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
            var elm_ = $("#selectAllUsersId").siblings("[rs-data-asid='"+user_id+"']").eq(0);
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
            var elm_ = $("#selectAllGroupId").siblings("[rs-data-asid='"+group_id+"']");
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
        /**
         * 保存选择的用户、用户组或其他，以及附属的属性
         */
        $scope.saveUserGroup = function(){
            if(angular.isUndefined($scope.assignerList)){
                $scope.assignerList = [];
            }
            if(angular.isUndefined($scope.userGroupChoices.userGroupIds) || $scope.userGroupChoices.userGroupIds==""){
                alert("请选择需要添加的用户或用户组");
                return;
            }
            var userGroupIdsArray = $scope.userGroupChoices.userGroupIds.toString().split(",");
            var selectedOptList = $.grep($scope.userGroupOptList,function(value){
                for(var i=0;i<userGroupIdsArray.length;++i){
                    if(userGroupIdsArray[i]==value.id){
                        return true;
                    }
                }
                return false;
            });
            var selectedModeOpt = $.grep($scope.selectModeOptList,function(value){
               return value.value==$("#selectMode").val();
            });
            for(var i=0;i<selectedOptList.length;++i){
                var assigner = {};
                if($scope.ugflag==addUserGroupTypes.ADD_USER.CODE){
                    assigner.assignTypeDesc = "用户";
                }else if($scope.ugflag==addUserGroupTypes.ADD_GROUP.CODE){
                    assigner.assignTypeDesc = "用户组";
                }else if($scope.ugflag=="R"){
                    assigner.assignTypeDesc = "岗位";
                }else{
                    assigner.assignTypeDesc = "自定义";
                }
                assigner.assignTypeCode = $scope.ugflag;
                assigner.name = selectedOptList[i].name;
                assigner.id = selectedOptList[i].id;
                assigner.defSelMod = $("#selectMode").val();
                assigner.defSelModTxt = selectedModeOpt[0].descp;
                assigner.checkFlag = $scope.userGroupChoices.allSelected;
                assigner.exeConn = $scope.userGroupChoices.exeConn;
                if(isAssignerSelected($scope.assignerList,assigner)){
                    $.grep($scope.assignerList,function(value){
                        if(value.id==assigner.id && value.assignTypeCode==assigner.assignTypeCode){
                            value.defSelMod = assigner.defSelMod;
                            value.defSelModTxt = assigner.defSelModTxt;
                            value.checkFlag = assigner.checkFlag;//fixed by using ng-if
                            value.exeConn = assigner.exeConn;
                        }
                        return true;
                    });
                }else{
                    $scope.assignerList[$scope.assignerList.length] = assigner;
                }
            }
            $("#selectMode").selectpicker('val', "0");
            $("#selectedUserGroup").selectpicker('val', "");
            $scope.userGroupChoices = {};
            $("#selectedAssignersTable .active").removeClass("active");
        };
        /**
         * 放弃添加、修改 用户、用户组等
         */
        $scope.cancelAddUserGroup = function(){
            $("#selectMode").selectpicker('val', "0");
            $("#selectedUserGroup").selectpicker('val', "");
            $scope.userGroupChoices = {};
            $("#selectedAssignersTable .active").removeClass("active");
        };
        $scope.deleteAddedUserGroup = function(){
            if(angular.isUndefined($scope.userGroupChoices.userGroupIds)){
                return;
            }
            var idArray = $scope.userGroupChoices.userGroupIds.toString().split(",");
            $scope.assignerList = $.grep($scope.assignerList,function(val){
                for(var i=0;i<idArray.length;++i){
                    if(idArray[i] == val.id){
                        return false;
                    }
                }
                return true;
            })
            $("#selectMode").selectpicker('val', "0");
            $("#selectedUserGroup").selectpicker('val', "");
            $scope.userGroupChoices = {};
            $("#selectedAssignersTable .active").removeClass("active");
        };
        /**
         * 选择一条已选的用户或用户组
         * @param evt
         */
        $scope.selectAddedUserGroup = function(evt){
            var selectedTr = $(evt.target).parent();
            selectedTr.addClass("active").siblings().removeClass("active");
            var assign_id = selectedTr.attr("assign-id");
            var assign_type = selectedTr.attr("assign-type");
            $scope.ugflag = assign_type;
             $scope.userGroupOptList = $scope.userList;
            if(assign_type==addUserGroupTypes.ADD_USER.CODE){
                $("#addDropdownTxt").text(addUserGroupTypes.ADD_USER.DESCP);
            }else if(assign_type==addUserGroupTypes.ADD_GROUP.CODE){
                $scope.userGroupOptList = $scope.groupList;
                $("#addDropdownTxt").text(addUserGroupTypes.ADD_GROUP.DESCP);
            }
            var def_sel_mod = selectedTr.attr("def-sel-mod");
            var exe_conn = selectedTr.attr("exe-conn");
            $scope.userGroupChoices.userGroupIds = assign_id;
            $("#selectedUserGroup").selectpicker('hide').selectpicker("destroy");
            $("#selectMode").selectpicker('hide').selectpicker("destroy");
            setTimeout(function(){
                $("#selectedUserGroup").selectpicker('show').selectpicker('val', assign_id);
                $("#selectMode").selectpicker('show').selectpicker('val', def_sel_mod);
            },100);
            $("#selectMode").val(def_sel_mod);
            $scope.userGroupChoices.allSelected = selectedTr.find("input").eq(0).is(":checked")
            $scope.userGroupChoices.exeConn = exe_conn;
            $("#addUserGroupId").css("display","");
        };
        /**
         * 选择添加用户、用户组、其他等
         * @param ugflag
         * @param evt
         */
        $scope.selectAddUserGroups = function(ugflag,evt){
            $scope.ugflag = ugflag;
            if(addUserGroupTypes.ADD_USER.CODE==ugflag){
                $("#showUsers").css("display","");
                $("#showGroups").css("display","none");
                $scope.userGroupOptList = $scope.userList;
                $("#selectedUserGroup").selectpicker('hide').selectpicker("destroy");
                setTimeout(function(){
                    $("#selectedUserGroup").selectpicker('show')
                },100);
                $("#addUserGroupId").css("display","");
            }
            else if(addUserGroupTypes.ADD_GROUP.CODE==ugflag){
                $("#showGroups").css("display","");
                $("#showUsers").css("display","none");
                $scope.userGroupOptList = $scope.groupList;
                $("#selectedUserGroup").selectpicker('hide').selectpicker("destroy");
                setTimeout(function(){
                    $("#selectedUserGroup").selectpicker('show')
                },100);
                $("#addUserGroupId").css("display","");
            }else{
                //TODO for other types
            }
            $("#addDropdownTxt").text($(evt.target).text());

        }
        $scope.selectAssignerDefSelMode = function(evt){
            var sel_target = $(evt.target);
            $("#selModeTxt").text(sel_target.text()).attr("def-sel-mod",sel_target.attr("def-sel-mod"));
            sel_target.parent().addClass("active");
            sel_target.parent().siblings().removeClass("active");
        }

        $scope.csFlag = false;
        $scope.$watch('task.txType', function(newVal, oldVal) {
            if(newVal=="M"){
                $scope.csFlag = true;
            }else if(newVal=="S"){
                $scope.csFlag = false;
                $scope.task.SIGN_CHOICES.AllHandledThenGo = false;
                $scope.task.SIGN_CHOICES.PartHandledThenGo = false;
                $scope.task.SIGN_CHOICES.AtLeastHandled = "";
                $scope.task.TX_PR_CHOICES.NoticeElseAfterGo = false;
            }
        });

        $scope.selectNotify = function(event){
            if($("#txpNotifyNextOnPro").is(':checked') || $("#txpNotifyCreOnPro").is(':checked') || $("#txpNotifyPreOnPro").is(':checked')){
                $scope.needNotifyFlag = true;
            }else{
                $scope.needNotifyFlag = false;
                $scope.task.TX_PR_CHOICES.MsgAlert = false;
                $scope.task.TX_PR_CHOICES.SmsAlert = false;
            }
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
    existed.exeConn ="update test todo";//TODO:for testing only to be removed
    return true;
}
