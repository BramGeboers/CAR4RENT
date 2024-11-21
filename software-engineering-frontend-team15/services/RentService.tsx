import { Car, Rent, checkoutForm } from "@/types";
import { sessionStorageService } from "./sessionStorageService";

const getAllRents = async (page: Number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents?page=" + page, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const getAllRentsBilling = async (page: Number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/all?page=" + page, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const addRentToCar = async (rentId: number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/add/" + rentId, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const payRent = async (rentId: number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/pay/" + rentId, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const rentRequest = async (carId: number, rentalId: number, rent: Rent) => {
  return fetch(
    process.env.NEXT_PUBLIC_API_URL +
      "/rents/addRequest/" +
      carId +
      "/" +
      rentalId,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
      body: JSON.stringify(rent),
    }
  );
};
const deleteRent = async (rentId: number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/remove/" + rentId, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const getRentWithId = async (carId: number | undefined ) => {
  const cars = await fetch(process.env.NEXT_PUBLIC_API_URL + "/rents", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
  const car = await cars.json();
  return car.filter((car: Car) => car.id === carId);
};

const getRentById = async (rentId: number | undefined) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + `/rents/${rentId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const getAllRentsOfCar = async (car: Car) => {
  const rents = await fetch(process.env.NEXT_PUBLIC_API_URL + "/rents", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
  const rent = await rents.json();
  return rent.filter((rent: Rent) => rent.car.id === car.id);
};

const searchRentByEmail = async (email: String) => {
  const rents: Response = await fetch(
    process.env.NEXT_PUBLIC_API_URL + `/rents/search?email=${email}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
    }
  );
  const rent = await rents.json();
  return rent;
};

const cancelRent = async (rentId: number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/remove/" + rentId, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const cancelRentRequest = async (rentId: number) => {
  return fetch(
    process.env.NEXT_PUBLIC_API_URL + "/rents/cancelRequest/" + rentId,
    {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
    }
  );
};

const rentCheckout = async (checkoutForm: checkoutForm) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/checkOut", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
    body: JSON.stringify(checkoutForm),
  });
};

const rentCheckin = async (checkoutForm: checkoutForm) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/rents/checkIn", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
    body: JSON.stringify(checkoutForm),
  });
};
const searchRents = async (searchCriteria: { startDate:Date|null, endDate: Date|null, city :string|null, email:string|null },page:Number=0) => {
  const criteriaWithStrings: Record<string, string> = Object.fromEntries(
      Object.entries(searchCriteria).map(([key, value]) => [key, String(value)])
    );
    //filter all values that are not null
      Object.keys(criteriaWithStrings).forEach(key => criteriaWithStrings[key] === "null" && delete criteriaWithStrings[key]);

  const queryParameters = new URLSearchParams(criteriaWithStrings).toString();
  console.log(queryParameters);
  return fetch(`${process.env.NEXT_PUBLIC_API_URL}/rents/search/filter?page=${page}&${queryParameters}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const RentService = {
  getAllRents,
  payRent,
  getRentById,
  getAllRentsBilling,
  addRentToCar,
  getRentWithId,
  rentRequest,
  deleteRent,
  getAllRentsOfCar,
  cancelRent,
  searchRentByEmail,
  cancelRentRequest,
  rentCheckout,
  rentCheckin,
  searchRents,
};

export default RentService;
