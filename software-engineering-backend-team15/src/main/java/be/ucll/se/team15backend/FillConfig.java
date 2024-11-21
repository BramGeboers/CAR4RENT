package be.ucll.se.team15backend;

import be.ucll.se.team15backend.authentication.model.RegisterRequest;
import be.ucll.se.team15backend.authentication.service.AuthService;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.rent.service.RentService;
import be.ucll.se.team15backend.rental.service.RentalService;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Data
public class FillConfig {

    @Value("${custom.fill}")
    private boolean fill;


        @Autowired
        private CarService carService;
        @Autowired
        private RentalService rentalService;

        @Autowired
        private RentService rentService;

        @Autowired
        private AuthService authService;

        @Autowired
        private UserService userService;

        @Autowired
        private ChatService chatService;

        @Autowired
        private JwtService jwtService;

        private List<Car> allCars;

        private Car getRandomCar() {
                return allCars.get(ThreadLocalRandom.current().nextInt(allCars.size()));
        }

        public static LocalDateTime getRandomDateTime() {
                // Get the current date and time
                LocalDateTime now = LocalDateTime.now().minusDays(10);

                // Calculate the maximum date as current date + 1 year
                LocalDateTime maxDate = now.plusMonths(1);

                // Generate a random number of seconds within the range between now and 1 year
                long randomSeconds = ThreadLocalRandom.current().nextLong(0, ChronoUnit.SECONDS.between(now, maxDate));

                // Add the random number of seconds to the current date and time to get the
                // random date
                return now.plusSeconds(randomSeconds);
        }

        public static int getRandomNumberInRange(int min, int max) {
                if (min >= max) {
                        throw new IllegalArgumentException("Max must be greater than min");
                }

                Random random = new Random();
                return random.nextInt(max - min + 1) + min;
        }

        @PostConstruct
        public void fill() throws ServiceException, IOException, InterruptedException, UserException {

                // can be optimized by using repos directly to avoid checking if the car exists

        if (fill) {  //change to false if you don't want to fill the database
            System.out.println("fill start");

                        System.out.println("Creating general chat room");

                        chatService.saveRoom(
                                        Room.builder()
                                                        .roomId("general")
                                                        .roomName("General Chat")
                                                        .build());

                        System.out.println("start filling users");

                        UserModel bot = authService.register(RegisterRequest.builder()
                                        .email("boxter.buddy@ucll.be")
                                        .password("bot")
                                        .role("BOT")
                                        .build(), "", "", true).getUser();

                        UserModel rein = authService.register(RegisterRequest.builder()
                                        .email("rein@ucll.be")
                                        .password("rein")
                                        .role("OWNER")
                                        .build(), "", "", true).getUser();

                        UserModel axel = authService.register(RegisterRequest.builder()
                                        .email("axel@ucll.be")
                                        .password("axel")
                                        .role("RENTER")
                                        .build(), "", "", true).getUser();

                        UserModel bram = authService.register(RegisterRequest.builder()
                                        .email("bram@ucll.be")
                                        .password("bram")
                                        .role("ACCOUNTANT")
                                        .build(), "", "", true).getUser();

                        UserModel bramgeboers = authService.register(RegisterRequest.builder()
                                        .email("bramgeboers@ucll.be")
                                        .password("bram")
                                        .role("ADMIN")
                                        .build(), "", "", true).getUser();

                        UserModel loveleen = authService.register(RegisterRequest.builder()
                                        .email("loveleen@ucll.be")
                                        .password("loveleen")
                                        .role("ADMIN")
                                        .build(), "", "", true).getUser();

                        UserModel wout = authService.register(RegisterRequest.builder()
                                        .email("wout@ucll.be")
                                        .password("wout")
                                        .role("ADMIN")
                                        .build(), "", "", true).getUser();

                        UserModel torben = authService.register(RegisterRequest.builder()
                                        .email("torben@ucll.be")
                                        .password("torben")
                                        .role("OWNER")
                                        .build(), "", "", true).getUser();

                        bot.setEnabled(true);
                        rein.setEnabled(true);
                        axel.setEnabled(true);
                        bram.setEnabled(true);
                        bramgeboers.setEnabled(true);
                        loveleen.setEnabled(true);
                        wout.setEnabled(true);
                        torben.setEnabled(true);

                        userService.updateUser(bot);
                        userService.updateUser(rein);
                        userService.updateUser(axel);
                        userService.updateUser(bram);
                        userService.updateUser(bramgeboers);
                        userService.updateUser(loveleen);
                        userService.updateUser(wout);
                        userService.updateUser(torben);



                        System.out.println("start creating cars");
                        Car bmw = Car.builder()
                                        .brand("BMW")
                                        .model("X5")
                                        .licensePlate("1-ABC-123")
                                        .types("SUV")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(55.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(7.0)
                                        .pricePerKm(0.5)
                                        .pricePerLiterFuel(1.5)
                                        .build();
                        Car audi = Car.builder()
                                        .brand("Audi")
                                        .model("A4")
                                        .licensePlate("2-DEF-456")
                                        .types("Sedan")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(false)
                                        .fuel(100.0)
                                        .fuelCapacity(60.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(6.0)
                                        .pricePerKm(0.6)
                                        .pricePerLiterFuel(1.6)
                                        .build();
                        Car volkswagen = Car.builder()
                                        .brand("Volkswagen")
                                        .model("Golf")
                                        .licensePlate("3-GHI-789")
                                        .types("Hatchback")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(50.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(5.0)
                                        .pricePerKm(0.4)
                                        .pricePerLiterFuel(1.4)
                                        .build();
                        Car volvo = Car.builder()
                                        .brand("Volvo")
                                        .model("XC90")
                                        .licensePlate("4-JKL-012")
                                        .types("SUV")
                                        .numberOfSeats(7)
                                        .numberOfChildSeats(3)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(65.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(8.0)
                                        .pricePerKm(0.7)
                                        .pricePerLiterFuel(1.7)
                                        .build();
                        Car tesla = Car.builder()
                                        .brand("Tesla")
                                        .model("Model S")
                                        .licensePlate("5-MNO-345")
                                        .types("Sedan")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(false)
                                        .fuel(100.0)
                                        .fuelCapacity(70.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(4.0)
                                        .pricePerKm(0.8)
                                        .pricePerLiterFuel(1.8)
                                        .build();

                        Car ford = Car.builder()
                                        .brand("Ford")
                                        .model("Mustang")
                                        .licensePlate("6-PQR-678")
                                        .types("Sports Car")
                                        .numberOfSeats(4)
                                        .numberOfChildSeats(0)
                                        .towBar(false)
                                        .foldingRearSeat(false)
                                        .fuel(100.0)
                                        .fuelCapacity(70.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(10.0)
                                        .pricePerKm(0.9)
                                        .pricePerLiterFuel(2.0)
                                        .build();

                        Car toyota = Car.builder()
                                        .brand("Toyota")
                                        .model("Camry")
                                        .licensePlate("7-STU-901")
                                        .types("Sedan")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(60.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(6.5)
                                        .pricePerKm(0.7)
                                        .pricePerLiterFuel(1.5)
                                        .build();

                        Car honda = Car.builder()
                                        .brand("Honda")
                                        .model("CR-V")
                                        .licensePlate("8-VWX-234")
                                        .types("SUV")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(65.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(7.5)
                                        .pricePerKm(0.6)
                                        .pricePerLiterFuel(1.6)
                                        .build();

                        Car mercedes = Car.builder()
                                        .brand("Mercedes-Benz")
                                        .model("E-Class")
                                        .licensePlate("9-YZA-567")
                                        .types("Luxury Sedan")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(false)
                                        .fuel(100.0)
                                        .fuelCapacity(65.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(7.0)
                                        .pricePerKm(0.8)
                                        .pricePerLiterFuel(1.8)
                                        .build();

                        Car chevrolet = Car.builder()
                                        .brand("Chevrolet")
                                        .model("Silverado")
                                        .licensePlate("10-BCD-890")
                                        .types("Pickup Truck")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(0)
                                        .towBar(true)
                                        .foldingRearSeat(false)
                                        .fuel(100.0)
                                        .fuelCapacity(80.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(12.0)
                                        .pricePerKm(0.7)
                                        .pricePerLiterFuel(1.5)
                                        .build();

                        Car subaru = Car.builder()
                                        .brand("Subaru")
                                        .model("Outback")
                                        .licensePlate("11-CDE-012")
                                        .types("Crossover")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(60.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(8.0)
                                        .pricePerKm(0.6)
                                        .pricePerLiterFuel(1.6)
                                        .build();

                        Car mazda = Car.builder()
                                        .brand("Mazda")
                                        .model("CX-5")
                                        .licensePlate("12-DEF-345")
                                        .types("Compact SUV")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(55.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(7.5)
                                        .pricePerKm(0.5)
                                        .pricePerLiterFuel(1.4)
                                        .build();

                        Car kia = Car.builder()
                                        .brand("Kia")
                                        .model("Sorento")
                                        .licensePlate("13-EFG-678")
                                        .types("Midsize SUV")
                                        .numberOfSeats(7)
                                        .numberOfChildSeats(3)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(70.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(9.0)
                                        .pricePerKm(0.7)
                                        .pricePerLiterFuel(1.7)
                                        .build();

                        Car jeep = Car.builder()
                                        .brand("Jeep")
                                        .model("Wrangler")
                                        .licensePlate("14-GHI-901")
                                        .types("Off-road SUV")
                                        .numberOfSeats(4)
                                        .numberOfChildSeats(0)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(70.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(10.0)
                                        .pricePerKm(0.8)
                                        .pricePerLiterFuel(1.9)
                                        .build();

                        Car fiat = Car.builder()
                                        .brand("Fiat")
                                        .model("500")
                                        .licensePlate("15-HIJ-234")
                                        .types("Compact Car")
                                        .numberOfSeats(4)
                                        .numberOfChildSeats(1)
                                        .towBar(false)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(40.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(5.5)
                                        .pricePerKm(0.4)
                                        .pricePerLiterFuel(1.3)
                                        .build();

                        Car landRover = Car.builder()
                                        .brand("Land Rover")
                                        .model("Range Rover Sport")
                                        .licensePlate("16-IJK-345")
                                        .types("Luxury SUV")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(true)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(80.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(9.5)
                                        .pricePerKm(0.9)
                                        .pricePerLiterFuel(2.0)
                                        .build();

                        Car hyundai = Car.builder()
                                        .brand("Hyundai")
                                        .model("Tucson")
                                        .licensePlate("17-JKL-456")
                                        .types("Compact SUV")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(55.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(7.0)
                                        .pricePerKm(0.6)
                                        .pricePerLiterFuel(1.5)
                                        .build();

                        Car lexus = Car.builder()
                                        .brand("Lexus")
                                        .model("RX")
                                        .licensePlate("18-KLM-567")
                                        .types("Luxury SUV")
                                        .numberOfSeats(5)
                                        .numberOfChildSeats(2)
                                        .towBar(false)
                                        .foldingRearSeat(true)
                                        .fuel(100.0)
                                        .fuelCapacity(70.0)
                                        .mileage(1000.0)
                                        .fuelEstimatedConsumption(8.5)
                                        .pricePerKm(0.7)
                                        .pricePerLiterFuel(1.8)
                                        .build();

                        System.out.println("start filling cars");

                        carService.addCar(bmw, rein.getEmail());
                        carService.addCar(audi, torben.getEmail());
                        carService.addCar(volkswagen, torben.getEmail());
                        carService.addCar(volvo, torben.getEmail());
                        carService.addCar(tesla, rein.getEmail());
                        carService.addCar(ford, rein.getEmail());
                        carService.addCar(toyota, rein.getEmail());
                        carService.addCar(honda, rein.getEmail());
                        carService.addCar(mercedes, rein.getEmail());
                        carService.addCar(chevrolet, rein.getEmail());
                        carService.addCar(subaru, rein.getEmail());
                        carService.addCar(mazda, rein.getEmail());
                        carService.addCar(kia, rein.getEmail());
                        carService.addCar(jeep, rein.getEmail());
                        carService.addCar(fiat, rein.getEmail());
                        carService.addCar(landRover, rein.getEmail());
                        carService.addCar(hyundai, rein.getEmail());
                        carService.addCar(lexus, rein.getEmail());

                        setAllCars(carService.getAllCars());

                        System.out.println("start filling rentals");

                        Rental rental1 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Tiensestraat")
                                        .number("1")
                                        .postal("3000")
                                        .city("Leuven")
                                        .phoneNumber("0472000000")
                                        // .email("Henk@ucll.be")
                                        .longitude(4.7017) // Longitude for Leuven
                                        .latitude(50.8798) // Latitude for Leuven
                                        .build(), bmw.getId());

                        Rental rental2 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now().plusDays(6))
                                        .endDate(LocalDateTime.now().plusDays(10))
                                        .street("Tiensestraat")
                                        .number("1")
                                        .postal("3000")
                                        .city("Leuven")
                                        .phoneNumber("0472000000")
                                        // .email("Dirk@ucll.be")
                                        .longitude(4.7017) // Longitude for Leuven
                                        .latitude(50.8798) // Latitude for Leuven
                                        .build(), bmw.getId());

                        Rental rental3 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Main Street")
                                        .number("42")
                                        .postal("12345")
                                        .city("Cityville")
                                        .phoneNumber("0487123456")
                                        // .email("user1@example.com")
                                        .longitude(-0.1276) // Longitude for Cityville (assuming it's close to London)
                                        .latitude(51.5074) // Latitude for Cityville (assuming it's close to London)
                                        .build(), audi.getId());

                        Rental rental4 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Main Street")
                                        .number("42")
                                        .postal("12345")
                                        .city("Cityville")
                                        .phoneNumber("0487123456")
                                        // .email("user2@example.com")
                                        .longitude(-0.1276) // Longitude for Cityville (assuming it's close to London)
                                        .latitude(51.5074) // Latitude for Cityville (assuming it's close to London)
                                        .build(), audi.getId());

                        Rental rental5 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Pine Street")
                                        .number("8")
                                        .postal("67890")
                                        .city("Villageville")
                                        .phoneNumber("0472345678")
                                        // .email("user3@example.com")
                                        .longitude(-74.0060) // Longitude for Villageville (assuming it's close to New
                                                             // York City)
                                        .latitude(40.7128) // Latitude for Villageville (assuming it's close to New York
                                                           // City)
                                        .build(), volkswagen.getId());

                        Rental rental6 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Pine Street")
                                        .number("8")
                                        .postal("67890")
                                        .city("Villageville")
                                        .phoneNumber("0472345678")
                                        // .email("user4@example.com")
                                        .longitude(-74.0060) // Longitude for Villageville (assuming it's close to New
                                                             // York City)
                                        .latitude(40.7128) // Latitude for Villageville (assuming it's close to New York
                                                           // City)
                                        .build(), volkswagen.getId());

                        Rental rental7 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Cedar Lane")
                                        .number("23")
                                        .postal("24680")
                                        .city("Countryside")
                                        .phoneNumber("0498567890")
                                        // .email("user5@example.com")
                                        .longitude(-95.7129) // Longitude for Countryside (assuming it's close to
                                                             // Houston)
                                        .latitude(37.0902) // Latitude for Countryside (assuming it's close to Houston)
                                        .build(), tesla.getId());

                        Rental rental8 = rentalService.addRental(Rental.builder()
                                        .startDate(LocalDateTime.now())
                                        .endDate(LocalDateTime.now().plusDays(5))
                                        .street("Cedar Lane")
                                        .number("23")
                                        .postal("24680")
                                        .city("Countryside")
                                        .phoneNumber("0498567890")
                                        // .email("bram@ucll.be")
                                        .longitude(-95.7129) // Longitude for Countryside (assuming it's close to
                                                             // Houston)
                                        .latitude(37.0902) // Latitude for Countryside (assuming it's close to Houston)
                                        .build(), tesla.getId());

                        // populate Leuven with 100 random rentals
                        for (int i = 0; i < 100; i++) {
                                double latitude = 50.8798 + Math.random() * 0.05 - 0.025; // Random latitude near Leuven
                                double longitude = 4.7017 + Math.random() * 0.05 - 0.025; // Random longitude near
                                                                                          // Leuven

                                Rental rental = Rental.builder()
                                                .startDate(LocalDateTime.now())
                                                .endDate(LocalDateTime.now().plusDays(5))
                                                .street("Street" + (i + 1)) // Example street name
                                                .number(String.valueOf(i + 2)) // Example street number
                                                .postal("3000") // Example postal code for Leuven
                                                .city("Leuven")
                                                .phoneNumber("047200000" + i) // Example phone number
                                                // .email("rental" + (i + 1) + "@example.com") // Example email address
                                                .longitude(longitude)
                                                .latitude(latitude)
                                                .build();

                                rentalService.addRental(rental, getRandomCar().getId());
                        }

                        // Create and add 1000 more rentals covering Europe
                        for (int i = 0; i < 100; i++) {
                                // Random latitude and longitude coordinates covering Belgium
                                double latitude = ThreadLocalRandom.current().nextDouble(36, 60); // Latitude range:
                                                                                                  // 36°N to 60°N
                                                                                                  // (roughly covers
                                                                                                  // Western Europe)
                                double longitude = ThreadLocalRandom.current().nextDouble(-10, 25); // Longitude range:
                                                                                                    // -10°E to 25°E
                                                                                                    // (roughly covers
                                                                                                    // Western Europe)

                                LocalDateTime startDate = getRandomDateTime();

                                LocalDateTime endDate = startDate.plusDays(getRandomNumberInRange(2, 10));

                                Rental rental = Rental.builder()
                                                .startDate(startDate)
                                                .endDate(endDate)
                                                .street("RandomCarStreet")
                                                .number(String.valueOf(i + 2))
                                                .postal("1")
                                                .city("RandomCarCity")
                                                .phoneNumber("0472000000")
                                                // .email("rental" + (i + 1) + "@example.com")
                                                .longitude(longitude)
                                                .latitude(latitude)
                                                .build();

                                rentalService.addRental(rental, getRandomCar().getId());
                        }

                        System.out.println("Start filling rents");

                        Rent rent1 = rentService.rentRequest(
                                        Rent.builder()
                                                        .phoneNumber("0472000000")
                                                        .birthDate(LocalDate.of(2000, 1, 1))
                                                        .nationalIdentificationNumber("123456789")
                                                        .drivingLicenseNumber("123456789")
                                                        .build(),
                                        rental1.getCar().getId(), rental1.getId(), axel.getEmail());

                        Rent rent2 = rentService.rentRequest(
                                        Rent.builder()
                                                        .phoneNumber("0472000001")
                                                        .birthDate(LocalDate.of(1995, 5, 15))
                                                        .nationalIdentificationNumber("987654321")
                                                        .drivingLicenseNumber("987654321")
                                                        .build(),
                                        rental2.getCar().getId(), rental2.getId(), axel.getEmail());

                        Rent rent3 = rentService.rentRequest(
                                        Rent.builder()
                                                        .phoneNumber("0472000002")
                                                        .birthDate(LocalDate.of(1998, 9, 20))
                                                        .nationalIdentificationNumber("135792468")
                                                        .drivingLicenseNumber("135792468")
                                                        .build(),
                                        rental3.getCar().getId(), rental3.getId(), axel.getEmail());

                        Rent rent4 = rentService.rentRequest(
                                        Rent.builder()
                                                        .phoneNumber("0472000003")
                                                        .birthDate(LocalDate.of(1985, 3, 10))
                                                        .nationalIdentificationNumber("246813579")
                                                        .drivingLicenseNumber("246813579")
                                                        .build(),
                                        rental4.getCar().getId(), rental4.getId(), axel.getEmail());

                        Rent rent5 = rentService.rentRequest(
                                        Rent.builder()
                                                        .phoneNumber("0472000004")
                                                        .birthDate(LocalDate.of(1992, 12, 25))
                                                        .nationalIdentificationNumber("369852147")
                                                        .drivingLicenseNumber("369852147")
                                                        .build(),
                                        rental5.getCar().getId(), rental5.getId(), axel.getEmail());

                        rentService.confirmRent(rent1.getId(), rein.getEmail());
                        rentService.confirmRent(rent2.getId(), rein.getEmail());

                        rentService.cancelRent(rent3.getId());
                        rentService.cancelRent(rent4.getId());

                        System.out.println("\nAll fills complete");

                        String tokenOwner = jwtService.generateToken(rein);
                        String tokenRenter = jwtService.generateToken(axel);
                        String tokenAccountant = jwtService.generateToken(bram);
                        String tokenAdmin = jwtService.generateToken(loveleen);

                        System.out.println("\n\n");
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("\n");
                        System.out.println("\nToken Owner:");
                        System.out.println(tokenOwner);
                        System.out.println("\nToken Renter:");
                        System.out.println(tokenRenter);
                        System.out.println("\nToken Accountant:");
                        System.out.println(tokenAccountant);
                        System.out.println("\nToken Admin:");
                        System.out.println(tokenAdmin);
                        System.out.println("\n");
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("\n\n");

                } else {
                        System.out.println("fill skipped");

                        // String tokenOwner =
                        // jwtService.generateToken(userService.getUserByEmail("rein@ucll.be"));
                        // String tokenRenter =
                        // jwtService.generateToken(userService.getUserByEmail("axel@ucll.be"));
                        // String tokenAccountant =
                        // jwtService.generateToken(userService.getUserByEmail("bram@ucll.be"));
                        // String tokenAdmin =
                        // jwtService.generateToken(userService.getUserByEmail("loveleen@ucll.be"));

                        // System.out.println("\n\n");
                        // System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        // System.out.println("\n");
                        // System.out.println("\nToken Owner:");
                        // System.out.println(tokenOwner);
                        // System.out.println("\nToken Renter:");
                        // System.out.println(tokenRenter);
                        // System.out.println("\nToken Accountant:");
                        // System.out.println(tokenAccountant);
                        // System.out.println("\nToken Admin:");
                        // System.out.println(tokenAdmin);
                        // System.out.println("\n");
                        // System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        // System.out.println("\n\n");

                }
        }
}
