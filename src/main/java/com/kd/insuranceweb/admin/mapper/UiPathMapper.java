package com.kd.insuranceweb.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kd.insuranceweb.admin.dto.UiPathNoticeDto;

@Mapper
public interface UiPathMapper {
	UiPathNoticeDto SelectOne();

	int insert(@Param("content") String content);
}
