import Loading from "@/components/Loading";
import UserOverview from "@/components/user/UserOverview";
import UserService from "@/services/UserService";
import { sessionStorageService } from "@/services/sessionStorageService";
import { User } from "@/types";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useEffect, useState } from "react";
import useSWR from "swr";

const index: React.FC = () => {
  const [users, setUsers] = useState<Array<User>>([]);
  const getUsers = async () => {
    const response = await UserService.getAllUsers();
    const users = await response.json();
    setUsers(users);
    return users;
  };

  const { data, isLoading, error } = useSWR("/users", getUsers);

  return (
    <>
      {isLoading && !users && <Loading />}
      {users && <UserOverview/>}
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

export default index;
