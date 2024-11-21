import { Inter } from "next/font/google";
import Login from "@/components/auth/Login";
import { useEffect, useState } from "react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

const Home: React.FC = () => {
  const [username, setUsername] = useState<string>("");

  useEffect(() => {
    const userFromSession = sessionStorage.getItem("user");
    if (userFromSession) {
      const user = JSON.parse(userFromSession);
      setUsername(user.username);
    }
  }, []);

  return (
    <>
      <div className=" p-20 max-w-[1080px] flex items-center justify-center mx-auto ">
        {!username && <Login />}
      </div>
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

export default Home;
