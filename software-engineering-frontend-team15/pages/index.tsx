import Head from "next/head";
import Link from "next/link";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";

const index: React.FC = () => {
  const { t } = useTranslation();
  const handleOverviewClick = (type: string) => {
    console.log(`Overview ${type} clicked`);
  };
  return (
    <>
      <div className="bg-gray-200 w-full">
        <Head>
          <title>Car4Rent</title>
          <meta name="description" content="An app for renting a Car" />
          <meta name="viewport" content="width=device-width, initial-scale=1" />
          <meta name="backend-url" content={process.env.NEXT_PUBLIC_API_URL} />
          <link rel="icon" href="/favicon.ico" />
        </Head>
        <main className="flex items-center w-[100%] justify-center p-8 flex-col">
          <h1 className="text-5xl font-bold mt-[5rem]">{t("home.welcome")}</h1>
            <p className="text-lg mt-4">{t("home.slogan")}</p>
              {/* <div className="max-w-[700px]  bg-white rounded-xl items-center justify-center px-4 py-6 m-12 uppercase">
                <p className="bg-white border-b-[1px] text-xl border-gray-300 text-left font-semibold uppercase  py-3 px-6 mb-4" > 
                {t("home.overview")}
                </p>
                <div className="flex flex-row flex-wrap gap-4 items-center justify-center">
                <Link
                  href="/cars"
                  className="rounded-xl text-center  p-2 px-3 font-semibold border-blue-400 border-2 bg-blue-400 flex items-center justify-center  text-base hover:bg-white  ease-in-out duration-200 cursor-pointer"
                >
                  {t("home.overview")}
                </Link>
                <Link
                  href="/cars/add"
                  className="rounded-xl text-center p-2 px-3 font-semibold border-blue-400 border-2 bg-blue-400 flex items-center justify-center  text-base hover:bg-white  ease-in-out duration-200 cursor-pointer"
                >
                  {t("home.addCar")}
                </Link>
                <Link
                  href="/rent"
                  className="rounded-xl text-center p-2 px-3 font-semibold border-blue-400 border-2 bg-blue-400 flex items-center justify-center  text-base hover:bg-white  ease-in-out duration-200 cursor-pointer"
                  >
                  {t("home.overview")} {t("general.rents")}
                </Link>

                <Link
                  href="/rentals"
                  className="rounded-xl text-center p-2 px-3 font-semibold border-blue-400 border-2 bg-blue-400 flex items-center justify-center  text-base hover:bg-white  ease-in-out duration-200 cursor-pointer"
                  >
                  {t("home.overview")} {t("general.rentals")}
                </Link>
                  <Link
                    href="/notifications"
                    className="rounded-xl text-center p-2 px-3 font-semibold border-blue-400 border-2 bg-blue-400 flex items-center justify-center  text-base hover:bg-white  ease-in-out duration-200 cursor-pointer"
                    >
                    {t("general.notifications")}
                  </Link>
                </div>
              </div> */}
          </main>
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

export default index;
