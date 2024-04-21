package com.example.springdemo.service;

import com.example.springdemo.bean.dto.SunoClipDTO;
import com.example.springdemo.bean.dto.SunoGenerationRequest;
import com.example.springdemo.bean.dto.SunoGenerationDTO;
import com.example.springdemo.bean.vo.protocol.Result;

import java.util.List;

public interface SunoService {

    Result<SunoGenerationDTO> generateByPrompt(Long userId, String prompt, boolean await);

    Result<SunoGenerationDTO> generateByCustom(Long userId, SunoGenerationRequest request, boolean await);

    Result<List<SunoClipDTO>> getGenerationHistory(int pageNum);

    Result<SunoClipDTO> getGenerationById(String id);

    Result<List<SunoClipDTO>> awaitGeneration(Long userId, List<SunoClipDTO> generationList);

}
