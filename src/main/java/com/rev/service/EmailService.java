package com.rev.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Value("${mail.from}")
    private String senderEmail;

    private final ObjectMapper objectMapper;

    public void sendVerificationOtpEmail(
            String userEmail,
            String otp,
            String subject,
            String text
    ) {

        try {

            System.out.println("START sending OTP to: " + userEmail);

            Map<String, Object> from = new HashMap<>();
            from.put("Email", senderEmail);
            from.put("Name", "Rev Bazaar");

            Map<String, Object> to = new HashMap<>();
            to.put("Email", userEmail);

            Map<String, Object> message = new HashMap<>();
            message.put("From", from);
            message.put("To", List.of(to));
            message.put("Subject", subject);
            message.put("TextPart", text);

            Map<String, Object> body = new HashMap<>();
            body.put("Messages", List.of(message));

            String json = objectMapper.writeValueAsString(body);

            String credentials = apiKey + ":" + apiSecret;

            String encodedCredentials = Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mailjet.com/v3.1/send"))
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Mailjet status: " + response.statusCode());
            System.out.println("Mailjet response: " + response.body());

            if (response.statusCode() < 200 ||
                    response.statusCode() >= 300) {

                throw new RuntimeException(
                        "Mailjet email failed: " + response.body()
                );
            }

            System.out.println(
                    "OTP email sent successfully to: " + userEmail
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to send OTP email",
                    e
            );
        }
    }
}
