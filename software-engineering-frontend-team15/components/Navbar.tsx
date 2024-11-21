import * as React from "react";
import { AiOutlineClose, AiOutlineMenu } from "react-icons/ai";
import logo from "@/public/simple.png";
import logo2 from "@/public/vercel.svg";
import Image from "next/image";
import { useEffect, useRef, useState } from "react";
import Link from "next/link";
import { User } from "@/types";
import AuthService from "@/services/AuthService";
import { useTranslation } from "next-i18next";
import Language from "./Language";
import { sessionStorageService } from "@/services/sessionStorageService";
import Chat from "./chat/chat";
import { FaUser } from "react-icons/fa";
import { useRouter } from "next/router";

const Navbar: React.FC = () => {
  const boxRef = useRef(null);
  const { t } = useTranslation();
  const [email, setEmail] = useState<String | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [role, setRole] = useState<String | null>(null);
  const [errors, setErrors] = useState({ linked: "" });

  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(
    null
  );

  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);

  const router = useRouter();

  useEffect(() => {
    const userDataString = sessionStorageService.isLoggedIn();
    setIsLoggedIn(userDataString as boolean);
    if (userDataString) {
      setEmail(sessionStorageService.getItem("email") || null);
      setRole(sessionStorageService.getItem("role") || null);
    }
  }, []);

  const [nav, SetNav] = useState(true);

  const [profile, SetProfile] = useState(true);

  const handleNav = () => {
    SetNav(!nav);
  };

  const handleLogout = async () => {
    setIsLoggedIn(false);
    try {
      const response = await AuthService.logout();

      if (!response.ok) {
        console.log(response.status); // Will show you the status
      }
      if (response.ok) {
        sessionStorage.clear();
        setEmail(null);
        setRole(null);
        router.push("/");
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleMouseEnter = () => {
    SetProfile(false);
  };

  const handleMouseLeave = () => {
    SetProfile(true);
  };

  const getName = () => {
    const email = sessionStorageService.getItem("email") as string;
    return email.split("@")[0].replace(".", " ");
  };

  const navJson = [
    {
      name: t("general.home"),
      link: "/",
      authorities: [],
    },
    {
      name: t("general.cars"),
      link: "/cars",
      authorities: ["OWNER", "ADMIN"],
    },
    {
      name: t("general.rentals"),
      link: "/rentals",
      authorities: [],
    },
    {
      name: t("general.rents"),
      link: "/rents",
      authorities: ["OWNER", "RENTER", "ADMIN"],
    },
    {
      name: t("general.search"),
      link: "/search",
      authorities: [],
    },
    {
      name: t("general.billing"),
      link: "/billing",
      authorities: ["ACCOUNTANT", "ADMIN"],
    },
    {
      name: t("general.users"),
      link: "/users",
      authorities: ["ADMIN"],
    },
    {
      name: t("general.complaint"),
      link: "/complaints",
      authorities: ["ADMIN"],
    },
  ];

  return (
    <>
      <div className="bg-[#1976d2]">
        <div className="flex justify-between items-center h-[90px] max-w-[1140px] mx-auto px-4 text-white">
          <a href="/">
            <Image src={logo} alt="" className="w-[90px]" />
          </a>
          <div className="flex flex-row items-center">
            <ul className="hidden md:flex font-semibold uppercase mr-6">
              {navJson.map((item, index) => {
                if (
                  item.authorities.length === 0 ||
                  item.authorities.includes(role as string)
                ) {
                  return (
                    <li key={index}>
                      <Link
                        href={item.link}
                        className={`m-3 p-1 hover:text-gray-300 transition-all ${
                          router.pathname === item.link
                            ? "text-gray-300 border-b-2 border-gray-300"
                            : ""
                        }`}
                      >
                        {item.name}
                      </Link>
                    </li>
                  );
                }
              })}
            </ul>
            <div className="flex flex-row font-medium">
              <Language />
              {email ? (
                <div
                  className="p-4 cursor-pointer"
                  onMouseEnter={handleMouseEnter}
                  onMouseLeave={handleMouseLeave}
                >
                  {" "}
                  <FaUser size={30} />
                  <div
                    className={
                      profile
                        ? "hidden"
                        : "absolute bg-white px-4 py-3 shadow-md text-black rounded-md mt-2"
                    }
                  >
                    <ul className="list-none flex flex-col">
                      <Link
                        href="/notifications"
                        className="pb-1 hover:text-gray-600"
                      >
                        {t("general.notifications")}
                      </Link>
                      <Link
                        href="/history"
                        className="pb-1 hover:text-gray-600"
                      >
                        {t("general.history")}
                      </Link>
                      <Link
                        href="/complaints/add"
                        className="pb-1 hover:text-gray-600"
                      >
                        {t("general.fileComplaint")}
                      </Link>
                      <div onClick={handleLogout} className="hover:text-gray-600">
                        {t("general.logout")}
                      </div>
                      <p>
                        {t("general.loggedInAs")}{" "}
                        {sessionStorageService.getItem("email")}
                      </p>
                    </ul>
                  </div>
                </div>
              ) : (
                <ul className="hidden md:flex items-center font-semibold uppercase mr-6">
                  <Link className="p-4" href={"/register"}>
                    {t("general.register")}
                  </Link>
                  <Link className="flex items-center" href={"/login"}>
                    <div className="px-3 py-2 bg-white text-[#1976d2] rounded-lg">
                      {t("general.login")}
                    </div>
                  </Link>
                </ul>
              )}
            </div>
          </div>

          <div onClick={handleNav} className="block md:hidden">
            {!nav ? <AiOutlineClose size={20} /> : <AiOutlineMenu size={20} />}
          </div>
          <div
            className={
              !nav
                ? "fixed left-0 top-0 w-[60%] h-full border-r border-r-gray-900 bg-white ease-in-out duration-500 z-50"
                : "fixed left-[-100%] top-0 w-[60%] h-full border-r border-r-gray-900 bg-white ease-in-out duration-500 z-50"
            }
          >
            <Link href="/">
              <Image src={logo2} alt="" className="w-[115px] m-4" />
            </Link>

            <ul className="p-4 uppercase text-black">
              {navJson.map((item, index) => {
                if (
                  item.authorities.length === 0 ||
                  item.authorities.includes(role as string)
                ) {
                  return (
                    <li
                      key={index}
                      className={`p-4 border-b border-gray-300 ${
                        router.pathname === item.link
                          ? "text-gray-300 border-b-2 border-gray-300"
                          : ""
                      }`}
                    >
                      <Link href={item.link}>{item.name}</Link>
                    </li>
                  );
                }
              })}
            </ul>
          </div>
        </div>
      </div>

      {isLoggedIn && <Chat />}
    </>
  );
};

export default Navbar;
