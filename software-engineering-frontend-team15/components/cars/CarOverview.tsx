import React, { useState, useEffect, useRef } from "react";
import Link from "next/link";
import CarService from "@/services/CarService";
import { Car, Info } from "@/types";
import { RxCheckCircled } from "react-icons/rx";
import { RxCrossCircled } from "react-icons/rx";
import RentService from "@/services/RentService";
import { useTranslation } from "next-i18next";
import { Carrois_Gothic } from "next/font/google";
import { Pagination } from "@mui/material";
import AdvancedCarSearch from "./AdvancedCarSearch";

const CarList: React.FC = () => {
  const { t } = useTranslation();
  const searchRef = useRef<{ handleSubmit: () => void } | null>(null);
  const [cars, setCars] = useState<Array<Car>>([]);
  const [loading, setLoading] = useState(true);
  const [deleteErrorMessage, setDeleteErrorMessage] = useState("");
  const [totalPages, setTotalPages] = useState(0);
  const [errors, setErrors] = useState({ linked: "" });

  const [showAdvancedSearch, setShowAdvancedSearch] = useState(false);

  const [currentPage, setCurrentPage] = useState(0);

  const fetchData = async (page: number) => {
    setLoading(true);
    try {
      const response = await CarService.getAllCars(page);
      const carsResponse = await response.json();
      const { content: cars, totalPages } = carsResponse;
      setCars(cars);
      setTotalPages(totalPages);
    } catch (error) {
      console.error("Error fetching data:", error);
      setErrors({ ...errors, linked: "Error fetching data" });
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (car: Car) => {
    const confirmDelete = window.confirm(
      "Do you want to delete car\n" +
        car.brand +
        " " +
        car.model +
        " with license plate " +
        car.licensePlate +
        " ?"
    );
    if (!confirmDelete) {
      return;
    }
    const res = await CarService.deleteCar(car.id);
    if (res.ok) {
      fetchData(currentPage);
    } else if (res.status === 400) {
      const error = await res.json();
      setDeleteErrorMessage(error["error"]);
    } else {
      setDeleteErrorMessage("Error deleting car");
    }
  };

  useEffect(() => {
    if (!showAdvancedSearch) {
      fetchData(currentPage);
    }

    //rerender AdvancedCarSearch if currentPage changes
    searchRef.current?.handleSubmit();


  }, [currentPage]);

 
  return (
    <>
      <div className="bg-gray-200">

        <div className="max-w-[1340px] justify-center py-10 px-8 mx-auto">
          {showAdvancedSearch && <AdvancedCarSearch ref={searchRef} page={currentPage} setTotalPages={setTotalPages} setCars={setCars} handleOpen={setShowAdvancedSearch} />}
        <div className="my-4">
        <input
          type="button"
          onClick={() => {
            fetchData(currentPage);
            setShowAdvancedSearch(!showAdvancedSearch)}}
          value={showAdvancedSearch?t("cars.close"):t("cars.advancedsearch")}
          className={ !showAdvancedSearch ? 'rounded-md font-semibold border-2 border-blue-400 bg-blue-400 border-blue-400" px-3 py-2 flex items-center justify-center text-lg hover:bg-white ease-in-out duration-200 cursor-pointer' : 'hidden'}
        />
        </div>
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
                    {t("cars.addRental")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("cars.delete")}
                  </th>
                </tr>
              </thead>
              {!loading && (
                <tbody className="bg-white">
                  {cars &&
                    cars.length > 0 &&
                    cars?.map((car: Car) => (
                      <tr key={car.id} className="even:bg-gray-100">
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{car.brand}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{car.model}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{car.licensePlate}</div>
                        </td>
                        <td>
                          <Link
                            className="px-6 py-4 "
                            href={`/cars/rental/${car.id}`}
                          >
                            <input
                              type="button"
                              value={t("cars.rental")}
                              className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                            />
                          </Link>
                        </td>

                        <td>
                          <input
                            type="button"
                            value={t("cars.delete")}
                            onClick={() => handleDelete(car)}
                            className="rounded-md text-center bg-red-300 border-red-300 border-2 mx-6 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                          />
                        </td>
                      </tr>
                    ))}
                </tbody>
              )}
            </table>
          </div>
          {deleteErrorMessage && (
            <p className="text-red-600 font-semibold">{deleteErrorMessage}</p>
          )}
          <div className="w-full flex justify-center p-4">
            <Pagination
              count={totalPages}
              page={currentPage + 1}
              onChange={(event, page) => setCurrentPage(page - 1)}
              size="large"
            />
          </div>
          <Link
            href="/cars/add"
            className="rounded-xl font-semibold border-blue-400 border-2 bg-blue-400 w-[150px] h-[55px] flex items-center justify-center text-lg hover:bg-white  ease-in-out duration-200 cursor-pointer"
          >
            {t("cars.addCar")}
          </Link>

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
