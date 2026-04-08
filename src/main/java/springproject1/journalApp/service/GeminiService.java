package springproject1.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springproject1.journalApp.dto.GeminiResponseDto;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.URL}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String checkGrammar(String journalText) {
        String prompt = "You are a grammar checker. Check the grammar of the following journal entry. "
                + " provide only the fully corrected version.\n\n"
                + journalText;

        // Build request body using plain Maps (no extra DTO classes needed)
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {

            String urlWithKey = apiUrl + "?key=" + apiKey;

            ResponseEntity<GeminiResponseDto> response = restTemplate.postForEntity(
                    urlWithKey, entity, GeminiResponseDto.class
            );
            GeminiResponseDto body = response.getBody();
            if (body != null
                    && body.getCandidates() != null
                    && !body.getCandidates().isEmpty()) {
                return body.getCandidates()
                        .get(0)
                        .getContent()
                        .getParts()
                        .get(0)
                        .getText();

            }
        } catch (Exception e) {
            System.out.println("reached here2");

            log.error("Gemini API call failed: {}", e.getMessage());
        }
        System.out.println("reached here3");

        return "Unable to process grammar check at this time.";
    }
}