import React, { useEffect, useRef, useState } from "react";
import RentOverview from "@/components/rents/RentOverview";
import RentNotificationOverview from "@/components/notifications/RentNotificationOverview";
import RentService from "@/services/RentService";
import NotificationService from "@/services/NotifcationService";
import { Rent, Notification } from "@/types";
import Loading from "@/components/Loading";
import useInterval from "use-interval";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { Pagination } from "@mui/material";
import useSWR from "swr";
import AdvancedRentSearch from "@/components/rents/AdvancedRentSearch";

const index: React.FC = () => {

  const [notifications, setNotifications] = useState<Array<Notification>>([]);
  const [rents, setRents] = useState<Array<Rent>>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [showAdvancedSearch, setShowAdvancedSearch] = useState(false);

  const searchRef = useRef<{ handleSubmit: () => void } | null>(null);

  const getRentsAndNotifications = async () => {
    const responses = await Promise.all([
      RentService.getAllRents(currentPage),
      NotificationService.getAllNotifications()
    ]);
    const [rentResponse, notificationResponse] = responses;
    const rentsReponse = await rentResponse.json();
    const { content: rents, totalPages } = rentsReponse
    setRents(rents);
    const notifications = await notificationResponse.json();
    setNotifications(notifications)
    return { rents, notifications};
  }
  
  const { data, isLoading, error } = useSWR("/rents", getRentsAndNotifications);

  if ( isLoading || !rents || !notifications) {
    return <Loading/>
    }
    else {

    return (
      <>
      <div className="bg-gray-200">
      <div className="max-w-[1340px] mx-auto py-10 px-8">
      {showAdvancedSearch && <AdvancedRentSearch ref={searchRef} page={currentPage} setTotalPages={setTotalPages} setRents={setRents} handleOpen={setShowAdvancedSearch} />}
        <div className="flex my-4">
          <button
            onClick={() => {
              getRentsAndNotifications().then(({ rents }) => {
                setRents(rents);
              });
              
              setShowAdvancedSearch(!showAdvancedSearch)}}
              className={ !showAdvancedSearch ? 'rounded-md font-semibold border-2 border-blue-400 bg-blue-400 border-blue-400" px-3 py-2 flex items-center justify-center text-lg hover:bg-white ease-in-out duration-200 cursor-pointer' : 'hidden'}
              >
            {showAdvancedSearch ? "Hide" : "Show"} Advanced Search
          </button>
        </div>
          {
            rents && (
              <RentOverview rents={rents} />
            )
          }
          {notifications.some(notification => notification.rent !== null) && (
            <RentNotificationOverview notifications={notifications} trigger={getRentsAndNotifications}/>
          )}
                    <div className="w-full flex justify-center p-4 bg-gray-200 pb-10">
                    {totalPages != 1 && (
                      <Pagination count={totalPages} page={currentPage + 1} onChange={(event, page) => setCurrentPage(page - 1)} size="large" />
                    )}
          </div>
          </div>
      </div>
      </>

    )
  }
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