import { PlannerResponse } from "@/types";
import React, { useState } from "react";
import infoIcon from "@/images/info.png";
import { useTranslation } from "next-i18next";
import { sessionStorageService } from "@/services/sessionStorageService";
import Link from "next/link";

type RentProps = {
  plannerRes: PlannerResponse;
  selected: boolean;
  showPrice: boolean;
};

const Rent: React.FC<RentProps> = ({ plannerRes, selected, showPrice }) => {
  const { t } = useTranslation();
  const [showDetails, setShowDetails] = useState(false);
  return (
    <div
      className={
        "border-2 w-full rounded-xl p-1 " +
        (selected ? "bg-gray-600 text-white" : "")
      }
    >
      <h1>
        {plannerRes.rental.car.brand} {plannerRes.rental.car.model}
      </h1>
      <p>
        {plannerRes.rental.street} {plannerRes.rental.number},{" "}
        {plannerRes.rental.postal} {plannerRes.rental.city}
      </p>
      <div className="flex justify-start gap-6">
        <p>{Math.round(plannerRes.distance * 10) / 10} km</p>
        <div className="relative flex gap-1 items-center flex-grow">
          {showPrice && (
            <p className=" whitespace-nowrap">
              ~ €{plannerRes.estimatedPrice.totalCost?.toFixed(2)}
            </p>
          )}
          {showPrice && (
            <img
              src={infoIcon.src}
              alt="info"
              className={"h-4 w-4 " + (selected ? "filter invert" : "")}
              onMouseEnter={() => setShowDetails(true)}
              onMouseLeave={() => setShowDetails(false)}
            />
          )}
          {showDetails && (
            <div className="absolute top-6 left-16 p-4 w-44 shadow-md bg-white rounded-xl text-black z-10 border-gray-200 border-2 overflow-hidden shadow-black">
              <p>
                {t("search.fixed")}: €
                {plannerRes.estimatedPrice.fixedCost?.toFixed(2)}
              </p>
              <p>
                {t("search.distance")}: €
                {plannerRes.estimatedPrice.distanceCost?.toFixed(2)}*
              </p>
              <p>
                {t("search.fuel")}: €
                {plannerRes.estimatedPrice.fuelCost?.toFixed(2)}*
              </p>
              <p className=" break-words mt-1 text-gray-600">
                * {t("search.differ")}
              </p>
            </div>
          )}
        </div>
        {sessionStorageService.isLoggedIn() && (
          <div className="flex-grow text-right">
            <Link
              className="text-right hover:underline hover:cursor-pointer pr-2"
              href={"/rentals/add/" + plannerRes.rental.id}
            >
              {t("search.rent")} ►
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

export default Rent;
