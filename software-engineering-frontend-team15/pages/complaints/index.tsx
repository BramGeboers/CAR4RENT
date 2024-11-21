import React from "react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import ComplaintOverview from "@/components/complaints/complaintOverview";

const index: React.FC = () => {

    return (
      <>
          <ComplaintOverview />
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