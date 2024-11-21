import React, { useState, useEffect } from 'react'
import Link from 'next/link';
import { Notification } from '@/types';
import notificationService from '@/services/NotifcationService';


const NotificationOverview = () => {

  const [notifications, setNotifications] = useState<Array<Notification>>();
  const [loading, setLoading] = useState(true);
  const [deleteErrorMessage, setDeleteErrorMessage] = useState('');
  const [errors, setErrors] = useState({
    linked: "",
  });

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await notificationService.getAllNotifications()
      const notifications = await response.json()
      setNotifications(notifications);
    } catch (error) {
      console.log(error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
      <>
        <div className="bg-gray-200">
          <div className="max-w-[1340px] justify-center py-20 px-8 mx-auto">
            <div className='p-5 bg-white rounded-xl'>
            <table className="min-w-full rounded-md table-fixed">
              <thead className="bg-white border-b-[1px] border-gray-300">
              <tr>
                <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    Id
                </th>
                <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    Message
                </th>
                <th className="text-left font-medium text-gray-500 uppercase  py-3 px-6">
                    licenseplate
                </th>
              </tr>
              </thead>
              {!loading && (
                  <tbody className="bg-white">
                  {notifications && notifications.length > 0 && notifications?.map((notification: Notification) => (
                      <tr key={notification.id} className='even:bg-gray-100'>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{notification.id}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{notification.message}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{notification.car.licensePlate}</div>
                        </td>
                      </tr>
                ))}
            </tbody>
            )}
          </table>
            </div>
        </div>
      </div>

    </>
  )

}

export default NotificationOverview