package com.example.springdemo.bean.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

@Data
public class OcrResult {

    @JsonProperty("words_result")
    private List<Map<String, String>> wordsResult;

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("words_result_num")
    private Integer wordsResultNum;

}
