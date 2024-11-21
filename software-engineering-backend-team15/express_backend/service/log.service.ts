// import Log from '../domain/model/Log';
// import logDb from '../domain/data-access/log.db';
import socketioService from './socketio.service';

type LogType = {
    ip: string,
    message: string,
    level: string,
    timestamp?: number
}

class Log {
    ip: string;
    message: string;
    level: string;
    timestamp: number;

    constructor(log: LogType) {
        this.ip = log.ip;
        this.message = log.message;
        this.level = log.level;
        this.timestamp = Date.now();
    }

}



export default class LogService {

    constructor() { };

    static async getAllLogs(): Promise<Log[]> {
        return []
        // return logDb.getAllLogs();
    }

    static async getLogById(id: number): Promise<Log | null> {
        return null
        // const log = await logDb.getLogById(id);
        // if (log === null) {
        //     throw new Error('Log not found');
        // }
        // return log;
    }

    static async createLog(log: Log): Promise<Log> {
        return log

        // return logDb.createLog(log);
    }

    static async getNextAmountOfLogs(logId: number, amount: number): Promise<Log[]> {
        return []
        // return logDb.getNextAmountOfLogs(logId, amount);
    }

    static async getFirstAmountOfLogs(amount: number): Promise<Log[]> {
        return []
        // return logDb.getFirstAmountOfLogs(amount);
    }

    static formatDate(timestamp: any) {
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

    static async addLog(log: Log) {
        const newLog = await LogService.createLog(log);
        console.log(`${this.formatDate(newLog.timestamp)} - ${newLog.ip} - ${newLog.level} - ${newLog.message}`)
        // socketioService.sendToAllSocketsOfRole('ADMIN', 'log', newLog);
    }

    static async addLogServer(message: string, level: string) {
        const log = new Log({
            ip: 'SERVER',
            message: message,
            level: level
        });
        await LogService.addLog(log);
    }

    static async addLogClient(message: string, level: string, ip: string) {
        const log = new Log({
            ip: ip,
            message: message,
            level: level
        });
        await LogService.addLog(log);
    }

}