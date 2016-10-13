/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('taskApp', [ ])
    .service('taskService', ['$http', '$q', function ($http, $q) {
        this.getNextTasks = function(instNum, refMkid, optCode){
            var delay = $q.defer();
            var parm = {instNum:instNum,refMkid:refMkid,optCode:optCode};
            var req = {
                method: 'POST',
                data:parm,
                url: basePath+"/task/next/tasks"
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
        this.getNextAssigners = function(instNum, refMkid, optCode){
            var delay = $q.defer();
            var parm = {instNum:instNum,refMkid:refMkid,optCode:optCode};
            var req = {
                method: 'POST',
                data:parm,
                url: basePath+"/task/next/usergroups"
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
        this.submitTask = function(optVO){
            var delay = $q.defer();
            var url = basePath+"/task/process";
            if(optVO.callbackUrl!=""){
                url = callbackUrl+"?userId="+userId+"&refMkid="+optVO.refMkid+"&wfInstNum="+optVO.instNum+"&optCode="+optVO.optCode
                    +"&comments="+optVO.comments+"&nextTaskId="+optVO.nextTaskId+"&nextUserIds="+optVO.nextAssigners+"&callback=JSON_CALLBACK";
               $http.jsonp(url).success(function(succ){
                    delay.resolve(succ);
               }).error(function(data){
                   delay.reject(data);
               });
            }else{
                var req = {
                    method: 'POST',
                    data:optVO,
                    url:url
                };
                $http(req)
                    .success(function(data, status, headers, config){
                        delay.resolve(data);
                    })
                    .error(function(data, status, headers, config){
                        delay.reject(data);
                    });
            }
            return delay.promise;
        }
    }])
    .controller('taskCtrl', ['$scope','$window', '$timeout','taskService', function ($scope,$window,$timeout, taskService) {
        if(typeof(TX_PR_CHOICES)!='undefined'){
            if(TX_PR_CHOICES.SignWhenGo==true || TX_PR_CHOICES.SignWhenGo=="true"){
                $scope.SignWhenGoDisp="(必须填写)"
            }else{
                $scope.SignWhenGoDisp="";
            }
        }
        $scope.isEndTask = false;
        if(typeof(callbackUrl)!='undefined'){
            $scope.fromRemote = true;
        }else{
            $scope.fromRemote = false;
        }
        taskService.getNextAssigners(instNum, refMkid, optCode).then(function(succ){
            var result = succ.result;
            $scope.userList = result.users;
            $scope.groupList = result.groups;
            $scope.custUserList = result.custUsers;
            if(!angular.isUndefined(result.prevProcessers) && result.prevProcessers.length!=0 ){
                $scope.actExecLabelFlag = true;
                $scope.actExecFlag = true;
                $scope.actExecList = result.prevProcessers;
                $scope.renderActExecerSelection();
            }else{
                $scope.renderDefaultUserGroupSelection();
            }
        });
        taskService.getNextTasks(instNum, refMkid, optCode).then(function(succ){
           $scope.taskList = succ.records;//[{taskDescpDisp:"xxx",taskType:"start-task"}]
           if($scope.taskList.length==1){
               $scope.taskList[0].checkedFlag = true;
               //下一步结束，无需选择人员
               if($scope.taskList[0].taskType==RS_TYPE_END){
                   $scope.isEndTask = true;
                   $("#selectAssignerPanel").prev().removeClass("col-xs-7").addClass("col-xs-12");
                   $("#selectAssignerPanel").hide();
               }
           }
           if(optCode=="RC"){
                $("#selectAssignerPanel").prev().removeClass("col-xs-7").addClass("col-xs-12");
                $("#selectAssignerPanel").hide();
           }
        });

        /**
         * 处理实际执行人默认选中的方法
         */
        $scope.renderActExecerSelection = function(){
            $timeout(function(){
                var selAs =",";
                for(var i=0;i<$scope.actExecList.length;++i){
                    var actEr = $scope.actExecList[i];
                    selectedUsersArray.push(actEr);
                    selAs += actEr.name+", ";
                }
                $scope.selectedAssigners=selAs;
            },200);
        };
        /**
         * 处理非退回操作下的默认人员和组选中，或者退回操作下，选择“重新选择人员”按钮时的默认选中
         */
        $scope.renderDefaultUserGroupSelection = function(){
            $timeout(function(){
                if(!angular.isUndefined($scope.userList)){
                    for(var i=0;i<$scope.userList.length;++i){
                        var user_ = $scope.userList[i];
                        if(user_.defSelMod=="1" || user_.defSelMod=="2"){
                            checkAndAddUser(user_);
                        }
                    }
                }
                if(!angular.isUndefined($scope.groupList)){
                    for(var i=0;i<$scope.groupList.length;++i){
                        var group_ = $scope.groupList[i];
                        if(group_.defSelMod=="1" || group_.defSelMod=="2"){
                            checkAndAddGroup(group_.usersInGroup);
                        }
                    }
                }
                var selAs =",";
                for(var i=0;i<selectedUsersArray.length;++i){
                    selAs += selectedUsersArray[i].name+", ";
                }
                $scope.selectedAssigners=selAs;
            },200);
        }
        $scope.selectedAssigners = ",";
        /**
         * 点击选择用户的checkbox事件：
         *  选中
         *  不选中（取消选中）
         * @param event
         */
        $scope.selectAssigner = function(event){
            var checkbox_ = $(event.target);
            var user_id = checkbox_.val();
            var user_name = checkbox_.attr("rs-attr-name");
            var user_ = {id:user_id,name:user_name};
            if(checkbox_.is(':checked')){
                if(checkAndAddUser(user_)){
                    $scope.selectedAssigners+= user_name+", "
                }
            }else{
                if(checkAndRemoveUser(user_)){
                    var selAs = $scope.selectedAssigners.toString();
                    selAs = selAs.replace(user_name+", ","");
                    $scope.selectedAssigners=selAs;
                }
            }
        };
        /**
         * 选择用户组的checkbox事件
         *  选中
         *  不选中（取消选中）
         * @param event
         */
        $scope.selectGroups = function(event){
            var checkbox_ = $(event.target);
            var userInGpStr = checkbox_.attr("rs-attr-name");
            var userInGpArray = $.parseJSON(userInGpStr);
            if(checkbox_.is(':checked')){
                var newUserArray = checkAndAddGroup(userInGpArray);
                var selAs = $scope.selectedAssigners.toString();
                for(var i=0;i<newUserArray.length;++i){
                    selAs += newUserArray[i].name+", ";
                }
                $scope.selectedAssigners=selAs;
            }else{
                var toRemoveUsers = checkAndRemoveGroup(userInGpArray);
                var selAs = $scope.selectedAssigners.toString();
                for(var i=0;i<toRemoveUsers.length;++i){
                    selAs = selAs.replace(toRemoveUsers[i].name+", ","");
                }
                $scope.selectedAssigners=selAs;
            }
        };
        $scope.submitTask = function(){
            if($scope.SignWhenGoDisp!="" && $.trim($("#comments").val())==""){
                alert("请填写处理意见");
                return;
            }
            var selectedNextTaskId = "";
            $("#taskTable input").each(function(){
                var checkbox_ = $(this);
                if(checkbox_.is(':checked')){
                    selectedNextTaskId = checkbox_.val();
                }
            });
            if(selectedNextTaskId==""){
                alert("请选择下一步处理事务");
                return;
            }
            if(!$scope.isEndTask &&selectedUsersArray.length==0){
                alert("请选择下一步处理人员");
                return;
            }
            var optVO = {instNum:instNum,refMkid:refMkid,optCode:optCode};
            optVO.nextTaskId = selectedNextTaskId;
            optVO.comments = $("#comments").val();
            var assigners = ",";
            for(var i=0;i<selectedUsersArray.length;++i){
                assigners+=selectedUsersArray[i].id+",";
            }
            optVO.nextAssigners = assigners;
            if(typeof(callbackUrl)!='undefined'){
                optVO.callbackUrl = callbackUrl;
            }else{
                optVO.callbackUrl = "";
            }
            taskService.submitTask(optVO).then(function(success){
                alert( $scope.optTitle+"成功");
                hideModal(optCode);
            },function(fail){
                alert( $scope.optTitle+"失败了");
            });
        };
        /**
         * 退回操作时，选择实际执行人/重新选择人员 按钮事件
         *
         * @param evt
         */
        $scope.selectNextAssignerType = function(id_){
            if("actExecFlag"==id_){
                $scope.actExecFlag = true;
                $scope.repickPeople = false;
                var actExecList = $scope.actExecList;
                $scope.actExecList = [];
                selectedUsersArray = [];
                $timeout(function(){
                    $scope.actExecList = actExecList;
                    $scope.renderActExecerSelection();
                },200);
            }else{
                $scope.actExecFlag = false;
                $scope.repickPeople = true;
                var userlist = $scope.userList;
                var grouplist = $scope.groupList;
                $scope.userList = [];
                $scope.groupList = [];
                selectedUsersArray = [];
                $timeout(function(){
                    $scope.userList = userlist;
                    $scope.groupList = grouplist;
                    $scope.renderDefaultUserGroupSelection();
                },200);
            }
        };

        $scope.selectActExecAssigner = function(evt){
            var checkbox_ = $(event.target);
            var user_id = checkbox_.val();
            var user_name = checkbox_.attr("rs-attr-name");
            var user_ = {id:user_id,name:user_name};
            if(checkbox_.is(':checked')){
                if(checkAndAddUser(user_)){
                    $scope.selectedAssigners+= user_name+", "
                }
            }else{
                if(checkAndRemoveUser(user_)){
                    var selAs = $scope.selectedAssigners.toString();
                    selAs = selAs.replace(user_name+", ","");
                    $scope.selectedAssigners=selAs;
                }
            }
        }

        if(optCode=="C"){
            $scope.optTitle = "工作流提交";
        }else if(optCode=="RJ"){
            $scope.optTitle = "工作流退回";
        }else if(optCode=="V"){
            $scope.optTitle = "工作流否决";
        }else if(optCode=="RC"){
            $scope.optTitle = "工作流撤回";
        }else if(optCode=="F"){
            $scope.optTitle = "工作流转交";
        }

    }]);
/**
 * Close opened modal / child page
 */
function hideModal(optCode_){
    var data_ = {opt:"C"};
    if(angular.isUndefined(optCode_)){
        data_.closeType="cancel";
        data_.optCode="";
    }else{
        data_.closeType="confirm";
        data_.optCode=optCode_;
    }
    window.parent.postMessage(JSON.stringify(data_), '*');
    window.close();
}


var selectedUsersArray = [];//{id:'xx', name:'xx'}
/**
 *
 * @param user
 * @returns {true: need add, false:already exist}
 */
function checkAndAddUser(user){
    var usr_array =  $.grep(selectedUsersArray, function(value) {
        return value.id == user.id;
    });
    if(usr_array==null || usr_array.length==0){
        selectedUsersArray.push(user);
        return true;
    }
    return false;
}
/**
 * 取消选中用户时，判断是否要从总的选择人员中删除该用户：
 *  如果下面有选中的组包含该用户，则不删除；
 *  否则删除该用户；
 * @param user
 * @returns {true:need remove, false: no need}
 */
function checkAndRemoveUser(user){
    var userInGroup = [];
    $("#groupTable input").each(function(){
        if(userInGroup.length!=0){
            return;
        }
        var checkbox_ = $(this);
        if(checkbox_.is(':checked')){
            var userInGpArray = $.parseJSON(checkbox_.attr("rs-attr-name"));
            userInGroup = $.grep(userInGpArray,function(value){
                return value.id==user.id;
            });
        }
    });
    if(userInGroup.length==0){
        var usr_array =  $.grep(selectedUsersArray, function(value) {
            return value.id != user.id;
        });
        if(usr_array.length!=selectedUsersArray.length){
            selectedUsersArray = usr_array;
            return true;
        }
    }
    return false;
}
/**
 * Check and add userInGroup into selected array.
 * @param userInGpArray
 * @returns {array:newAddedUsersInGroup}
 */
function checkAndAddGroup(userInGpArray){
    return $.grep(userInGpArray,function(value){
        var existFlag = false;
        for(var i=0;i<selectedUsersArray.length;++i){
            if(selectedUsersArray[i].id==value.id){
                existFlag = true; break;
            }
        }
        if(!existFlag){
            selectedUsersArray.push(value);
        }
        return !existFlag;
    });
}
/**
 * 取消选中用户组，判断是否要从已选人列表中删除该组包含的所有用户
 *  如果单选用户里有已选用户在该组，则该用户不用从已选人列表中删除；
 *  否则删除；
 * @param userInGpArray
 * @returns {*}
 */
function checkAndRemoveGroup(userInGpArray){
    $("#userTable input").each(function(){
        var checkbox_ = $(this);
        if(checkbox_.is(':checked')){
            var user_id = checkbox_.val();
            userInGpArray = $.grep(userInGpArray,function(value){
                return value.id!=user_id;
            });
        }
    });
    selectedUsersArray = $.grep(selectedUsersArray,function(value){
        var existFlag = false;
        for(var i=0;i<userInGpArray.length;++i){
            if(value.id==userInGpArray[i].id){
                existFlag = true;break;
            }
        }
        return !existFlag;
    });
    return userInGpArray;

}
/**
 * 展开或者收缩用户/用户组
 * @param obj
 */
function expandClick(obj){
    var span_ = $(obj);
    if(span_.hasClass("glyphicon-minus")){//当前是展开状态
        span_.parent().next().hide();
        span_.removeClass("glyphicon-minus").addClass("glyphicon-plus");
    }else{
        span_.parent().next().show();
        span_.removeClass("glyphicon-plus").addClass("glyphicon-minus");
    }
}