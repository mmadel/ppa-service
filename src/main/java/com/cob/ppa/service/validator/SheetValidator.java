package com.cob.ppa.service.validator;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SheetValidator {
    public List<String> validate(String filePath , List<String> requiredHeaders) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IllegalArgumentException("CSV file is empty or missing header line");
            }

            // Remove BOM if present (common with UTF-8 files)
            headerLine = headerLine.replace("\uFEFF", "");

            Set<String> actualHeaders = Arrays.stream(headerLine.split("\\s*,\\s*"))
                    .map(header -> header.trim().replaceAll("^\"|\"$", ""))
                    .collect(Collectors.toSet());

            List<String> missingHeaders = new ArrayList<>();
            for (String required : requiredHeaders) {
                if (!actualHeaders.contains(required)) {
                    missingHeaders.add(required);
                }
            }

            return missingHeaders;
        }
    }
}
