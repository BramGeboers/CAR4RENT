package be.ucll.se.team15backend.map.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    private String street;
    private String number;
    private String postalCode;
    private String city;
    private String country;

    private String addressString;

    public String toString() {
        return street + " " + number + ", " + postalCode + " " + city;
    }
}
