package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.Test;

public class ManageE2E extends HelperTest{

    @Test
    public void testUpdateRoleAdmin() {

        UserModel rein = userService.getUserByEmail("rein@ucll.be");

        client.put()
                .uri("manage/role/update?userId=" + rein.getUserId() + "&role=renter")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo(rein.getUserId())
                .jsonPath("$.role").isEqualTo("RENTER");

        UserModel reinUpdated = userService.getUserByEmail("rein@ucll.be");
        assert reinUpdated.getRole().equals(Role.RENTER);
    }

    @Test
    public void testUpdateRoleOwner() {
        client.put()
                .uri("manage/role/update?userId=1&role=ROLE_OWNER")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testUpdateRoleRenter() {
        client.put()
                .uri("manage/role/update?userId=1&role=ROLE_OWNER")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenAxelRenter)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testUpdateRoleAccountant() {
        client.put()
                .uri("manage/role/update?userId=1&role=ROLE_OWNER")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenBramAccountant)
                .exchange()
                .expectStatus().isForbidden();
    }
}
