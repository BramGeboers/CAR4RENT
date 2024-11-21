package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.authentication.model.RegisterRequest;
import be.ucll.se.team15backend.authentication.service.AuthService;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.car.repo.CarRepository;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.chats.repo.ChatRepository;
import be.ucll.se.team15backend.chats.repo.RoomRepository;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.notification.repo.NotificationRepository;
import be.ucll.se.team15backend.notification.service.NotificationService;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.rent.repo.RentRepository;
import be.ucll.se.team15backend.rent.service.RentService;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import be.ucll.se.team15backend.rental.service.RentalService;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.repo.UserRepository;
import be.ucll.se.team15backend.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DataJpaTest
//@Transactional
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class HelperTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public WebTestClient client;


    @Autowired
    public CarRepository carRepository;

    @Autowired
    public ChatRepository chatRepository;

    @Autowired
    public RoomRepository roomRepository;

    @Autowired
    public NotificationRepository notificationRepository;

    @Autowired
    public RentRepository rentRepository;

    @Autowired
    public RentalRepository rentalRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CarService carService;
    @Autowired
    public RentalService rentalService;

    @Autowired
    public RentService rentService;

    @Autowired
    public AuthService authService;

    @Autowired
    public UserService userService;

    @Autowired
    public ChatService chatService;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public NotificationService notificationService;

    public String tokenReinOwner;
    public String tokenAxelRenter;
    public String tokenBramAccountant;
    public String tokenLoveleenAdmin;
    public String tokenTorbenOwner;

//    @BeforeAll

    @BeforeEach
    public void setUp() throws UserException, ServiceException, IOException, InterruptedException {


        notificationRepository.deleteAll();
        rentalRepository.deleteAll();
        rentRepository.deleteAll();
        carRepository.deleteAll();
        //debug message
        System.out.println("Data deleted");
        //List<Chat> chats = chatRepository.findAll();
        roomRepository.deleteAll();
        chatRepository.deleteAll();
        //console log to see if the data is deleted
        System.out.println("chats deleted");

//        userRepository.forceDelete();
        System.out.println("users:");
        System.out.println(userRepository.findAll().stream().map(UserModel::getEmail).toList());
        System.out.println("chats:");
        System.out.println(chatRepository.findAll().stream().map(Chat::toString).toList());


//        roomRepository.deleteAll();

//        roomRepository.deleteAll();


        chatService.saveRoom(
                Room.builder()
                        .roomId("general")
                        .roomName("General Chat")
                        .build()
        );



        UserModel bot  = authService.register(RegisterRequest.builder()
                .email("boxter.buddy@ucll.be")
                .password("bot")
                .role("BOT")
                .build(), "", "", true).getUser();

        UserModel rein  = authService.register(RegisterRequest.builder()
                .email("rein@ucll.be")
                .password("rein")
                .role("OWNER")
                .build(), "", "", true).getUser();

        UserModel axel  = authService.register(RegisterRequest.builder()
                .email("axel@ucll.be")
                .password("axel")
                .role("RENTER")
                .build(), "", "", true).getUser();

        UserModel bram  = authService.register(RegisterRequest.builder()
                .email("bram@ucll.be")
                .password("bram")
                .role("ACCOUNTANT")
                .build(), "", "", true).getUser();

        UserModel bramgeboers  = authService.register(RegisterRequest.builder()
                .email("bramgeboers@ucll.be")
                .password("bram")
                .role("ADMIN")
                .build(), "", "", true).getUser();

        UserModel loveleen  = authService.register(RegisterRequest.builder()
                .email("loveleen@ucll.be")
                .password("loveleen")
                .role("ADMIN")
                .build(), "", "", true).getUser();

        UserModel wout  = authService.register(RegisterRequest.builder()
                .email("wout@ucll.be")
                .password("wout")
                .role("ADMIN")
                .build(), "", "", true).getUser();

        UserModel torben  = authService.register(RegisterRequest.builder()
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




        Rental rental1 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Tiensestraat")
                .number("1")
                .postal("3000")
                .city("Leuven")
                .phoneNumber("0472000000")
//                    .email("Henk@ucll.be")
                .longitude(4.7017)  // Longitude for Leuven
                .latitude(50.8798)   // Latitude for Leuven
                .build(), bmw.getId());



        Rental rental2 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Tiensestraat")
                .number("1")
                .postal("3000")
                .city("Leuven")
                .phoneNumber("0472000000")
//                    .email("Dirk@ucll.be")
                .longitude(4.7017)  // Longitude for Leuven
                .latitude(50.8798)   // Latitude for Leuven
                .build(), bmw.getId());



        Rental rental3 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Main Street")
                .number("42")
                .postal("12345")
                .city("Cityville")
                .phoneNumber("0487123456")
//                    .email("user1@example.com")
                .longitude(-0.1276)  // Longitude for Cityville (assuming it's close to London)
                .latitude(51.5074)   // Latitude for Cityville (assuming it's close to London)
                .build(), audi.getId());



        Rental rental4 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Main Street")
                .number("42")
                .postal("12345")
                .city("Cityville")
                .phoneNumber("0487123456")
//                    .email("user2@example.com")
                .longitude(-0.1276)  // Longitude for Cityville (assuming it's close to London)
                .latitude(51.5074)   // Latitude for Cityville (assuming it's close to London)
                .build(), audi.getId());



        Rental rental5 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Pine Street")
                .number("8")
                .postal("67890")
                .city("Villageville")
                .phoneNumber("0472345678")
//                    .email("user3@example.com")
                .longitude(-74.0060)  // Longitude for Villageville (assuming it's close to New York City)
                .latitude(40.7128)    // Latitude for Villageville (assuming it's close to New York City)
                .build(), volkswagen.getId());



        Rental rental6 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Pine Street")
                .number("8")
                .postal("67890")
                .city("Villageville")
                .phoneNumber("0472345678")
//                    .email("user4@example.com")
                .longitude(-74.0060)  // Longitude for Villageville (assuming it's close to New York City)
                .latitude(40.7128)    // Latitude for Villageville (assuming it's close to New York City)
                .build(), volkswagen.getId());




        Rental rental7 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Cedar Lane")
                .number("23")
                .postal("24680")
                .city("Countryside")
                .phoneNumber("0498567890")
//                    .email("user5@example.com")
                .longitude(-95.7129)  // Longitude for Countryside (assuming it's close to Houston)
                .latitude(37.0902)    // Latitude for Countryside (assuming it's close to Houston)
                .build(), tesla.getId());

        Rental rental8 = rentalService.addRental(Rental.builder()
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .street("Cedar Lane")
                .number("23")
                .postal("24680")
                .city("Countryside")
                .phoneNumber("0498567890")
//                .email("bram@ucll.be")
                .longitude(-95.7129)  // Longitude for Countryside (assuming it's close to Houston)
                .latitude(37.0902)    // Latitude for Countryside (assuming it's close to Houston)
                .build(), tesla.getId());






        Rent rent1 = rentService.rentRequest(
                Rent.builder()
                        .phoneNumber("0472000000")
                        .birthDate(LocalDate.of(2000, 1, 1))
                        .nationalIdentificationNumber("123456789")
                        .drivingLicenseNumber("123456789")
                        .build(), rental1.getCar().getId(), rental1.getId(), axel.getEmail());

        Rent rent2 = rentService.rentRequest(
                Rent.builder()
                        .phoneNumber("0472000001")
                        .birthDate(LocalDate.of(1995, 5, 15))
                        .nationalIdentificationNumber("987654321")
                        .drivingLicenseNumber("987654321")
                        .build(), rental2.getCar().getId(), rental2.getId(), axel.getEmail());

        Rent rent3 = rentService.rentRequest(
                Rent.builder()
                        .phoneNumber("0472000002")
                        .birthDate(LocalDate.of(1998, 9, 20))
                        .nationalIdentificationNumber("135792468")
                        .drivingLicenseNumber("135792468")
                        .build(), rental3.getCar().getId(), rental3.getId(), axel.getEmail());

        Rent rent4 = rentService.rentRequest(
                Rent.builder()
                        .phoneNumber("0472000003")
                        .birthDate(LocalDate.of(1985, 3, 10))
                        .nationalIdentificationNumber("246813579")
                        .drivingLicenseNumber("246813579")
                        .build(), rental4.getCar().getId(), rental4.getId(), axel.getEmail());

        Rent rent5 = rentService.rentRequest(
                Rent.builder()
                        .phoneNumber("0472000004")
                        .birthDate(LocalDate.of(1992, 12, 25))
                        .nationalIdentificationNumber("369852147")
                        .drivingLicenseNumber("369852147")
                        .build(), rental5.getCar().getId(), rental5.getId(), axel.getEmail());

        rentService.confirmRent(rent1.getId(), rein.getEmail());
        rentService.confirmRent(rent2.getId(), rein.getEmail());

        rentService.cancelRent(rent3.getId());
        rentService.cancelRent(rent4.getId());

        tokenReinOwner = jwtService.generateToken(rein);
        tokenAxelRenter = jwtService.generateToken(axel);
        tokenBramAccountant = jwtService.generateToken(bram);
        tokenLoveleenAdmin = jwtService.generateToken(loveleen);
        tokenTorbenOwner = jwtService.generateToken(torben);




    }

    @AfterEach
    public void cleanUp() {
//        notificationRepository.deleteAll();
//        rentalRepository.deleteAll();
//        rentRepository.deleteAll();
//        carRepository.deleteAll();
//        userRepository.deleteAll();

    }

//    @AfterEach
//    public void tearDown() {
//        entityManager.flush(); // Flush any changes to the database to ensure consistency
//        entityManager.clear(); // Clear the persistence context to detach all entities
//    }


}
