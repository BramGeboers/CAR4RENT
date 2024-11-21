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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const log_service_1 = __importDefault(require("../service/log.service"));
const dotenv = __importStar(require("dotenv"));
dotenv.config();
const authenticationSocketIoMiddleware = (socket, next) => {
    // Extract JWT from handshake or initial message
    const token = socket.handshake.query.token || ''; // Example: Extract token from query param
    // Verify JWT token
    jsonwebtoken_1.default.verify(token, getSigningKey(), (err, decoded) => {
        if (err) {
            if (!decoded) {
                log_service_1.default.addLogServer(`Authentication failed`, 'ERROR');
                return next(new Error('Authentication error'));
            }
            log_service_1.default.addLogServer(`Authentication failed for ${decoded.email}`, 'ERROR');
            // Token verification failed
            return next(new Error('Authentication error'));
        }
        // Token verification succeeded
        log_service_1.default.addLogServer(`Authentication succeeded for ${decoded.email}`, 'INFO');
        socket.decoded = decoded; // Attach decoded token data to socket object
        next();
    });
};
function getSigningKey() {
    return Buffer.from(process.env.JWT_SECRET, 'base64');
}
exports.default = authenticationSocketIoMiddleware;
