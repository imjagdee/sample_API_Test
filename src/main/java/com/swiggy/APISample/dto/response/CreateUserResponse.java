package com.swiggy.APISample.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateUserResponse {

    private String name;
    private String job;
    private String id;
    private String createdAt;
}
