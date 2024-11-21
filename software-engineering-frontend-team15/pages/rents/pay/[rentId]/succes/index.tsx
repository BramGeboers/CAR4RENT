import React, { useEffect, useState } from "react";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useRouter } from "next/router";
import Checkin from "@/components/rents/Checkin";
import Pay from "@/components/rents/Pay";
import RentService from "@/services/RentService";
import { Billing, Rent } from "@/types";
import Loading from "@/components/Loading";
import useSWR from "swr";
import Succes from "@/components/rents/Succes";
import BillingService from "@/services/BillingService";

const PayById = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const { rentId } = router.query;
  const [rent, setRent] = useState<Rent>();
  const [billing, setBilling] = useState<Billing>();


  const getRentAndBilling = async () => {
    const rentIdNumber = parseInt(rentId as string);

    const responses = await Promise.all([
      RentService.getRentById(rentIdNumber),
      BillingService.getPaymentById( rentIdNumber )
    ]);
    const [rentResponse, billingResponse] = responses;
    const rent = await rentResponse.json();
    setRent(rent);
    const billing = await billingResponse.json();
    setBilling(billing)
    return { rent, billing};
  }

  const { data, isLoading, error } = useSWR("/pay/succes", getRentAndBilling);
    return (
    <>{rent && billing &&
      <Succes rent={rent} billing={billing} />
    }
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

export default PayById;
