package com.fitness.ai_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.ai_service.model.Activity;
import com.fitness.ai_service.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponce = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {} ", aiResponce);
        return processAiResponce(activity, aiResponce);
    }

    public Recommendation processAiResponce(Activity activity, String aiResponse){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\n","")
                    .replaceAll("```","")
                    .replaceAll("   ", "")
                    .trim();
            log.info("PARSED RESPONSE FROM AI: {}", jsonContent);

            //Extract Analysis from the JSON
            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories:");

            //Extract the Improvements from the JSON
            List<String> improvements = exractImprovements(analysisJson.path("improvements"));

            //Extract the suggestions from the JSON
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));

            //Extract the safety from the JSON
            List<String> safety = extractSafety(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendations(fullAnalysis.toString())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now()).build();

        }catch (Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }

    }
    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }
    public List<String> exractImprovements(JsonNode improvmentsNode){
        List<String> improvments = new ArrayList<>();
        if(improvmentsNode.isArray()){
            improvmentsNode.forEach(improvment -> {
                String area = improvment.path("area").asText();
                String detail = improvment.path("recommendation").asText();
                improvments.add(String.format("%s: %s", area, detail));
            });
        }
        return improvments.isEmpty() ? Collections.singletonList("No specific improvements provided"): improvments;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion ->{
                String workout = suggestion.path("workout").asText();
                String detail = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s",workout,detail));
            });
        }
        return suggestions.isEmpty()? Collections.singletonList("No specific suggestions provided"): suggestions;
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safetyList = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(safe -> {
                safetyList.add(safe.asText());
            });
        }
        return safetyList.isEmpty()? Collections.singletonList("Follow general safety guidelines"): safetyList;
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendations("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(Arrays.asList("Always warm up before exercise","Stay hydrated", "Listen to your body"))
                .createdAt(LocalDateTime.now()).build();
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                {
                    "analysis" : {
                       "overall" : "Overall analysis here",
                       "pace": "Pace analysis here",
                       "heartRate" : "Heart rate analysis here",
                       "caloriesBurned" : "Calories analysis here"
                    },
                    "improvements" : {
                        {
                            "area" : "Area name",
                            "recommendation" : "Detailed recommendation",
                            
                        }
                    },
                    "suggestions": {
                        {
                            "workout" : "Workout  name",
                            "description" : "Detailed workout description"
                        }
                    },
                    "safety": [
                        "Safety point 1",
                        "Safety point 2"
                    ]
                }
                
                Analyze this activity:
                Activity Type: %s
                Duration: %d minutes
                Calories Burned: %d
                Additional Metrics: %s
                
                Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                Ensure the response follows the EXACT JSON format shown above.
                 
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
                );
    }

}
