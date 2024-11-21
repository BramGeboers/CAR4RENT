import React from "react";
import dynamic from "next/dynamic";
import { MapProps } from "@/components/map/MapElement";

const MapElement = dynamic(() => import("@/components/map/MapElement"), {
  ssr: false,
});

const MapContainer: React.FC<MapProps> = (props) => {
  return <MapElement {...props} />;
};

export default MapContainer;
