package com.example.springdemo.service.third.response;


import com.example.springdemo.bean.dto.juhe.NewsInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class JuheNewsResponse {

    @Data
    public static class Result {
        private String stat;
        private List<NewsInfoDTO> data;
    }

    private String reason;
    private Result result;

}
