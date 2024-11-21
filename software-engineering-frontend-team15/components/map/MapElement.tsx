import React, { useState, useEffect } from "react";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  useMapEvents,
} from "react-leaflet";
import { LeafletMouseEvent, LatLng } from "leaflet";
import "leaflet/dist/leaflet.css";

import { Icon, divIcon, point } from "leaflet";
import placeHolderImage from "@/images/placeholder.png";
import { MarkerType } from "@/types";

import redCarIcon from "@/images/redCar.png";

const customIcon = new Icon({
  iconUrl: placeHolderImage.src,
  iconSize: [38, 38], // size of the icon
});

export type MapProps = {
  callback?: (coordinates: { lat: number; long: number }) => void;
  markerList?: MarkerType[];
  selectedCar?: MarkerType | null;
  selectCar?: (carId: number) => void;
};

const MapElement: React.FC<MapProps> = ({
  callback,
  markerList,
  selectedCar,
  selectCar,
}) => {
  const [position, setPosition] = useState<LatLng | null>(null);

  function LocationMarker() {
    // const map = useMapEvents({
    //   //move marker to click
    //   click(e) {
    //     if (!callback) return; //if no callback is given, do nothing
    //     // map.locate();
    //     console.log(e.latlng);
    //     setPosition(e.latlng);
    //     callback({ lat: e.latlng.lat, long: e.latlng.lng });
    //   },
    //   // click() {
    //   //   setPosition(map.getCenter());

    //   // },

    //   locationfound(e) {
    //     setPosition(e.latlng);
    //     map.flyTo(e.latlng, map.getZoom());
    //   },
    // });
    const map = useMapEvents({});
    const teleportMarker = markerList?.find((marker) => marker.teleport);

    if (selectedCar) {
      map.flyTo([selectedCar.latitude, selectedCar.longitude], 13);
    } else if (teleportMarker) {
      map.flyTo([teleportMarker.latitude, teleportMarker.longitude], 13);
    }

    return (
      <>
        {markerList &&
          markerList.map((marker, index) => {
            return (
              <Marker
                key={index}
                position={[marker.latitude, marker.longitude]}
                icon={
                  new Icon({
                    iconUrl:
                      marker.id !== (selectedCar?.id || 0)
                        ? marker.icon || placeHolderImage.src
                        : redCarIcon.src,
                    iconSize: [38, 38],
                  })
                }
                eventHandlers={{
                  click: () => {
                    marker.clickable && marker.onClick?.();
                    if (selectCar && marker.clickable) {
                      selectCar(marker.id);
                    }
                  },
                }}
              >
                <Popup>{marker.popup}</Popup>
              </Marker>
            );
          })}
        {position === null ? null : (
          <Marker position={position} icon={customIcon}>
            <Popup>Selected Position</Popup>
          </Marker>
        )}
      </>
    );
  }

  return (
    <MapContainer
      style={{ height: "100%", width: "100%" }}
      center={[51.505, -0.09]}
      zoom={13}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <LocationMarker />
      {/* <Marker position={position} icon={customIcon}>
        <Popup>
          A pretty CSS3 popup. <br /> Easily customizable.
        </Popup>
      </Marker> */}
    </MapContainer>
  );
};

export default MapElement;
