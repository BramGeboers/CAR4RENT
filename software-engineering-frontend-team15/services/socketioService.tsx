import io, { Socket } from "socket.io-client";
import { sessionStorageService } from "./sessionStorageService";
import { UUID } from "crypto";

let socket: Socket | null = null;

const socketInitializer = () => {
  console.log("socketInitializer");
  if (socket) {
    console.log("socket already exists");
    socket.connect();
    return socket;
  }

  let url = process.env.NEXT_PUBLIC_SOCKETIO_URL as string;
  if (!url) {
    url = "http://localhost:8090";
  }

  socket = io(url, {
    query: {
      token: sessionStorageService.getItem("token"),
    },
  });

  console.log("socket", socket);

  socket.on("connect", () => {
    console.log("connected");
  });

  socket.on("disconnect", () => {
    console.log("disconnected");
  });

  socket.on("reconnect", () => {
    console.log("reconnected");
  });

  socket.on("reconnect_error", () => {
    console.log("reconnect error");
  });

  socket.on("reconnect_failed", () => {
    console.log("reconnect failed");
  });

  socket.on("connect_error", (error) => {
    console.log("connect error", error);
  });

  socket.on("connect_timeout", () => {
    console.log("connect timeout");
  });

  socket.on("error", (error) => {
    console.log("error", error);
  });

  socket.on("connect_failed", () => {
    console.log("connect failed");
  });

  console.log("socket", socket);
  return socket;
};

const disconnect = () => {
  if (!socket) return;
  console.log("disconnecting ignored");
  // socket.disconnect();
  socket = null;
};

const sendMessage = async (roomId: string, msg: string, localId: UUID) => {
  if (!socket) {
    socket = socketInitializer();
  }
  socket.emit("message", {
    localId: localId,
    roomId: roomId,
    msg: msg,
    token: sessionStorageService.getItem("token"),
  });
};

const getSocket = () => {
  if (!socket) {
    return socketInitializer();
  }
  return socket;
};

const sendIsTyping = (roomId: string, value: string) => {
  if (!socket) {
    socket = socketInitializer();
  }

  socket.emit("is typing", { roomId: roomId, value: value.length > 0 });

  // if (sessionStorageService.isCustomer()) {
  //     socket.emit('is typing', { receiverEmail, value })
  // } else {
  //     socket.emit('is typing', { receiverEmail, value: value.length > 0 })
  // }
};

export const socketioService = {
  socketInitializer,
  getSocket,
  sendMessage,
  disconnect,
  sendIsTyping,
};
