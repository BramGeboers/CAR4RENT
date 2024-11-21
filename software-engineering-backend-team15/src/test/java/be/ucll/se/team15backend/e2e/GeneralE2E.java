package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.authentication.model.RegisterRequest;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.Test;

public class GeneralE2E extends HelperTest {

    @Test
    public void generalTest() {

        RegisterRequest registerNewOwner = RegisterRequest.builder()
                .role("OWNER")
                .email("owner@ucll.be")
                .password("owner")
                .build();

        RegisterRequest registerNewRenter = RegisterRequest.builder()
                .role("RENTER")
                .email("renter@ucll.be")
                .password("renter")
                .build();

        UserModel preOwner = userService.getUserByEmail("owner@ucll.be");

        assert preOwner == null;

        UserModel preRenter = userService.getUserByEmail("renter@ucll.be");

        assert preRenter == null;

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .bodyValue(registerNewOwner)
                .exchange()
                .expectStatus().isOk();

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .bodyValue(registerNewRenter)
                .exchange()
                .expectStatus().isOk();

        UserModel owner = userService.getUserByEmail("owner@ucll.be");

        assert owner != null;

        UserModel renter = userService.getUserByEmail("renter@ucll.be");

        assert renter != null;

        owner.setEnabled(true);
        renter.setEnabled(true);

        userService.updateUser(owner);
        userService.updateUser(renter);


        client.post()
                .uri("auth/login")
                .header("Content-Type", "application/json")
                .bodyValue("{\"email\":\"owner@ucll.be\",\"password\":\"owner\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo("owner@ucll.be")
                .jsonPath("$.token").exists()
                .jsonPath("$.role").isEqualTo("OWNER")
                .jsonPath("$.id").exists();

        client.post()
                .uri("auth/login")
                .header("Content-Type", "application/json")
                .bodyValue("{\"email\":\"renter@ucll.be\",\"password\":\"renter\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo("renter@ucll.be")
                .jsonPath("$.token").exists()
                .jsonPath("$.role").isEqualTo("RENTER")
                .jsonPath("$.id").exists();



        String tokenOwner = jwtService.generateToken(owner);
        String tokenRenter = jwtService.generateToken(renter);

        client.post()
                .uri("/cars/add")
                .header("Authorization", "Bearer " + tokenOwner)
                .header("Content-Type", "application/json")
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
}
