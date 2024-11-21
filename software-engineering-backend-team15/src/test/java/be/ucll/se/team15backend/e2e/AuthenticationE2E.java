package be.ucll.se.team15backend.e2e;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.reactive.function.BodyInserters;

public class AuthenticationE2E extends HelperTest{


    @Test
    public void validLogin() {

        client.post()
                .uri("auth/login")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"rein@ucll.be\",\"password\":\"rein\"}"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo("rein@ucll.be")
                .jsonPath("$.token").exists()// Check if token is present and is a string
                .jsonPath("$.role").isEqualTo("OWNER")
                .jsonPath("$.id").exists();

    }

    @Test
//    @Transactional
//    @DirtiesContext
    public void testWrongPasswordLogin() {

        client.post()
                .uri("auth/login")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"rein@ucll.be\",\"password\":\"wrongPassword\"}"))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void testShortPasswordLogin() {

        client.post()
                .uri("auth/login")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"rein@ucll.be\",\"password\":\"a\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.password").isEqualTo("length must be between 4 and 2147483647");
    }

    @Test
    public void noEmailLogin() {

        client.post()
                .uri("auth/login")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"\",\"password\":\"rein\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.email").isEqualTo("must not be blank");
    }

    @Test
    public void register() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"newpass\",\"role\":\"OWNER\"}"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.user.email").isEqualTo("new@ucll.be")
                .jsonPath("$.user.role").isEqualTo("OWNER")
                .jsonPath("$.user.locked").isEqualTo(false)
                .jsonPath("$.user.enabled").isEqualTo(false)
                .jsonPath("$.token").exists();
    }

    @Test
    public void usedEmailRegister() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"rein@ucll.be\",\"password\":\"newpass\",\"role\":\"OWNER\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.Email").isEqualTo("Email: Email already in use");
    }

    @Test
    public void registerShortPassword() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"a\",\"role\":\"OWNER\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.password").isEqualTo("length must be between 4 and 2147483647");
    }

    @Test
    public void registerAdmin() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"newpass\",\"role\":\"ADMIN\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.Role").isEqualTo("Role: You cannot register as an admin");
    }

    @Test
    public void registerAccountant() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"newpass\",\"role\":\"ACCOUNTANT\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.Role").isEqualTo("Role: You cannot register as an accountant");
    }

    @Test
    public void registerBot() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"newpass\",\"role\":\"BOT\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.Role").isEqualTo("Role: You cannot register as a bot");
    }

    @Test
    public void registerNoRole() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"newpass\",\"role\":\"\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.Role").isEqualTo("Role: Please provide a valid role (OWNER, RENTER, ACCOUNTANT, ADMIN)");
    }

    @Test
    public void registerNoEmail() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"\",\"password\":\"newpass\",\"role\":\"OWNER\"}"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.email").isEqualTo("must not be blank");
    }

    @Test
    public void registerNoPassword() {

        client.post()
                .uri("auth/register")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"email\":\"new@ucll.be\",\"password\":\"\",\"role\":\"OWNER\"}"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void validLogout() {

        client.post()
                .uri("auth/logout")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isOk();
    }














//    @Test
//    public void testLoginFail() {
//        driver.get("http://localhost:8080/login");
//        WebElement username = driver.findElement(By.name("username"));
//        WebElement password = driver.findElement(By.name("password"));
//        WebElement submit = driver.findElement(By.name("submit"));
//        username.sendKeys("admin");
//        password.sendKeys("admin1");
//        submit.click();
//        assertEquals("http://localhost:8080/login?error", driver.getCurrentUrl());
//    }
//
//    @Test
//    public void testLogout() {
//        driver.get("http://localhost:8080/login");
//        WebElement username = driver.findElement(By.name("username"));
//        WebElement password = driver.findElement(By.name("password"));
//        WebElement submit = driver.findElement(By.name("submit"));
//        username.sendKeys("admin");
//        password.sendKeys("admin");
//        submit.click();
//        driver.get("http://localhost:8080/logout");
//        assertEquals("http://localhost:8080/login?logout", driver.getCurrentUrl());
//    }
//
//    @Test
//    public void testLogoutFail() {
//        driver.get("http://localhost:8080/logout");
//        assertEquals("http://localhost:8080/login?logout", driver.getCurrentUrl());
//    }
//
//    @Test
//    public void testRegister() {
//        driver.get("http://localhost:8080/register");
//        WebElement username = driver.findElement(By.name("username"));
//        WebElement password = driver.findElement(By.name("password"));
//        WebElement submit = driver.findElement(By.name("submit"));
//        username.sendKeys("admin1");
//        password.sendKeys("admin1");
//        submit.click();
//        assertEquals("http://localhost:8080/login?register", driver.getCurrentUrl());
//    }
//
//    @Test
//    public void testRegisterFail() {
//        driver.get("http://localhost:8080/register");
//        WebElement username = driver.findElement(By.name("username"));
//        WebElement password = driver.findElement(By.name("password"));
//        WebElement submit = driver.findElement(By.name("submit"));
//        username.sendKeys("admin");
//        password.sendKeys("admin");
//        submit.click();

}
