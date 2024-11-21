package be.ucll.se.team15backend.e2e;


import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.Test;

public class UserE2E extends HelperTest {

    @Test
    public void testGetUsersAdmin() {

        client.get()
                .uri("user/all")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].email").exists()
                .jsonPath("$[0].role").exists()
                .jsonPath("$[0].userId").exists();
    }

    @Test
    public void testGetUsersOwner() {

        client.get()
                .uri("user/all")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testGetUsersRenter() {

        client.get()
                .uri("user/all")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenAxelRenter)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testGetUsersAccountant() {

        client.get()
                .uri("user/all")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenBramAccountant)
                .exchange()
                .expectStatus().isForbidden();
    }




}
