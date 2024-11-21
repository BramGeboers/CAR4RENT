import React, { useState } from "react";
import Navbar from "@/components/Navbar";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import RentService from "@/services/RentService";
import { useRouter } from "next/router";
import { Rent } from "@/types";
import Link from "next/link";

interface CheckoutProps {
  rent: Rent;
}

const Checkout: React.FC<CheckoutProps> = ({ rent }) => {
  const { t } = useTranslation();

  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const response = await RentService.payRent( rent.id );

      if (response.ok) {
        router.push(`${router.asPath}/succes`)
      }
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
            <div className="flex flex-col">
            <h3 className="text-xl font-semibold pb-2">Betaal de rekening</h3>
            <p> {t("rent.amount")}: $ { rent.rental &&  rent.rental?.car?.pricePerKm && ((rent.endMileage - rent.startMileage) * rent.rental.car.pricePerKm + (rent.startFuel - rent.endFuel) * 1.75 + 5.99) }
            </p>
            </div>
            <div className="flex flex-row gap-2">
            <button
              type="submit"
              className="rounded-xl mt-4 font-semibold bg-blue-400 border-2 border-blue-400 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
            >
              {t("cars.pay")}
            </button>
            <Link href={"/history"}
              className="rounded-xl mt-4 font-semibold bg-gray-400 border-2 border-gray-400 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
            >
              {t("cars.cancel")}
            </Link>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default Checkout;
