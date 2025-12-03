package com.swiggy.APISample.utils;


import com.swiggy.APISample.commons.UserConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserUtils {

    @Value("${x.api.value}")
    private String xApiValue;

    public Map<String,Object> getXpiHeaders(){
        Map<String,Object> headers  = new HashMap<>();
        headers.put(UserConstants.xApiKey,xApiValue);
        return headers;
    }
}
