import React, { useEffect, useState } from "react";
import { useParams } from "react-router";
import { getActivityDetail } from "../services/api";
import { Box, Card, CardContent, Divider, Typography } from '@mui/material';

function ActivityDetail() {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [rec, setRec] = useState(null);

  useEffect(() => {
    async function fetchActivityDetail() {
      try {
        const { data } = await getActivityDetail(id);

        // If API returns an activity with an embedded recommendation:
        //   { ..., recommendation: { recommendations, improvements, ... } }
        // OR it returns the recommendation object itself (like your sample)
        const recommendationObj = data?.recommendation ? data.recommendation : data;

        setActivity(data?.activity || data?.activityDto || null); // optional if provided
        setRec(recommendationObj || null);
      } catch (err) {
        console.error(err);
      }
    }
    fetchActivityDetail();
  }, [id]);

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>
      {/* Activity card shown only if you actually have activity fields */}
      {activity && (
        <Card sx={{ mb: 2 }}>
          <CardContent>
            <Typography variant="h5" gutterBottom>Activity Details</Typography>
            {activity.type && <Typography>Type: {activity.type}</Typography>}
            {activity.duration != null && <Typography>Duration: {activity.duration} minutes</Typography>}
            {/* Use the correct key your backend uses: caloriesBurn vs caloriesBurned */}
            {activity.caloriesBurn != null && <Typography>Calories Burned: {activity.caloriesBurn}</Typography>}
            {activity.createdAt && (
              <Typography>Date: {new Date(activity.createdAt).toLocaleString()}</Typography>
            )}
            {activity.additionalMetrics && (
              <>
                <Divider sx={{ my: 2 }} />
                <Typography variant="h6">Metrics</Typography>
                <Typography>Distance: {activity.additionalMetrics.distance} km</Typography>
                <Typography>Average Speed: {activity.additionalMetrics.averageSpeed} km/h</Typography>
                <Typography>Max HR: {activity.additionalMetrics.maxHeartRate} bpm</Typography>
              </>
            )}
          </CardContent>
        </Card>
      )}

      {/* Recommendation card */}
      {rec && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>AI Recommendation</Typography>

            {/* Your payload uses "recommendations" (string) */}
            {rec.recommendations && (
              <>
                <Typography variant="h6">Analysis</Typography>
                <Typography paragraph>{rec.recommendations}</Typography>
                <Divider sx={{ my: 2 }} />
              </>
            )}

            {Array.isArray(rec.improvements) && rec.improvements.length > 0 && (
              <>
                <Typography variant="h6">Improvements</Typography>
                {rec.improvements.map((item, i) => (
                  <Typography key={i} paragraph>• {item}</Typography>
                ))}
                <Divider sx={{ my: 2 }} />
              </>
            )}

            {Array.isArray(rec.suggestions) && rec.suggestions.length > 0 && (
              <>
                <Typography variant="h6">Suggestions</Typography>
                {rec.suggestions.map((item, i) => (
                  <Typography key={i} paragraph>• {item}</Typography>
                ))}
                <Divider sx={{ my: 2 }} />
              </>
            )}

            {Array.isArray(rec.safety) && rec.safety.length > 0 && (
              <>
                <Typography variant="h6">Safety Guidelines</Typography>
                {rec.safety.map((item, i) => (
                  <Typography key={i} paragraph>• {item}</Typography>
                ))}
              </>
            )}
          </CardContent>
        </Card>
      )}

      {!activity && !rec && <Typography>Loading...</Typography>}
    </Box>
  );
}

export default ActivityDetail;
