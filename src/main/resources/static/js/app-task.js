/**
 * Created by yuxiaobin on 2016/8/5.
 *
 * Popup page for Edit Task
 */
var addUserGroupTypes = {ADD_USER:{CODE:"U",DESCP:"添加用户"}, ADD_GROUP:{CODE:"G",DESCP:"添加用户组"},ADD_CUST:{CODE:"C",DESCP:"添加自定义人员" /*ADD_POST:{CODE:"R",DESCP:"添加岗位"*/}};
var selectModeTypes = {NOT_SELECTED:"默认不选中", SELECTED_WITH_CANCEL:"默认选中，允许取消", SELECTED_WITHOUT_CANCEL:"默认选中，不允许取消"};
var assignTypeDescps = {USER:"用户", GROUP:"用户组", /*POST:"岗位", */OTHER:"自定义"};
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
        this.getBuzStatus = function(refMkid){
            var delay = $q.defer();
            var req = {
                method: 'GET',
                url: basePath+'/wfadmin/buzStatus?refMkid='+refMkid
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
    .controller('taskCtrl', ['$scope', '$timeout', 'userGroupService', function ($scope, $timeout, userGroupService) {
        if(angular.isUndefined( $scope.userGroupChoices)){
            $scope.userGroupChoices = {};
        }
        $scope.selectModeOptList = [{value:0,descp:selectModeTypes.NOT_SELECTED},{value:1,descp:selectModeTypes.SELECTED_WITH_CANCEL},{value:2,descp:selectModeTypes.SELECTED_WITHOUT_CANCEL}];
        userGroupService.getAllUsers().then(function(success){
            $scope.userList = success.records;
            userGroupService.getAllGroups().then(function(success){
                $scope.groupList = success.records;
                if(taskData.assigners!=""){
                    $timeout(function(){
                        $scope.assignerList = taskData.assigners;
                        angular.forEach($scope.assignerList,function(item,index){
                            if(item.assignTypeCode==addUserGroupTypes.ADD_USER.CODE){
                                item.assignTypeDesc = assignTypeDescps.USER;
                            }else if(item.assignTypeCode==addUserGroupTypes.ADD_GROUP.CODE){
                                item.assignTypeDesc =  assignTypeDescps.GROUP;
//                            } else if(item.assignTypeCode==addUserGroupTypes.ADD_POST.CODE){
//                                item.assignTypeDesc =  assignTypeDescps.POST;
                            }else{
                                item.assignTypeDesc = assignTypeDescps.OTHER;
                            }
                            if(item.defSelMod=="1"){
                                item.defSelModTxt = selectModeTypes.SELECTED_WITH_CANCEL;
                            }else if(item.defSelMod=="2"){
                                item.defSelModTxt = selectModeTypes.SELECTED_WITHOUT_CANCEL;
                            }else{
                                item.defSelModTxt = selectModeTypes.NOT_SELECTED;
                            }
                        });

                    },500);
                }
            })
        });

        $scope.getBuzStatusOptions = function(refMkid){
            userGroupService.getBuzStatus(refMkid).then(function(succ){
                if(succ.length==0){
                    $scope.buzStatusOptions = [{value:"",descp:"没有定义状态"}];
                    $scope.task.buzStatus = "";
                    $("#buzStatus").selectpicker('hide').selectpicker("destroy");
                    setTimeout(function(){
                        $("#buzStatus").selectpicker('show').selectpicker('val', "");
                    },100);
                }else{
                    $scope.buzStatusOptions = succ;
                    if(angular.isUndefined($scope.task.buzStatus) || $scope.task.buzStatus==""){
                        if(taskData.taskType==RS_TYPE_END){
                            $scope.task.buzStatus = succ[succ.length-1].value;
                        }else{
                            $scope.task.buzStatus = succ[0].value;
                        }
                    }
                    $("#buzStatus").selectpicker('hide').selectpicker("destroy");
                    setTimeout(function(){
                        $("#buzStatus").selectpicker('show').selectpicker('val', $scope.task.buzStatus);
                    },100);
                }
            },function(fail){
                $scope.buzStatusOptions = [{value:"",descp:"没有定义状态"}];
                $scope.task.buzStatus = "";
                $("#buzStatus").selectpicker('hide').selectpicker("destroy");
                setTimeout(function(){
                    $("#buzStatus").selectpicker('show').selectpicker('val', "");
                },100);
            });
        };
        $scope.getBuzStatusOptions(taskData.refMkid);

        $scope.task = taskData;
        if(taskData.taskType==RS_TYPE_START || taskData.taskType==RS_TYPE_END){
            $scope.showAssignerEdit = false;
            $scope.isStartEndNode = true;
            if(taskData.taskType==RS_TYPE_START){
                $scope.isStartNode = true;
                $scope.isEndNode = false;
            }else{
                $scope.isStartNode = false;
                $scope.isEndNode = true;
            }
            $scope.txTypeOptions = [{value:"B",descp:"开始"},{value:"E",descp:"结束"}]
        }else{
            $scope.showAssignerEdit = true;
            $scope.isStartEndNode = false;
            $scope.txTypeOptions = [{value:"S",descp:"一般事务"},{value:"M",descp:"会签事务"}]
        }

        $scope.custUsers = [];
        if(typeof(taskData.custFuncVarArray)!='undefined'){
            var funcVarArray = taskData.custFuncVarArray;
            for(var i=0;i<funcVarArray.length;++i){
                if(funcVarArray[i].varType=="U"){
                    $scope.custUsers[$scope.custUsers.length] = {varCode:funcVarArray[i].varCode,varDescp:funcVarArray[i].varDescp};
                }
            }
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
                assigner.defSelMod = item.defSelMod;
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
            taskData.TX_CHOICES = $scope.task.TX_CHOICES;
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
            if(angular.isUndefined($scope.userGroupChoices.userGroupIds) || $scope.userGroupChoices.userGroupIds==""){//dropdown:selected id(s)
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
                if($scope.ugflag == addUserGroupTypes.ADD_USER.CODE){
                    assigner.assignTypeDesc = assignTypeDescps.USER;
                }else if($scope.ugflag == addUserGroupTypes.ADD_GROUP.CODE){
                    assigner.assignTypeDesc = assignTypeDescps.GROUP;
//                }else if($scope.ugflag == addUserGroupTypes.ADD_POST.CODE){
//                    assigner.assignTypeDesc = assignTypeDescps.POST;
                }else{
                    assigner.assignTypeDesc = assignTypeDescps.OTHER;
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
            }else{
                $scope.userGroupOptList = [];
                for(var i=0;i<$scope.custUsers.length;++i){
                    var custUser = $scope.custUsers[i];
                    $scope.userGroupOptList[$scope.userGroupOptList.length] = {id:custUser.varCode, name:custUser.varDescp};
                }
                $("#addDropdownTxt").text(addUserGroupTypes.ADD_CUST.DESCP);
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
         * 选择添加用户、用户组、自定义用户等
         * @param ugflag
         * @param evt
         */
        $scope.selectAddUserGroups = function(ugflag,evt){
            $scope.ugflag = ugflag;
            if(addUserGroupTypes.ADD_USER.CODE==ugflag){
                $scope.userGroupOptList = $scope.userList;
            }
            else if(addUserGroupTypes.ADD_GROUP.CODE==ugflag){
                $scope.userGroupOptList = $scope.groupList;
            }else{
                $scope.userGroupOptList = [];
                for(var i=0;i<$scope.custUsers.length;++i){
                    var custUser = $scope.custUsers[i];
                    $scope.userGroupOptList[$scope.userGroupOptList.length] = {id:custUser.varCode, name:custUser.varDescp};
                }
            }
            $("#selectedUserGroup").selectpicker('hide').selectpicker("destroy");
            setTimeout(function(){
                $("#selectedUserGroup").selectpicker('show')
            },100);
            $("#addUserGroupId").css("display","");
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
                $scope.task.SIGN_CHOICES.AllHandledThenGo = true;
                if($scope.task.SIGN_CHOICES.PartHandledThenGo == true){
                    $scope.task.SIGN_CHOICES.AllHandledThenGo = false;
                }

            }else if(newVal=="S"){
                $scope.csFlag = false;
                $scope.task.SIGN_CHOICES.AllHandledThenGo = false;
                $scope.task.SIGN_CHOICES.PartHandledThenGo = false;
                $scope.task.SIGN_CHOICES.AtLeastHandled = 1;
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
        $scope.csOptionSelect = function(csOptCode){
            if("ALL"==csOptCode){
                $scope.task.SIGN_CHOICES.AllHandledThenGo=true;
                $scope.task.SIGN_CHOICES.PartHandledThenGo=false;
            }else{
                $scope.task.SIGN_CHOICES.PartHandledThenGo=true;
                $scope.task.SIGN_CHOICES.AllHandledThenGo=false;
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
