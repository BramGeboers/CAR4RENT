import { sessionStorageService } from "./sessionStorageService";

const getPaymentById = async (rentId: number) => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + `/billing/${rentId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
    });
  };

  const BillingService = {
    getPaymentById,
  };
  
  export default BillingService;