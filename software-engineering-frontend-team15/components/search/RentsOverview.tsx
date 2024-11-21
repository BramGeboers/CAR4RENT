import { SearchService } from "@/services/SearchService";
import {
  ClosestsResponse,
  Coordinates,
  MarkerType,
  PlannerResponse,
  Rental,
  Address,
} from "@/types";
import React, { useEffect, useRef } from "react";
import { useState } from "react";
import Rent from "./Rent";

import redCarIcon from "@/images/redCar.png";
import blueCarIcon from "@/images/blueCar.png";

import currentPostionIcon from "@/images/currentPosition.webp";
import swapIcon from "@/images/swap.png";
import { useTranslation } from "next-i18next";

type RentsOverviewProps = {
  addCars: (cars: MarkerType[]) => void;
  selectedCar: MarkerType | null;
  selectCar: (carId: number) => void;
};

const RentsOverview: React.FC<RentsOverviewProps> = ({
  addCars,
  selectCar,
  selectedCar,
}) => {
  const { t } = useTranslation();
  const [foundRentals, setFoundRentals] = useState<PlannerResponse[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [searchTerm2, setSearchTerm2] = useState("");
  const [cachedCoords, setCachedCoords] = useState<Record<string, Coordinates>>(
    {}
  );

  const [inputDate, setInputDate] = useState(new Date());

  const [loading, setLoading] = useState(false);
  const [firstSearch, setFirstSearch] = useState(true);
  const [showAdvancedSearch, setShowAdvancedSearch] = useState(false);

  const [showPrice, setShowPrice] = useState(false);

  const elementRef = useRef<HTMLDivElement>(null);

  const handleSearch = async (coordinates?: Coordinates) => {
    if (!searchTerm && !coordinates) return;
    setLoading(true);
    setFoundRentals([]);
    setFirstSearch(false);
    selectCar(0);
    addCars([]);

    let coords: Coordinates;
    let wait = false;
    if (coordinates) {
      coords = coordinates;
    } else {
      if (cachedCoords[searchTerm]) {
        coords = cachedCoords[searchTerm];
      } else {
        coords = await SearchService.getCoordsFromAddress(searchTerm);
        wait = true;
        if (coords) {
          setCachedCoords({ ...cachedCoords, [searchTerm]: coords });
        }
      }
    }

    if (!coords) {
      alert("No coordinates found for start address");
      setLoading(false);
      return;
    }

    let coords2: Coordinates | null = null;
    if (searchTerm2) {
      if (cachedCoords[searchTerm2]) {
        coords2 = cachedCoords[searchTerm2];
        setShowPrice(true);
      } else {
        if (wait) console.log("waiting for 1.2 seconds");
        if (wait) await new Promise((r) => setTimeout(r, 1200));
        coords2 = await SearchService.getCoordsFromAddress(searchTerm2);
        if (coords2) {
          setCachedCoords({ ...cachedCoords, [searchTerm2]: coords2 });
          setShowPrice(true);
        }
      }

      if (!coords2) {
        alert("No coordinates found for end address");
        setLoading(false);
        setShowPrice(false);
        return;
      }
    } else {
      setShowPrice(false);
    }

    const rentals: PlannerResponse[] = await SearchService.getRentals({
      start: coords,
      end: coords2 || coords, // if no end is given, use start as end
      date: inputDate.toISOString(),
    });

    setFoundRentals(rentals);
    addCars(
      rentals
        .map((r) => {
          const carPointer: MarkerType = {
            id: r.rental.id || -10,
            latitude: r.rental.latitude || 0,
            longitude: r.rental.longitude || 0,
            popup: r.rental.car.brand + " " + r.rental.car.model,
            icon: blueCarIcon.src,
            teleport: false,
            clickable: true,
          };
          return carPointer;
        })
        .concat([
          {
            id: -1,
            latitude: coords.latitude,
            longitude: coords.longitude,
            popup: "Your start location",
            teleport: true,
            clickable: false,
          },
        ])
        .concat(
          coords2
            ? [
                {
                  id: -2,
                  latitude: coords2.latitude,
                  longitude: coords2.longitude,
                  popup: "Your end location",
                  teleport: false,
                  clickable: false,
                },
              ]
            : []
        )
    );
    setLoading(false);
  };

  const getClientLocation = async () => {
    if (!navigator) {
      alert("Geolocation is not supported by this browser.");
      return;
    }
    if (!navigator.permissions) {
      alert("Geolocation is not supported by this browser.");
      return;
    }
    if (searchTerm === t("search.loading")) return;

    if (navigator.geolocation) {
      setSearchTerm(t("search.loading"));
      setLoading(true);
      navigator.geolocation.getCurrentPosition(async (position) => {
        handleSearch({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });

        const address: Address = await SearchService.getAddressesFromCoords({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
        if (!address) {
          setSearchTerm("");
          alert("No address found for these coordinates");
          return;
        }
        setSearchTerm(address.addressString);
      });
    } else {
      setLoading(false);
      alert("Geolocation is not supported by this browser.");
    }
  };

  useEffect(() => {
    if (elementRef.current) {
      elementRef.current.scrollIntoView({
        behavior: "smooth",
        block: "nearest",
        inline: "start",
      });
    }
  }, [selectedCar]);

  return (
    <div className="h-full flex flex-col">
      <h2 className="text-2xl font-bold text-[#1976d2] m-2 h-fit">
        {t("search.findNearbyRentals")}
      </h2>
      <div className="flex flex-col flex-grow h-0">
        <div className="grid grid-cols-8 ms-2">
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="col-span-7 p-2 border rounded-3xl "
            placeholder={t("search.currentInput")}
          />
          <img
            src={currentPostionIcon.src}
            alt="current position"
            className="col-span-1 cursor-pointer h-10 w-10"
            onClick={getClientLocation}
          />
        </div>

        {showAdvancedSearch && (
          <>
            <div className="grid grid-cols-8 ms-2 mt-2">
              <input
                type="text"
                value={searchTerm2}
                onChange={(e) => setSearchTerm2(e.target.value)}
                className="col-span-7 p-2 border rounded-3xl "
                placeholder={t("search.destinationInput")}
              />

              <img
                src={swapIcon.src}
                alt="swap positions"
                className="col-span-1 cursor-pointer h-10 w-10 p-2"
                onClick={() => {
                  setSearchTerm(searchTerm2);
                  setSearchTerm2(searchTerm);
                }}
              />
            </div>
            <div className="flex m-2 items-center">
              <label htmlFor="" className="mr-2">
                {t("search.date")}
              </label>
              <input
                name="travelDate"
                type="date"
                value={inputDate.toISOString().split("T")[0]}
                onChange={(e) =>
                  setInputDate(
                    e.target.value ? new Date(e.target.value) : new Date()
                  )
                }
                className="border-gray-400 border-2 rounded-md"
              />
            </div>
          </>
        )}
        <p
          className=" text-gray-600 ms-2 cursor-pointer"
          onClick={() => setShowAdvancedSearch(!showAdvancedSearch)}
        >
          {showAdvancedSearch
            ? `${t("search.closeAdvanced")} ↑`
            : `${t("search.showAdvanced")} ↓`}
        </p>

        <button
          onClick={() => handleSearch()}
          className="bg-[#1976d2] text-white rounded-3xl p-2 m-2"
        >
          {t("search.search")}
        </button>
        <div className="flex-grow overflow-auto ms-2">
          {!loading && foundRentals.length !== 0 && (
            <div className="flex flex-col">
              {foundRentals.map((plannerRes, index) => (
                <>
                  <div
                    key={index}
                    className="m-2 last-of-type:mb-[70%]"
                    onClick={() => selectCar(plannerRes.rental.id || 0)}
                    ref={
                      selectedCar?.id === plannerRes.rental.id
                        ? elementRef
                        : null
                    }
                  >
                    <Rent
                      plannerRes={plannerRes}
                      selected={selectedCar?.id === plannerRes.rental.id}
                      key={index}
                      showPrice={showPrice}
                    />
                  </div>
                </>
              ))}
            </div>
          )}
          {loading && <p>{t("search.loading")}</p>}
          {!loading && foundRentals.length === 0 && !firstSearch && (
            <p className="col-span-5">{t("search.noRentals")}</p>
          )}
          {firstSearch && !loading && (
            <p className="col-span-5">{t("search.firstSearch")}</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default RentsOverview;
