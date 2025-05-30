import React, { useState } from "react";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useRouter } from "next/router";
import Checkout from "@/components/rents/Checkout";

const CheckoutById = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const { rentId } = router.query;

  const rentIdValue = typeof rentId === "string" ? rentId : "";

  return (
    <>
      <Checkout rentId={rentIdValue} />
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

export default CheckoutById;
