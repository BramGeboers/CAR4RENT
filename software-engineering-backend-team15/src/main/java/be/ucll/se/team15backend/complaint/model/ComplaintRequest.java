
package be.ucll.se.team15backend.complaint.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintRequest {
    private String userEmail;
    private String title;
    private String description;

    public ComplaintRequest() {}

    public ComplaintRequest(String userEmail, String title, String description) {
        this.userEmail = userEmail;
        this.title = title;
        this.description = description;
    }
}
