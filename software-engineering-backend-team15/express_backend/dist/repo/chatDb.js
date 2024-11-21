"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const dotenv = __importStar(require("dotenv"));
dotenv.config();
const backendUrl = process.env.SPRINGBOOT_URL;
class chatDb {
    static isAvailable(localId, token) {
        return __awaiter(this, void 0, void 0, function* () {
            const response = yield fetch(backendUrl + '/chat/available?chatId=' + localId, {
                method: 'GET',
                headers: {
                    'authorization': 'Bearer ' + token
                },
            });
            return response;
        });
    }
    static send(localId, roomId, data, token) {
        return __awaiter(this, void 0, void 0, function* () {
            const response = yield fetch(backendUrl + '/chat/send', {
                method: 'POST',
                headers: {
                    'authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    localId: localId,
                    roomId: roomId,
                    data: data
                })
            });
            return response;
        });
    }
    static getEmails(roomId, token) {
        return __awaiter(this, void 0, void 0, function* () {
            const response = yield fetch(backendUrl + '/chat/room/' + roomId + "/emails", {
                method: 'GET',
                headers: {
                    'authorization': 'Bearer ' + token
                },
            });
            return response;
        });
    }
    static askBot(message, token, roomId) {
        return __awaiter(this, void 0, void 0, function* () {
            const response = yield fetch(backendUrl + '/bot?message=' + message + "&roomId=" + roomId, {
                method: 'GET',
                headers: {
                    'authorization': 'Bearer ' + token
                },
            });
            return response;
        });
    }
}
exports.default = chatDb;
