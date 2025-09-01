import { MenuItem } from "@mui/material";
import React, { useState } from "react";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import TextField from "@mui/material/TextField";
import { addActivity } from "../services/api";

const ActivityForm = ({ onActivityAdded }) => {
  const [activity, setActivity] = useState({
    type: "RUNNING",
    duration: "",
    caloriesBurn: "",
    startTime: "",
    additionalMetrics: {
      distance: "",
      averageSpeed: "",
      maxHeartRate: "",
    },
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addActivity(activity);
      onActivityAdded();
      setActivity({
        type: "RUNNING",
        duration: "",
        caloriesBurn: "",
        startTime: "",
        additionalMetrics: {
          distance: "",
          averageSpeed: "",
          maxHeartRate: "",
        },
      });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ mb: 4 }}>
      <FormControl fullWidth sx={{ mb: 2 }}>
        <InputLabel>Activity Type</InputLabel>
        <Select
          value={activity.type}
          onChange={(e) => setActivity({ ...activity, type: e.target.value })}
        >
          <MenuItem value="RUNNING">Running</MenuItem>
          <MenuItem value="WALKING">Walking</MenuItem>
          <MenuItem value="CYCLING">Cycling</MenuItem>
        </Select>
      </FormControl>
      <TextField
        fullWidth
        label="Duration (Minutes)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.duration}
        onChange={(e) => setActivity({ ...activity, duration: e.target.value })}
      />

      <TextField
        fullWidth
        label="Calories Burned"
        type="number"
        sx={{ mb: 2 }}
        value={activity.caloriesBurn}
        onChange={(e) =>
          setActivity({ ...activity, caloriesBurn: e.target.value })
        }
      />

      {/* Start Time */}
      <TextField
        fullWidth
        type="datetime-local"
        sx={{ mb: 2 }}
        value={activity.startTime || ""}
        onChange={(e) =>
          setActivity({ ...activity, startTime: e.target.value })
        }
      />

      <TextField
        fullWidth
        label="Distance (km)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.additionalMetrics?.distance || ""}
        onChange={(e) =>
          setActivity({
            ...activity,
            additionalMetrics: {
              ...activity.additionalMetrics,
              distance: parseFloat(e.target.value),
            },
          })
        }
      />
      <TextField
        fullWidth
        label="Average Speed (km/h)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.additionalMetrics?.averageSpeed || ""}
        onChange={(e) =>
          setActivity({
            ...activity,
            additionalMetrics: {
              ...activity.additionalMetrics,
              averageSpeed: parseFloat(e.target.value),
            },
          })
        }
      />
      <TextField
        fullWidth
        label="Max Heart Rate (bpm)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.additionalMetrics?.maxHeartRate || ""}
        onChange={(e) =>
          setActivity({
            ...activity,
            additionalMetrics: {
              ...activity.additionalMetrics,
              maxHeartRate: parseInt(e.target.value, 10),
            },
          })
        }
      />

      <Button type="submit" variant="contained">
        Add Activity
      </Button>
    </Box>
  );
};

export default ActivityForm;
