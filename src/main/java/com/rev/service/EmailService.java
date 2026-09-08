package com.rev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Value("${mail.from}")
    private String senderEmail;

    public void sendVerificationOtpEmail(
            String userEmail,
            String otp,
            String subject,
            String text
    ) {

        try {

            System.out.println("START sending OTP to: " + userEmail);

            String json = """
                    {
                      "Messages": [
                        {
                          "From": {
                            "Email": "%s",
                            "Name": "Rev Bazaar"
                          },
                          "To": [
                            {
                              "Email": "%s"
                            }
                          ],
                          "Subject": "%s",
                          "TextPart": "%s"
                        }
                      ]
                    }
                    """.formatted(
                    escapeJson(senderEmail),
                    escapeJson(userEmail),
                    escapeJson(subject),
                    escapeJson(text)
            );

            String credentials = apiKey + ":" + apiSecret;

            String encodedCredentials = Base64.getEncoder()
                    .encodeToString(
                            credentials.getBytes(StandardCharsets.UTF_8)
                    );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mailjet.com/v3.1/send"))
                    .header(
                            "Authorization",
                            "Basic " + encodedCredentials
                    )
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Mailjet status: " + response.statusCode()
            );

            System.out.println(
                    "Mailjet response: " + response.body()
            );

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

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
