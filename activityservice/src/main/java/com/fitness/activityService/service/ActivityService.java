package com.fitness.activityService.service;

import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponce;
import com.fitness.activityService.model.Activity;
import com.fitness.activityService.repository.ActivityRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRespository activityRepository;
    private final UserValidateService userValidateService;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponce trackActivity(ActivityRequest request) {

        //Here before we will going to store the user in the database, we have to validate the user exist or not, by calling the user-service
        boolean isValidUser = userValidateService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid User: " + request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurn())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepository.save(activity);
        // Publish to RabbitMQ for AI processing
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        }catch (Exception e){
            log.error("Failed to publish activity to RabbitMQ: ", e);
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponce mapToResponse(Activity savedActivity) {
        return ActivityResponce.builder()
                .id(savedActivity.getId())
                .userId(savedActivity.getUserId())
                .type(savedActivity.getType())
                .duration(savedActivity.getDuration())
                .caloriesBurned(savedActivity.getCaloriesBurned())
                .startTime(savedActivity.getStartTime())
                .createdAt(savedActivity.getCreatedAt())
                .updatedAt(savedActivity.getUpdatedAt())
                .additionalMetrics(savedActivity.getAdditionalMetrics())
                .build();
    }

    public List<ActivityResponce> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponce getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(()-> new RuntimeException("No Activity Found For The ID"));
        return mapToResponse(activity);
    }
}
