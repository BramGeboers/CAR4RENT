import React, { useEffect } from "react";
import { useState } from "react";
import router, { useRouter } from "next/router";
import Link from "next/link";
import CarService from "@/services/CarService";
import RentalService from "@/services/RentalService";

import { Car, Rental } from "@/types";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";

const addRental: React.FC = () => {
  const { t } = useTranslation();
  const [car, setCar] = useState<Car>();

  const [errors, setErrors] = useState({
    carId: "",
    startDate: "",
    endDate: "",
    city: "",
    street: "",
    postal: "",
    number: "",
    phoneNumber: "",
    // email: "",
    status: "",
  });

  const validate = (): boolean => {
    let result = true;

    let errors = {
      carId: "",
      startDate: "",
      endDate: "",
      city: "",
      street: "",
      postal: "",
      number: "",
      phoneNumber: "",
      // email: "",
      status: "",
    };

    if (!rental.startDate) {
      errors.startDate = t("error.sDRequired");
      result = false;
    } else if (new Date(rental.startDate) <= new Date()) {
      errors.startDate = t("error.sDFuture");
    } else if (rental.startDate > rental.endDate) {
      errors.startDate = t("error.sDBefore");
    }

    if (!rental.endDate) {
      errors.endDate = t("error.eDRequired");
      result = false;
    } else if (rental.startDate > rental.endDate) {
      errors.endDate = t("error.eDFuture");
    } else if (new Date(rental.endDate) <= new Date()) {
      errors.endDate = t("error.eDBefore");
    }

    if (!rental.city) {
      errors.city = t("error.cityR");
      result = false;
    }

    if (!rental.phoneNumber) {
      errors.phoneNumber = t("error.phoneNumberR");
      result = false;
    }

    car?.rentals?.forEach((rental) => {
      if (
        rental.startDate >= rental.startDate &&
        rental.startDate <= rental.endDate
      ) {
        new Date(rental.endDate);
        errors.endDate = t("error.eDOverlap", {
          startDate: rental.startDate,
          endDate: rental.endDate,
        });
      }
    });

    console.log(result);

    setErrors(errors);

    return result;
  };

  const router = useRouter();
  const { carId } = router.query;

  const [rental, setRental] = useState({
    startDate: "",
    endDate: "",
    city: "",
    status: "available",
    street: "",
    postal: 0,
    number: 0,
    phoneNumber: 0,
    // email: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await CarService.getCarById(carId as string);
        const _car = await response.json();
        setCar(_car);
      } catch (error) {
        console.log(error);
      }
    };
    if (carId) {
      fetchData();
    }
  }, [carId]);

  const handleChange = (event: any) => {
    const value = event.target.value;
    setRental({ ...rental, [event.target.name]: value });
  };

  const addRental = async (e: any) => {
    e.preventDefault();

    if (!validate()) {
      return;
    }

    try {
      const response = await RentalService.addRentalToCar(
        carId as string,
        rental as unknown as Rental
      );

      if (!response.ok) {
        console.log(response.status);
      }
      if (response.ok) {
        router.push("/cars");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <div className="bg-gray-200">
        <div className=" flex max-w-[1140px] justify-center py-20 px-8 mx-auto">
          <div className="flex flex-col flex-wrap md:w-1/2 max-w-[600px] justify-between bg-white p-5 rounded-xl">
            <h1 className="text-4xl">Add 4 Rental</h1>
            <div className="bg-gray-200 mt-6 p-2 rounded-xl">
              <h2 className="text-2xl">{t("cars.car")}</h2>
              <p className="pt-1">
                {car?.brand} {car?.model}
              </p>
              <p className="pt-1">{car?.licensePlate}</p>
            </div>
            <form>
              <h2 className="text-xl pt-4">{t("rent.period")}</h2>
              <label htmlFor="" className="pb-2">
                {t("rent.startDate")}
              </label>
              <input
                name="startDate"
                type="datetime-local"
                value={rental.startDate}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.startDate && (
                <p className="text-red-600 text-sm pb-2">{errors.startDate}</p>
              )}

              <label htmlFor="" className="pb-2 pt-4">
                End Date
              </label>
              <input
                name="endDate"
                type="datetime-local"
                value={rental.endDate}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.endDate && (
                <p className="text-red-600 text-sm pb-2">{errors.endDate}</p>
              )}

              <h2 className="text-xl pt-4">{t("rent.pickupPoint")}</h2>

              <label htmlFor="" className="pb-2 pt-4">
                {t("rent.street")}
              </label>
              <input
                name="street"
                type="text"
                value={rental.street}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.street && (
                <p className="text-red-600 text-sm pb-2">{errors.street}</p>
              )}

              <label htmlFor="" className="pb-2 pt-4">
                {t("rent.number")}
              </label>
              <input
                name="number"
                type="number"
                value={rental.number}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.number && (
                <p className="text-red-600 text-sm pb-2">{errors.number}</p>
              )}

              <label htmlFor="" className="pb-2 pt-4">
                {t("rent.postal")}
              </label>
              <input
                name="postal"
                type="number"
                value={rental.postal}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.postal && (
                <p className="text-red-600 text-sm pb-2">{errors.postal}</p>
              )}

              <label htmlFor="" className="pb-2 pt-4">
                {t("rent.city")}
              </label>
              <input
                name="city"
                type="text"
                value={rental.city}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.city && (
                <p className="text-red-600 text-sm pb-2">{errors.city}</p>
              )}

              <h2 className="text-xl pt-2">{t("rent.contact")}</h2>

              <label className="pb-2 pt-4 pb">{t("rent.phoneNumber")}</label>
              <input
                name="phoneNumber"
                type="tel"
                value={rental.phoneNumber}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />

              {errors.phoneNumber && (
                <p className="text-red-600 text-sm pb-2">
                  {errors.phoneNumber}
                </p>
              )}

              {/* <label className="pb-2 pt-4">{t("rent.email")}</label>
              <input
                name="email"
                type="email"
                value={rental.email}
                onChange={(e) => handleChange(e)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              /> */}

              {/* {errors.email && (
                <p className="text-red-600 text-sm pb-2">{errors.email}</p>
              )} */}

              <div className="flex flex-row">
                <Link
                  href="/cars"
                  className="mr-4 rounded-xl mt-8 font-semibold bg-gray-400 border-gray-400 border-2 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
                >
                  {t("cars.cancel")}
                </Link>
                <input
                  type="submit"
                  onClick={addRental}
                  value={t("rent.rent")}
                  className="rounded-xl mt-8 font-semibold bg-blue-400 border-blue-400 border-2 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
                />
              </div>
            </form>
          </div>
        </div>
      </div>
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

export default addRental;
