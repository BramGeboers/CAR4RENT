import React, { use, useEffect, useRef, useState } from "react";
import RentalOverview from "@/components/rentals/RentalOverview";
import RentService from "@/services/RentService";
import { Car, Rent, Rental } from "@/types";
import Loading from "@/components/Loading";
import useInterval from "use-interval";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import RentalService from "@/services/RentalService";
import { Pagination } from "@mui/material";
import useSWR from "swr";
import AdvancedRentalSearch from "@/components/rentals/AdvancedRentalSearch";

const index: React.FC = () => {
  const [rentals, setRentals] = useState<Array<Rental>>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [showAdvancedSearch, setShowAdvancedSearch] = useState(false);

  const searchRef = useRef<{ handleSubmit: () => void } | null>(null);

  const getRentsAndCars = async () => {
    console.log("Fetching rentals");
    const response = await RentalService.getAllRentals(currentPage);
    const rentalsRaw = await response.json();
    const { content: rentals, totalPages } = rentalsRaw; // Extracting rentals from content
    setTotalPages(totalPages);
    setRentals(rentals);
    return { rentals };
  };

  const {data, isLoading, error} = useSWR("/api/rentals", getRentsAndCars);

  useEffect(() => {
    getRentsAndCars();
  }, [currentPage]);


  if (isLoading && !rentals) {
    return <Loading />;
  } else {
    return (
      <>
      <div className="bg-gray-200">
      <div className="max-w-[1340px] justify-center py-5 px-8 mx-auto">
      {showAdvancedSearch && <AdvancedRentalSearch ref={searchRef} page={currentPage} setTotalPages={setTotalPages} setRentals={setRentals} handleOpen={setShowAdvancedSearch} />}
        <div className="flex my-4">
          <button
            onClick={() => {
              getRentsAndCars().then(({ rentals }) => {
                setRentals(rentals);
              });
              
              setShowAdvancedSearch(!showAdvancedSearch)}}
              className={ !showAdvancedSearch ? 'rounded-md font-semibold border-2 border-blue-400 bg-blue-400 border-blue-400" px-3 py-2 flex items-center justify-center text-lg hover:bg-white ease-in-out duration-200 cursor-pointer' : 'hidden'}
              >
            {showAdvancedSearch ? "Hide" : "Show"} Advanced Search
          </button>
        </div>
        {rentals && <RentalOverview rentals={rentals} />}
        <div className="w-full flex justify-center p-4 bg-gray-200 pb-10">
          <Pagination
            count={totalPages}
            page={currentPage + 1}
            onChange={(event, page) => setCurrentPage(page - 1)}
            size="large"
          />
        </div>
        </div>
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
