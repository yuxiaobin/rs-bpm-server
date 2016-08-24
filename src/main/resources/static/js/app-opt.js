/**
 * Created by yuxiaobin on 2016/7/28.
 */
angular.module('taskApp', [ ])
    .service('taskService', ['$http', '$q', function ($http, $q) {
        this.getNextTasks = function(rsWfId ,instNum, refMkid, optCode){
            var delay = $q.defer();
            var parm = {rsWfId:rsWfId,instNum:instNum,refMkid:refMkid,optCode:optCode};
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
        this.getNextAssigners = function(rsWfId ,instNum, refMkid, optCode){
            var delay = $q.defer();
            var parm = {rsWfId:rsWfId,instNum:instNum,refMkid:refMkid,optCode:optCode};
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
            var req = {
                method: 'POST',
                data:optVO,
                url: basePath+"/task/process"
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
    .controller('taskCtrl', ['$scope','$window', '$timeout','taskService', function ($scope,$window,$timeout, taskService) {
        if(typeof(TX_PR_CHOICES)!='undefined'){
            if(TX_PR_CHOICES.SignWhenGo==true || TX_PR_CHOICES.SignWhenGo=="true"){
                $scope.SignWhenGoDisp="(必须填写)"
            }else{
                $scope.SignWhenGoDisp="";
            }
        }
        $scope.isEndTask = false;
        taskService.getNextAssigners(rsWfId ,instNum, refMkid, optCode).then(function(succ){
            var result = succ.result;
            $scope.userList = result.users;
            $scope.groupList = result.groups;
            $timeout(function(){
                if(!angular.isUndefined(result.users)){
                    for(var i=0;i<result.users.length;++i){
                        var user_ = result.users[i];
                        if(user_.defSelMod=="1" || user_.defSelMod=="2"){
                            checkAndAddUser(user_);
                        }
                    }
                }
                if(!angular.isUndefined(result.groups)){
                    for(var i=0;i<result.groups.length;++i){
                        var group_ = result.groups[i];
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
        });
        taskService.getNextTasks(rsWfId ,instNum, refMkid, optCode).then(function(succ){
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
        });
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
            var optVO = {rsWfId:rsWfId,instNum:instNum,refMkid:refMkid,optCode:optCode};
            optVO.nextTaskId = selectedNextTaskId;
            optVO.comments = $("#comments").val();
            var assigners = ",";
            for(var i=0;i<selectedUsersArray.length;++i){
                assigners+=selectedUsersArray[i].id+",";
            }
            optVO.nextAssigners = assigners;
            taskService.submitTask(optVO).then(function(success){
                alert( $scope.optTitle+"成功");
                hideModal();
            },function(fail){
                alert( $scope.optTitle+"失败了");
            });
        }

        if(optCode=="C"){
            $scope.optTitle = "工作流提交";
        }else if(optCode=="RJ"){
            $scope.optTitle = "工作流退回";
        }else if(optCode=="V"){
            $scope.optTitle = "工作流否决";
        }else if(optCode=="RD"){
            $scope.optTitle = "工作流撤回";
        }

    }]);

function hideModal(){
    var data_ = {opt:"C"};
    window.parent.postMessage(JSON.stringify(data_), '*');
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