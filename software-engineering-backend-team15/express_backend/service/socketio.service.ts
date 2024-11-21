import { Socket } from "socket.io";
import os, { totalmem } from 'os'
// import pidusage from 'pidusage';
// import chatService from "./chat.service";
import { UUID, randomUUID } from "crypto";
// import chatDb from "../domain/data-access/chat.db";
// import employeeService from "./employee.service";
// import customerService from "./customer.service";
import LogService from "./log.service";
import chatDb from "../repo/chatDb";
import dotenv from 'dotenv'
import { ro } from "date-fns/locale";

dotenv.config();



type SocketDict = {
    [key: string]: Socket & { decoded: any }
}

type IntervalDict = {
    [key: string]: NodeJS.Timeout
}

type ChatJSON = {
    id: UUID;
    timestamp: string;
    senderEmail: string;
    roomId: string;
    data: string;
}




let sockets: SocketDict = {};
let activeIntervals: IntervalDict = {}


export default class socketioService {

    constructor() { };


    static connectSocket(socket: Socket & { decoded:any }) {
        sockets[socket.id] = socket

        if (socket.decoded.roles.includes('ADMIN')) {
            console.log('admin connected')
            activeIntervals[socket.id] = setInterval(() => {
                // this.sendSystemMetrics(socket)
            }, 1000)
        }


    }

    static disconnectSocket(socket: Socket & { decoded:any }) {
        clearInterval(activeIntervals[socket.id])

        delete activeIntervals[socket.id]
        delete sockets[socket.id]
    }

    static getSocketsOfRole(role: string) {
        let socketsOfRole = []
        for (const socketId in sockets) {
            if (sockets[socketId].decoded.roles.includes(role)) {
                socketsOfRole.push(sockets[socketId])
            }
        }
        return socketsOfRole
    }

    static sendToAllSocketsOfRole(role: string, event: string, data: any) {
        const socketsOfRole = this.getSocketsOfRole(role)
        for (const socket of socketsOfRole) {
            socket.emit(event, data)
        }
    }

    static async sendMessage(senderEmail: string, roomId: string, token: string, event: string, data: string, localId: UUID) {
        const startTime = new Date().getTime()
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

        const chatAvailable = await chatDb.isAvailable(localId, token)
        if (!chatAvailable || !chatAvailable.ok) {
            console.error("error checking chat availability")
            return
        }

        console.log('time1: ' + (new Date().getTime() - startTime) + 'ms')

        const chatAvailableJson = await chatAvailable.json()
        if (!chatAvailableJson) {
            LogService.addLogServer('ignoring existing chat with id: ' + localId, 'INFO')
            // chat already exists (sended when server was down)
            return
        }

        console.log('time2: ' + (new Date().getTime() - startTime) + 'ms')


        try {
            const returndata = await chatDb.send(localId, roomId, data, token)  
            if (!returndata.ok){
                //chat already send
                return
            }
        } catch (error) {
            console.error("error sending chat to db")
            return
        }

        console.log('time3: ' + (new Date().getTime() - startTime) + 'ms')
        

        const emailReceivers = await chatDb.getEmails(roomId, token)
        if (!emailReceivers.ok) {
            console.error("error getting email receivers")
            return
        }

        console.log('time4: ' + (new Date().getTime() - startTime) + 'ms')

        const emailReceiversJson: string[] = await emailReceivers.json()



        const returnChat: ChatJSON = {
            id: localId,
            timestamp: new Date().toISOString(),
            senderEmail: senderEmail,
            roomId: roomId,
            data: data
        }

        for (const socketId in sockets) {
            if (emailReceiversJson.includes(sockets[socketId].decoded.email)) {
                sockets[socketId].emit('new message', { returnChat })

            }

        }

        console.log('time5: ' + (new Date().getTime() - startTime) + 'ms')

        if (roomId.startsWith('bot-')) {

            this.sendBotMessage(data, senderEmail, roomId, token)
        }

        console.log('time6: ' + (new Date().getTime() - startTime) + 'ms')


    }


    static async sendBotMessage(message: string, senderEmail, roomId: string, token: string) {
        let response: Response;
        try {
            response = await chatDb.askBot(message, token, roomId)
        } catch (error) {
            console.error("error asking bot")
            return
        }
        if (!response.ok) {
            console.error("error asking bot")
            return
        }


        const responseJson : ChatJSON = await response.json()

        const returnChat: ChatJSON = {
            id: responseJson.id,
            timestamp: responseJson.timestamp,
            senderEmail: responseJson.senderEmail,
            roomId: responseJson.roomId,
            data: responseJson.data
        }


        for (const socketId in sockets) {
            if (sockets[socketId].decoded.email === senderEmail) {
                sockets[socketId].emit('new message', { returnChat })
            }
        }
    }


    static async sendTypingToEmployee(senderEmail: string, receiverEmail: string, event: string, data: string) {
        // const employee = await employeeService.getEmployeeByEmail(receiverEmail)
        // if (!employee) {
        //     return
        // }


        for (const socketId in sockets) {
            if (sockets[socketId].decoded.email === receiverEmail) {
                sockets[socketId].emit(event, data)
            }
        }
    }

    static async sendTypingToCustomer(senderEmail: string, receiverEmail: string, event: string, data: string) {
        // const customer = await customerService.getCustomerByEmail(receiverEmail)
        // if (!customer) {
        //     return
        // }


        for (const socketId in sockets) {
            if (sockets[socketId].decoded.email === receiverEmail) {
                sockets[socketId].emit(event, data)
            }
        }
    }







    static sendToAllSockets(event: string, data: any) {
        for (const socketId in sockets) {
            sockets[socketId].emit(event, data)
        }
    }

    static getOnlineCustomers() {
        let customers = []
        for (const socketId in sockets) {
            if (sockets[socketId].decoded.roles.includes('CUSTOMER')) {
                customers.push(sockets[socketId].decoded.email)
            }
        }
        return customers
    }

    static getOnlineEmployees() {
        let employees = []
        for (const socketId in sockets) {
            if (sockets[socketId].decoded.roles.includes('EMPLOYEE')) {
                employees.push(sockets[socketId].decoded.email)
            }
        }
        return employees
    }

    // static async sendSystemMetrics(socket: Socket & { decoded: any }) {
    //     // const cpuUsage = os.loadavg()[0]


    //     const cpuUsage = (await pidusage(process.pid)).cpu / 100;

    //     const memUsage = (os.totalmem() - os.freemem()) / os.totalmem()
    //     const data = {
    //         cpuUsage,
    //         memUsage,
    //         totalmem: os.totalmem(),
    //         freemem: os.freemem()
    //     }
    //     socket.emit('systemMetrics', data)
    // }


}
