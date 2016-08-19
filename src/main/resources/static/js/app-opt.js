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
    .controller('taskCtrl', ['$scope','$window', 'taskService', function ($scope,$window, taskService) {
        taskService.getNextAssigners(rsWfId ,instNum, refMkid, optCode).then(function(succ){
            var result = succ.result;
            $scope.userList = result.users;
            $scope.groupList = result.groups;
        });
        taskService.getNextTasks(rsWfId ,instNum, refMkid, optCode).then(function(succ){
            $scope.taskList = succ.records;
        });
        $scope.selectedAssigners = ",";
        $scope.selectAssigner = function(event){
            var checkbox_ = $(event.target);
            var user_id = checkbox_.val();
            var user_name = checkbox_.attr("rs-attr-name");
            var user_ = {id:user_id,name:user_name};
            if(checkbox_.is(':checked')){
                if(checkAndAddUser(user_)){
                    $scope.selectedAssigners+= user_name+","
                }
            }else{
                if(checkAndRemoveUser(user_)){
                    var selAs = $scope.selectedAssigners.toString();
                    selAs = selAs.replace(user_name+",","");
                    $scope.selectedAssigners=selAs;
                }
            }
        };

        $scope.selectGroups = function(event){
            var checkbox_ = $(event.target);
            var userInGpStr = checkbox_.attr("rs-attr-name");
            var userInGpArray = $.parseJSON(userInGpStr);
            if(checkbox_.is(':checked')){
                var newUserArray = checkAndAddGroup(userInGpArray);
                var selAs = $scope.selectedAssigners.toString();
                for(var i=0;i<newUserArray.length;++i){
                    selAs += newUserArray[i].name+",";
                }
                $scope.selectedAssigners=selAs;
            }else{
                var toRemoveUsers = checkAndRemoveGroup(userInGpArray);
                var selAs = $scope.selectedAssigners.toString();
                for(var i=0;i<toRemoveUsers.length;++i){
                    selAs = selAs.replace(toRemoveUsers[i].name+",","");
                }
                $scope.selectedAssigners=selAs;
            }
        };
        $scope.submitTask = function(){
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
            if(selectedUsersArray.length==0){
                alert("请选择下一步处理人员");
                return;
            }
            var optVO = {rsWfId:rsWfId,instNum:instNum,refMkid:refMkid,optCode:optCode};
            optVO.nextTaskId = selectedNextTaskId;
            optVO.comments = $("#comments").val();
            var assigners = ",";
            for(i=0;i<selectedUsersArray.length;++i){
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
    var data_ = {};
    data_.opt="C";
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
 *
 * @param user
 * @returns {true:need remove, false: no need}
 */
function checkAndRemoveUser(user){
    var usr_array =  $.grep(selectedUsersArray, function(value) {
        return value.id != user.id;
    });
    if(usr_array.length!=selectedUsersArray.length){
        selectedUsersArray = usr_array;
        return true;
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