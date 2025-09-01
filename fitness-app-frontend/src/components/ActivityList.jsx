import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getActivities } from "../services/api";
import Grid2 from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();
  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  return (
    <>
      <Grid2 container spacing={2}>
        {activities.map((activity) => (
          <Grid2
            container
            spacing={{ xs: 2, md: 3 }}
            columns={{ xs: 4, sm: 8, md: 12 }}
          >
            <Card
              sx={{ cursor: "pointer" }}
              onClick={() => navigate(`/activities/${activity.id}`)}
            >
              <CardContent>
                <Typography variant="h6">
                  Activity Type: {activity.type}
                </Typography>
                <Typography variant="h6">
                  Duration: {activity.duration}
                </Typography>
                <Typography variant="h6">
                  Calories Burned: {activity.caloriesBurned}
                </Typography>
              </CardContent>
            </Card>
          </Grid2>
        ))}
      </Grid2>
    </>
  );
};

export default ActivityList;
