import { chatService } from "@/services/chatService";
import { ChatJSON } from "@/types";
import React, { use, useEffect } from "react";
import { IoSendSharp } from "react-icons/io5";
import { useRef } from "react";
import { FaMinus } from "react-icons/fa6";
import { FaXmark } from "react-icons/fa6";
import { emojiMap } from "./emojiMap";
import { socketioService } from "@/services/socketioService";
import { useInView } from "react-intersection-observer";
import { UUID, randomUUID } from "crypto";
import { v4 as uuidv4 } from "uuid";
import { sessionStorageService } from "@/services/sessionStorageService";
import { transform } from "./transform";
import { mutate } from "swr";

type openChatProps = {
  roomId: string;
  name: string;
  closeEvent: (id: string) => void;
  minimizeEvent: (id: string) => void;
};

const OpenChat: React.FC<openChatProps> = ({
  roomId,
  name,
  closeEvent,
  minimizeEvent,
}) => {
  const [chat, setChat] = React.useState<(ChatJSON & { send: boolean })[]>([]);

  const [typed, setTyped] = React.useState<string>("");
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const loadingRef = useRef<HTMLDivElement>(null);
  const [ref, inView] = useInView();
  const [firstId, setFirstId] = React.useState<UUID | null>(null);
  const [previosFirstId, setPreviousFirstId] = React.useState<UUID | null>(
    null
  );
  const [end, setEnd] = React.useState<boolean>(false);
  const [showGoToBottom, setShowGoToBottom] = React.useState<boolean>(false);
  const [bottomRef, inViewBottom] = useInView();
  const [gettingNextChats, setGettingNextChats] =
    React.useState<boolean>(false);
  const [otherTyped, setOtherTyped] = React.useState<string>("");

  useEffect(() => {
    chatService
      .getFirstChats(roomId)
      .then((res) => {
        if (res) {
          if (res.length === 0) {
            setChat([]);
            return;
          }

          res.sort(
            (a, b) =>
              new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime()
          );
          setFirstId(res[0].id);
          const sendres = res.map((c) => {
            return { ...c, send: true };
          });
          setChat(sendres);
          if (res.length < 10) {
            setEnd(true);
          }
        }
      })
      .catch((err) => {
        alert("error getting chats");
      });
  }, []);

  useEffect(() => {
    if (gettingNextChats) {
      teleportToRef();
      setGettingNextChats(false);
    } else {
      scrollToBottom();
    }
  }, [chat]);

  const scrollToBottom = (instant = false) => {
    if (messagesEndRef.current) {
      if (instant) {
        messagesEndRef.current.scrollIntoView({ behavior: "instant" });
        return;
      }
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  };

  const teleportToRef = () => {
    if (loadingRef.current) {
      loadingRef.current.scrollIntoView({ behavior: "instant" });
    }
  };

  // not perfect, :// is 😕/
  function replaceTextWithEmoji(message: string) {
    message = message + " ";
    const regex = new RegExp(
      `(^|[^\\p{L}\\p{N}])(?:${Object.keys(emojiMap)
        .map(escapeRegExp)
        .join("|")})(?![\\p{L}\\p{N}]|$)`,
      "gu"
    );

    type EmojiMap = {
      [key: string]: string;
    };

    return message.replace(regex, (match) => {
      const emoji = (emojiMap as EmojiMap)[match];
      // Return the emoji if found in the map, otherwise return the original match
      return emoji ? emoji : match;
    });
  }

  function escapeRegExp(string: string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  }

  const sendMessage = () => {
    if (typed === "") {
      return;
    }

    onChange("");

    const toSend = replaceTextWithEmoji(typed);

    const localId = uuidv4() as UUID;

    socketioService.sendMessage(roomId, toSend, localId);

    const universalDate = new Date();
    universalDate.setHours(universalDate.getHours() + 1); // Date that is stored in db

    setChat([
      ...chat,
      {
        id: localId,
        senderEmail: sessionStorageService.getItem("email") as string,
        data: toSend,
        timestamp: new Date().toISOString(),
        send: false,
        roomId: roomId,
      },
    ]);
    setTyped("");
  };

  const addMessage = (chatInput: ChatJSON) => {
    console.log("adding message");
    setChat([...chat, { ...chatInput, send: true }]);
  };

  useEffect(() => {
    const socket = socketioService.getSocket();
    if (!socket) {
      return;
    }
    socket.on("message " + roomId, (chatInput: ChatJSON) => {
      addMessage(chatInput);
    });

    socket.on("new message", (data: { returnChat: ChatJSON }) => {
      console.log("new message2", data);
      mutate("chatOverview");
      const chatInput = data.returnChat;
      if (chatInput.roomId !== roomId) {
        return;
      }
      if (chatInput.senderEmail === sessionStorageService.getItem("email")) {
        let found = false;
        console.log("setting chat to send");
        setChat(
          chat.map((c) => {
            if (c.id === chatInput.id) {
              found = true;
              return { ...c, send: true };
            }
            return c;
          })
        );

        if (!found) {
          // chat is send from a different window
          addMessage(chatInput);
        }
        return;
      }
      addMessage(chatInput);
    });

    socket.on("is typing " + roomId, (value: string | boolean) => {
      if (value === true) {
        setOtherTyped("Typing...");
        return;
      } else if (value === false) {
        setOtherTyped("");
        return;
      } else {
        setOtherTyped(value);
      }
    });

    socket.on("private message error " + roomId, (chatInput: ChatJSON) => {
      console.error("error");
    });

    return () => {
      socket.off("message " + roomId);
      socket.off("new message");
      socket.off("private message " + roomId);
      socket.off("private message confirm " + roomId);
      socket.off("private message error " + roomId);
      socket.off("is typing " + roomId);
    };
  }, [chat]);

  useEffect(() => {
    const interval = setInterval(() => {
      const socket = socketioService.getSocket();
      if (!socket) {
        return;
      }
      chat.forEach((c) => {
        if (!c.send) {
          console.log(c);
          socketioService.sendMessage(roomId, c.data, c.id);
        }
      });
    }, 10000);

    return () => clearInterval(interval);
  }, [chat]);

  const isOwnMessage = (senderEmail: string) => {
    return senderEmail === sessionStorageService.getItem("email");
  };

  const lastTimestampsSenders: {
    [key: string]: { lastChatTimestamp: Date; lastChatDate: Date };
  } = {};

  const foundChats: { [key: string]: boolean } = {};

  const showSender = (chat: ChatJSON) => {
    if (foundChats.hasOwnProperty(chat.id)) {
      return foundChats[chat.id];
    }

    const chatDate = new Date(chat.timestamp);
    const senderEmail = chat.senderEmail;

    if (!lastTimestampsSenders[senderEmail]) {
      lastTimestampsSenders[senderEmail] = {
        lastChatTimestamp: chatDate,
        lastChatDate: chatDate,
      };
      foundChats[chat.id] = true;
      return true;
    }

    const lastChatTimestamp =
      lastTimestampsSenders[senderEmail].lastChatTimestamp;
    const lastChatDate = lastTimestampsSenders[senderEmail].lastChatDate;

    // Check if it's the first chat of the day
    if (
      lastChatDate.getDate() !== chatDate.getDate() ||
      lastChatDate.getMonth() !== chatDate.getMonth() ||
      lastChatDate.getFullYear() !== chatDate.getFullYear()
    ) {
      lastTimestampsSenders[senderEmail] = {
        lastChatTimestamp: chatDate,
        lastChatDate: chatDate,
      };
      foundChats[chat.id] = true;
      return true;
    }

    // Check if it's the first chat of that sender or if the last chat was more than 5 minutes ago
    if (lastChatTimestamp.getTime() + 5 * 60 * 1000 < chatDate.getTime()) {
      lastTimestampsSenders[senderEmail] = {
        lastChatTimestamp: chatDate,
        lastChatDate: lastChatDate,
      };
      foundChats[chat.id] = true;
      return true;
    }

    // Update lastChatTimestamp if it's not the first chat or more than 5 minutes ago
    lastTimestampsSenders[senderEmail].lastChatTimestamp = chatDate;
    foundChats[chat.id] = false;
    return false;
  };

  let lastChat: String = "";

  const isNewChat = (chat: ChatJSON) => {
    if (lastChat === chat.senderEmail) {
      return false;
    }

    lastChat = chat.senderEmail;
    return true;
  };

  let lastDate: string = "";

  const showTimestamp = (chat: ChatJSON) => {
    const currentDate = new Date(chat.timestamp).toLocaleDateString();

    if (lastDate === currentDate) {
      return false;
    }

    lastDate = currentDate;
    return true;
  };

  const formatTime = (dateString: string): string => {
    const date = new Date(dateString);
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${hours}:${minutes}`;
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const currentDate = new Date();
    const differenceInDays = Math.floor(
      (currentDate.getTime() - date.getTime()) / (1000 * 60 * 60 * 24)
    );

    if (differenceInDays <= 5) {
      return date.toLocaleDateString("en-US", { weekday: "long" });
    } else {
      const options: Intl.DateTimeFormatOptions = {
        day: "numeric",
        month: "short",
        year: "numeric",
      };
      return date.toLocaleDateString("en-US", options);
    }
  };

  const getNameFromEmail = (email: string) => {
    return email
      .split("@")[0]
      .split(".")
      .map((s) => s[0].toUpperCase() + s.slice(1).toLowerCase())
      .join(" ");
  };

  useEffect(() => {
    if (inView) {
      nextChats();
    }
  }, [inView]);

  useEffect(() => {
    if (inViewBottom) {
      setShowGoToBottom(false);
    } else {
      setShowGoToBottom(true);
    }
  }, [inViewBottom]);

  const nextChats = () => {
    if (!firstId) {
      return;
    }
    if (end) {
      return;
    }
    setGettingNextChats(true);
    setPreviousFirstId(firstId);
    chatService
      .getNextChats(roomId, firstId)
      .then((res) => {
        if (res) {
          res.sort(
            (a, b) =>
              new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime()
          );
          setFirstId(res[0].id);
          const sendres = res.map((c) => {
            return { ...c, send: true };
          });
          setChat([...sendres, ...chat]);
        }
      })
      .catch((err) => {
        setEnd(true);
      });
  };

  const onChange = (value: string) => {
    setTyped(value);
    socketioService.sendIsTyping(roomId, value);
  };

  return (
    <div className="bg-gray-300 rounded-t-lg">
      <div className="flex items-center bg-primary rounded-t-lg w-96">
        <h1 className="flex-grow text-2xl font-bold text-white py-2 px-3 drop-shadow-sm truncate ">
          {name}
        </h1>

        <div
          className="text-white p-2 transition-colors duration-300 ease-in-out cursor-pointer hover:text-gray-400"
          onClick={() => {
            onChange("");
            minimizeEvent(roomId);
          }}
        >
          <FaMinus height={40} width={40} />
        </div>

        <div
          className="text-white p-2 transition-colors duration-300 ease-in-out cursor-pointer hover:text-gray-400"
          onClick={() => {
            onChange("");
            closeEvent(roomId);
          }}
        >
          <FaXmark height={40} width={40} />
        </div>
      </div>

      <div className="p-3 overflow-y-scroll w-96 h-72">
        <div key="loadingreftop" ref={ref} className="loading text-center">
          {!end && chat && chat.length > 9 && "Loading..."}
          {end && chat && "Start of your conversation"}
          {chat && chat.length === 0 && "Start your conversation with " + name}
        </div>

        {chat &&
          chat.map((chat, index) => {
            return (
              <>
                {previosFirstId === chat.id && (
                  <div
                    ref={loadingRef}
                    key={index}
                    className="loadingref ref"
                  />
                )}
                {showTimestamp(chat) && (
                  <div className="flex items-center justify-center">
                    <p className="bg-slate-400 w-fit rounded-lg p-1 m-2 pl-2 pr-2">
                      {formatDate(chat.timestamp)}
                    </p>
                  </div>
                )}
                <div
                  key={chat.id}
                  className={
                    "flex flex-col mt-1" +
                    (isOwnMessage(chat.senderEmail)
                      ? " own items-end " + (chat.send ? " send" : " nosend")
                      : " other items-start ")
                  }
                >
                  {!chat.send && <p>sending...</p>}
                  <div
                    className={
                      "data rounded-lg p-2 max-w-[80%] break-words relative pb-2 " +
                      (isOwnMessage(chat.senderEmail)
                        ? chat.send
                          ? " bg-white rounded-br-none "
                          : "bg-slate-400 "
                        : " bg-green-100 rounded-bl-none ") +
                      (showSender(chat) ? " mt-3 " : "")
                    }
                  >
                    <p
                      className={
                        "sender text-primary " +
                        (isOwnMessage(chat.senderEmail)
                          ? " text-right "
                          : " text-left ")
                      }
                    >
                      {/* {(showSender(chat) || isNewChat(chat)) && */}
                      {!isOwnMessage(chat.senderEmail) &&
                        getNameFromEmail(chat.senderEmail)}
                    </p>
                    <p>
                      {transform(chat.data)}
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </p>
                    <span className=" text-gray-500 text-sm absolute bottom-1 right-1 ">
                      {formatTime(chat.timestamp)}
                    </span>
                  </div>
                </div>
              </>
            );
          })}
        {!previosFirstId && <div ref={loadingRef} className="ref" />}
        <div ref={messagesEndRef} className="ref" />
        <div ref={bottomRef} className="ref" />
        {chat && showGoToBottom && (
          <div
            key="goToBottom"
            className="bottom"
            onClick={() => scrollToBottom()}
          >
            Go to Bottom
          </div>
        )}
      </div>
      <div className="otherTyped">
        {otherTyped !== "" && <p>"Typing..."</p>}
      </div>

      <div className="grid grid-cols-6 w-96 ">
        <input
          type="text"
          onChange={(e) => onChange(e.target.value)}
          value={typed}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              sendMessage();
            }
          }}
          tabIndex={0}
          className="col-span-5 p-2 m-2 rounded-lg border-2"
        />
        <div
          className="col-span-1  text-primary flex justify-center items-center cursor-pointer text-3xl"
          onClick={sendMessage}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              sendMessage();
            }
          }}
          tabIndex={0}
        >
          <IoSendSharp />
        </div>
      </div>
    </div>
  );
};

export default OpenChat;
