import React, { useEffect } from "react";
import { useState } from "react";
import { useRouter } from "next/router";
import Link from "next/link";
import CarService from "@/services/CarService";
import RentService from "@/services/RentService";
import { Car, Rent, Rental } from "@/types";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { useTranslation } from "next-i18next";
import { StatusMessage } from "@/types/login";
import RentalService from "@/services/RentalService";

const EditCar: React.FC = () => {
  const { t } = useTranslation();

  const [rental, setRental] = useState<Rental>();

  const [errorMessage, setErrorMessage] = useState<StatusMessage>();

  const [errors, setErrors] = useState({
    carId: "",
    phoneNumber: "",
    status: "",
    nationalIdentificationNumber: "",
    birthDate: "",
    drivingLicenseNumber: "",
  });

  const validate = (): boolean => {
    let result = true;

    let errors = {
      carId: "",
      phoneNumber: "",
      status: "",
      nationalIdentificationNumber: "",
      birthDate: "",
      drivingLicenseNumber: "",
    };

    if (!rent.phoneNumber) {
      errors.phoneNumber = t("error.phoneNumberR");
      result = false;
    } else if (
      // !/^\+?[0-9]{1,3}?[-. ]?\(?[0-9]{3}\)?[-. ]?[0-9]{3}[-. ]?[0-9]{4}$/.test(
      //   rent.phoneNumber
      // )
      !/^\d{10}$/.test(rent.phoneNumber)
    ) {
      errors.phoneNumber = t("error.phoneNumberInvalid");
      result = false;
    }

    if (!rent.nationalIdentificationNumber) {
      errors.nationalIdentificationNumber = t("error.nationalIDR");
      result = false;
    } else if (!/^\d{11}$/.test(rent.nationalIdentificationNumber)) {
      errors.nationalIdentificationNumber = t("error.nationalIDInvalid");
      result = false;
    }

    if (!rent.birthDate) {
      errors.birthDate = t("error.birthDateR");
      result = false;
    } else {
      const birthDate = new Date(rent.birthDate);
      const eighteenYearsAgo = new Date();
      eighteenYearsAgo.setFullYear(eighteenYearsAgo.getFullYear() - 18);

      if (birthDate >= new Date()) {
        errors.birthDate = t("error.birthDatePast");
        result = false;
      } else if (birthDate > eighteenYearsAgo) {
        errors.birthDate = t("error.birthDateAge");
        result = false;
      }
    }

    if (!rent.drivingLicenseNumber) {
      errors.drivingLicenseNumber = t("error.drivingLicenseR");
      result = false;
    } else if (!/^[0-9]{10}$/.test(rent.drivingLicenseNumber)) {
      errors.drivingLicenseNumber = t("error.drivingLicenseInvalid");
      result = false;
    }

    setErrors(errors);

    return result;
  };

  const router = useRouter();
  const { rentalId } = router.query;

  const [rent, setRent] = useState({
    phoneNumber: "",
    // email: "",
    nationalIdentificationNumber: "",
    birthDate: "",
    drivingLicenseNumber: "",
    active: false,
  });

  const downloadICSFile = () => {
    const formatDate = (dateString: string | undefined) => {
      const date = new Date(dateString ?? "");
      return date.toISOString().replace(/[:-]/g, "").replace(/\.\d{3}Z/, "Z");
    };
  
    const eventStart = formatDate(rental?.startDate);
    const eventEnd = formatDate(rental?.endDate);

    const icsContent = `BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//Software Engineering//Rental App//EN
BEGIN:VEVENT
SUMMARY:${rental?.car.brand} ${rental?.car.model} ${t("cars.rental")}
DTSTART:${eventStart}
DTEND:${eventEnd}
END:VEVENT
END:VCALENDAR`;

    const blob = new Blob([icsContent], { type: "text/calendar" });
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "rental_event.ics");
    document.body.appendChild(link);
    link.click();

    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  };

  useEffect(() => {
    const rentalIdNumber = parseInt(rentalId as string);
    const fetchData = async () => {
      try {
        const response = await RentalService.getRentalWithId(rentalIdNumber);
        const rentalJson = await response.json();
        setRental(rentalJson);
      } catch (error) {
        console.log(error);
      }
    };
    if (rentalIdNumber) {
      fetchData();
    }
  }, [rentalId]);

  const handleChange = (event: any) => {
    const value = event.target.value;
    setRent({ ...rent, [event.target.name]: value });
  };

  const addRental = async (e: any) => {
    e.preventDefault();

    if (!validate()) {
      return;
    }

    try {
      const response = await RentService.rentRequest(
        rental?.car.id as number,
        rental?.id as number,
        rent as Rent
      );

      const data = await response.json();

      if (!response.ok) {
        setErrorMessage({
          type: "error",
          message: "something went wrong, try again later",
        });
      }
      if (response.ok) {
        const confirmed = window.confirm(t("calendar.confirm"));
        if (confirmed) {
          downloadICSFile();
        }
        router.push("/rents");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <main>
        <div className="bg-gray-200">
          <div className=" flex max-w-[1140px] justify-center py-20 px-8 mx-auto">
            <div className="flex flex-col flex-wrap md:w-1/2 max-w-[600px] justify-between bg-white p-5 rounded-xl">
              <h1 className="text-4xl">Add 4 Rental</h1>
              {errorMessage && (
                <p className="text-red-600 text-sm pb-2">
                  {errorMessage.message}
                </p>
              )}
              <div className="bg-gray-200 mt-6 p-2 rounded-xl">
                <h2 className="text-2xl">{t("cars.car")}</h2>
                <p className="pt-1">
                  {rental?.car?.brand} {rental?.car?.model}
                </p>
                <p className="pt-1">{rental?.car?.licensePlate}</p>
              </div>

              <div className="bg-gray-200 mt-6 p-2 rounded-xl">
                <h2 className="text-2xl">{t("rent.period")}</h2>

                <div key={rental?.id}>
                  <p className="pt-1">{rental?.startDate}</p>
                  <p className="pt-1">{rental?.endDate}</p>
                </div>
              </div>
              <form>
                <h2 className="text-xl pt-2">{t("rent.infoRenter")}</h2>

                <label className="pb-2 pt-4 pb">{t("rent.phoneNumber")}</label>
                <input
                  name="phoneNumber"
                  type="tel"
                  value={rent.phoneNumber}
                  onChange={(e) => handleChange(e)}
                  className="w-[100%] bg-gray-200 p-1 mb-3 focus:outline-none rounded-md"
                />

                {errors.phoneNumber && (
                  <p className="text-red-600 text-sm pb-2">
                    {errors.phoneNumber}
                  </p>
                )}

                {/* <label className="pb-2 pt-4">{t("rent.email")}</label>
                <input
                  name="email"
                  type="email"
                  value={rent.email}
                  onChange={(e) => handleChange(e)}
                  className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
                />

                {errors.email && (
                  <p className="text-red-600 text-sm pb-2">{errors.email}</p>
                )} */}

                <label className="pb-2 pt-4">{t("rent.ssn")}</label>
                <input
                  name="nationalIdentificationNumber"
                  type="number"
                  value={rent.nationalIdentificationNumber}
                  onChange={(e) => handleChange(e)}
                  className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md"
                />

                {errors.nationalIdentificationNumber && (
                  <p className="text-red-600 text-sm pb-2">
                    {errors.nationalIdentificationNumber}
                  </p>
                )}

                <label className="pb-2 pt-4">{t("rent.bDay")}</label>
                <input
                  name="birthDate"
                  type="date"
                  value={rent.birthDate}
                  onChange={(e) => handleChange(e)}
                  className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md"
                />

                {errors.birthDate && (
                  <p className="text-red-600 text-sm pb-2">
                    {errors.birthDate}
                  </p>
                )}

                <label className="pb-2 pt-4">{t("rent.licenseNum")}</label>
                <input
                  name="drivingLicenseNumber"
                  type="number"
                  value={rent.drivingLicenseNumber}
                  onChange={(e) => handleChange(e)}
                  className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md"
                />

                {errors.drivingLicenseNumber && (
                  <p className="text-red-600 text-sm pb-2">
                    {errors.drivingLicenseNumber}
                  </p>
                )}

                <div className="flex flex-row">
                  <Link
                    // go back to previous page

                    href={router.asPath}
                    className="mr-4 rounded-xl mt-8 font-semibold bg-gray-400 border-gray-400 border-2 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
                  >
                    {t("cars.cancel")}
                  </Link>
                  <input
                    type="submit"
                    onClick={addRental}
                    value={t("general.rent")}
                    className="rounded-xl mt-8 font-semibold bg-blue-400 border-blue-400 border-2 w-[150px] h-[55px] flex items-center justify-center text-lg mb-20 md:mb-0 hover:bg-white  ease-in-out duration-200 cursor-pointer"
                  />
                </div>
              </form>
            </div>
          </div>
        </div>
      </main>
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

export default EditCar;
