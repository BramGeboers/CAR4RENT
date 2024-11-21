import { tr } from 'date-fns/locale';
import * as dotenv from 'dotenv';


dotenv.config();

const backendUrl = process.env.SPRINGBOOT_URL;


class chatDb {


    static async isAvailable(localId: string, token: string) {
        try {
        const response = await fetch(backendUrl + '/chat/available?chatId=' + localId, {
            method: 'GET',
            headers: {
                'authorization': 'Bearer ' + token
            },
        });
        return response
    } catch (error) {
        console.error("Error checking if chat is available")
    }

        
    }

    static async send(localId: string, roomId: string, data: string, token: string) {
        try {
        const response = await fetch(backendUrl + '/chat/send', {
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
        return response
    } catch (error) {
        console.error("Error sending message")
    }
    }

    static async getEmails(roomId: string, token: string) {
        try {
        const response = await fetch(backendUrl + '/chat/room/' + roomId + "/emails", {
            method: 'GET',
            headers: {
                'authorization': 'Bearer ' + token
            },
        });
        return response
    } catch (error) {
        console.error("Error getting emails")
    }
    }

    static async askBot(message: string, token: string, roomId: string) {
        try {
        const response = await fetch(backendUrl + '/bot?message=' + message + "&roomId=" + roomId, {
            method: 'GET',
            headers: {
                'authorization': 'Bearer ' + token
            },
        })
        return response
    } catch (error) {
        console.error("Error asking bot")
    }
    }





}

export default chatDb;