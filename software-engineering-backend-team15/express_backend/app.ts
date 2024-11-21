import { Server, Socket } from 'socket.io';

import authenticationSocketIoMiddleware from './middleware/authentication.socetio.middleware';
import socketioService from './service/socketio.service';
import LogService from './service/log.service';
import dotenv from 'dotenv';


dotenv.config();

const port = parseInt(process.env.PORT) || 8090;

const startSocketServer = async () => {
    LogService.addLogServer("starting socket server", "INFO")
    const io = new Server(port, {
        cors: {
            origin: "*",
        },
    });

    io.use(authenticationSocketIoMiddleware as any)

    io.on('connection', (socket: any) => {
        LogService.addLogServer(`New client connected: ${socket.decoded.email}`, "INFO")

        socketioService.connectSocket(socket)

        socket.on('public_message', (msg: string) => {
            console.log('message: ' + msg);
            try {
                const email = socket.decoded.email;
                io.emit('message', `${email}: ${msg}`); // Broadcast 
            } catch (error) {
                console.error("Error sending public message")
            }

        });

        socket.on('message', (data: any) => {
            try {
                const { roomId, msg, localId, token } = data;
                const senderEmail = socket.decoded.email;
                socketioService.sendMessage(senderEmail, roomId, token, 'message ' + senderEmail + ' to room ' + roomId, msg, localId)
            } catch (error) {
                console.error("Error sending message")
            }
        });

        socket.on('is typing', (data: any) => {
            try {
                const { receiverEmail, value } = data;
                const senderEmail = socket.decoded.email;
                socketioService.sendTypingToEmployee(senderEmail, receiverEmail, 'is typing ' + senderEmail, value)
                socketioService.sendTypingToCustomer(senderEmail, receiverEmail, 'is typing ' + senderEmail, value)
            } catch (error) {
                console.error("Error sending is typing")
            }
        });

        socket.on('disconnect', () => {
            try {
                socketioService.disconnectSocket(socket)
                LogService.addLogServer(`Client disconnected: ${socket.decoded.email}`, "INFO")
            } catch (error) {
                console.error("Error disconnecting socket")
            }
        });

        socket.on('error', (err: any) => {
            LogService.addLogServer(`Received error from client: ${socket.decoded.email}`, "ERROR")
            console.error("Error from client")
        })

        logIncomingEvents(socket)
    });

    function logIncomingEvents(socket: any) {
        Object.keys(socket._events).forEach((eventName) => {
            if (!eventName.startsWith('_')) {
                socket.on(eventName, (...args: any[]) => {
                    // LogService.addLogServer(`Received event '${eventName}':`, "INFO")
                    // console.log(`Received event '${eventName}':`, ...args);
                });
            }
        });
    }

    LogService.addLogServer("socket server is running on port " + port, "INFO")
};

// const main = async () => {
//     let counter = 0;
//     while (true) {
//         counter++;
//         console.log("Restarting server, attempt: " + counter)
        
//         try {
//             await startSocketServer();
//         } catch (error) {
//             console.error("Error during runtime, restarting server...")
//         }
//     }
// }

// main();

startSocketServer();

