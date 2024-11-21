import React from "react";
import { useTranslation } from "next-i18next";
import { Billing, Rent } from "@/types";
import image1 from "@/public/large.png";
import { jsPDF } from "jspdf";
import "@/public/fonts/Inter-Bold-normal.js";
import "@/public/fonts/Inter-Regular-normal.js";
import Link from "next/link";

interface CheckoutProps {
  rent: Rent;
  billing: Billing;
}

const Checkout: React.FC<CheckoutProps> = ({ billing }) => {
  const { t } = useTranslation();

  const handlePdf = () => {
    const doc = new jsPDF("portrait", "pt", "A4"); // Portrait orientation, A4 size

    doc.setFontSize(12); // Set the font size
    doc.setFont("Inter-Regular"); // set custom font

    const imgUrl = image1.src;
    const img = new Image();
    img.src = imgUrl;
    doc.addImage(img, "png", 50, 41, 130, 80);

    //logo invoice rechts boven
    doc.setFontSize(11);
    doc.setFont("Inter-Regular", "normal");
    doc.text("INVOICE#" + billing.id, 545, 50 + 11, { align: "right" });
    doc.text("ISSUE DATE", 545, 67 + 11, { align: "right" });
    const billingDate = new Date(billing.date);
    const formattedBillingDate =
      ("0" + billingDate.getDate()).slice(-2) +
      "/" +
      ("0" + (billingDate.getMonth() + 1)).slice(-2) +
      "/" +
      billingDate.getFullYear();

    doc.text(formattedBillingDate, 545, 80 + 11, { align: "right" });

    //slogan
    doc.setFontSize(16);

    doc.setFont("Inter-Bold", "normal");
    doc.text("Car 4 rent", 50, 189 + 16);
    doc.setFont("Inter-Regular", "normal");

    doc.text("The best place to rent a car!", 50, 208 + 16);

    //lijn onder logo
    doc.setLineWidth(1); // Set line thickness to 1pt
    doc.line(50, 149, 545, 149); // Draw a line from (50, 149) to (160, 149)

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

    doc.text("FROM " + formattedStartDate, 250, 297 + 11);
    doc.text("UNTIL " + formattedEndDate, 250, 312 + 11);

    //sectie 3
    doc.setFontSize(12);
    doc.setFont("Inter-Bold", "normal");
    doc.setLineWidth(1); // Set line thickness to 1pt
    doc.line(417, 266, 527, 266); // Draw a line from (50, 149) to (160, 149)
    doc.text("PAYMENT", 422, 275 + 12);
    doc.setFontSize(11);
    doc.setFont("Inter-Regular","normal");
    doc.text(billing.rent.payed ? "Complete" : "Pending", 422, 297+11);
    doc.text("$"+billing.cost.toFixed(2).toString(), 422, 312+11);

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

  return (
    <>
      <div className="bg-gray-200">
        <div className="flex max-w-[1140px] justify-center py-20 px-8 mx-auto">
          <div className="flex flex-col md:w-1/2 max-w-[400px] bg-white p-5 rounded-xl">
            <h3 className="text-xl font-semibold pb-2">
              {t("billing.succes")}
            </h3>
            <div className="flex flex-col gap-1">
              <p>${billing.cost}</p>
              <p>{billing.user.email}</p>
              <p>{billing.date}</p>
            </div>
            <div className="flex flex-row gap-4 justify-around">
              <div
                className="rounded-md text-center font-semibold bg-blue-300 border-blue-300 border-2 mt-3 flex hover:bg-white px-3 py-2 uppercase ease-in-out duration-200 cursor-pointer"
                onClick={handlePdf}
              >
                {t("billing.invoiceD")}
              </div>
              <Link
                className="rounded-md text-center font-semibold bg-gray-300 border-gray-300 border-2 mt-3 flex hover:bg-white px-3 py-2 uppercase ease-in-out duration-200 cursor-pointer"
                href="/rents"
              >
                {t("general.return")}
              </Link>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Checkout;
