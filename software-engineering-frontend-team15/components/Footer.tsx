import { useTranslation } from "next-i18next";
import React from "react";

const Footer: React.FC = () => {
  const { t } = useTranslation();
  return (
    <div className="bg-gray-200 w-full">
      <div className="bg-[#1976d2] w-full p-4 text-center font-semibold">
        <ul className="flex justify-around text-white">
          <li>Bram Geboers</li>
          <li>Loveleen Sidhu</li>
          <li>Axel Jacobs</li>
          <li>Torben Ombelets</li>
          <li>Wout Paepen</li>
          <li>Rein Van Wanseele</li>
        </ul>
      </div>
      <div className="h-[150px] flex items-center justify-center flex-col">
        <p className="pb-2 font-semibold text-lg">
          Car 4 Rent App Team 15 Software Engineering
        </p>
        <p>Copyright © 2024. {t("general.copyright")}</p>
      </div>
    </div>
  );
};

export default Footer;
