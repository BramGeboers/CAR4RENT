import React, { useState } from "react";
import RentService from "@/services/RentService";
import { Rent, Car, Billing } from "@/types";
import { useTranslation } from "next-i18next";
import router, { useRouter } from "next/router";
import { sessionStorageService } from "@/services/sessionStorageService";
import { CiCircleCheck } from "react-icons/ci";
import { CiCircleRemove } from "react-icons/ci";
import image1 from "@/public/large.png";
import BillingService from "@/services/BillingService";
import jsPDF from "jspdf";
import "@/public/fonts/Inter-Bold-normal.js";
import "@/public/fonts/Inter-Regular-normal.js";

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

  const handleInvoice = async (rentId: number) => {
    const responses = await Promise.all([
      BillingService.getPaymentById(rentId),
      RentService.getRentById(rentId),
    ]);
    const [billingResponse, rentResponse] = responses;
    const rent: Rent = await rentResponse.json();
    const billing: Billing = await billingResponse.json();
    const doc = new jsPDF("portrait", "pt", "A4"); // Portrait orientation, A4 size

    if (!billingResponse.ok && rentResponse.ok && rent.renter) {
      //logo invoice rechts boven
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");
      doc.text("INVOICE#TBD", 545, 50 + 11, { align: "right" });
      doc.text("ISSUE DATE", 545, 67 + 11, { align: "right" });
      const billingDate = new Date();
      const formattedBillingDate =
        ("0" + billingDate.getDate()).slice(-2) +
        "/" +
        ("0" + (billingDate.getMonth() + 1)).slice(-2) +
        "/" +
        billingDate.getFullYear();

      doc.text(formattedBillingDate, 545, 80 + 11, { align: "right" });
      //sectie 1
      doc.setFontSize(12);
      doc.setFont("Inter-Bold", "normal");
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(68, 266, 178, 266); // Draw a line from (50, 149) to (160, 149)
      doc.text("BILL TO", 73, 275 + 12);
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");
      doc.text(rent.renter?.email, 73, 297 + 11);
      doc.text(rent.phoneNumber, 73, 312 + 11);
      doc.text(rent.nationalIdentificationNumber, 73, 327 + 11);

      //sectie 2
      doc.setFontSize(12);
      doc.setFont("Inter-Bold", "normal");
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(245, 266, 355, 266); // Draw a line from (50, 149) to (160, 149)
      doc.text("RENTAL", 250, 275 + 12);
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");

      // Parse and format start date
      const startDate = new Date(rent.startDate);
      const formattedStartDate =
        ("0" + startDate.getDate()).slice(-2) +
        "/" +
        ("0" + (startDate.getMonth() + 1)).slice(-2) +
        "/" +
        startDate.getFullYear();

      // Parse and format end date
      const endDate = new Date(rent.endDate);
      const formattedEndDate =
        ("0" + endDate.getDate()).slice(-2) +
        "/" +
        ("0" + (endDate.getMonth() + 1)).slice(-2) +
        "/" +
        endDate.getFullYear();

      doc.text("FROM " + formattedStartDate, 250, 297 + 11);
      doc.text("UNTIL " + formattedEndDate, 250, 312 + 11);

      //sectie 3
      doc.setFontSize(12);
      doc.setFont("Inter-Bold", "normal");
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(417, 266, 527, 266); // Draw a line from (50, 149) to (160, 149)
      doc.text("PAYMENT", 422, 275 + 12);
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");
      doc.text("Pending", 422, 297 + 11);
      doc.text("Est.", 422, 312 + 11);

      //lijn onder items
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(50, 708, 545, 708); // Draw a line from (50, 149) to (160, 149)

      //total
      doc.setFontSize(14);
      doc.setFont("Inter-Bold", "normal");
      doc.text("Total Due", 68, 723 + 14);
      doc.text("TBD", 527, 723 + 14, { align: "right" });
    } else {
      //sectie 1
      doc.setFontSize(12);
      doc.setFont("Inter-Bold", "normal");
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(68, 266, 178, 266); // Draw a line from (50, 149) to (160, 149)
      doc.text("BILL TO", 73, 275 + 12);
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");
      doc.text(billing.user.email, 73, 297 + 11);
      doc.text(billing.rent.phoneNumber, 73, 312 + 11);
      doc.text(billing.rent.nationalIdentificationNumber, 73, 327 + 11);

      //sectie 2
      doc.setFontSize(12);
      doc.setFont("Inter-Bold", "normal");
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(245, 266, 355, 266); // Draw a line from (50, 149) to (160, 149)
      doc.text("RENTAL", 250, 275 + 12);
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");

      // Parse and format start date
      const startDate = new Date(billing.rent.startDate);
      const formattedStartDate =
        ("0" + startDate.getDate()).slice(-2) +
        "/" +
        ("0" + (startDate.getMonth() + 1)).slice(-2) +
        "/" +
        startDate.getFullYear();

      // Parse and format end date
      const endDate = new Date(billing.rent.endDate);
      const formattedEndDate =
        ("0" + endDate.getDate()).slice(-2) +
        "/" +
        ("0" + (endDate.getMonth() + 1)).slice(-2) +
        "/" +
        endDate.getFullYear();

          //sectie 3
            doc.setFontSize(12);
            doc.setFont("Inter-Bold","normal");
            doc.setLineWidth(1); // Set line thickness to 1pt
            doc.line(417, 266, 527, 266); // Draw a line from (50, 149) to (160, 149)
            doc.text("PAYMENT", 422, 275+12);
            doc.setFontSize(11);
            doc.setFont("Inter-Regular","normal");
            doc.text(billing.rent.payed ? "Complete" : "Pending", 422, 297+11);
            doc.text("$"+billing.cost.toFixed(2).toString(), 422, 312+11);
      doc.text("FROM " + formattedStartDate, 250, 297 + 11);
      doc.text("UNTIL " + formattedEndDate, 250, 312 + 11);

      //sectie 3
      doc.setFontSize(12);
      doc.setFont("Inter-Bold", "normal");
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(417, 266, 527, 266); // Draw a line from (50, 149) to (160, 149)
      doc.text("PAYMENT", 422, 275 + 12);
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");
      doc.text(billing.rent.payed ? "Complete" : "Pending", 422, 297 + 11);
      doc.text("$"+billing.cost.toFixed(2).toString(), 422, 312 + 11);

      //row1
      doc.setFontSize(11);
      doc.setFont("Inter-Regular", "normal");
      doc.text("Service Cost", 68, 426 + 11);
      doc.text("1", 349, 426 + 11);
      doc.text("$5.99", 422, 426 + 11, { align: "center" });
      doc.text("$5.99", 527, 426 + 11, { align: "right" });

      // Use pricePerKm in calculations and formatting

      //row2
      doc.text("Cost per mile", 68, 450 + 11);
      var mileDifference = billing.rent.endMileage - billing.rent.startMileage;
      var roundedMileDifference = Math.round(mileDifference * 100) / 100; // Round to two decimal places
      doc.text(roundedMileDifference.toString(), 349, 450 + 11);
      const pricePerKm = billing.rent.car?.pricePerKm || 0;

      doc.text("$" + pricePerKm.toFixed(2).toString(), 422, 450 + 11, {
        align: "center",
      });
      doc.text(
        "$" +
          (pricePerKm * (billing.rent.endMileage - billing.rent.startMileage))
            .toFixed(2)
            .toString(),
        527,
        450 + 11,
        { align: "right" }
      );

      //row3
      doc.text("Cost fuel", 68, 474 + 11);
      var fuelDifference = billing.rent.startFuel - billing.rent.endFuel;
      var roundedFuelDifference = Math.round(fuelDifference * 100) / 100; // Round to two decimal places
      doc.text(roundedFuelDifference.toString(), 349, 474 + 11);
      doc.text("$1.75", 422, 474 + 11, { align: "center" });
      doc.text(
        "$" +
          (1.75 * (billing.rent.startFuel - billing.rent.endFuel))
            .toFixed(2)
            .toString(),
        527,
        474 + 11,
        { align: "right" }
      );

      //lijn onder items
      doc.setLineWidth(1); // Set line thickness to 1pt
      doc.line(50, 708, 545, 708); // Draw a line from (50, 149) to (160, 149)

      //total
      doc.setFontSize(14);
      doc.setFont("Inter-Bold", "normal");
      doc.text("Total Due", 68, 723 + 14);
      doc.text("$" + billing.cost.toFixed(2).toString(), 527, 723 + 14, {
        align: "right",
      });
    }

    doc.setFontSize(12); // Set the font size
    doc.setFont("Inter-Regular"); // set custom font

    const imgUrl = image1.src;
    const img = new Image();
    img.src = imgUrl;
    doc.addImage(img, "png", 50, 41, 130, 80);

    //slogan
    doc.setFontSize(16);

    doc.setFont("Inter-Bold", "normal");
    doc.text("Car 4 rent", 50, 189 + 16);
    doc.setFont("Inter-Regular", "normal");

    doc.text("The best place to rent a car!", 50, 208 + 16);

    //lijn onder logo
    doc.setLineWidth(1); // Set line thickness to 1pt
    doc.line(50, 149, 545, 149); // Draw a line from (50, 149) to (160, 149)

    //lijn onder secties
    doc.setLineWidth(1); // Set line thickness to 1pt
    doc.line(50, 363, 545, 363); // Draw a line from (50, 149) to (160, 149)

    //thead
    doc.setFontSize(12);
    doc.setFont("Inter-Bold", "normal");
    doc.text("ITEM", 68, 377 + 12);
    doc.text("QTY", 349, 377 + 12);
    doc.text("PRICE", 422, 377 + 12, { align: "center" });
    doc.text("AMOUNT", 527, 377 + 12, { align: "right" });

    //lijn onder thead
    doc.setLineWidth(1); // Set line thickness to 1pt
    doc.line(50, 407, 545, 407); // Draw a line from (50, 149) to (160, 149)

    // Footer
    doc.setFontSize(8); // Set footer text size
    doc.setFont("Inter-Regular", "normal");
    doc.setTextColor("#A6A6A6"); // Set footer text color
    doc.text("CAR4RENT", 50, 790 + 8);
    doc.text(
      "Bram Geboers  |  Loveleen Sidhu  |  Axel Jacobs  |  Torben Ombelets  |  Wout Paepen  |  Rein Van Wanseele",
      106,
      790 + 8
    );
    doc.text("Page 1", 520, 790 + 8);

    // Save PDF
    doc.save("invoice.pdf");
  };

  // Function to format date in 'YYYY-MM-DD' format
  const formatDate = (date: string) => {
    const d = new Date(date);
    const year = d.getFullYear();
    let month = "" + (d.getMonth() + 1);
    let day = "" + d.getDate();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  };

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

  const isNew = (rent: Rent) => {
    return rent.startDate == null;
  };

  const isRunning = (rent: Rent) => {
    return rent.startDate != null && rent.endDate == null;
  };

  const isFinished = (rent: Rent) => {
    return rent.endDate != null;
  };

  const router = useRouter();
  const handleCheckout = (rentId: Number) => {
    router.push(`/rents/pay/${rentId}`);
  };

  const getPrice = (rent: Rent) => {
    if (!rent.rental) return "0.00";
    if (!rent.rental?.car?.pricePerKm) return "0.00";

    const value =
      (rent.endMileage - rent.startMileage) * rent.rental.car.pricePerKm +
      (rent.startFuel - rent.endFuel) * 1.75 +
      5.99;
    return value.toFixed(2).toString();
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
        <div className="max-w-[1340px] justify-center pt-10 px-8 mx-auto">
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
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("rent.payed")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                    {t("rent.amount")}
                  </th>
                  {["ADMIN", "RENTER"].includes(
                    sessionStorageService.getRole() as string
                  ) && (
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("cars.pay")}
                    </th>
                  )}
                  {["ADMIN", "ACCOUNTANT"].includes(
                    sessionStorageService.getRole() as string
                  ) && (
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("cars.invoice")}
                    </th>
                  )}
                </tr>
              </thead>
              <tbody className="bg-white">
                {/*  */}
                {searchResult.length > 0
                  ? searchResult &&
                    searchResult
                      .filter(
                        (rent) => rent.rental !== null && rent.active == true
                      )
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
                              value={t("cars.pay")}
                              onClick={() => handleCheckout(rent.id)}
                              className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                            />
                          </td>
                        </tr>
                      ))
                  : rents &&
                    rents
                      .filter(
                        (rent) => rent.rental !== null && rent.active == true
                      )
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
                          <td className="text-left px-6 py-4 ">
                            {rent.payed ? (
                              <CiCircleCheck size={28} color="green" />
                            ) : (
                              <CiCircleRemove size={28} color="red" />
                            )}
                          </td>
                          {isFinished(rent) && (
                            <>
                              <td className="text-left px-6 py-4 ">
                                $ {getPrice(rent)}
                              </td>
                              {rent.payed ? (
                                <td>
                                  <input
                                    type="button"
                                    value={t("rent.payed")}
                                    className="rounded-md text-center bg-green-300 border-green-300 border-2 mx-6 my-4 px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-not-allowed"
                                  />
                                </td>
                              ) : (
                                <td>
                                  <input
                                    type="button"
                                    value={t("cars.pay")}
                                    onClick={() => handleCheckout(rent.id)}
                                    className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                                  />
                                </td>
                              )}
                            </>
                          )}
                          {!isFinished(rent) && (
                            <>
                              <td className="text-left px-6 py-4 ">
                                {t("rent.active")}
                              </td>

                              <td>
                                <input
                                  type="button"
                                  value={t("cars.pay")}
                                  disabled
                                  className="rounded-md text-center bg-gray-300 border-gray-300 border-2 mx-6 my-4  px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-not-allowed"
                                />
                              </td>
                            </>
                          )}

                          {rent.payed != true ? (
                            <td>
                              <input
                                type="button"
                                onClick={() => handleInvoice(rent.id)}
                                value={t("cars.invoice")}
                                disabled
                                className="rounded-md text-center bg-gray-300 border-gray-300 border-2 mx-6 my-4  px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-not-allowed"
                              />
                            </td>
                          ) : (
                            <td>
                              <input
                                type="button"
                                onClick={() => handleInvoice(rent.id)}
                                value={t("cars.invoice")}
                                className="rounded-md text-center bg-blue-300 border-blue-300 border-2 mx-6 my-4 hover:bg-white px-3 py-2 flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                              />
                            </td>
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
