import { Coordinates } from "@/types";

export class SearchService {
  static async getRentals(input: {
    start: Coordinates;
    end: Coordinates;
    date: string;
  }) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/planner`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(input),
    });
    if (!response.ok) {
      return null;
    }
    return response.json();
  }

  static async getCoordsFromAddress(address: string) {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/map/coordinates?address=${address}`
    );
    if (!response.ok) {
      return null;
    }
    return response.json();
  }

  static async getAddressesFromCoords(coords: Coordinates) {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/map/address?latitude=${coords.latitude}&longitude=${coords.longitude}`
    );
    if (!response.ok) {
      return null;
    }
    return response.json();
  }
}
