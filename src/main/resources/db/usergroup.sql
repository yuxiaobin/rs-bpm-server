insert into tbl_user(id,name) values('staff1','staff1');
insert into tbl_user(id,name) values('staff2','staff2');
insert into tbl_user(id,name) values('staff3','staff3');

insert into tbl_user(id,name) values('manager1','manager1');
insert into tbl_user(id,name) values('manager2','manager2');
insert into tbl_user(id,name) values('manager3','manager3');

insert into tbl_user(id,name) values('admin','admin');



insert into tbl_group(group_id,group_name) values('Group1ID','Group 1');
insert into tbl_group(group_id,group_name) values('Group2ID','测试组');
insert into tbl_group(group_id,group_name) values('Group3ID','开发组');


insert into tbl_user2group(user2group_id,user_id,group_id) values('11','staff1','Group2ID');
insert into tbl_user2group(user2group_id,user_id,group_id) values('22','staff2','Group2ID');
insert into tbl_user2group(user2group_id,user_id,group_id) values('33','staff3','Group3ID');

insert into tbl_user2group(user2group_id,user_id,group_id) values('44','manager1','Group1ID');
insert into tbl_user2group(user2group_id,user_id,group_id) values('55','manager2','Group3ID');
insert into tbl_user2group(user2group_id,user_id,group_id) values('66','manager3','Group3ID');
