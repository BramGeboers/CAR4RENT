import React, { useState, useEffect } from "react";
import Link from "next/link";
import { Notification } from "@/types";
import notificationService from "@/services/NotifcationService";
import RentService from "@/services/RentService";
import router from "next/router";
import { useTranslation } from "next-i18next";
import { sessionStorageService } from "@/services/sessionStorageService";

type Props = {
  notifications: Array<Notification>;
  trigger: () => void;
};

const RentNotificationOverview: React.FC<Props> = ({
  notifications,
  trigger,
}: Props) => {
  const { t } = useTranslation();
  const downloadICSFile = (rentId: number) => {
    const notification = notifications.find(notification => notification.rent?.id === rentId);
    if (!notification) return;
    const formatDate = (dateString: string | undefined) => {
      const date = new Date(dateString ?? "");
      return date.toISOString().replace(/[:-]/g, "").replace(/\.\d{3}Z/, "Z");
    };
  
    const eventStart = formatDate(notification.rent.rental?.startDate);
    const eventEnd = formatDate(notification.rent.rental?.endDate);
    const icsContent = `BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//Software Engineering//Rental App//EN
BEGIN:VEVENT
SUMMARY:${notification.car.brand} ${notification.car.model} ${t("calendar.rentedOut")}
DTSTART:${eventStart}
DTEND:${eventEnd}
END:VEVENT
END:VCALENDAR`;

    const blob = new Blob([icsContent], { type: "text/calendar" });
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "rental_event.ics");
    document.body.appendChild(link);
    link.click();

    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  };

  const confirmRent = async (rentId: number) => {
    try {
      const response = await RentService.addRentToCar(rentId as number);

      if (!response.ok) {
        alert("Error");
      }
      if (response.ok) {
        trigger();
        const confirmed = window.confirm(t("calendar.confirm"));
        if (confirmed) {
          downloadICSFile(rentId);
        }
      }
    } catch (error) {
      console.log(error);
    }
  };

  const cancelRentRequest = async (rentId: number) => {
    try {
      const response = await RentService.cancelRentRequest(rentId as number);

      if (!response.ok) {
        alert("Error");
      }
      if (response.ok) {
        trigger();
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <div className="bg-gray-200">
        <div className="max-w-[1340px] justify-center pt-20 px-8 mx-auto">
          <div className="p-5 bg-white rounded-xl">
            <table className="min-w-full rounded-md table-fixed">
              <thead className="bg-white border-b-[1px] border-gray-300">
                <tr>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    Id
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("rent.message")}
                  </th>
                  <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    {t("cars.licensePlate")}
                  </th>
                  {["OWNER", "ADMIN"].includes(
                    sessionStorageService.getItem("role") as string
                  ) && (
                    <>
                      <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                        {t("rent.accept")}
                      </th>
                      <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                        {t("rent.deny")}
                      </th>
                    </>
                  )}
                </tr>
              </thead>
              <tbody className="bg-white">
                {notifications &&
                  notifications.length > 0 &&
                  notifications
                    .filter(
                      (notification) =>
                        notification.rent && !notification.rent.active
                    ) // Filter notifications where rent is not null and rent.active is false
                    .map((notification: Notification) => (
                      <tr key={notification.id} className="even:bg-gray-100">
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{notification.id}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{notification.message}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">
                            {notification.car.licensePlate}
                          </div>
                        </td>
                        {["OWNER", "ADMIN"].includes(
                          sessionStorageService.getItem("role") as string
                        ) && (
                          <>
                            <td className="text-left px-6 py-4 font-semibold">
                              <button
                                onClick={() =>
                                  confirmRent(notification.rent.id)
                                }
                                className="rounded-md text-center bg-blue-300 border-blue-300 border-2 px-3 py-2 hover:bg-white flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                              >
                                {t("rent.confirm")}
                              </button>
                            </td>
                            <td className="text-left px-6 py-4 font-semibold">
                              <button
                                onClick={() =>
                                  cancelRentRequest(notification.rent.id)
                                }
                                className="rounded-md text-center bg-gray-300 border-gray-300 border-2 px-3 py-2 hover:bg-white flex items-center justify-center uppercase ease-in-out duration-200 cursor-pointer"
                              >
                                {t("cars.cancel")}
                              </button>
                            </td>
                          </>
                        )}
                      </tr>
                    ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
};

export default RentNotificationOverview;
