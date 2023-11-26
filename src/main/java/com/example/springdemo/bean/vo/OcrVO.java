package com.example.springdemo.bean.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OcrVO {

    @JsonProperty("words_result")
    private List<Map<String, String>> wordsResult;

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("words_result_num")
    private Integer wordsResultNum;

}
