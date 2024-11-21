import "@/styles/globals.css";
import { appWithTranslation } from "next-i18next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import type { AppProps } from "next/app";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

const App = ({ Component, pageProps }: AppProps) => {

  return(
    <>
    <Navbar />
    <div className="min-h-[calc(105vh-296px)] bg-gray-200">
      <Component {...pageProps} />
    </div>
    <Footer />
    </>
  )

   
};

export default appWithTranslation(App);
