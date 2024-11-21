import React, { useState } from "react";
import Navbar from "@/components/Navbar";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import RentService from "@/services/RentService";
import { useRouter } from "next/router";

interface CheckinProps {
  rentId: string;
}

const Checkin: React.FC<CheckinProps> = ({ rentId }) => {
  const { t } = useTranslation();

  const router = useRouter();

  const [mileage, setMileage] = useState<number>(0);
  const [fuelLevel, setFuelLevel] = useState<number>(0);
  const [errors, setErrors] = useState<{ mileage: string; fuelLevel: string }>({
    mileage: "",
    fuelLevel: "",
  });

  const handleMileageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMileage(Number(e.target.value));
    setErrors({ ...errors, mileage: "" });
  };

  const handleFuelLevelChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFuelLevel(Number(e.target.value));
    setErrors({ ...errors, fuelLevel: "" });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (mileage === 0 || fuelLevel === 0) {
      setErrors({
        mileage: mileage === 0 ? t("error.mileageReq") : "",
        fuelLevel: fuelLevel === 0 ? t("error.fuelReq") : "",
      });
      return;
    }

    if (mileage < 0 || fuelLevel < 0) {
      setErrors({
        mileage: mileage < 0 ? t("error.mileageNeg") : "",
        fuelLevel: fuelLevel < 0 ? t("error.fuelNeg") : "",
      });
      return;
    }

    try {
      const response = await RentService.rentCheckin({
        rentId: Number(rentId),
        mileage: mileage,
        fuelLevel: fuelLevel,
      });

      router.push("/rents");
    } catch (error) {
        alert("Error");
    }
  };

  return (
    <>
      <div className="bg-gray-200">
        <div className="flex max-w-[1140px] justify-center py-20 px-8 mx-auto">
          <form
            onSubmit={handleSubmit}
            className="flex flex-col flex-wrap md:w-1/2 max-w-[600px] justify-between bg-white p-5 rounded-xl"
          >
            <div className="flex flex-col mb-4">
              <label htmlFor="mileage" className="pb-2 pt-4">
                {t("cars.mileage")}
              </label>
              <input
                type="number"
                id="mileage"
                name="mileage"
                value={mileage}
                onChange={handleMileageChange}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />
              {errors.mileage && (
                <p className="text-red-600 text-sm pb-2">{errors.mileage}</p>
              )}
            </div>
            <div className="flex flex-col mb-4">
              <label htmlFor="fuelLevel" className="pb-2 pt-4">
                {t("cars.fuelLevel")}
              </label>
              <input
                type="number"
                id="fuelLevel"
                name="fuelLevel"
                value={fuelLevel}
                onChange={handleFuelLevelChange}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />
              {errors.fuelLevel && (
                <p className="text-red-600 text-sm pb-2">{errors.fuelLevel}</p>
              )}
            </div>
            <button
              type="submit"
              className="rounded-xl mt-8 font-semibold bg-blue-400 border-2 border-blue-400 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
            >
              {t("cars.submit")}
            </button>
          </form>
        </div>
      </div>
    </>
  );
};

export default Checkin;
