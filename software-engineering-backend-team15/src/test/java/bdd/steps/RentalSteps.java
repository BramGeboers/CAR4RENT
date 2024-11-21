package bdd.steps;

import be.ucll.se.team15backend.authentication.model.LoginRequest;
import be.ucll.se.team15backend.authentication.model.LoginResponse;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.car.repo.CarRepository;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import be.ucll.se.team15backend.rental.service.RentalService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest(classes = {be.ucll.se.team15backend.Team15BackendApplication.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
public class RentalSteps {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    private WebTestClient.ResponseSpec response;

    private String token;

    private Rental rental;

    private Car car;

    private UserModel user;

    @Before
    public void setup() throws UserException {
        car = new Car();
        car.setBrand("BMW");
        car.setModel("M3");
        car.setLicensePlate("1-ABC-123");
        car.setTypes("SUV");
        car.setFoldingRearSeat(false);
        car.setFuel(0);
        car.setMileage(0);
//car1.setOwner(user);
        car.setNumberOfSeats(5);


        user = userService.getUserByEmail("wout@ucll.be");
        if (user == null) {
            user = new UserModel();
            user.setEmail("wout@ucll.be");
            user.setPassword("wout");
            user.setLocked(false);
            user.setEnabled(true);
            user.setRole(Role.ADMIN);
            userService.addUser(user);
        }

        LoginRequest request = new LoginRequest();
        request.setEmail("wout@ucll.be");
        request.setPassword("wout");
        WebTestClient.BodySpec<LoginResponse,?> responseBody =
                webTestClient.post().uri("/auth/login")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(LoginResponse.class);

        token = responseBody.returnResult().getResponseBody().getToken();
        user = userService.getUserByEmail("wout@ucll.be");
        carService.addCar(car, user.getEmail());
    }
    @After
    public void teardown() {
        carRepository.deleteAll();
        rentalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Given("Some rentals")
    public void some_rentals() throws ServiceException, IOException, InterruptedException {
        rental = new Rental();

        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(6));
        rental.setCity("Leuven");
        rental.setPostal("3000");
        rental.setStreet("Tiensestraat");
        rental.setNumber("123");
        rental.setPhoneNumber("0499123456");

        //long id = carRepository.findAll().get(0).getId();
        rentalRepository.save(rental);
    }

    @When("User requests all rentals")
    public void user_requests_all_rentals() {
        response = webTestClient.get().uri("/rentals")
                .header("Authorization", "Bearer " + token)
                .exchange();
    }

    @Then("The rentals are returned")
    public void the_rentals_are_returned() {
        response.expectStatus().isOk().expectBodyList(Rental.class);
    }

}
