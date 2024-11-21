package bdd.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import
org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.se.team15backend.Team15BackendApplication;
import be.ucll.se.team15backend.authentication.model.LoginRequest;
import be.ucll.se.team15backend.authentication.model.LoginResponse;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.car.repo.CarRepository;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.repo.UserRepository;
import be.ucll.se.team15backend.user.service.UserService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_lol.WEN;
import io.cucumber.spring.CucumberContextConfiguration;

// @CucumberContextConfiguration
@SpringBootTest(classes= {Team15BackendApplication.class}, webEnvironment =
SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class CarSteps {
@Autowired
private WebTestClient webTestClient;

@Autowired
private CarRepository carRepository;

@Autowired
private UserRepository userRepository;

@Autowired
private CarService carService;

@Autowired
private UserService userService;

private String token;

private Car car;

private WebTestClient.ResponseSpec response;

private UserModel user;

@Before
public void setup() throws UserException {
car = new Car();
user = new UserModel();
user.setEmail("tester@ucll.be");
user.setPassword("tester");
user.setLocked(false);
user.setEnabled(true);
user.setRole(Role.ADMIN);
userService.addUser(user);

LoginRequest request = new LoginRequest();
request.setEmail("tester@ucll.be");
request.setPassword("tester");
WebTestClient.BodySpec<LoginResponse,?> responseBody =
webTestClient.post().uri("/auth/login")
.bodyValue(request)
.exchange()
.expectStatus().isOk()
.expectBody(LoginResponse.class);

token = responseBody.returnResult().getResponseBody().getToken();
user = userService.getUserByEmail("tester@ucll.be");

}
@After
public void teardown() {
carRepository.deleteAll();
userRepository.deleteAll();
}

@Given("some cars")
public void some_cars() {
Car car1 = new Car();
car1.setBrand("BMW");
car1.setModel("M3");
car1.setLicensePlate("6-ABC-123");
car1.setTypes("SUV");
car1.setFoldingRearSeat(false);
car1.setFuel(0);
car1.setMileage(0);
car1.setOwner(user);
car1.setNumberOfSeats(5);

Car car2 = new Car();
car2.setBrand("Audi");
car2.setModel("A3");
car2.setFoldingRearSeat(true);
car2.setLicensePlate("8-ADC-143");
car2.setTypes("SUV");
car2.setFuel(0);
car2.setMileage(0);
car2.setOwner(user);
car2.setNumberOfSeats(5);

carRepository.save(car1);
carRepository.save(car2);
}

@When("user requests all cars")
public void user_requests_all_cars() {
response = webTestClient.get().uri("/cars")
.header("Authorization", "Bearer " + token)
.exchange();
}

@Then("all cars are returned")
public void all_cars_are_returned() {
response.expectStatus().isOk()
.expectBodyList(Car.class);
}

@Given("no cars")
public void no_cars() {
carRepository.deleteAll();
}

@When("user requests all cars but there are none")
public void user_requests_all_cars_but_there_are_none() {
response = webTestClient.get().uri("/cars")
.header("Authorization", "Bearer " + token)
.exchange();
}

@Then("no cars are returned")
public void no_cars_are_returned() {
response.expectStatus().isOk()
.expectBody().jsonPath("$.content").isEmpty();
}

}
