import React from "react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import ComplaintForm from "@/components/complaints/complaintForm";


const index: React.FC = () => {

    return (
      <>
          <ComplaintForm />
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