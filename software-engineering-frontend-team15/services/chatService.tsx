import { ChatJSON, ErrorResponse, roomOverview } from "@/types";
import { sessionStorageService } from "./sessionStorageService";
import { UUID } from "crypto";

const backend = process.env.NEXT_PUBLIC_API_URL;

const getOverview = async () => {
  return await fetch(`${backend}/chat/overview`, {
    method: "GET",
    headers: {
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data: roomOverview[] | ErrorResponse) => {
      if ("status" in data && "errorMessage" in data) {
        alert(data.errorMessage);
        return;
      }
      return data;
    })
    .catch((err) => {
      console.error(err);
    });
};

// const getOverviewCustomer = async () => {

//     return await fetch(`${backend}/cs/chat/overview`, {
//         method: "GET",
//         headers: {
//             'authorization': 'Bearer ' + sessionStorageService.getItem('jwt')
//         },
//     }).then(response => response.json())
//         .then((data: roomOverviewCustomer[] | ErrorResponse) => {
//             if ('status' in data && 'errorMessage' in data) {
//                 alert(data.errorMessage)
//                 return
//             }
//             return data
//         })
//         .catch(err => { console.error(err) })
// }

// const getOverviewEmployee = async () => {

//     return await fetch(`${backend}/es/chat/overview`, {
//         method: "GET",
//         headers: {
//             'authorization': 'Bearer ' + sessionStorageService.getItem('jwt')
//         },
//     }).then(response => response.json())
//         .then((data: roomOverviewEmployee[] | ErrorResponse) => {
//             if ('status' in data && 'errorMessage' in data) {
//                 alert(data.errorMessage)
//                 return
//             }
//             return data
//         })
//         .catch(err => { console.error(err) })
// }

const getFirstChats = async (roomId: string) => {
  return await fetch(`${backend}/chat/first?roomId=${roomId}&amount=10`, {
    method: "GET",
    headers: {
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  })
    .then((response) => response.json())
    .then((data: ChatJSON[] | ErrorResponse) => {
      if ("status" in data && "errorMessage" in data) {
        alert(data.errorMessage);
        return;
      }
      return data;
    })
    .catch((err) => {
      console.error(err);
    });
};

const getNextChats = async (roomid: string, startId: UUID) => {
  return await fetch(
    `${backend}/chat/next?roomId=${roomid}&startId=${startId}&amount=10`,
    {
      method: "GET",
      headers: {
        authorization: "Bearer " + sessionStorageService.getItem("token"),
      },
    }
  )
    .then((response) => response.json())
    .then((data: ChatJSON[] | ErrorResponse) => {
      if ("status" in data && "errorMessage" in data) {
        alert(data.errorMessage);
        return;
      }
      return data;
    })
    .catch((err) => {
      console.error(err);
    });
};

const read = async (roomId: string) => {
  return await fetch(`${backend}/chat/read?roomId=${roomId}`, {
    method: "PUT",
    headers: {
      authorization: "Bearer " + sessionStorageService.getItem("token"),
    },
  })
    .then((data) => {
      if (data.ok) {
        return;
      }
      data.json().then((data: ErrorResponse) => {
        alert(data.errorMessage);
        return;
      });
      return;
    })
    .catch((err) => {
      console.error(err);
    });
};

export const chatService = {
  getOverview,
  getFirstChats,
  getNextChats,
  read,
};
