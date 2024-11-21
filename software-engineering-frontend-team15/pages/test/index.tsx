import React, { useState } from "react";
import NotificationOverview from "@/components/notifications/NotificationOverview";
import MapContainer from "@/components/map/MapContainer";

type Coordinates = {
  lat: number;
  long: number;
};

const index = () => {
  const [coordinates, setCoordinates] = useState<Coordinates>({
    lat: 0,
    long: 0,
  });

  return (
    <div>
      <div className="w-screen h-screen80">
        <MapContainer callback={setCoordinates} />
      </div>

      <p>lat: {coordinates.lat}</p>
      <p>long: {coordinates.long}</p>
    </div>
  );
};

export default index;
