import React, { useState, useEffect } from "react";
import Link from "next/link";
import CarService from "@/services/CarService";
import { Car, Rent, Rental } from "@/types";
import RentService from "@/services/RentService";
import RentalService from "@/services/RentalService";
import { useTranslation } from "next-i18next";
import { sessionStorageService } from "@/services/sessionStorageService";
import { mutate } from "swr";

type Props = {
  rentals: Array<Rental>;
};

const CarList: React.FC<Props> = ({ rentals }: Props) => {
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const { t } = useTranslation();
  const [deleteErrorMessage, setDeleteErrorMessage] = useState("");
  const [errors, setErrors] = useState({
    linked: "",
  });

  const [isOwner, setIsOwner] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    if (sessionStorageService.isLoggedIn()) {
      setIsOwner(
        sessionStorageService.getItem("role") === "OWNER" ||
          sessionStorageService.getItem("role") === "ADMIN"
      );
    }
  }, []);

  useEffect(() => {
    setIsLoggedIn(sessionStorageService.isLoggedIn() ? true : false);
  });

  const handleDelete = async (rental: Rental) => {
    try {
      // const rentals = await RentService.getAllRentsOfCar(rental);
      const confirmDelete = window.confirm(
        "Do you want to delete rental of\n" +
          rental.car.brand +
          " " +
          rental.car.model +
          " " +
          rental.car.licensePlate +
          " ?"
      );
      if (confirmDelete) {
        console.log("Deleting rental with id: ", rental.id);
        console.log(rental);
        if (!rental.rent) {
          const res = await RentalService.deleteRental(rental.id!);
          if (res.ok) {
            setDeleteErrorMessage(" ");
            mutate("/api/rentals");
          }
          if (res.status === 400) {
            const error = await res.json();
            setDeleteErrorMessage(error["error"]);
          }
        } else {
          console.log("Car with active rents can not be deleted !");
          setDeleteErrorMessage("Car with active rents cannot be deleted !");
        }
      }
    } catch (error: any | undefined) {
      setErrors(error.message);
    }
  };

  return (
    <>
      <div className="bg-gray-200">
        <div className="max-w-[1340px] justify-center mx-auto">
          <div className="p-5 bg-white rounded-xl">
            <table className="min-w-full rounded-md table-fixed">
              <thead className="bg-white border-b-[1px] border-gray-300">
                <tr>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("cars.brand")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("cars.model")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("cars.licensePlate")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("rent.period")}
                  </th>

                  {isLoggedIn && (
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("general.rent")}
                    </th>
                  )}
                  {isOwner && (
                    <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                      {t("cars.cancel")}
                    </th>
                  )}
                </tr>
              </thead>
              <tbody className="bg-white">
                {rentals &&
                  rentals.length > 0 &&
                  rentals.map((rental: Rental) => (
                    <tr key={rental.id} className="even:bg-gray-100">
                      <td className="text-left px-6 py-4 ">
                        <div className="text-sm">{rental.car.brand}</div>
                      </td>
                      <td className="text-left px-6 py-4 ">
                        <div className="text-sm">{rental.car.model}</div>
                      </td>
                      <td className="text-left px-6 py-4 ">
                        <div className="text-sm">{rental.car.licensePlate}</div>
                      </td>

                      <td className="pt-4 pb-2">
                        <div
                          className="flex flex-row p-2 items-center"
                          key={rental.id}
                        >
                          <div className="flex flex-row px-4">
                            <div>{`${new Date(
                              rental.startDate
                            ).toLocaleTimeString("en-US", {
                              hour: "2-digit",
                              minute: "2-digit",
                              hour12: false,
                            })} ${new Date(rental.startDate).toLocaleDateString(
                              "en-US",
                              {
                                day: "2-digit",
                                month: "2-digit",
                                year: "numeric",
                              }
                            )} - ${new Date(rental.endDate).toLocaleTimeString(
                              "en-US",
                              {
                                hour: "2-digit",
                                minute: "2-digit",
                                hour12: false,
                              }
                            )} ${new Date(rental.endDate).toLocaleDateString(
                              "en-US",
                              {
                                day: "2-digit",
                                month: "2-digit",
                                year: "numeric",
                              }
                            )}`}</div>
                          </div>
                        </div>
                      </td>

                      {isLoggedIn && (
                        <td>
                          <Link
                            className="px-6 py-4"
                            href={`/rentals/add/${rental.id}`}
                          >
                            <input
                              type="button"
                              value={t("general.rent")}
                              className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                            />
                          </Link>
                        </td>
                      )}

                      {isOwner && (
                        <td>
                          {rental.car.owner?.email ==
                            sessionStorageService.getItem("email") && (
                            <input
                              type="button"
                              value={t("cars.cancel")}
                              onClick={() => handleDelete(rental)}
                              className="rounded-md text-center bg-red-300 border-red-300 border-2 mx-6 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                            />
                          )}
                        </td>
                      )}
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
          {deleteErrorMessage && (
            <p className="text-red-600 font-semibold">{deleteErrorMessage}</p>
          )}
          {errors.linked && (
            <p className="text-red-600 mt-2 text-md font-semibold pb-2">
              {errors.linked}
            </p>
          )}
        </div>
      </div>
    </>
  );
};

export default CarList;
