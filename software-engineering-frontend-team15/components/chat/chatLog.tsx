import { sessionStorageService } from "@/services/sessionStorageService";
import { roomOverview } from "@/types";
import { FaXmark } from "react-icons/fa6";
import React, { useEffect } from "react";
import { transformPreview } from "./open/transform";

type chatLogProps = {
  openEvent: (roomId: string, name: string) => void;
  show: boolean;
  isLoading: boolean;
  error: boolean;
  chats: roomOverview[];
  toggleShowLog: () => void;
};

const ChatLog: React.FC<chatLogProps> = ({
  openEvent,
  isLoading,
  error,
  chats,
  toggleShowLog,
}) => {
  const handleOnClick = (chat: roomOverview) => {
    openEvent(chat.roomId, chat.roomName);
    // if ((chat as roomOverview).id) {
    //     openEvent('employee', (chat as roomOverviewCustomer).employeeId, (chat as roomOverviewCustomer).employeeName, chat.email)
    // } else if ((chat as roomOverviewEmployee).customerId) {
    //     openEvent('customer', (chat as roomOverviewEmployee).customerId, (chat as roomOverviewEmployee).customerName, chat.email)
    // }
  };

  const isOwnMessage = (senderEmail: string) => {
    return senderEmail === sessionStorageService.getItem("email");
  };

  const getNameFromEmail = (email: string) => {
    if (isOwnMessage(email)) {
      return "You";
    }
    return email
      .split("@")[0]
      .split(".")
      .map((s) => s[0].toUpperCase() + s.slice(1).toLowerCase())
      .join(" ");
  };

  return (
    <div
      id="Chatlog"
      className={
        "flex flex-col h-screen bg-gray-300 p-4 pointer-events-auto w-80 shadow-md"
      }
    >
      <div className="text-black text-3xl mt-3 mb-3 font-medium flex justify-between items-end">
        <h1>Chatlog</h1>
        <FaXmark
          height={40}
          width={40}
          className="hover:text-gray-400 transition-all cursor-pointer"
          onClick={toggleShowLog}
        />
      </div>
      <div className="flex flex-col gap-1 overflow-scroll">
        {isLoading && !chats && <h2>Loading...</h2>}
        {error && <h2>Error</h2>}
        {chats &&
          chats.map((chat, index) => {
            return (
              <div
                onClick={() => {
                  handleOnClick(chat);
                }}
                key={index}
                className="bg-white flex flex-col border-r-2 border-b-2 border-gray-200 p-2 cursor-pointer shadow-md hover:bg-gray-100 m-2 rounded-md relative"
              >
                <h2 className="truncate">{chat.roomName}</h2>
                {chat.unreadMessages > 0 && (
                  <span className="absolute -top-2 -right-2 w-6 h-6 flex justify-center items-center bg-red-600 text-white rounded-full">
                    <p>
                      {chat.unreadMessages < 100 ? chat.unreadMessages : "99+"}
                    </p>
                  </span>
                )}
                {chat && chat.lastMessage && (
                  <p className="truncate">
                    {getNameFromEmail(chat.lastMessageSender) +
                      ": " +
                      transformPreview(chat.lastMessage)}
                  </p>
                )}
                {chat && !chat.lastMessage && <p>No messages</p>}
              </div>
            );
          })}
        {chats && chats.length === 0 && <h2>No chats</h2>}
      </div>
    </div>
  );
};

export default ChatLog;
