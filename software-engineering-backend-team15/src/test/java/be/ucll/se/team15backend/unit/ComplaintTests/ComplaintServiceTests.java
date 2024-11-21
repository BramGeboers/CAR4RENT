package be.ucll.se.team15backend.unit.ComplaintTests;

import be.ucll.se.team15backend.complaint.model.Complaint;
import be.ucll.se.team15backend.complaint.model.ComplaintRequest;
import be.ucll.se.team15backend.complaint.repo.ComplaintRepository;
import be.ucll.se.team15backend.complaint.service.ComplaintService;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;

import org.hibernate.query.Page;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ComplaintServiceTests {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    public void givenComplaint_whenSubmitComplaint_thenComplaintIsSubmitted() {
        // given
        ComplaintRequest complaintRequest = new ComplaintRequest();
        complaintRequest.setTitle("Title");
        complaintRequest.setDescription("Description");
        complaintRequest.setUserEmail("user@example.com");

        Complaint expectedComplaint = new Complaint();
        expectedComplaint.setTitle("Title");
        expectedComplaint.setDescription("Description");
        expectedComplaint.setUserEmail("user@example.com");

        when(complaintRepository.save(expectedComplaint)).thenReturn(expectedComplaint);

        // when
        Complaint submittedComplaint = complaintService.submitComplaint(complaintRequest);

        // then
        assertEquals(expectedComplaint, submittedComplaint);
    }
    @Test
    public void givenNullComplaintRequest_whenSubmitComplaint_thenComplaintWontBeSubmitted() {
        // then
        assertThrows(NullPointerException.class, () -> {
            // when
            complaintService.submitComplaint(null);
        });
    }
}
