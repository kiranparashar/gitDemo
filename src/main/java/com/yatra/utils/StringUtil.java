package com.yatra.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class StringUtil {
	  
    public static String generateRequestString(Object obj) 
    		throws JsonProcessingException{

    	return new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(obj);
	}
    
    public static void preetyPrint(Object obj) throws JsonProcessingException{
    	ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    	mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	    Log.message(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }
}
