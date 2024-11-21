import { UUID } from "crypto";

export interface CheckInRequest {
    rentId: number;
    mileage: number;
    fuelLevel: number;
}

export interface Info {
    
}

export interface Billing{
    id?: number;
    cost: number;
    user: User;
    rent: Rent;
    date: string;
}

export interface Car {
    id: number;
    brand: string;
    model?: string|null;
    types: string|null;
    licensePlate: string|null;
    numberOfSeats: number|null;
    numberOfChildSeats?: number|null;
    foldingRearSeat?: boolean|null;
    towBar?: boolean|null;
    rentals?: Rental[]|null;
    mileage?: number|null;
    fuel?: number|null;
    fuelCapacity?: number|null;
    fuelEstimatedConsumption?: number|null;
    pricePerKm?: number|null;
    pricePerLiterFuel?: number|null;
    owner?: User|null;
}

export interface Rent {
    id: number;
    car: Car;
    email: string;
    phoneNumber: string;
    nationalIdentificationNumber: string;
    birthDate: string;
    active?: boolean;
    drivingLicenseNumber: string;
    rental?: Rental;
    startDate: string;
    startMileage: number;
    startFuel: number;
    endDate: string;
    endMileage: number;
    endFuel: number;
    price: number;
    renter?: User;
    payed?: Boolean;
}

export interface checkoutForm {
    rentId: Number;
    mileage: Number;
    fuelLevel: Number;
}

export interface Rental {
    id?: number;
    startDate: string;
    endDate: string;
    city: string;
    street?: string;
    number?: string;
    postal?: string;
    email: string;
    phoneNumber: string;
    longitude?: number;
    latitude?: number;
    car: Car;
    rent?: Rent;
}

export interface User {
    userId: number;
    email: string;
    password: string;
    role: "OWNER" | "RENTER" | "ACCOUNTANT" | "ADMIN";
    locked?: boolean;
    enabled?: boolean;
    accountNonLocked?: boolean;
}

export interface Coordinates {
    latitude: number;
    longitude: number;
}

export interface PlannerRequest {
    start: Coordinates;
    end: Coordinates;
    date: string;
}

export interface CostOverview {
    fixedCost?: number;
    distanceCost?: number;
    fuelCost?: number;
    totalCost?: number;
}

export interface PlannerResponse {
    rental: Rental;
    distance: number;
    estimatedPrice: CostOverview;
}

export interface Address {
    street: string;
    number: string;
    postalCode: string;
    city: string;
    country: string;
    addressString: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    role: string;
}

export interface RegisterResponse {
    user: User;
    token: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    email: string;
    token: string;
    role: "OWNER" | "RENTER" | "ACCOUNTANT" | "ADMIN";
    id: number;
}

export interface Notification {
    id: number;
    message: string;
    car: Car;
    rent: Rent;
}

export interface ClosestsResponse {
    rental: Rental;
    distance: number;
}

export interface MarkerType {
    id: number;
    latitude: number;
    longitude: number;
    popup: string;
    icon?: string;
    onClick?: () => void;
    teleport?: boolean;
    clickable?: boolean;
}

export type ChatJSON = {
    id: UUID;
    timestamp: string;
    senderEmail: string;
    roomId: string;
    data: string;
}

export type ErrorResponse = {
    status: number;
    errorMessage: string;
}

export type roomOverview = {
    roomId: string;
    roomName: string;
    lastMessage: string;
    lastMessageSender: string;
    lastMessageTimestamp: string;
    unreadMessages: number;
}
export interface Complaint  {
    id: number;
    userEmail: string;
    title: string;
    description: string;
    renter?: User;
}
  