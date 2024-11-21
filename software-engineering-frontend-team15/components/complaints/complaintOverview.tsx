import React, { useState, useEffect } from 'react';
import { Complaint } from '@/types';
import ComplaintService from '@/services/ComplaintService';
import Loading from '@/components/Loading';
import { sessionStorageService } from "@/services/sessionStorageService";
import { useTranslation } from "next-i18next";


const ComplaintOverview: React.FC = () => {
    const [complaints, setComplaints] = useState<Array<Complaint>>([]);
    const [loading, setLoading] = useState(true);
    const [filteredComplaints, setFilteredComplaints] = useState<Array<Complaint>>([]);
    const [userEmail, setUserEmail] = useState<string>('');
    const { t } = useTranslation();


    useEffect(() => {
        const fetchComplaints = async () => {
            setLoading(true);
            try {
                const response = await ComplaintService.getAllComplaints();
                if (response.ok) {
                  console.log('Complaints data:', complaints);

                    const complaintsData = await response.json();
                    setComplaints(complaintsData.content);
                    setFilteredComplaints(complaintsData.content);
                } else {
                    console.error('Error fetching complaints:', response.statusText);
                }
            } catch (error) {
                console.error('Error fetching complaints:', error);
            }
            setLoading(false);
        };
        fetchComplaints();

        // Retrieve user email from sessionStorage
        const email = sessionStorageService.getItem('email');
        if (email) {
            setUserEmail(email);
            console.log('User email retrieved from sessionStorage:', email);

        } else {
            console.error('User email not found in sessionStorage');
        }
    }, []);

    return (
        <div className="bg-gray-200">
            <div className="max-w-[1340px] justify-center py-20 px-8 mx-auto">
                <div className="p-5 bg-white rounded-xl">
                    <div className="pb-4 flex justify-between items-center">
                    </div>
                    <table className="min-w-full rounded-md table-fixed">
                        <thead className="bg-white border-b-[1px] border-gray-300">
                            <tr>
                                <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                                {t("complaint.id")}
                                </th>
                                <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                                {t("complaint.email")}
                                </th>
                                <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                                {t("complaint.title")}
                                </th>
                                <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                                {t("complaint.description")}
                                </th>
                            </tr>
                        </thead>
                        <tbody className="bg-white">
                            {loading ? (
                                <tr>
                                    <td colSpan={4} className="text-center py-4">
                                        <Loading />
                                    </td>
                                </tr>
                            ) : filteredComplaints.length > 0 ? (
                                filteredComplaints.map((complaint: Complaint) => (
                                    <tr key={complaint.id} className="even:bg-gray-100">
                                        <td className="text-left px-6 py-4">
                                            <div className="text-sm">{complaint.id}</div>
                                        </td>
                                        <td className="text-left px-6 py-4 "><div className="text-sm">{complaint.userEmail}</div>
                                        </td>
                                        <td className="text-left px-6 py-4">
                                            <div className="text-sm">{complaint.title}</div>
                                        </td>
                                        <td className="text-left px-6 py-4">
                                            <div className="text-sm">{complaint.description}</div>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={4} className="text-center py-4">
                                    {t("complaint.noComplaints")}
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default ComplaintOverview;
