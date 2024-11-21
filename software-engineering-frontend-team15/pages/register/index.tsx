import Register from '@/components/auth/Register';
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useEffect, useState } from 'react';

const Home: React.FC = () => {
  const [username, setUsername] = useState<string>('');

  useEffect(() => {
    const userFromSession = sessionStorage.getItem('user');
    if (userFromSession) {
      const user = JSON.parse(userFromSession);
      setUsername(user.username);
    }
  }, []);

  return (
    <div className=" p-20 max-w-[1080px] flex items-center justify-center mx-auto ">
    {!username && <Register />}
      </div>
  );
}

export const getServerSideProps = async (context: { locale: any; }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  }
}

export default Home
