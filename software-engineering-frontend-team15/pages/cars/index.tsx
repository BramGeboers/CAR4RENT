import React from "react";
import CarOverview from "@/components/cars/CarOverview";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

const index: React.FC = () => {

    return (
      <>
          <CarOverview />
      </>
    )
  }

export const getServerSideProps = async (context: { locale: any; }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  }
}
  
export default index