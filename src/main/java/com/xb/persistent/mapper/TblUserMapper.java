package com.xb.persistent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.CommonMapper;
import com.xb.persistent.TblUser;
import com.xb.persistent.WfInstance;

/**
 *
 * TblUser 表数据库控制层接口
 *
 */
public interface TblUserMapper extends CommonMapper<TblUser> {

	@Delete({
		"delete from wf_awt where inst_id in (select inst_id from wf_instance where ref_mkid=#{refMkid})"
		})
	public void deleteUnitResult1(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wf_inst_hist  where inst_id in (select inst_id from wf_instance where ref_mkid=#{refMkid})"
		})
	public void deleteUnitResult2(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wf_instance where ref_mkid=#{refMkid}"
	})
	public void deleteUnitResult3(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wf_task where wf_id in (select wf_version_id from wf_version where ref_mkid=#{refMkid})"
	})
	public void deleteUnitResult4WfTask(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wf_task_conn where wf_id in (select wf_version_id from wf_version where ref_mkid=#{refMkid})"
	})
	public void deleteUnitResult4WfConn(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wf_version where ref_mkid =#{refMkid}"
	})
	public void deleteUnitResult4WfDef(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wfdef where wfdef_mkid=#{refMkid}"
	})
	public void deleteUnitResult4RsWorkflow(@Param("refMkid") String refMkid);
	
	@Delete({
		"delete from wf_inst_track where TRACK_ID like 'ju_%' "
	})
	public void deleteUnitResult4Track();
	
	@Delete({
		"delete from wf_inst_tracklog where TRACK_ID like 'ju_%' "
	})
	public void deleteUnitResult4Tracklog();
	
	@Select(" select * from wf_instance where REF_MKID=#{refMkid} and inst_num=#{instNum}")
	@Results({
        @Result(id=true,property="instId",column="INST_ID",javaType=String.class),
        @Result(property="currAssigners",column="CURR_ASSIGNERS",javaType=String.class),
        @Result(property="optUsersPre",column="OPT_USERS_PRE",javaType=String.class)
    })
	public List<WfInstance> getCurrentAssigner4Instance(@Param("refMkid") String refMkid, @Param("instNum") int instNum); 

}