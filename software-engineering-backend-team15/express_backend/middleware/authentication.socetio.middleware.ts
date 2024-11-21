import jwt, { JwtPayload } from 'jsonwebtoken';
import { Socket } from 'socket.io';
import { NextFunction } from 'express';
import LogService from '../service/log.service';
import * as dotenv from 'dotenv';
import { createHmac, BinaryLike } from 'crypto';
import { get } from 'http';
import { buffer } from 'stream/consumers';



dotenv.config();


const authenticationSocketIoMiddleware = (socket: Socket & { decoded: any}, next: NextFunction) => {


    // Extract JWT from handshake or initial message
    const token = socket.handshake.query.token as string || ''; // Example: Extract token from query param
    // Verify JWT token

    jwt.verify(token, getSigningKey(), (err, decoded) => {
        if (err) {
            if (!(decoded as JwtPayload)) {
                LogService.addLogServer(`Authentication failed`, 'ERROR');
                return next(new Error('Authentication error'));
            }
            LogService.addLogServer(`Authentication failed for ${(decoded as JwtPayload).email}`, 'ERROR');
            // Token verification failed
            return next(new Error('Authentication error'));
        }
        // Token verification succeeded
        LogService.addLogServer(`Authentication succeeded for ${(decoded as JwtPayload).email}`, 'INFO');
        socket.decoded = decoded; // Attach decoded token data to socket object
        next();
    });
};

function getSigningKey(): Buffer {
    return Buffer.from(process.env.JWT_SECRET, 'base64');
}


export default authenticationSocketIoMiddleware;