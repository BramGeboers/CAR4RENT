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
// import chatDb from "../domain/data-access/chat.db";
// import employeeService from "./employee.service";
// import customerService from "./customer.service";
const log_service_1 = __importDefault(require("./log.service"));
const chatDb_1 = __importDefault(require("../repo/chatDb"));
const dotenv_1 = __importDefault(require("dotenv"));
dotenv_1.default.config();
let sockets = {};
let activeIntervals = {};
class socketioService {
    constructor() { }
    ;
    static connectSocket(socket) {
        sockets[socket.id] = socket;
        if (socket.decoded.roles.includes('ADMIN')) {
            console.log('admin connected');
            activeIntervals[socket.id] = setInterval(() => {
                // this.sendSystemMetrics(socket)
            }, 1000);
        }
    }
    static disconnectSocket(socket) {
        clearInterval(activeIntervals[socket.id]);
        delete activeIntervals[socket.id];
        delete sockets[socket.id];
    }
    static getSocketsOfRole(role) {
        let socketsOfRole = [];
        for (const socketId in sockets) {
            if (sockets[socketId].decoded.roles.includes(role)) {
                socketsOfRole.push(sockets[socketId]);
            }
        }
        return socketsOfRole;
    }
    static sendToAllSocketsOfRole(role, event, data) {
        const socketsOfRole = this.getSocketsOfRole(role);
        for (const socket of socketsOfRole) {
            socket.emit(event, data);
        }
    }
    static sendMessage(senderEmail, roomId, token, event, data, localId) {
        return __awaiter(this, void 0, void 0, function* () {
            const startTime = new Date().getTime();
            // console.log(event)
            // if (!chatService.canSend(senderEmail, receiverEmail)) {
            //     for (const socketId in sockets) {
            //         if (sockets[socketId].decoded.email === senderEmail) {
            //             if (sockets[socketId].decoded.email === senderEmail) {
            //                 sockets[socketId].emit('deny', data)
            //             }
            //         }
            //     // }
            //     return
            // }
            const chatAvailable = yield chatDb_1.default.isAvailable(localId, token);
            if (!chatAvailable.ok) {
                console.error("error checking chat availability");
                return;
            }
            console.log('time1: ' + (new Date().getTime() - startTime) + 'ms');
            const chatAvailableJson = yield chatAvailable.json();
            if (!chatAvailableJson) {
                log_service_1.default.addLogServer('ignoring existing chat with id: ' + localId, 'INFO');
                // chat already exists (sended when server was down)
                return;
            }
            console.log('time2: ' + (new Date().getTime() - startTime) + 'ms');
            try {
                const returndata = yield chatDb_1.default.send(localId, roomId, data, token);
                if (!returndata.ok) {
                    //chat already send
                    return;
                }
            }
            catch (error) {
                console.error("error sending chat to db");
                return;
            }
            console.log('time3: ' + (new Date().getTime() - startTime) + 'ms');
            const emailReceivers = yield chatDb_1.default.getEmails(roomId, token);
            if (!emailReceivers.ok) {
                console.error("error getting email receivers");
                return;
            }
            console.log('time4: ' + (new Date().getTime() - startTime) + 'ms');
            const emailReceiversJson = yield emailReceivers.json();
            const returnChat = {
                id: localId,
                timestamp: new Date().toISOString(),
                senderEmail: senderEmail,
                roomId: roomId,
                data: data
            };
            for (const socketId in sockets) {
                if (emailReceiversJson.includes(sockets[socketId].decoded.email)) {
                    sockets[socketId].emit('new message', { returnChat });
                }
            }
            console.log('time5: ' + (new Date().getTime() - startTime) + 'ms');
            if (roomId.startsWith('bot-')) {
                this.sendBotMessage(data, senderEmail, roomId, token);
            }
            console.log('time6: ' + (new Date().getTime() - startTime) + 'ms');
        });
    }
    static sendBotMessage(message, senderEmail, roomId, token) {
        return __awaiter(this, void 0, void 0, function* () {
            let response;
            try {
                response = yield chatDb_1.default.askBot(message, token, roomId);
            }
            catch (error) {
                console.error("error asking bot");
                return;
            }
            if (!response.ok) {
                console.error("error asking bot");
                return;
            }
            const responseJson = yield response.json();
            const returnChat = {
                id: responseJson.id,
                timestamp: responseJson.timestamp,
                senderEmail: responseJson.senderEmail,
                roomId: responseJson.roomId,
                data: responseJson.data
            };
            for (const socketId in sockets) {
                if (sockets[socketId].decoded.email === senderEmail) {
                    sockets[socketId].emit('new message', { returnChat });
                }
            }
        });
    }
    static sendTypingToEmployee(senderEmail, receiverEmail, event, data) {
        return __awaiter(this, void 0, void 0, function* () {
            // const employee = await employeeService.getEmployeeByEmail(receiverEmail)
            // if (!employee) {
            //     return
            // }
            for (const socketId in sockets) {
                if (sockets[socketId].decoded.email === receiverEmail) {
                    sockets[socketId].emit(event, data);
                }
            }
        });
    }
    static sendTypingToCustomer(senderEmail, receiverEmail, event, data) {
        return __awaiter(this, void 0, void 0, function* () {
            // const customer = await customerService.getCustomerByEmail(receiverEmail)
            // if (!customer) {
            //     return
            // }
            for (const socketId in sockets) {
                if (sockets[socketId].decoded.email === receiverEmail) {
                    sockets[socketId].emit(event, data);
                }
            }
        });
    }
    static sendToAllSockets(event, data) {
        for (const socketId in sockets) {
            sockets[socketId].emit(event, data);
        }
    }
    static getOnlineCustomers() {
        let customers = [];
        for (const socketId in sockets) {
            if (sockets[socketId].decoded.roles.includes('CUSTOMER')) {
                customers.push(sockets[socketId].decoded.email);
            }
        }
        return customers;
    }
    static getOnlineEmployees() {
        let employees = [];
        for (const socketId in sockets) {
            if (sockets[socketId].decoded.roles.includes('EMPLOYEE')) {
                employees.push(sockets[socketId].decoded.email);
            }
        }
        return employees;
    }
}
exports.default = socketioService;
