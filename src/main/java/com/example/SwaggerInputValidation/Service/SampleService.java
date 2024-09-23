package com.example.SwaggerInputValidation.Service;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.report.SimpleValidationReportFormat;
import com.atlassian.oai.validator.report.ValidationReport;
import com.google.common.base.Charsets;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SampleService {

    OpenApiInteractionValidator validator = OpenApiInteractionValidator
            .createForSpecificationUrl("/Users/JK/Dev/SwaggerInputValidation/src/main/resources/sample.yaml")
            .build();

    public String concat(HttpServletRequest request, String s1, String s2) {
        Map<String, Collection<String>> headersMap = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h))
                ));

        SimpleRequest.Builder builder = switch (request.getMethod()) {
            case "GET" -> new SimpleRequest.Builder(Request.Method.GET, request.getRequestURI());
            case "POST" -> new SimpleRequest.Builder(Request.Method.POST, request.getRequestURI());
            default -> null;
        };
        for (String k : request.getParameterMap().keySet()) {
            builder.withQueryParam(k, request.getParameterValues(k));
        }
        Request simmpleRequest = builder.build();
        ValidationReport validationReport = validator.validateRequest(simmpleRequest);
        if (validationReport.hasErrors()) {
            log.error(SimpleValidationReportFormat.getInstance().apply(validationReport));
        }
        return s1+s2;
    }

    public ResponseEntity<?> createUser(HttpServletRequest request, String body) {
        SimpleRequest.Builder builder = new SimpleRequest.Builder(Request.Method.POST, request.getRequestURI());
        builder.withContentType("application/json");
        builder.withBody(body);
        SimpleRequest simpleRequest = builder.build();
        ValidationReport validationReport = validator.validateRequest(simpleRequest);
        if (validationReport.hasErrors()) {
            log.error(SimpleValidationReportFormat.getInstance().apply(validationReport));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }
}
