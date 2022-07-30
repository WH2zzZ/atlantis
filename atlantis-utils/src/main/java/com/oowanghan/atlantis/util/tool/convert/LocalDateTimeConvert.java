package com.oowanghan.atlantis.util.tool.convert;

import cn.hutool.core.util.StrUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface LocalDateTimeConvert {

    LocalDateTimeConvert INSTANCE = Mappers.getMapper(LocalDateTimeConvert.class);

    String LOCAL_DATE_TO_STR = "LocalDateToStr";

    String LOCAL_DATE_TIME_TO_STR = "LocalDateTimeToStr";

    String STR_TO_LOCAL_DATE_TIME = "StrToLocalDateTime";

    @Named(LOCAL_DATE_TO_STR)
    default String localDateToStr(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    @Named(LOCAL_DATE_TIME_TO_STR)
    default String localDateTimeToStr(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    @Named(STR_TO_LOCAL_DATE_TIME)
    default LocalDateTime toLocalDateTime(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(dateStr, formatter);
        return parse;
    }
}