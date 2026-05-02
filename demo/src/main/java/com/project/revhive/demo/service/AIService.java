package com.project.revhive.demo.service;

import com.project.revhive.demo.config.AIContentConfig;
import com.project.revhive.demo.dto.request.AIRequest;
import com.project.revhive.demo.dto.response.AIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    @Autowired
    private AIContentConfig config;
    private final String PYTHON_CONNECTIN_URL="http://localhostL800/api";

    public String process(AIRequest request)
    {
        ResponseEntity<AIResponse> response=config.restTemplate().postForEntity(PYTHON_CONNECTIN_URL,request, AIResponse.class);
        return response.getBody().getResult();
    }
}
