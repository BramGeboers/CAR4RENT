"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const socket_io_1 = require("socket.io");
const authentication_socetio_middleware_1 = __importDefault(require("./middleware/authentication.socetio.middleware"));
const socketio_service_1 = __importDefault(require("./service/socketio.service"));
const log_service_1 = __importDefault(require("./service/log.service"));
const dotenv_1 = __importDefault(require("dotenv"));
dotenv_1.default.config();
const port = parseInt(process.env.PORT) || 8090;
const startSocketServer = () => __awaiter(void 0, void 0, void 0, function* () {
    log_service_1.default.addLogServer("starting socket server", "INFO");
    const io = new socket_io_1.Server(port, {
        cors: {
            origin: "*",
        },
    });
    io.use(authentication_socetio_middleware_1.default);
    io.on('connection', (socket) => {
        log_service_1.default.addLogServer(`New client connected: ${socket.decoded.email}`, "INFO");
        socketio_service_1.default.connectSocket(socket);
        socket.on('public_message', (msg) => {
            console.log('message: ' + msg);
            try {
                const email = socket.decoded.email;
                io.emit('message', `${email}: ${msg}`); // Broadcast 
            }
            catch (error) {
                console.error("Error sending public message");
            }
        });
        socket.on('message', (data) => {
            try {
                const { roomId, msg, localId, token } = data;
                const senderEmail = socket.decoded.email;
                socketio_service_1.default.sendMessage(senderEmail, roomId, token, 'message ' + senderEmail + ' to room ' + roomId, msg, localId);
            }
            catch (error) {
                console.error("Error sending message");
            }
        });
        socket.on('is typing', (data) => {
            try {
                const { receiverEmail, value } = data;
                const senderEmail = socket.decoded.email;
                socketio_service_1.default.sendTypingToEmployee(senderEmail, receiverEmail, 'is typing ' + senderEmail, value);
                socketio_service_1.default.sendTypingToCustomer(senderEmail, receiverEmail, 'is typing ' + senderEmail, value);
            }
            catch (error) {
                console.error("Error sending is typing");
            }
        });
        socket.on('disconnect', () => {
            try {
                socketio_service_1.default.disconnectSocket(socket);
                log_service_1.default.addLogServer(`Client disconnected: ${socket.decoded.email}`, "INFO");
            }
            catch (error) {
                console.error("Error disconnecting socket");
            }
        });
        socket.on('error', (err) => {
            log_service_1.default.addLogServer(`Received error from client: ${socket.decoded.email}`, "ERROR");
            console.error("Error from client");
        });
        logIncomingEvents(socket);
    });
    function logIncomingEvents(socket) {
        Object.keys(socket._events).forEach((eventName) => {
            if (!eventName.startsWith('_')) {
                socket.on(eventName, (...args) => {
                    // LogService.addLogServer(`Received event '${eventName}':`, "INFO")
                    // console.log(`Received event '${eventName}':`, ...args);
                });
            }
        });
    }
    log_service_1.default.addLogServer("socket server is running on port " + port, "INFO");
});
startSocketServer();
