import { Car } from "@/types";
import { sessionStorageService } from "./sessionStorageService";

const getAllCars = async (page: Number) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/cars?page=" + page, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const addCar = async (car: Car) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + "/cars/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
    body: JSON.stringify(car),
  });
};

const deleteCar = async (carId: number | undefined) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + `/cars/delete/${carId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const getCarById = async (carId: string) => {
  return fetch(process.env.NEXT_PUBLIC_API_URL + `/cars/${carId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  });
};

const searchCars = async (searchCriteria: { brand:string|null, model:string|null, types:string|null, licensePlate:string|null, numberOfSeats:number|null, numberOfChildSeats:number|null, foldingRearSeat:boolean|null, towBar:boolean|null, mileage:number|null, fuel:number|null, fuelCapacity:number|null, fuelEstimatedConsumption:number|null, pricePerKm:number|null, pricePerLiterFuel:number|null },page:Number=0) => {
    const criteriaWithStrings: Record<string, string> = Object.fromEntries(
        Object.entries(searchCriteria).map(([key, value]) => [key, String(value)])
      );
      //filter all values that are not null
        Object.keys(criteriaWithStrings).forEach(key => criteriaWithStrings[key] === "null" && delete criteriaWithStrings[key]);

    const queryParameters = new URLSearchParams(criteriaWithStrings).toString();
    console.log(queryParameters);
    return fetch(`${process.env.NEXT_PUBLIC_API_URL}/cars/search?page=${page}&${queryParameters}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
    });
  };

const CarService = {
  getAllCars,
  addCar,
  getCarById,
  deleteCar,
    searchCars,
};

export default CarService;
