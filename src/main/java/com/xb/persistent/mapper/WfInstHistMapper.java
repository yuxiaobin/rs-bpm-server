package com.xb.persistent.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.CommonMapper;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;

/**
 *
 * WfInstHist 表数据库控制层接口
 *
 */
public interface WfInstHistMapper extends CommonMapper<WfInstHist> {

	public List<WfInstHist> getInstHistByInstId(@Param("instId") String instId);
	
	@Select({
				"<script>"
				+ "select task.task_descp_disp,a.ref_mkid,a.inst_num,a.curr_assigners "+
					"from wf_inst_hist hist "+
					"join ("+
					"select max(opt_seq) as opt_seq, inst.inst_id, ref_mkid,inst.inst_num,inst.curr_assigners, inst.task_id_curr "+
					"from wf_instance inst "+
					"join wf_inst_hist hist on hist.inst_id=inst.inst_id and opt_user=#{userId} "+
					"<if test=\"refMkid != null\">"+
					"where inst.ref_mkid=#{refMkid} and inst_num=#{instNum} "+
					"</if>"+
					"group by inst.inst_id,ref_mkid,inst_num,curr_assigners,task_id_curr "+
					")a on a.inst_id=hist.inst_id and hist.opt_seq=a.opt_seq "+
					"join wf_task task on task.task_id=a.task_id_curr"
					+"</script>"
			})
	@Results({
        @Result(id=true,property="taskDescpDisp",column="task_descp_disp",javaType=String.class),
        @Result(property="refMkid",column="ref_mkid",javaType=String.class),
        @Result(property="instNum",column="inst_num",javaType=Integer.class),
        @Result(property="taskOwner",column="curr_assigners",javaType=String.class)
    })
	public List<WfAwt> getDoneListByMe(Map<String,Object> parm);
	
}