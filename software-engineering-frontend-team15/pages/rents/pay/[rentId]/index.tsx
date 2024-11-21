import React, { useEffect, useState } from "react";
import { useTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useRouter } from "next/router";
import Checkin from "@/components/rents/Checkin";
import Pay from "@/components/rents/Pay";
import RentService from "@/services/RentService";
import { Rent } from "@/types";
import Loading from "@/components/Loading";
import useSWR from "swr";

const PayById = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const { rentId } = router.query;
  const [rent, setRent] = useState<Rent>();

  useEffect(() => {
    const rentIdNumber = parseInt(rentId as string);
    const fetchData = async () => {
      try {
        const response = await RentService.getRentById(rentIdNumber);
        const rentalJson = await response.json();
        setRent(rentalJson);
      } catch (error) {
        console.log(error);
      }
    };
    if (rentIdNumber) {
      fetchData();
    }
  }, [rentId]);


    return (
    <>{rent &&
      <Pay rent={rent} />
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
