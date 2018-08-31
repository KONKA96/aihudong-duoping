package com.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
 
/**
 * 时间转换器（将返回的json数据中的日期格式转换为指定格式）
 * Created by  on 2017/4/24.
 */
public class JsonDateValueProcessorUtil implements JsonValueProcessor {
    private String format ="yyyy-MM-dd'T'HH:mm:ss.SSSZ";
 
    public JsonDateValueProcessorUtil() {
        super();
    }
 
    public JsonDateValueProcessorUtil(String format) {
        super();
        this.format = format;
    }
 
    @Override
    public Object processArrayValue(Object paramObject,
                                    JsonConfig paramJsonConfig) {
        return process(paramObject);
    }
 
    @Override
    public Object processObjectValue(String paramString, Object paramObject,
                                     JsonConfig paramJsonConfig) {
        return process(paramObject);
    }
 
 
    private Object process(Object value){
        if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}

