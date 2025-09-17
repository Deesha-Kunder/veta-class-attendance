package com.shadee.Veeta.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private  String SUPABASE_URL;

    @Value("${supabase.key}")
    private String SUPABASE_KEY;

    @Value("${supabase.bucket}")
    private String BACKECT;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String uploadFile(MultipartFile multipartFile, String fileId, String originalFileName) throws IOException, InterruptedException {

            String fileName = fileId+"_"+originalFileName;
            String uploadUrl = String.format("/storage/v1/object/%s/%s/?upsert=true",SUPABASE_URL,BACKECT,fileName);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uploadUrl))
                    .header("Authorization","Bearer "+SUPABASE_KEY)
                    .header("Content-Type", multipartFile.getContentType() != null ? multipartFile.getContentType():"application/octet-stream")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartFile.getBytes()))
                    .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() < 200 || response.statusCode() >= 300){
            throw new RuntimeException("Failed to upload " +response.body());
        }
        return String.format("%s/%s",BACKECT,fileName);//file path

    }
    public String generateSignedURL(String filePath,int expirationInMin) throws IOException, InterruptedException {
        String signedUrl = String.format("%s/v1/object/sign/%s",SUPABASE_URL,filePath);
        String requestBody = String.format("{\"expiresIn\" : %d} ",expirationInMin);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(signedUrl))
                .header("Authorization","Bearer "+SUPABASE_KEY)
                .header("Content-Type","appplication/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() < 200 || response.statusCode() >= 300){
            throw new RuntimeException("Failed to generateSigned URl " +response.body());
        }

        String signedPart = response.body()
                .replace("{\"signedURL\":\"", "")
                .replace("\"}","");

        return SUPABASE_URL+signedPart;
    }
}
