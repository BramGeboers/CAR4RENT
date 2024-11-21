import { chatService } from "@/services/chatService";
import { ChatJSON, roomOverview } from "@/types";
import React, { useEffect, useState } from "react";
import useSWR from "swr";
import ChatLog from "./chatLog";
import OpenChat from "./open/openChat";
import MinChat from "./open/minChat";
import { BsChatLeftText } from "react-icons/bs";
import { socketioService } from "@/services/socketioService";
import { sessionStorageService } from "@/services/sessionStorageService";

const Chat = () => {
  const [openChats, setOpenChats] = useState<
    { roomId: string; name: string }[]
  >([]);
  const [minimzedChats, setMinimizedChats] = useState<
    {
      roomId: string;
      name: string;
      hasNew: number;
    }[]
  >([]);
  const [showLog, setShowLog] = useState(false);
  const [unread, setUnread] = useState<boolean>(false);
  const [chats, setChats] = React.useState<roomOverview[]>([]);

  const toggleShowLog = () => {
    setShowLog(!showLog);
  };

  const fetcher = () => {
    if (!sessionStorageService.isLoggedIn()) {
      return;
    }
    let res: Promise<roomOverview[]>;
    res = chatService.getOverview() as Promise<roomOverview[]>;
    res
      .then((res) => {
        setChats(res);
      })
      .catch((err) => {
        alert("error getting chats");
      });
  };

  const { data, isLoading, error } = useSWR("chatOverview", fetcher);

  const openNewChat = (roomId: string, name: string) => {
    if (openChats.find((chat) => chat.roomId === roomId)) {
      //maybe move to front
      setShowLog(false);
      return;
    }

    chatService.read(roomId);
    setChats(
      chats.map((chat) => {
        if (chat.roomId === roomId) {
          console.log("chat clearing");
          return { ...chat, unreadMessages: 0 };
        }
        return chat;
      }) as roomOverview[]
    );

    if (minimzedChats.find((chat) => chat.roomId === roomId)) {
      setMinimizedChats(minimzedChats.filter((chat) => chat.roomId !== roomId));
    }

    setOpenChats([...openChats, { roomId: roomId, name: name }]);
    setShowLog(false);
  };

  const closeChat = (roomId: string) => {
    setOpenChats(openChats.filter((chat) => chat.roomId !== roomId));
  };

  const minimizeChat = (roomId: string) => {
    if (minimzedChats.find((chat) => chat.roomId === roomId)) {
      setOpenChats(openChats.filter((chat) => chat.roomId !== roomId));
      return;
    }
    const chat = openChats.find((chat) => chat.roomId === roomId);
    if (chat) {
      setMinimizedChats([...minimzedChats, { ...chat, hasNew: 0 }]);
      setOpenChats(openChats.filter((chat) => chat.roomId !== roomId));
    }
  };

  useEffect(() => {
    const socket = socketioService.getSocket();
    if (!socket) {
      return;
    }

    // socket.on("new message", (data: { returnChat: ChatJSON }) => {
    //   console.log("new message", data);
    // read message if chat is open
    // if (openChats.find((chat) => chat.roomId === data.returnChat.roomId)) {
    //   chatService.read(data.returnChat.roomId);
    //   return;
    // }
    // // does not need a fetch
    // else {
    //   fetcher();
    // }

    // update unread messages if chat is minimized
    // if (
    //   minimzedChats.find((chat) => chat.roomId === data.returnChat.roomId)
    // ) {
    //   setMinimizedChats(
    //     minimzedChats.map((chat) => {
    //       if (chat.roomId === data.returnChat.roomId) {
    //         return { ...chat, hasNew: chat.hasNew + 1 };
    //       }
    //       return chat;
    //     })
    //   );
    //   return;
    // }

    // // update unread messages if chat is not open (in chatlog)
    // const updatedChats = chats.map((chat) => {
    //   if (chat.roomId === data.returnChat.roomId) {
    //     const unread = chat.unreadMessages + 1;
    //     return {
    //       ...chat,
    //       unreadChats: unread,
    //       lastChat: data.returnChat.data,
    //     };
    //   }
    //   return chat;
    // }) as roomOverview[];

    // setChats(updatedChats);
    // });

    // return () => {
    //   socket.off("new message");
    // };
  }, [openChats, minimzedChats, chats]);

  useEffect(() => {
    if (!chats) {
      return;
    }
    setUnread(
      chats.filter((chat) => chat.unreadMessages > 0).length > 0 ||
        minimzedChats.filter((chat) => chat.hasNew > 0).length > 0
    );
  }, [chats, minimzedChats]);

  return (
    <div
      id="Chat"
      className={
        "flex flex-row fixed bottom-0 right-0 gap-3 items-end z-[9999999] pointer-events-none " +
        (showLog
          ? " transform translate-x-0 transition-transform duration-500 ease-in-out"
          : " transform translate-x-[20rem] transition-transform duration-500 ease-in-out")
      }
    >
      <div id="OpenChats" className="flex gap-4 pointer-events-auto">
        {openChats.map((chat) => {
          return (
            <OpenChat
              roomId={chat.roomId}
              name={chat.name}
              minimizeEvent={minimizeChat}
              closeEvent={closeChat}
              key={chat.roomId}
            />
          );
        })}
      </div>
      <div
        id="MinimizedChats"
        className="flex flex-col gap-3 m-4 pointer-events-auto"
      >
        {minimzedChats.map((chat) => {
          return (
            <MinChat
              roomId={chat.roomId}
              name={chat.name}
              openEvent={openNewChat}
              key={chat.roomId}
              hasNew={chat.hasNew}
            />
          );
        })}
        <div
          className="bg-[#1976d2] w-16 h-16 border-4 border-gray-300 rounded-full flex justify-center items-center cursor-pointer relative drop-shadow-md"
          onClick={() => {
            setShowLog(!showLog);
          }}
        >
          <BsChatLeftText size={30} color="white" />
          {unread && (
            <div className="bg-red-500 w-4 h-4 rounded-full absolute top-0 right-0 shadow-md"></div>
          )}
        </div>
      </div>

      <ChatLog
        openEvent={openNewChat}
        show={showLog}
        isLoading={isLoading}
        error={error}
        chats={chats}
        toggleShowLog={toggleShowLog}
      />
    </div>
  );
};

export default Chat;
