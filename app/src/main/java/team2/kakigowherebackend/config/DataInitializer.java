package team2.kakigowherebackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team2.kakigowherebackend.model.*;
import team2.kakigowherebackend.repository.AdminRepository;
import team2.kakigowherebackend.repository.ItineraryRepository;
import team2.kakigowherebackend.repository.PlaceRepository;
import team2.kakigowherebackend.repository.TouristRepository;
import team2.kakigowherebackend.service.ItineraryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final PlaceRepository placeRepo;
    private final TouristRepository touristRepo;
    private final AdminRepository adminRepo;
    private final ItineraryRepository itineraryRepo;
    private final ItineraryService itineraryService;

    public DataInitializer(
            PlaceRepository placeRepo,
            TouristRepository touristRepo,
            AdminRepository adminRepo,
            ItineraryRepository itineraryRepo, ItineraryService itineraryService) {
        this.placeRepo = placeRepo;
        this.touristRepo = touristRepo;
        this.adminRepo = adminRepo;
        this.itineraryRepo = itineraryRepo;
        this.itineraryService = itineraryService;
    }

    @Override
    public void run(String... args) throws Exception {
        addPlaces();
        addTourist();
        addAdmin();
        addItineraries();
    }

    private void addPlaces() {
        Place checkPlace = placeRepo.findByGoogleId("ChIJFQzeR6cZ2jERgM6--iWeY-U");
        if (checkPlace != null) return;

        log.info("Initializing places...");

        List<Place> places = new ArrayList<>();
        places.add(new Place("National Gallery Singapore", "ChIJFQzeR6cZ2jERgM6--iWeY-U"));
        places.add(new Place("Sultan Mosque", "ChIJb6xq2bAZ2jERE1P6sVKtofw"));
        places.add(new Place("Sri Mariamman Temple", "ChIJ0yjU0QwZ2jER6gg0ImuCTRU"));
        places.add(
                new Place(
                        "Armenian Apostolic Church of St. Gregory the Illuminator",
                        "ChIJqc0-Q6EZ2jERbzBEE3qq6p0"));
        places.add(new Place("CHIJMES", "ChIJNfw8b6QZ2jEREbGYft7-Q4A"));
        places.add(new Place("St Andrew's Cathedral", "ChIJ_xBP6qYZ2jERrO-IiImXiGw"));
        places.add(new Place("Kreta Ayer Square", "ChIJycZu3HIZ2jER5Miq4PYPupY"));
        places.add(new Place("Albert Mall Walking Street", "ChIJg766UwAZ2jERcygIqfA86eI"));
        places.add(new Place("Chinatown Food Street", "ChIJY4OYJXMZ2jERq9aW_MKjwAc"));
        places.add(new Place("Chinatown Heritage Centre", "ChIJ3eg6SXMZ2jERCxutt4adUyY"));
        places.add(new Place("Thian Hock Keng Temple", "ChIJ9e68CQ0Z2jER4G80iFVcJgo"));
        places.add(new Place("Eurasian Heritage Gallery", "ChIJCxTorhMY2jERNG9vFuhEdQE"));
        places.add(new Place("Capitol Building", "ChIJc1XmpqYZ2jERPY-Bsk7YDQI"));
        places.add(
                new Place(
                        "Esplanade - Theatres on the Bay, Singapore",
                        "ChIJSeUa7KcZ2jERNVg2CvmlVbk"));
        places.add(new Place("Gardens by the Bay", "ChIJMxZ-kwQZ2jERdsqftXeWCWI"));
        places.add(new Place("The Interlace", "ChIJi-aa3Mgb2jERGN15MeHVxtQ"));
        places.add(new Place("PARKROYAL COLLECTION Pickering", "ChIJo8cvPgsZ2jERzUp0v52SdI0"));
        places.add(new Place("LASALLE College of the Arts", "ChIJRbtK5bsZ2jERnDd0Le_s0A4"));
        places.add(new Place("Kranji War Memorial", "ChIJuwtSxDwS2jER8rdI5kRmbl0"));
        places.add(
                new Place("Indian National Army Historic Marker", "ChIJdV9ag6cZ2jER9a31HQ2jN5w"));
        places.add(new Place("Reflections at Bukit Chandu", "ChIJ97ZlvLkb2jERzq5JKKSBQEE"));
        places.add(new Place("Singapore Cenotaph", "ChIJdRyuoacZ2jERVtOObm6P2Cg"));
        places.add(new Place("Civilian War Memorial", "ChIJw10lcqYZ2jERTvrSjFnHjts"));
        places.add(new Place("Sri Veeramakaliamman Temple", "ChIJ8zLPY8cZ2jEROE-GeGiS7GQ"));
        places.add(new Place("Maghain Aboth Synagogue", "ChIJc3Wuq6QZ2jERYH1Op8S-DL0"));
        places.add(new Place("Former Supreme Court", "ChIJfSAMRacZ2jER_qPZbi1mx6w"));
        places.add(new Place("Tiong Bahru View", "ChIJrc5itnkZ2jERHOx697G2ylI"));
        places.add(new Place("Lau Pa Sat", "ChIJ5Y6l4Q0Z2jERYL0KDIjT6v0"));
        places.add(new Place("Istana Kampong Glam", "ChIJ2TH1froZ2jERnsehsXN2VJc"));
        places.add(new Place("Peranakan Museum", "ChIJWZX956MZ2jERIGdnbs_SgMw"));
        places.add(new Place("Raffles Singapore", "ChIJwUTKu6UZ2jERwt5ctkU4f2Q"));
        places.add(new Place("Gillman Barracks", "ChIJd-ET2cUb2jER9nfWEH4mCX0"));
        places.add(new Place("ArtScience Museum", "ChIJnWdQKQQZ2jERScXuKeFHyIE"));
        places.add(new Place("SAM at 8Q", "ChIJB0fevaQZ2jERCSQzB8iAY70"));
        places.add(new Place("Masjid Jamae (Chulia)", "ChIJVQ7VtwwZ2jERJiI42tiD34w"));
        places.add(
                new Place(
                        "Institute of Contemporary Arts Singapore", "ChIJp5zm5LsZ2jERHpGcpdFqjcI"));
        places.add(new Place("Singapore Flyer", "ChIJzVHFNqkZ2jERboLN2YrltH8"));
        places.add(new Place("Marina Bay Sands Singapore", "ChIJA5LATO4Z2jER111V-v6abAI"));
        places.add(new Place("Marina Barrage", "ChIJ50uIMa0Z2jER0cTt5fLaZt0"));
        places.add(new Place("Henderson Waves", "ChIJeXrAvBcb2jERhWAujA0K0LE"));
        places.add(new Place("Pinnacle@Duxton", "ChIJVSyoe2wZ2jERcL3VxGbPvHs"));
        places.add(new Place("Masjid Hajjah Fatimah", "ChIJI15m97MZ2jERI3pwY4tc-1o"));
        places.add(new Place("Haw Par Villa", "ChIJ9ak4I6wb2jERO6x4oAUh_-g"));
        places.add(new Place("NUS Baba House", "ChIJvwiR9W4Z2jER3TtTDTsmA7I"));
        places.add(new Place("Hong San See Temple", "ChIJ1dGx2p4Z2jERX7D0mj1D0Hk"));
        places.add(new Place("Chinese Heritage Centre", "ChIJwxpUtZ8P2jERPbeGV3upuaY"));
        places.add(new Place("Kampong Glam", "ChIJ18kcLbEZ2jERHV-sR1RGyZc"));
        places.add(
                new Place(
                        "NTU Centre for Contemporary Art Singapore",
                        "ChIJmc-Y_sMb2jERz1q_-aHQgRk"));
        places.add(new Place("Red Dot Design Museum", "ChIJq1KHxhIZ2jER7fOxY9pR-vo"));
        places.add(
                new Place(
                        "Singapore Art Museum at Tanjong Pagar Distripark",
                        "ChIJPS0vSwsZ2jERulqTcd4CFDA"));
        places.add(new Place("National Design Centre", "ChIJX3kq1roZ2jERA1959fq81us"));
        places.add(new Place("Victoria Concert Hall", "ChIJzf487wkZ2jERP3nJhCFBdrE"));
        places.add(new Place("NUS Museum", "ChIJcwAAUPoa2jERiv_0ph5iKlg"));
        places.add(new Place("Dr. Sun Yat-Sen Memorial House", "ChIJ6abUBm6pQjQRwfEJmDRkczE"));
        places.add(new Place("Asian Civilisations Museum", "ChIJoZOhmQkZ2jERehLfvKlsoCA"));
        places.add(new Place("National Museum of Singapore", "ChIJD1u-EaMZ2jERaLhNfFkR45I"));
        places.add(new Place("Japanese Cemetery Park", "ChIJvb6oO1MW2jER05RT5zRS-o8"));
        places.add(new Place("Dalhousie Obelisk", "ChIJRbqIowkZ2jERitZMRQRGCH0"));
        places.add(new Place("Statue of Sir Stamford Raffles", "ChIJN21BlAkZ2jERGmrNMLOQQEI"));
        places.add(new Place("Children's Museum Singapore", "ChIJixzGhuMZ2jERpijfVdsTdFw"));
        places.add(new Place("Former Ford Factory", "ChIJTcyXK1oQ2jERMjEftNlLGk0"));
        places.add(new Place("Singapore City Gallery", "ChIJRaIcWw0Z2jERts3whuuZPsM"));
        places.add(new Place("Changi Chapel & Museum", "ChIJCQHB6vc82jERq6iFty4Fzo4"));
        places.add(new Place("MINT Museum of Toys", "ChIJW7B7zRIZ2jERINTI_uV1O-A"));
        places.add(new Place("Lim Bo Seng Memorial", "ChIJq6ru0QkZ2jERI5XgbHAqbnA"));
        places.add(new Place("Universal Studios Singapore", "ChIJQ6MVplUZ2jERn1LmNH0DlDA"));
        places.add(new Place("Science Centre Singapore", "ChIJY618FAQQ2jERzo1f5IAj4Bg"));
        places.add(new Place("Fuk Tak Chi Museum", "ChIJSQv4eg0Z2jERc2v3-CNAdPs"));
        places.add(new Place("Katong Antique House", "ChIJtThnSnMY2jER1o1poSw5EtE"));
        places.add(new Place("Chinese Garden", "ChIJuZ4kdB0Q2jERdp1SrojDF_I"));
        places.add(new Place("East Coast Park", "ChIJ0QX_Brki2jER-pZKNdqk_a8"));
        places.add(new Place("Fort Canning Park", "ChIJVSYjJKIZ2jERpRFinATD52s"));
        places.add(new Place("Kusu Island", "ChIJSbEk2-Ye2jERy8TeRShVgQo"));
        places.add(new Place("Bird Paradise", "ChIJkyCpkWcT2jER1Nsl0ZvOI4Q"));
        places.add(new Place("HortPark", "ChIJC_hOgcgb2jERfF9HNB10xfI"));
        places.add(new Place("Adventure Cove Waterpark", "ChIJS0Dhxf0b2jERAuG9YaPrYjA"));
        places.add(new Place("Singapore River", "ChIJb0N2WoIZ2jERUAQo0rer1jY"));
        places.add(new Place("Peranakan Houses", "ChIJQ83MYhIY2jERwAOUsQ_RdLA"));
        places.add(new Place("Sentosa", "ChIJRYMSeKwe2jERAR2QXVU39vg"));
        places.add(new Place("Chinatown", "ChIJ42h1onIZ2jERBbs-VGqmwrs"));
        places.add(new Place("Buddha Tooth Relic Temple", "ChIJ0bwmznIZ2jEREOCMNggtIBk"));
        places.add(new Place("Labrador Nature Reserve", "ChIJoy3rj-sb2jERA_wMVazFAiI"));
        places.add(new Place("Chek Jawa Wetlands", "ChIJUxOzlaQ-2jERRAkZtSCzcXI"));
        places.add(new Place("Merlion Park", "ChIJBTYg1g4Z2jERp_MBbu5erWY"));
        places.add(new Place("SkyPark Observation Deck", "ChIJOeEf9S2vewIRM0B9a06CKwg"));
        places.add(new Place("Resorts World Sentosa", "ChIJLR75v_0b2jERJrR28stYwMU"));
        places.add(new Place("Singapore Botanic Gardens", "ChIJvWDbfRwa2jERgNnTOpAU3-o"));
        places.add(new Place("Sungei Buloh Wetland Reserve", "ChIJe0cEPoIS2jERs_rS2qvGOxw"));
        places.add(new Place("Singapore Oceanarium", "ChIJj-3Cq_0b2jERJv2MBkSVsPQ"));
        places.add(new Place("Singapore Zoo", "ChIJr9wqENkT2jERkRs7pMj6FLQ"));
        places.add(new Place("River Wonders", "ChIJxZfX_9gT2jERknwK8es7IHU"));
        places.add(new Place("Singapore Cable Car", "ChIJAQAAAK0e2jERMFQeIT5IR-E"));
        places.add(new Place("Night Safari", "ChIJ9xUuiNcT2jER49FS2OpE8W8"));
        places.add(new Place("Pulau Ubin", "ChIJYcYVP2I-2jERQTiBy40oT6w"));
        places.add(new Place("Indian Heritage Centre", "ChIJlShO3LgZ2jER4Xlf4Yo-jbs"));
        places.add(new Place("Orchard Road", "ChIJu_7mSJEZ2jER-vT-Nz_3mY4"));
        places.add(new Place("MacRitchie Reservoir", "ChIJVVRNg0oX2jERxH0FUCJhoz4"));
        places.add(new Place("Civic District", "ChIJw4huyqAZ2jERXIhnlRWOfhI"));
        places.add(new Place("HarbourFront", "ChIJpV0-3uEb2jERl85WTf6I9n0"));
        places.add(new Place("Bras Basah Complex", "ChIJtxaD8KQZ2jERgCmvEt9PdiU"));
        placeRepo.saveAll(places);

        log.info("Initialized places");
    }

    private void addTourist() {
        Tourist checkTourist = touristRepo.findByEmail("a@kaki.com");
        if (checkTourist != null) return;

        log.info("Initializing tourists...");

        List<Tourist> tourists = new ArrayList<>();
        tourists.add(
                new Tourist(
                        "a@kaki.com",
                        "$2a$10$W1CH5b8ZXbj7bv3xr/DOhul3yzq8Xo7YKuNnvkx.FnSCFfICsDCZq",
                        "Adrian"));
        tourists.add(
                new Tourist(
                        "cy@kaki.com",
                        "$2a$10$hw5BfF4LiBRslUBUHOJrzePw3umSdJmC4UWuZPhhYzBrxLir5VSiG",
                        "Cai Yun"));
        tourists.add(
                new Tourist(
                        "gy@kaki.com",
                        "$2a$10$hgVY1PEMpNwdLBnZN7QJh.uaBqjxg5PxqYIgKVz1M2MQZkJXp.DtC",
                        "Gong Yuan"));
        tourists.add(
                new Tourist(
                        "ks@kaki.com",
                        "$2a$10$/Hur9v2dwu3aj.vxe.b4tumzSnwX4GiRFXf/p95JLIBMdF7HSzyDG",
                        "Kin Seng"));
        tourists.add(
                new Tourist(
                        "bf@kaki.com",
                        "$2a$10$NjngW1K/0brSfHiAhQVfv.ijVZ8jShMgcnPlM16Erg4SDXFVGEfKW",
                        "Bo Fei"));
        tourists.add(
                new Tourist(
                        "rx@kaki.com",
                        "$2a$10$JL1W16uSXhYMZ5F17WxTzeSCgp6tUExPukQ7v6zJGN5Mr5KQCcOse",
                        "Runxin"));
        touristRepo.saveAll(tourists);

        log.info("Initialized tourists");
    }

    private void addAdmin() {
        String email = "admin@kaki.com";
        Admin checkAdmin = adminRepo.findByEmail(email);
        if (checkAdmin != null) return;

        log.info("Initializing admins...");

        List<Admin> admins = new ArrayList<>();
        admins.add(
                new Admin(
                        email,
                        "$2a$12$8LH6o8kdF8MpW476frv4B.5wOJW08bIqIwxjUFfUEOsp6EQyBNYtO",
                        "Admin"));
        adminRepo.saveAll(admins);

        log.info("Initialized admins");
    }

    private void addItineraries() {
        String email = "cy@kaki.com";
        List<Itinerary> itinerariesCheck = itineraryRepo.findByTouristEmail(email);
        if (!itinerariesCheck.isEmpty()) return;

        log.info("Initializing itineraries...");

        Tourist tourist = touristRepo.findByEmail(email);

        Itinerary i1 = new Itinerary("My awesome itinerary", LocalDate.of(2025, 8, 1));
        i1.setItineraryDetails(List.of(
                new ItineraryDetail(
                        LocalDate.of(2025, 8, 1),
                        "Day 1 enjoy",
                        1,
                        placeRepo.findById(1L).get()),
                new ItineraryDetail(
                        LocalDate.of(2025, 8, 2),
                        "Day 2 woohoo",
                        2,
                        placeRepo.findById(2L).get()),
                new ItineraryDetail(
                        LocalDate.of(2025, 8, 3),
                        "Day 3 almost time to go :(",
                        3,
                        placeRepo.findById(3L).get())
        ));

        i1.setTourist(tourist);
        i1.getItineraryDetails().forEach(itineraryDetail -> { itineraryDetail.setItinerary(i1); });
        itineraryRepo.save(i1);

        Itinerary i2 = new Itinerary("My fun itinerary", LocalDate.of(2025, 8, 5));
        i2.setItineraryDetails(List.of(
                new ItineraryDetail(
                        LocalDate.of(2025, 8, 5),
                        "Day 1 of fun",
                        1,
                        placeRepo.findById(4L).get()),
                new ItineraryDetail(
                        LocalDate.of(2025, 8, 6),
                        "Day 2 of awesome-ness",
                        2,
                        placeRepo.findById(5L).get())
        ));

        i2.setTourist(tourist);
        i2.getItineraryDetails().forEach(itineraryDetail -> { itineraryDetail.setItinerary(i2); });
        itineraryRepo.save(i2);
    }

}
