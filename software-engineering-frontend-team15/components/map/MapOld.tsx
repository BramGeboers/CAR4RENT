import React, { useState, useEffect } from "react";
import "leaflet/dist/leaflet.css";

import { Icon, divIcon, point } from "leaflet";
import placeHolderImage from "@/images/placeholder.png";

interface MapElementProps {
  callback: (coordinates: { lat: number; long: number }) => void;
}

const customIcon = new Icon({
  iconUrl: placeHolderImage.src,
  iconSize: [38, 38], // size of the icon
});

const MapElement: React.FC<MapElementProps> = ({ callback }) => {
  const [position, setPosition] = useState<[number, number]>([51.505, -0.09]); // Initial position (London)

  useEffect(() => {
    const handleMapClick = (e: any) => {
      setPosition([e.latlng.lat, e.latlng.lng]); // Update position on click
      callback({ lat: e.latlng.lat, long: e.latlng.lng });
    };

    if (typeof window !== "undefined") {
      const L = require("leaflet"); // needed for the leaflet library to work

      const map = L.map("map").setView([51.505, -0.09], 13);

      map.on("click", handleMapClick);

      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution:
          '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      }).addTo(map);

      L.marker([51.5, -0.09], { icon: customIcon })
      .addTo(map).bindPopup("Position 1").openPopup();

      

      return () => {
        map.off("click", handleMapClick);
        map.remove();
      };
    }
  }, [callback]);

  return <div id="map" style={{ height: "100%", width: "100%" }} />;
};

export default MapElement;
