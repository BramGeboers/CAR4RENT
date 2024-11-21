import React from "react";

type minChatProps = {
  roomId: string;
  name: string;
  openEvent: (roomId: string, name: string) => void;
  hasNew: number;
};

const MinChat: React.FC<minChatProps> = ({
  roomId,
  name,
  openEvent,
  hasNew,
}) => {
  const getInitials = () => {
    const splitName = name.split(" ");
    if (splitName.length === 1) {
      const newSplitName = name.split("");
      return newSplitName[0] + newSplitName[newSplitName.length - 1];
    }
    return splitName.map((name) => name[0]).join("");
  };

  return (
    <div
      onClick={() => openEvent(roomId, name)}
      className="bg-[#1976d2]  border-gray-300 border-4 w-16 h-16 rounded-full flex justify-center items-center cursor-pointer relative text-3xl text-white shadow-md"
    >
      {hasNew ? (
        <div className="bg-red-500 w-8 h-8 rounded-full absolute top-0 right-0 text-center text-xl">
          {hasNew > 99 ? "99+" : hasNew}
        </div>
      ) : null}
      <div>{getInitials().toUpperCase()}</div>
    </div>
  );
};

export default MinChat;
