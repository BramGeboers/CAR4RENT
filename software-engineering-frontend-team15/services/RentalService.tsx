import { Rental } from "@/types"
import { sessionStorageService } from "./sessionStorageService"

const getAllRentals = async (page: Number) => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + "/rentals?page=" + page,{
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        // authorization: "Bearer " + sessionStorageService.getItem("token"),
    }})
}

const addRentalToCar = async (carId: string, rental: Rental) => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + "/rentals/add/" + carId ,{
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
    body: JSON.stringify(rental),
})
}

const getRentalWithId = async (carId: number | undefined) => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + "/rentals/" + carId, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            authorization: "Bearer " + sessionStorageService.getItem("token"),
        }
    })
}

const deleteRental = async (rentalId: number) => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + "/rentals/remove/" + rentalId, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            authorization: "Bearer " + sessionStorageService.getItem("token"),
        }
    })
}

const searchRentals = async (searchCriteria: { startDate:Date|null, endDate: Date|null, city :string|null, postal:string|null, street:string|null  },page:Number=0) => {
    const criteriaWithStrings: Record<string, string> = Object.fromEntries(
        Object.entries(searchCriteria).map(([key, value]) => [key, String(value)])
      );
      //filter all values that are not null
        Object.keys(criteriaWithStrings).forEach(key => criteriaWithStrings[key] === "null" && delete criteriaWithStrings[key]);

    const queryParameters = new URLSearchParams(criteriaWithStrings).toString();
    console.log(queryParameters);
    return fetch(`${process.env.NEXT_PUBLIC_API_URL}/rentals/search?page=${page}&${queryParameters}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
    });
  };

const RentalService = {
    getAllRentals,
    addRentalToCar,
    getRentalWithId,
    deleteRental,
    searchRentals
}

export default RentalService