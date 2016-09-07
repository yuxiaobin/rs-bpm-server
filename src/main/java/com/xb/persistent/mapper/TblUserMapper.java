package com.xb.persistent.mapper;

import com.xb.persistent.TblUser;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.CommonMapper;

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

}