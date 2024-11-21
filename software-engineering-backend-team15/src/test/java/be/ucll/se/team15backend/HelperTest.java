//package be.ucll.se.team15backend;
//
//import be.ucll.se.team15backend.authentication.model.RegisterRequest;
//import be.ucll.se.team15backend.authentication.service.AuthService;
//import be.ucll.se.team15backend.bot.service.BotService;
//import be.ucll.se.team15backend.car.model.Car;
//import be.ucll.se.team15backend.car.repo.CarRepository;
//import be.ucll.se.team15backend.car.service.CarService;
//import be.ucll.se.team15backend.chats.model.Room;
//import be.ucll.se.team15backend.chats.repo.ChatRepository;
//import be.ucll.se.team15backend.chats.service.ChatService;
//import be.ucll.se.team15backend.customexception.ServiceException;
//import be.ucll.se.team15backend.manage.service.ManageService;
//import be.ucll.se.team15backend.map.service.MapService;
//import be.ucll.se.team15backend.notification.repo.NotificationRepository;
//import be.ucll.se.team15backend.notification.service.NotificationService;
//import be.ucll.se.team15backend.planner.service.PlannerService;
//import be.ucll.se.team15backend.rent.model.Rent;
//import be.ucll.se.team15backend.rent.repo.RentRepository;
//import be.ucll.se.team15backend.rent.service.RentService;
//import be.ucll.se.team15backend.rental.model.Rental;
//import be.ucll.se.team15backend.rental.repo.RentalRepository;
//import be.ucll.se.team15backend.rental.service.RentalService;
//import be.ucll.se.team15backend.user.model.Role;
//import be.ucll.se.team15backend.user.model.UserException;
//import be.ucll.se.team15backend.user.model.UserModel;
//import be.ucll.se.team15backend.user.repo.UserRepository;
//import be.ucll.se.team15backend.user.service.UserService;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.ThreadLocalRandom;
//
//import static org.mockito.Mockito.clearInvocations;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//public class HelperTest {
//
//    @Mock
//    CarRepository carRepository;
//
//    @Mock
//    ChatRepository chatRepository;
//
//    @Mock
//    NotificationRepository notificationRepository;
//
//    @Mock
//    RentRepository rentRepository;
//
//    @Mock
//    RentalRepository rentalRepository;
//
//    @Mock
//    UserRepository userRepository;
//
//    @InjectMocks
//    AuthService authService;
//
//    @InjectMocks
//    BotService botService;
//
//    @InjectMocks
//    CarService carService;
//
//    @InjectMocks
//    ChatService chatService;
//
//    @InjectMocks
//    ManageService manageService;
//
//    @InjectMocks
//    MapService mapService;
//
//    @InjectMocks
//    NotificationService notificationService;
//
//    @InjectMocks
//    PlannerService plannerService;
//
//    @InjectMocks
//    RentService rentService;
//
//    @InjectMocks
//    RentalService rentalService;
//
//    @InjectMocks
//    UserService userService;
//
//    public static ValidatorFactory validatorFactory;
//    public static Validator validator;
//
//
//    public Room generalroom;
//
//    public UserModel bot;
//
//    public UserModel rein;
//
//    public UserModel axel;
//
//    public UserModel bram;
//
//
//    public Car bmw;
//
//    public Car audi;
//
//
//    public Rental rental1;
//
//    public Rental rental2;
//
//
//    public Rent rent1;
//
//    public Rent rent2;
//
//    Pageable pageable = PageRequest.of(0, 1000);
//
//
//    @BeforeAll
//    public static void createValidator() {
//        validatorFactory = Validation.buildDefaultValidatorFactory();
//        validator = validatorFactory.getValidator();
//    }
//
//    @AfterAll
//    public static void close() {
//        validatorFactory.close();
//    }
//
//    @BeforeEach
//    public void setup() throws UserException, ServiceException, IOException, InterruptedException {
//        MockitoAnnotations.openMocks(this);
//
//
//        generalroom =
//                Room.builder()
//                        .roomId("general")
//                        .roomName("General Chat")
//                        .build();
//
//
//
//
//
//
//        bot  = UserModel.builder()
//                .userId(1L)
//                .email("boxter.buddy@ucll.be")
//                .password("bot")
//                .role(Role.BOT)
//                .build();
//
//        rein  = UserModel.builder()
//                .userId(2L)
//                .email("rein@ucll.be")
//                .password("rein")
//                .role(Role.OWNER)
//                .build();
//
//        axel  = UserModel.builder()
//                .userId(3L)
//                .email("axel@ucll.be")
//                .password("axel")
//                .role(Role.RENTER)
//                .build();
//
//        bram  = UserModel.builder()
//                .userId(4L)
//                .email("bram@ucll.be")
//                .password("bram")
//                .role(Role.ACCOUNTANT)
//                .build();
//
//
//
//        bmw = Car.builder()
//                .id(1L)
//                .brand("BMW")
//                .model("X5")
//                .licensePlate("1-ABC-123")
//                .types("SUV")
//                .numberOfSeats(5)
//                .numberOfChildSeats(2)
//                .towBar(true)
//                .foldingRearSeat(true)
//                .fuel(100.0)
//                .fuelCapacity(55.0)
//                .mileage(1000.0)
//                .fuelEstimatedConsumption(7.0)
//                .pricePerKm(0.5)
//                .pricePerLiterFuel(1.5)
//                .build();
//
//        audi = Car.builder()
//                .id(2L)
//                .brand("Audi")
//                .model("A4")
//                .licensePlate("2-DEF-456")
//                .types("Sedan")
//                .numberOfSeats(5)
//                .numberOfChildSeats(2)
//                .towBar(false)
//                .foldingRearSeat(false)
//                .fuel(100.0)
//                .fuelCapacity(60.0)
//                .mileage(1000.0)
//                .fuelEstimatedConsumption(6.0)
//                .pricePerKm(0.6)
//                .pricePerLiterFuel(1.6)
//                .build();
//
//
//
//
//
//        rental1 = Rental.builder()
//                .startDate(LocalDateTime.now())
//                .endDate(LocalDateTime.now().plusDays(5))
//                .street("Tiensestraat")
//                .number("1")
//                .postal("3000")
//                .city("Leuven")
//                .phoneNumber("0472000000")
//                .longitude(4.7017)  // Longitude for Leuven
//                .latitude(50.8798)   // Latitude for Leuven
//                .build();
//
//
//
//        rental2 = Rental.builder()
//                .startDate(LocalDateTime.now())
//                .endDate(LocalDateTime.now().plusDays(5))
//                .street("Tiensestraat")
//                .number("1")
//                .postal("3000")
//                .city("Leuven")
//                .phoneNumber("0472000000")
//                .longitude(4.7017)  // Longitude for Leuven
//                .latitude(50.8798)   // Latitude for Leuven
//                .build();
//
//
//
//
//        rent1 =
//                Rent.builder()
//                        .phoneNumber("0472000000")
//                        .birthDate(LocalDate.of(2000, 1, 1))
//                        .nationalIdentificationNumber("123456789")
//                        .drivingLicenseNumber("123456789")
//                        .build();
//
//        rent2 =
//                Rent.builder()
//                        .phoneNumber("0472000001")
//                        .birthDate(LocalDate.of(1995, 5, 15))
//                        .nationalIdentificationNumber("987654321")
//                        .drivingLicenseNumber("987654321")
//                        .build();
//
//
//        when(carRepository.findById(1L)).thenReturn(bmw);
//        when(carRepository.findById(2L)).thenReturn(audi);
//
//        when(carRepository.findAllBy()).thenReturn(List.of(bmw, audi));
//
//        when(carRepository.findAllByOwner(pageable, rein)).thenReturn(new PageImpl<>(List.of(bmw)));
//        when(carRepository.findAllByOwner(pageable, axel)).thenReturn(new PageImpl<>(List.of(audi)));
//
//        when(carRepository.findByIdAndOwner(1L, rein)).thenReturn(Optional.ofNullable(bmw));
//        when(carRepository.findByIdAndOwner(2L, axel)).thenReturn(Optional.ofNullable(audi));
//
//        when(carRepository.save(bmw)).thenReturn(bmw);
//        when(carRepository.save(audi)).thenReturn(audi);
//
//
//
//
//
//
//
//
//
//
//
//        when(rentalRepository.findById(1L)).thenReturn(rental1);
//        when(rentalRepository.findById(2L)).thenReturn(rental2);
//        when(rentRepository.findById(1L)).thenReturn(Optional.ofNullable(rent1));
//        when(rentRepository.findById(2L)).thenReturn(Optional.ofNullable(rent2));
//
//
//
//
//        clearInvocations(carRepository, chatRepository, notificationRepository, rentRepository, rentalRepository, userRepository);
//    }
//
//}
