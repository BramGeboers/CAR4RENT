import React from "react";
import AddCar from "@/components/cars/AddCar";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";


const index: React.FC = () => {

    return (
      <>
          <AddCar />
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