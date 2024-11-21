import { sessionStorageService } from "./sessionStorageService"

const getAllNotifications = async () => {
    return fetch(process.env.NEXT_PUBLIC_API_URL + "/notifications",{
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        authorization: "Bearer " + sessionStorageService.getItem("token"),
    }})
}

const NotificationService = {
    getAllNotifications,
}

export default NotificationService