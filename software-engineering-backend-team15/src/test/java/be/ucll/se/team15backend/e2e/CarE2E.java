package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.car.model.Car;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class CarE2E extends HelperTest{

    @Test
    public void testGetCarsAdmin() {
        client.get()
                .uri("cars?page=0")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content[0].id").exists()
                .jsonPath("$.content[0].brand").exists()
                .jsonPath("$.content[0].model").exists();
    }

    @Test
    public void testGetCarsOwner() {
        client.get()
                .uri("cars?page=0")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content[0].id").exists()
                .jsonPath("$.content[0].brand").exists()
                .jsonPath("$.content[0].model").exists()
                .jsonPath("$.content[0].owner").exists()
                .jsonPath("$.content[0].owner.email").isEqualTo("rein@ucll.be");
    }

    @Test
    public void testGetCarsRenter() {
        client.get()
                .uri("cars?page=0")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenAxelRenter)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testGetCarsAccountant() {
        client.get()
                .uri("cars?page=0")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenBramAccountant)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testGetCarsNoToken() {
        client.get()
                .uri("cars?page=0")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    public void testGetCarsByValidIdOwner() {
        List<Car> carsRein = carService.getAllCarsPageable(PageRequest.of(0, 10), "rein@ucll.be").getContent();

        Car firstCar = carsRein.get(0);
        client.get()
                .uri("cars/" + firstCar.getId())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(firstCar.getId())
                .jsonPath("$.brand").isEqualTo(firstCar.getBrand())
                .jsonPath("$.model").isEqualTo(firstCar.getModel());
    }

    @Test
    public void testGetCarsByValidIdAdmin() {
        List<Car> carsRein = carService.getAllCarsPageable(PageRequest.of(0, 10), "rein@ucll.be").getContent();

        Car firstCar = carsRein.get(0);
        client.get()
                .uri("cars/" + firstCar.getId())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(firstCar.getId())
                .jsonPath("$.brand").isEqualTo(firstCar.getBrand())
                .jsonPath("$.model").isEqualTo(firstCar.getModel());
    }

    @Test
    public void testGetCarsByInvalidIdRenter() {
        List<Car> carsRein = carService.getAllCarsPageable(PageRequest.of(0, 10), "rein@ucll.be").getContent();

        Car firstCar = carsRein.get(0);
        client.get()
                .uri("cars/" + firstCar.getId())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenTorbenOwner)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testGetCarsByInvalidIdAdmin() {

        client.get()
                .uri("cars/" + 999999)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testGetCarsByInvalidIdNoToken() {

        client.get()
                .uri("cars/" + 999999)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void addCarValid() {
        client.post()
                .uri("cars/add")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"brand\":\"string\",\"model\":\"string\",\"types\":\"string\",\"licensePlate\":\"string\",\"numberOfSeats\":1,\"numberOfChildSeats\":0,\"foldingRearSeat\":true,\"towBar\":true}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand").isEqualTo("string")
                .jsonPath("$.model").isEqualTo("string")
                .jsonPath("$.types").isEqualTo("string")
                .jsonPath("$.licensePlate").isEqualTo("string")
                .jsonPath("$.numberOfSeats").isEqualTo(1)
                .jsonPath("$.numberOfChildSeats").isEqualTo(0)
                .jsonPath("$.foldingRearSeat").isEqualTo(true)
                .jsonPath("$.towBar").isEqualTo(true);
    }

    @Test
    public void addCarInvalid() {
        client.post()
                .uri("cars/add")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .bodyValue("{\"invalid\":\"string\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void deleteCarValid() {
        Car car = new Car();
        car.setBrand("string");
        car.setModel("string");
        car.setTypes("string");
        car.setLicensePlate("string");
        car.setNumberOfSeats(1);
        car.setNumberOfChildSeats(0);
        car.setFoldingRearSeat(true);
        car.setTowBar(true);
        car.setOwner(userService.getUserByEmail("rein@ucll.be"));

        Car savedCar = carRepository.save(car);

        client.delete()
                .uri("cars/delete/" + savedCar.getId())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand").isEqualTo(savedCar.getBrand())
                .jsonPath("$.model").isEqualTo(savedCar.getModel());
    }


    @Test
    public void deleteCarInvalidId() {
        client.delete()
                .uri("cars/delete/" + 999999)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void deleteCarInvalidOwner() {
        Car car = new Car();
        car.setBrand("string");
        car.setModel("string");
        car.setTypes("string");
        car.setLicensePlate("string");
        car.setNumberOfSeats(1);
        car.setNumberOfChildSeats(0);
        car.setFoldingRearSeat(true);
        car.setTowBar(true);
        car.setOwner(userService.getUserByEmail("rein@ucll.be"));

        Car savedCar = carRepository.save(car);
        System.out.println(tokenTorbenOwner);
        client.delete()
                .uri("cars/delete/" + savedCar.getId())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenTorbenOwner)
                .exchange()
                .expectStatus().isBadRequest();
    }



}
