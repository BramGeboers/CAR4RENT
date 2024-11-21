import { sessionStorageService } from "./sessionStorageService"

const getAllUsers = async () => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + `/user/all`, {
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
    }}) 
}

const updateUserRole = async (userId: number, role: string) => {
    return fetch(
      process.env.NEXT_PUBLIC_API_URL +
        "/manage/role/update?userId=" +
        userId +
        "&role=" +
        role,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          authorization: "Bearer " + sessionStorageService.getItem("token"),
        }
      }
    );
  };

const getUserByEmail = async () => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + `/user/find`, {
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
    }}) 
}

const UserService = {
    getUserByEmail
}

export default {getAllUsers,updateUserRole,getUserByEmail,UserService}