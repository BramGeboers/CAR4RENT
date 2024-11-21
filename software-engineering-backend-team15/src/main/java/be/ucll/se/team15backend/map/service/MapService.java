package be.ucll.se.team15backend.map.service;



import be.ucll.se.team15backend.map.model.Address;
import be.ucll.se.team15backend.map.model.ClosestsResponse;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.map.model.MapException;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class MapService {

    @Autowired
    private RentalRepository rentalRepository;

    //https://geocode.maps.co/
    String apiKey = "65f37f2015ff2071278889tfubaeef7";  //key van rein.vanwanseele@student.ucll.be (1 request per seconde - 1mil per maand)

    String addressEndpoint = "https://geocode.maps.co/search?q=%s&api_key=%s";

    String coordsEndpoint = "https://geocode.maps.co/reverse?lat=%s&lon=%s&api_key=%s";


    public Coordinates fetchCoordinates(String addressString) throws IOException, InterruptedException, MapException {
        URL url = new URL(String.format(addressEndpoint, addressString, apiKey));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");


        int statusCode = connection.getResponseCode();
        if (statusCode == 429) {
            throw new MapException("request", "Too Many Requests (429)");
        } else if (statusCode != 200) {
            throw new MapException("error", "Unexpected status code: " + statusCode);
        }

        // Parse JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> jsonArray = objectMapper.readValue(connection.getInputStream(), new TypeReference<List<Object>>() {});


        if (jsonArray.isEmpty()) {
            throw new MapException("address", "No results found");
        }

        Map<String, Object> jsonMap = (Map<String, Object>) jsonArray.get(0);

        // Extract latitude and longitude
        double latitude = Double.parseDouble(jsonMap.get("lat").toString());
        double longitude = Double.parseDouble(jsonMap.get("lon").toString());

        return new Coordinates(latitude, longitude);
    }

    public Coordinates fetchCoordinates(Address address) throws IOException, InterruptedException, MapException {
        return fetchCoordinates(address.toString()); //later nog aanpassen
    }
    public Address fetchAddress(double latitude, double longitude) throws MapException, IOException {
        Coordinates coordinates = new Coordinates(latitude, longitude);
        return fetchAddress(coordinates);
    }
    public Address fetchAddress(Coordinates coordinates) throws MapException, IOException {
        URL url = new URL(String.format(coordsEndpoint, coordinates.getLatitude(), coordinates.getLongitude(), apiKey));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int statusCode = connection.getResponseCode();
        if (statusCode == 429) {
            throw new MapException("error", "Too Many Requests, please try again later");
        } else if (statusCode != 200) {
            throw new MapException("error", "Unexpected status code: " + statusCode);
        }

        // Parse JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(connection.getInputStream(), new TypeReference<Map<String, Object>>() {
        });



        // Extract address
        String addressString = jsonMap.get("display_name").toString();
        Map<String, Object> addressMap = (Map<String, Object>) jsonMap.get("address");
        String street = null;
        String city = null;
        String country = null;
        String postalCode = null;
        String number = null;

        try {
            street = addressMap.get("road").toString();
        } catch (Exception e) {
            street = null;
        }

        try {
            city = addressMap.get("village").toString();
        } catch (Exception e) {
            try {
                city = addressMap.get("town").toString();
            } catch (Exception e2) {
                city = null;
            }
        }


        try {
            country = addressMap.get("country").toString();
        } catch (Exception e) {
            country = null;
        }

        try {
            postalCode = addressMap.get("postcode").toString();
        } catch (Exception e) {
            postalCode = null;
        }

        try {
            number = addressMap.get("house_number").toString();
        } catch (Exception e) {
            number = null;
        }


        return new Address(street, number, postalCode, city, country, addressString);
    }



    public Double getDistance(Coordinates c1, Coordinates c2) {
        double lat1 = c1.getLatitude();
        double lon1 = c1.getLongitude();
        double lat2 = c2.getLatitude();
        double lon2 = c2.getLongitude();
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    public List<ClosestsResponse> getClosestRentals(double latitude, double longitude) {

        Coordinates coordinates = new Coordinates(latitude, longitude);
        return getClosestRentals(coordinates, 5);
    }
    public List<ClosestsResponse> getClosestRentals(Coordinates coordinates, int amount) {
        List<Rental> rentals = rentalRepository.findAll();

        List<ClosestsResponse> returnList = new ArrayList<>();

        rentals.forEach(rental -> {
            Coordinates rentalCoordinates = new Coordinates(rental.getLatitude(), rental.getLongitude());
            double distance = getDistance(coordinates, rentalCoordinates);
            if (distance > 100) {
                return;
            }
            returnList.add(new ClosestsResponse(rental, distance));
        });

        returnList.sort((c1, c2) -> Double.compare(c1.getDistance(), c2.getDistance()));

        if (amount == -1) {
            return returnList;
        }
        return returnList.subList(0, Math.min(amount, returnList.size()));
//        rentals.sort((c1, c2) -> {
//            Coordinates coordinates1 = new Coordinates(c1.getLatitude(), c1.getLongitude());
//            Coordinates coordinates2 = new Coordinates(c2.getLatitude(), c2.getLongitude());
//            double dist1 = getDistance(coordinates, coordinates1);
//            double dist2 = getDistance(coordinates, coordinates2);
//            return Double.compare(dist1, dist2);
//        });
//        return rentals.subList(0, Math.min(amount, rentals.size()));
    }


}
