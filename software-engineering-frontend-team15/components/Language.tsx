import Link from "next/link";
import { useRouter } from "next/router";
import { useState } from "react";
import { FaGlobe } from "react-icons/fa";

const Language: React.FC = () => {
  const router = useRouter();
  const { locale, pathname, asPath, query } = router;

  const handleLanguageChange = (newLocale: string) => {
    router.push({ pathname, query }, asPath, { locale: newLocale });
  };

  const [languageH, setLanguageH] = useState(true);

  const handleMouseEnter = () => {
    setLanguageH(false);
  };

  const handleMouseLeave = () => {
    setLanguageH(true);
  };

  return (
    <div className="items-center align-middle">
      <div
        className="p-4 cursor-pointer"
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        <FaGlobe size={30} />

        <div
          className={
            languageH
              ? "hidden"
              : "absolute bg-white px-4 py-3 shadow-md text-black rounded-md mt-2"
          }
        >
          <ul className="list-none flex flex-col">
            <li className="pb-1 hover:text-gray-600" onClick={() => handleLanguageChange("en")}>English</li>
            <li className="pb-1 hover:text-gray-600" onClick={() => handleLanguageChange("nl")}>Nederlands</li>
            <li className="pb-1 hover:text-gray-600" onClick={() => handleLanguageChange("fr")}>Français</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Language;
