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
Object.defineProperty(exports, "__esModule", { value: true });
class Log {
    constructor(log) {
        this.ip = log.ip;
        this.message = log.message;
        this.level = log.level;
        this.timestamp = Date.now();
    }
}
class LogService {
    constructor() { }
    ;
    static getAllLogs() {
        return __awaiter(this, void 0, void 0, function* () {
            return [];
            // return logDb.getAllLogs();
        });
    }
    static getLogById(id) {
        return __awaiter(this, void 0, void 0, function* () {
            return null;
            // const log = await logDb.getLogById(id);
            // if (log === null) {
            //     throw new Error('Log not found');
            // }
            // return log;
        });
    }
    static createLog(log) {
        return __awaiter(this, void 0, void 0, function* () {
            return log;
            // return logDb.createLog(log);
        });
    }
    static getNextAmountOfLogs(logId, amount) {
        return __awaiter(this, void 0, void 0, function* () {
            return [];
            // return logDb.getNextAmountOfLogs(logId, amount);
        });
    }
    static getFirstAmountOfLogs(amount) {
        return __awaiter(this, void 0, void 0, function* () {
            return [];
            // return logDb.getFirstAmountOfLogs(amount);
        });
    }
    static formatDate(timestamp) {
        const months = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];
        const date = new Date(timestamp);
        const month = months[date.getMonth()];
        const day = date.getDate();
        const year = date.getFullYear();
        const hours = ('0' + date.getHours()).slice(-2);
        const minutes = ('0' + date.getMinutes()).slice(-2);
        const seconds = ('0' + date.getSeconds()).slice(-2);
        return `${month} ${day}, ${year} ${hours}:${minutes}:${seconds}`;
    }
    static addLog(log) {
        return __awaiter(this, void 0, void 0, function* () {
            const newLog = yield LogService.createLog(log);
            console.log(`${this.formatDate(newLog.timestamp)} - ${newLog.ip} - ${newLog.level} - ${newLog.message}`);
            // socketioService.sendToAllSocketsOfRole('ADMIN', 'log', newLog);
        });
    }
    static addLogServer(message, level) {
        return __awaiter(this, void 0, void 0, function* () {
            const log = new Log({
                ip: 'SERVER',
                message: message,
                level: level
            });
            yield LogService.addLog(log);
        });
    }
    static addLogClient(message, level, ip) {
        return __awaiter(this, void 0, void 0, function* () {
            const log = new Log({
                ip: ip,
                message: message,
                level: level
            });
            yield LogService.addLog(log);
        });
    }
}
exports.default = LogService;
