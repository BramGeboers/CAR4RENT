import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";
import { useEffect, useState } from "react";
import { Rent, Rental } from "@/types";
import RentalService from "@/services/RentalService";
import useSWR from "swr";
import Loading from "@/components/Loading";
import { Pagination } from "@mui/material";
import RentHistoryOverview from "@/components/rents/RentsHistoryOverview";
import RentService from "@/services/RentService";


const index: React.FC = () => {
  const { t } = useTranslation();
  const handleOverviewClick = (type: string) => {
    console.log(`Overview ${type} clicked`);
  };

  const [rents, setRents] = useState<Array<Rent>>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const getRentsAndCars = async () => {
    const response = await RentService.getAllRentsBilling(currentPage);
    const rentsRaw = await response.json();
    const { content: rents, totalPages } = rentsRaw; // Extracting rentals from content
    setTotalPages(totalPages);
    return { rents };
  };

  const {data, isLoading, error} = useSWR("/api/rentals", getRentsAndCars);

  useEffect(() => {
    getRentsAndCars().then(({ rents }) => {
      setRents(rents);
    });
  }, [currentPage]);


  if (isLoading || !rents) {
    return <Loading />;
  } else {
    return (
      <>
        {rents && (
          <RentHistoryOverview rents={rents} />
        )}
        <div className="w-full flex justify-center p-4 bg-gray-200 pb-10">
        {totalPages != 1 && (
          <Pagination count={totalPages} page={currentPage + 1} onChange={(event, page) => setCurrentPage(page - 1)} size="large" />
        )}
        </div>
      </>
    );
  }
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
