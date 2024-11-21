import { sessionStorageService } from "./sessionStorageService";
import { Complaint } from "@/types";

const baseUrl = process.env.NEXT_PUBLIC_API_URL;

const submitComplaint =  (complaint: Complaint) => {
  return fetch(`${baseUrl}/complaints/submit`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
    body: JSON.stringify(complaint),
  })
};


const getAllComplaints = async () => {
  return fetch(`${baseUrl}/complaints`, {
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),

    },
  })
};

const ComplaintService = {
  submitComplaint,
  getAllComplaints,
};

export default ComplaintService;
