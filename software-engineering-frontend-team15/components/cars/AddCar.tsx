import React, { useEffect } from "react";
import { useState } from "react";
import Link from "next/link";
import router from "next/router";
import CarService from "@/services/CarService";
import { Car } from "@/types";
import { useTranslation } from "next-i18next";

const CarAdd = () => {
  const { t } = useTranslation();
  const [inputValue, setInputValue] = useState<string>("");
  const handleBoolean1 = (event: any) => {
    const value = event.target.value;
    setCar({ ...car, [event.target.name]: !car.foldingRearSeat });
    console.log(value);
  };
  const handleBoolean2 = (event: any) => {
    const value = event.target.value;
    setCar({ ...car, [event.target.name]: !car.towBar });
    console.log(value);
  };
  const [errors, setErrors] = useState({
    brand: "",
    model: "",
    types: "",
    licensePlate: "",
    numberOfSeats: "",
    numberOfChildSeats: "",
    foldingRearSeat: "",
    towBar: "",
  });
  const [car, setCar] = useState({
    brand: "",
    model: "",
    types: "",
    licensePlate: "",
    numberOfSeats: 0,
    numberOfChildSeats: 0,
    foldingRearSeat: false,
    towBar: false,
  });
  const handleChange = (event: any) => {
    const value = event.target.value;
    setCar({ ...car, [event.target.name]: value });
  };
  const validate = (): boolean => {
    let result = true;
    let errors = {
      brand: "",
      model: "",
      types: "",
      licensePlate: "",
      numberOfSeats: "",
      numberOfChildSeats: "",
      foldingRearSeat: "",
      towBar: "",
    };

    if (!car.brand) {
      errors.brand = "Brand is required";
    } else if (car.brand.length < 3) {
      errors.brand = "Too short";
      console.log(errors.brand);
      result = false;
    }

    if (!car.types) {
      errors.types = "Type is required";
    }
    if (!car.licensePlate) {
      errors.licensePlate = "License plate is required";
    }
    if (car.numberOfSeats == 0) {
      errors.numberOfSeats = "Number of seats is required";
    }

    setErrors(errors);
    return result;
  };

  const saveCar = async (e: any) => {
    e.preventDefault();
    if (!validate()) {
      return;
    }

    try {
      const response = await CarService.addCar(car as Car);
      console.log(response.json());

      if (!response.ok) {
        console.log(response.status); // Will show you the status
      }
      if (response.ok) {
        router.push("/cars");
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleInputChange = (
    event: React.ChangeEvent<{}>,
    newInputValue: string
  ) => {
    setInputValue(newInputValue);
  };

  return (
    <>
      <div className="bg-gray-200">
        <div className=" flex max-w-[1140px] justify-center py-20 px-8 mx-auto">
          <form className="flex flex-col flex-wrap md:w-1/2 max-w-[600px] justify-between bg-white p-5 rounded-xl">
            <label htmlFor="" className="pb-2 pt-4">
              {t("cars.brand")}
            </label>
            <input
              name="brand"
              type="text"
              value={car.brand}
              onChange={(e) => handleChange(e)}
              className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
            />

            {errors.brand && (
              <p className="text-red-600 text-sm pb-2">{errors.brand}</p>
            )}

            <label htmlFor="" className="pb-2 pt-4">
              {t("cars.model")}
            </label>
            <input
              name="model"
              type="text"
              value={car.model}
              onChange={(e) => handleChange(e)}
              className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
            />

            {errors.model && (
              <p className="text-red-600 text-sm pb-2">{errors.model}</p>
            )}

            <label htmlFor="" className="pb-2 pt-4">
              {t("cars.types")}
            </label>
            <input
              name="types"
              type="text"
              value={car.types}
              onChange={(e) => handleChange(e)}
              className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
            />

            {errors.types && (
              <p className="text-red-600 text-sm pb-2">{errors.types}</p>
            )}

            <label htmlFor="" className="pb-2 pt-4">
              {t("cars.licensePlate")}
            </label>
            <input
              name="licensePlate"
              type="text"
              value={car.licensePlate}
              onChange={(e) => handleChange(e)}
              className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
            />

            {errors.licensePlate && (
              <p className="text-red-600 text-sm pb-2">{errors.licensePlate}</p>
            )}
            <label className="pb-2 pt-4">{t("cars.numberOfSeats")}</label>
            <input
              name="numberOfSeats"
              type="number"
              min={0}
              value={car.numberOfSeats}
              onChange={(e) => handleChange(e)}
              className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
            />
            {errors.numberOfSeats && (
              <p className="text-red-600 text-sm pb-2">
                {errors.numberOfSeats}
              </p>
            )}
            <label className="pb-2 pt-4">{t("cars.numberOfChildSeats")}</label>
            <input
              name="numberOfChildSeats"
              type="number"
              min={0}
              value={car.numberOfChildSeats}
              onChange={(e) => handleChange(e)}
              className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
            />
            {errors.numberOfChildSeats && (
              <p className="text-red-600 text-sm pb-2">
                {errors.numberOfChildSeats}
              </p>
            )}
            <label className="pt-4 pb-1">{t("cars.foldingRearSeat")}</label>
            <input
              name="foldingRearSeat"
              type="checkbox"
              onChange={(e) => handleBoolean1(e)}
              className="w-5 h-5 mt-[2px]"
            />

            <label className="pt-4 pb-1">{t("cars.towBar")}</label>
            <input
              name="towBar"
              type="checkbox"
              onChange={(e) => handleBoolean2(e)}
              className="w-5 h-5 mt-[2px]"
            />

            <div className="flex flex-row">
              <Link
                href="/cars"
                className="mr-4 rounded-xl mt-8 font-semibold bg-gray-400 border-2 border-gray-400 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
              >
                {t("cars.cancel")}
              </Link>
              <input
                type="submit"
                onClick={saveCar}
                value={t("cars.addCar")}
                className="rounded-xl mt-8 font-semibold bg-blue-400 border-2 border-blue-400  w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
              />
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default CarAdd;
