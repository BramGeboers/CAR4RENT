package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.rent.model.Rent;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RentE2E extends HelperTest {

    @Test
    public void testGetRents() {
        client.get()
                .uri("rents")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray();
    }

    @Test
    public void testGetRentsOwner() {
        client.get()
                .uri("rents")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray();
    }

    @Test
    public void testSearchRent() {
        client.get()
                .uri("rents/search?email=axel@ucll.be")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].renter.email").isEqualTo("axel@ucll.be");
    }

    @Test
    public void testSearchRentInvalid() {
        client.get()
                .uri("rents/search?email=aaaaa")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }

    @Test
    public void testCheckIn() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.startMileage").isEqualTo(10000)
                .jsonPath("$.startFuel").isEqualTo(50);

    }


    @Test
    public void testCheckInTwice() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.startMileage").isEqualTo(10000)
                .jsonPath("$.startFuel").isEqualTo(50);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.rent").isEqualTo("The rent has already been checked in.");

    }

    @Test
    public void testCheckOut() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.startMileage").isEqualTo(10000)
                .jsonPath("$.startFuel").isEqualTo(50);

        client.put()
                .uri("rents/checkOut")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10100, \"fuelLevel\": 30}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.endMileage").isEqualTo(10100)
                .jsonPath("$.endFuel").isEqualTo(30);

    }

    @Test
    public void testCheckOutTwice() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.startMileage").isEqualTo(10000)
                .jsonPath("$.startFuel").isEqualTo(50);

        client.put()
                .uri("rents/checkOut")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10100, \"fuelLevel\": 30}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.endMileage").isEqualTo(10100)
                .jsonPath("$.endFuel").isEqualTo(30);

        client.put()
                .uri("rents/checkOut")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10100, \"fuelLevel\": 30}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.rent").isEqualTo("The rent has already been checked out.");

    }

    @Test
    public void testCheckOutBeforeCheckIn() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkOut")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10100, \"fuelLevel\": 30}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.rent").isEqualTo("The rent has not been checked in.");

    }

    @Test
    public void testInvalidMilage() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": -1, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.mileage").isEqualTo("must be greater than or equal to 0");

    }

    @Test
    public void testInvalidFuelLevel() {

        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": -1}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.fuelLevel").isEqualTo("must be greater than or equal to 0");

    }

    @Test
    public void testDecreasingMilage() {
        List<Rent> rents = rentRepository.findAll();

        List<Rent> newRents = rents.stream().filter(rent -> rent.getStartDate() == null).toList();

        Rent rent = newRents.get(0);

        client.put()
                .uri("rents/checkIn")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 10000, \"fuelLevel\": 50}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.startMileage").isEqualTo(10000)
                .jsonPath("$.startFuel").isEqualTo(50);

        client.put()
                .uri("rents/checkOut")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"rentId\":" + rent.getId() +", \"mileage\": 1000, \"fuelLevel\": 30}")
                .exchange()
                .expectStatus().isBadRequest();

    }



}
