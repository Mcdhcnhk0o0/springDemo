package com.example.springdemo.bean.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class SunoGenerationDTO {

    private String id;
    private String status;
    @JsonAlias("batch_size")
    private String batchSize;
    private List<SunoClipDTO> clips;

}
