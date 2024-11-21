package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.complaint.model.Complaint;
import be.ucll.se.team15backend.complaint.model.ComplaintRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ComplaintsE2E extends HelperTest{

    @Autowired
    private WebTestClient client;

    @Test
    public void testSubmitComplaint() {
        ComplaintRequest complaintRequest = new ComplaintRequest();
        complaintRequest.setTitle("Test Complaint");
        complaintRequest.setDescription("This is a test complaint.");

        client.post()
                .uri("/complaints/submit")
                .header("Authorization", "Bearer " + tokenAxelRenter)
                .bodyValue(complaintRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }
    @Test
    public void testGetAllComplaints() {
        client.get()
                .uri("/complaints")
                .header("Authorization", "Bearer " + tokenLoveleenAdmin)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }
}
