import React, { use } from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import SearchMap from "@/components/search/SearchMap";
import RentsOverview from "@/components/search/RentsOverview";
import { useState } from "react";
import { Coordinates, MarkerType } from "@/types";
import { useEffect } from "react";
import { map } from "leaflet";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";

const index = () => {
  const { t } = useTranslation();
  const [mapInput, setMapInput] = useState<MarkerType[]>([]);
  const [selectedCar, setSelectedCar] = useState<MarkerType | null>(null);

  const addCars = (cars: MarkerType[]) => {
    setMapInput(cars);
  };

  const selectCar = (carId: number) => {
    console.log("selectCar", carId);
    setSelectedCar(mapInput.find((m) => m.id === carId) || null);
  };

  return (
    <>
      <main className="h-[100vh] flex justify-center items-center">
        <div className="w-4/5 h-4/5 flex flex-col bg-white p-10 rounded-2xl">
          <h1 className="text-5xl h-fit pb-6 font-medium">
            {t("search.RoutePlanner")}
          </h1>
          <div className="flex flex-grow gap-4">
            <div className=" w-9/12">
              <SearchMap
                markerList={mapInput}
                selectedCar={selectedCar}
                selectCar={selectCar}
              />
            </div>
            <div className="w-3/12">
              <RentsOverview
                addCars={addCars}
                selectedCar={selectedCar}
                selectCar={selectCar}
              />
            </div>
          </div>
        </div>
      </main>
    </>
  );
};

export const getServerSideProps = async (context: { locale: any }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  };
};

export default index;
