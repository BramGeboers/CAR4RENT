import React from "react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

const index = () => {
  return (
    <div>
      <h1 className="text-center text-[3rem] pt-[2rem]">
        Account created succesfully, please verify your email
      </h1>
    </div>
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

export default index;
