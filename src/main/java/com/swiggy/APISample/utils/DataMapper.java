package com.swiggy.APISample.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DataMapper {

    public <T> List<T> userList(String filePath, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
            new File(filePath),
            objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
        );
    }
}
