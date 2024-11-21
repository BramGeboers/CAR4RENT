import React from "react";
import MapContainer from "../map/MapContainer";
import { MapProps } from "../map/MapElement";

const SearchMap: React.FC<MapProps> = (props: MapProps) => {
  return (
    <div className="h-full w-full rounded-3xl overflow-hidden">
      <MapContainer {...props} />
    </div>
  );
};

export default SearchMap;
