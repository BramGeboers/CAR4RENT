import React, { useState } from "react";
import UserService from "@/services/UserService";
import { User } from "@/types";
import { useTranslation } from "next-i18next";
import router from "next/router";
import Loading from "@/components/Loading";
import useSWR from "swr";
import { get } from "http";

type Props = {};

const Useroverview: React.FC<Props> = () => {
  const { t } = useTranslation();

  const [users, setUsers] = useState<Array<User>>([]);
  const getUsers = async () => {
    const response = await UserService.getAllUsers();
    const users = await response.json();
    setUsers(users);
    return users;
  };

  const extractUsername = (email: string) => {
    const match = email.match(/^(.*?)@/);
    return match ? match[1] : email;
  };

  const handleRoleChange = async (userId: number, role: string) => {
    try {
      const confirmChange = window.confirm(t("user.roleConfirm") + role + "?");
      if (confirmChange) {
        await UserService.updateUserRole(userId, role);
        getUsers();
      }
    } catch (error) {
      alert("Error");
    }
  };

  const { data, isLoading, error } = useSWR("/users", getUsers);

  return (
    <>
      {isLoading && !users && <Loading />}
      {error && <div>{t("error")}</div>}
      {users && users.length === 0 && <div>{t("user.noUsers")}</div>}
      {users && users.length > 0 && (
        <div className="bg-gray-200">
          <div className="max-w-[750px] justify-center py-10 px-8 mx-auto">
            <div className="p-5 bg-white rounded-xl">
              <table className="min-w-full rounded-md table-fixed">
                <thead className="bg-white border-b-[1px] border-gray-300">
                  <tr>
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("user.user")}
                    </th>
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("user.email")}
                    </th>
                    <th className="text-left font-medium text-gray-500 uppercase py-3 px-6">
                      {t("user.role")}
                    </th>
                    <th></th>
                  </tr>
                </thead>
                <tbody className="bg-white">
                  {users &&
                    users.map((user: User) => (
                      <tr key={user.userId} className="even:bg-gray-100">
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">
                            {extractUsername(user.email)}
                          </div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{user.email}</div>
                        </td>
                        <td className="text-left px-6 py-4 ">
                          <div className="text-sm">{user.role}</div>
                        </td>
                        <td>
                          <div className="relative">
                            <select
                              className="appearance-none text-center w-full bg-white border border-gray-300 hover:border-gray-500 px-4 py-2 rounded shadow leading-tight focus:outline-none focus:shadow-outline"
                              defaultValue=""
                              onChange={async (e) => {
                                await handleRoleChange(
                                  user.userId,
                                  e.target.value
                                );
                                e.target.value = "";
                              }}
                            >
                              <option value="" disabled hidden>
                                {t("user.selectRole")}
                              </option>
                              {["OWNER", "RENTER", "ACCOUNTANT", "ADMIN"]
                                .filter((role) => role !== user.role)
                                .map((role) => (
                                  <option key={role} value={role}>
                                    {role}
                                  </option>
                                ))}
                            </select>
                          </div>
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Useroverview;
