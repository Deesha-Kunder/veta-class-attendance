package com.shadee.Veeta.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String SUPABASE_URL;

    @Value("${supabase.key}")
    private String SUPABASE_KEY;

    @Value("${supabase.bucket}")
    private String BUCKET;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String uploadFile(MultipartFile multipartFile, String fileId, String originalFileName) throws IOException, InterruptedException {
        String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8).replace("+", "%20");
        String fileName = fileId + "/" + encodedFileName;
        String uploadUrl = String.format("%s/storage/v1/object/%s/%s?upsert=true", SUPABASE_URL, BUCKET, fileName);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", multipartFile.getContentType() != null ? multipartFile.getContentType() : "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartFile.getBytes()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Failed to upload " + response.body());
        }
        return String.format("%s/%s", BUCKET, fileName);//file path

    }

    public String generateSignedURL(String filePath, int expirationInMin) throws IOException, InterruptedException {
        System.out.println("File path: " + filePath);
        String signedUrl = String.format("%s/storage/v1/object/sign/%s", SUPABASE_URL, filePath);
        String requestBody = String.format("{\"expiresIn\" : %d} ", expirationInMin *60);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(signedUrl))
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Failed to generateSigned URl " + response.body());

        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());
        String signedPart = json.get("signedURL").asText();

        System.out.println("SignedPart1: " + signedPart);
        signedPart = signedPart.replace(" ", "%20");
        System.out.println("SignedPart2: " + signedPart);

        return signedPart.startsWith("https") ? signedPart : SUPABASE_URL+"/storage/v1"+ signedPart;
    }
}
