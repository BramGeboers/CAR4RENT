import React, { useState } from "react";
import RentService from "@/services/RentService";
import { Rent, Car } from "@/types";
import { useTranslation } from "next-i18next";
import router, { useRouter } from "next/router";
import { sessionStorageService } from "@/services/sessionStorageService";
import Link from "next/link";
import { mutate } from "swr";

type Props = {
  rents: Array<Rent>;
};

const RentOverview: React.FC<Props> = ({ rents }: Props) => {
  const [searchEmail, setSearchEmail] = useState("");
  const [searchResult, setSearchResult] = useState<Array<Rent>>([]);
  const [confirm, setConfirm] = useState(true);
  const [errors, setErrors] = useState({
    linked: "",
  });
  const { t } = useTranslation();

  const handleSearch = async () => {
    if (!searchEmail.trim()) {
      setErrors({ linked: t("error.searchR") });
      return;
    }

    try {
      const result = await RentService.searchRentByEmail(searchEmail);
      setSearchResult(result);
      setErrors({ linked: "" });
    } catch (error) {
      console.log(error);
      setErrors({ linked: t("error.searchErr") });
    }
  };

  const handleCancel = async (rent: Rent, car: Car) => {
    try {
      const confirmDelete = window.confirm(
        t("cars.cancel?") +
          car.brand +
          " " +
          car.model +
          " " +
          car.licensePlate +
          " ?"
      );
      if (confirmDelete) {
        await RentService.cancelRent(rent.id);
        mutate("/rents");
      } else {
        console.log("Cancel operation cancelled by user.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  const isNew = (rent: Rent) => {
    return rent.startDate == null;
  };

  const isRunning = (rent: Rent) => {
    return rent.startDate != null && rent.endDate == null;
  };

  const isFinished = (rent: Rent) => {
    return rent.endDate != null && rent.payed == null;
  };

  const isPayed = (rent: Rent) => {
    return rent.payed == true;
  };

  const router = useRouter();
  const handleCheckout = (rentId: Number) => {
    router.push(`/rents/checkout/${rentId}`);
  };

  const handleCheckin = (rentId: Number) => {
    router.push(`/rents/checkin/${rentId}`);
  };

  return (
    <>
      <div
        className={
          !confirm
            ? "w-[100%] h-[100%] z-10 fixed bg-black bg-opacity-20 backdrop-blur-[2px]"
            : "hidden"
        }
      ></div>
      <div className="bg-gray-200">
        <div className="max-w-[1340px] justify-center mx-auto">
          <div className="pb-4">
            <input
              type="text"
              value={searchEmail}
              onChange={(e) => setSearchEmail(e.target.value)}
              placeholder={t("rent.searchPlaceH")}
              className="rounded-md py-2 px-3 border-gray-300 focus:outline-none focus:ring focus:border-blue-300 w-full sm:w-auto"
            />
            <button
              onClick={handleSearch}
              className="ml-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:bg-blue-600"
            >
              {t("rent.search")}
            </button>
          </div>
          <div className="p-5 bg-white rounded-xl">
            <table className="min-w-full rounded-md table-fixed">
              <thead className="bg-white border-b-[1px] border-gray-300">
                <tr>
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("cars.car")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("rent.start")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("rent.end")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("rent.city")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("rent.email")}
                  </th>
                  {["ADMIN", "RENTER"].includes(
                    sessionStorageService.getRole() as string
                  ) && (
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {/* {t("cars.checkout")} */}
                    </th>
                  )}
                  {["ADMIN", "OWNER"].includes(
                    sessionStorageService.getRole() as string
                  ) && (
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("cars.cancel")}
                    </th>
                  )}
                </tr>
              </thead>
              <tbody className="bg-white">
                {/*  */}
                {searchResult.length > 0
                  ? searchResult &&
                    searchResult
                      .filter((rent) => rent.active)
                      .map((rent: Rent) => (
                        <tr
                          key={rent.car.licensePlate}
                          className="even:bg-gray-100"
                        >
                          <td className="text-left px-6 py-4 ">
                            <div className="text-sm">
                              {rent.car?.brand +
                                " " +
                                rent.car?.model +
                                " " +
                                rent.car?.licensePlate}
                            </div>
                          </td>
                          <td className="text-left px-6 py-4 ">
                            <div className="text-sm">
                              {rent.rental?.startDate}
                            </div>
                          </td>
                          <td className="text-left px-6 py-4 ">
                            <div className="text-sm">
                              {rent.rental?.endDate}
                            </div>
                          </td>
                          <td className="text-left px-6 py-4 ">
                            <div className="text-sm">{rent.rental?.city}</div>
                          </td>
                          <td className="text-left px-6 py-4 ">
                            <div className="text-sm">{rent.rental?.email}</div>
                          </td>
                          <td>
                            <input
                              type="button"
                              value={t("cars.checkout")}
                              onClick={() => handleCheckout(rent.id)}
                              className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                            />
                          </td>
                          <td>
                            <input
                              type="button"
                              value={t("cars.cancel")}
                              onClick={() => handleCancel(rent, rent.car)}
                              className="rounded-md text-center bg-red-300 border-red-300 border-2 hover:bg-white my-4 h-[35px] w-[70px] flex items-end justify-center uppercase ease-in-out duration-200 cursor-pointer"
                            />
                          </td>
                        </tr>
                      ))
                  : rents &&
                    rents
                      .filter((rent) => rent.active)
                      .map((rent: Rent) => (
                        <tr
                          key={rent.car.licensePlate}
                          className="even:bg-gray-100"
                        >
                          <td className="text-left px-6 py-4 ">
                            <div className="text-sm">
                              {rent.car?.brand +
                                " " +
                                rent.car?.model +
                                " " +
                                rent.car?.licensePlate}
                            </div>
                          </td>
                          {rent.rental && (
                            <>
                              <td className="text-left px-6 py-4 ">
                                <div>{`${new Date(
                                  rent.rental.startDate
                                ).toLocaleTimeString("en-US", {
                                  hour: "2-digit",
                                  minute: "2-digit",
                                  hour12: false,
                                })} ${new Date(
                                  rent.rental.startDate
                                ).toLocaleDateString("en-US", {
                                  day: "2-digit",
                                  month: "2-digit",
                                  year: "numeric",
                                })}`}</div>
                              </td>
                              <td className="text-left px-6 py-4 ">
                                <div>{`${new Date(
                                  rent.rental.endDate
                                ).toLocaleTimeString("en-US", {
                                  hour: "2-digit",
                                  minute: "2-digit",
                                  hour12: false,
                                })} ${new Date(
                                  rent.rental.endDate
                                ).toLocaleDateString("en-US", {
                                  day: "2-digit",
                                  month: "2-digit",
                                  year: "numeric",
                                })}`}</div>
                              </td>
                              <td className="text-left px-6 py-4 ">
                                <div className="text-sm">
                                  {rent.rental?.city}
                                </div>
                              </td>

                              <td className="text-left px-6 py-4 ">
                                <div className="text-sm">
                                  {sessionStorageService.isRenter()
                                    ? rent.car.owner?.email
                                    : rent.renter?.email}
                                </div>
                              </td>
                            </>
                          )}
                          {["ADMIN", "RENTER"].includes(
                            sessionStorageService.getRole() as string
                          ) && (
                            <td>
                              {isNew(rent) && (
                                <input
                                  type="button"
                                  value={t("cars.checkin")}
                                  onClick={() => handleCheckin(rent.id)}
                                  className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                                />
                              )}
                              {isRunning(rent) && (
                                <input
                                  type="button"
                                  value={t("cars.checkout")}
                                  onClick={() => handleCheckout(rent.id)}
                                  className="rounded-md text-center bg-blue-300 border-orange-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                                />
                              )}
                              {isFinished(rent) && (
                                <Link
                                  href={`/rents/pay/${rent.id}`}
                                  className=" w-auto rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                                >
                                  {t("cars.pay")}
                                </Link>
                              )}
                              {isPayed(rent) && (
                                <input
                                  type="button"
                                  value={t("rent.payed")}
                                  disabled
                                  className="rounded-md text-center bg-green-300 border-green-300 border-2 mx-6 my-4 px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-not-allowed"
                                />
                              )}
                            </td>
                          )}
                          {["ADMIN", "OWNER"].includes(
                            sessionStorageService.getRole() as string
                          ) && (
                            <>
                              <td>
                                <input
                                  type="button"
                                  value={t("cars.cancel")}
                                  onClick={() => handleCancel(rent, rent.car)}
                                  className="rounded-md text-center bg-red-300 border-red-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                                />
                              </td>
                            </>
                          )}
                        </tr>
                      ))}
                {searchResult.length === 0 && rents.length === 0 && (
                  <tr>
                    <td colSpan={6} className="text-center py-4">
                      {t("rent.searchNoRes")}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
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

export default RentOverview;
