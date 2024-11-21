package bdd.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ucll.se.team15backend.Team15BackendApplication;
import be.ucll.se.team15backend.authentication.model.LoginRequest;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.repo.UserRepository;
import be.ucll.se.team15backend.user.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes= {Team15BackendApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginSteps {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private WebTestClient.ResponseSpec response;

    @Given("valid credentials")
    public void valid_credentials() throws UserException {
        UserModel user = new UserModel();
        user.setEmail("testing@ucll.be");
        user.setPassword("testing");
        user.setLocked(false);
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
       
        userService.addUser(user);
    }

    @When("user logs in")
    public void user_logs_in() {
        LoginRequest request = new LoginRequest();
        request.setEmail("testing@ucll.be");
        request.setPassword("testing");
        response = webTestClient.post().uri("/auth/login")
                .bodyValue(request)
                .exchange();
    }

    @Then("user is logged in")
    public void user_is_logged_in() {

        response.expectBody()
                .jsonPath("$.email").isEqualTo("testing@ucll.be")
                .jsonPath("$.role").isEqualTo("ADMIN")
                .jsonPath("$.id").isNotEmpty();
    }

    @Given("credentials")
    public void invalid_credentials() throws UserException {
        UserModel user = new UserModel();
        user.setEmail("testing2@ucll.be");
        user.setPassword("testing2");
        user.setLocked(false);
        user.setEnabled(true);
        user.setRole(Role.RENTER);
        userService.addUser(user);
    }

    @When("user logs in with wrong password")
    public void user_logs_in_with_invalid_credentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("testing2@ucll.be");
        request.setPassword("wrongpassword");
        response = webTestClient.post().uri("/auth/login")
                .bodyValue(request)
                .exchange();
    }

    @Then("user is not logged in")
    public void user_is_not_logged_in() {
        response.expectStatus().isForbidden();
    }

    @Given("credentials 2")
    public void credentials_2() throws UserException {
        UserModel user = new UserModel();
        user.setEmail("torben@ucll.be");
        user.setPassword("torben");
        user.setLocked(false);
        user.setEnabled(true);
        user.setRole(Role.RENTER);
        userService.addUser(user);
    }

    @When("user logs in with wrong email")
    public void user_logs_in_with_wrong_email() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@ucll.be");
        request.setPassword("torben");
        response = webTestClient.post().uri("/auth/login")
                .bodyValue(request)
                .exchange();
    }

    @Then("user is not logged in 2")
    public void user_is_not_logged_in_2() {
        response.expectStatus().isForbidden();
    }

    @Given("credentials 3")
    public void credentials_3() throws UserException {
        UserModel user = new UserModel();
        user.setEmail("disabled@ucll.be");
        user.setPassword("disabled");
        user.setLocked(false);
        user.setEnabled(false);
        user.setRole(Role.RENTER);
        userService.addUser(user);
    }

    @When("user logs in with account not enabled")
    public void user_logs_in_with_account_not_enabled() {
        LoginRequest request = new LoginRequest();
        request.setEmail("disabled@ucll.be");
        request.setPassword("disabled");
        response = webTestClient.post().uri("/auth/login")
                .bodyValue(request)
                .exchange();
    }

    @Then("user is not logged in 3")
    public void user_is_not_logged_in_3() {
        response.expectStatus().isForbidden();
    }

    @Given("credentials 4")
    public void credentials_4() throws UserException {
        UserModel user = new UserModel();
        user.setEmail("locked@ucll.be");
        user.setPassword("locked");
        user.setLocked(false);
        user.setEnabled(false);
        user.setRole(Role.RENTER);
        userService.addUser(user);
    }

    @When("user logs in with account locked")
    public void user_logs_in_with_account_locked() {
        LoginRequest request = new LoginRequest();
        request.setEmail("locked@ucll.be");
        request.setPassword("locked");
        response = webTestClient.post().uri("/auth/login")
                .bodyValue(request)
                .exchange();
    }

    @Then("user is not logged in 4")
    public void user_is_not_logged_in_4() {
        response.expectStatus().isForbidden();
    }
}


