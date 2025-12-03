package com.swiggy.APISample.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GetUserResponse {

    private Data data;
    private Support support;
    @JsonProperty("_meta")
    private Meta meta;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)

    public static class Meta{
        private String powered_by;
        private String upgrade_url;
        private String docs_url;
        private String template_gallery;
        private String message;
        private List<String> features;
        private String upgrade_cta;
    }

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Data{
        private int id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;
    }

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Support{
        private String url;
        private String text;
    }
}
