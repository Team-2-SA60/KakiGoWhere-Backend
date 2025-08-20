package team2.kakigowherebackend.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import team2.kakigowherebackend.model.*;
import team2.kakigowherebackend.repository.*;
import team2.kakigowherebackend.service.ExportPlaceService;
import team2.kakigowherebackend.service.ExportRatingService;

@Slf4j
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final PlaceRepository placeRepo;
    private final TouristRepository touristRepo;
    private final InterestCategoryRepository interestCategoryRepo;
    private final AdminRepository adminRepo;
    private final ItineraryRepository itineraryRepo;
    private final ExportPlaceService exportPlaceService;
    private final ExportRatingService exportRatingService;
    private final DailyStatsRepository dailyStatsRepo;
    private final PlaceStatsRepository placeStatsRepo;
    private final PlaceEventRepository placeEventRepo;

    public DataInitializer(
            PlaceRepository placeRepo,
            TouristRepository touristRepo,
            InterestCategoryRepository interestCategoryRepo,
            AdminRepository adminRepo,
            ItineraryRepository itineraryRepo,
            ExportPlaceService exportPlaceService,
            ExportRatingService exportRatingService,
            DailyStatsRepository dailyStatsRepo,
            PlaceStatsRepository placeStatsRepo,
            PlaceEventRepository placeEventRepo) {
        this.placeRepo = placeRepo;
        this.touristRepo = touristRepo;
        this.interestCategoryRepo = interestCategoryRepo;
        this.adminRepo = adminRepo;
        this.itineraryRepo = itineraryRepo;
        this.exportPlaceService = exportPlaceService;
        this.exportRatingService = exportRatingService;
        this.dailyStatsRepo = dailyStatsRepo;
        this.placeStatsRepo = placeStatsRepo;
        this.placeEventRepo = placeEventRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        addPlaces();
        addPlaceEvents();
        addTourist();
        addCategories();
        addInterests();
        addAdmin();
        addItineraries();
        addDailyStats();
        addPlaceStats();
        exportCsvs();
    }

    private void addPlaces() {
        Place checkPlace = placeRepo.findByGoogleId("ChIJFQzeR6cZ2jERgM6--iWeY-U").orElse(null);
        if (checkPlace != null) return;

        log.info("Initializing places...");

        List<Place> places = new ArrayList<>();
        places.add(
                new Place(
                        "National Gallery Singapore",
                        "ChIJFQzeR6cZ2jERgM6--iWeY-U",
                        "Museum of Singapore & Southeast Asian arts housed in a restored municipal"
                                + " building dating to 1929.",
                        "https://www.nationalgallery.sg/"));
        places.add(
                new Place(
                        "Sultan Mosque",
                        "ChIJb6xq2bAZ2jERE1P6sVKtofw",
                        "Landmark Islamic house of worship in the Kampong Glam Malay Heritage"
                                + " District, with guided tours.",
                        "https://www.sultanmosque.sg/"));
        places.add(
                new Place(
                        "Sri Mariamman Temple",
                        "ChIJ0yjU0QwZ2jER6gg0ImuCTRU",
                        "Built in 1827, the city's oldest Hindu temple features a tower densely"
                                + " ornamented with deities.",
                        "http://www.smt.org.sg/"));
        places.add(
                new Place(
                        "Armenian Apostolic Church of St. Gregory the Illuminator",
                        "ChIJqc0-Q6EZ2jERbzBEE3qq6p0",
                        "Visitors can stroll through the tranquil memorial garden behind"
                                + " Singapore's oldest Christian church.",
                        "http://armeniansinasia.org/"));
        places.add(
                new Place(
                        "CHIJMES",
                        "ChIJNfw8b6QZ2jEREbGYft7-Q4A",
                        "A former convent & school, this 19th-century structure now houses"
                                + " restaurants, bars & event space.",
                        "http://www.chijmes.com.sg/"));
        places.add(
                new Place(
                        "St Andrew's Cathedral",
                        "ChIJ_xBP6qYZ2jERrO-IiImXiGw",
                        "This expansive cathedral offers complimentary guided tours of its"
                                + " sanctuary in early Gothic style.",
                        "http://cathedral.org.sg/"));
        places.add(
                new Place(
                        "Kreta Ayer Square",
                        "ChIJycZu3HIZ2jER5Miq4PYPupY",
                        "The heart of Chinatown, Kreta Ayer is home to an assortment of traditional"
                            + " trades, homegrown brands and some of the best hawker food around.",
                        "https://chinatown.sg/kreta-ayer/"));
        places.add(
                new Place(
                        "Albert Mall Walking Street",
                        "ChIJg766UwAZ2jERcygIqfA86eI",
                        "Albert Mall Walking Street is a captivating historical landmark located in"
                            + " the heart of Bras Basah, Singapore. This bustling pedestrian street"
                            + " is not only a shopping haven but also a vibrant reflection of the"
                            + " city's rich cultural tapestry. As you stroll along the path, you'll"
                            + " encounter an array of shops featuring local artisans, boutiques,"
                            + " and cafÃ©s that serve up delectable local dishes.",
                        "https://www.nlb.gov.sg/main/article-detail?cmsuuid=aff32241-880d-4b81-b3da-580a9999e8fa"));
        places.add(
                new Place(
                        "Chinatown Food Street",
                        "ChIJY4OYJXMZ2jERq9aW_MKjwAc",
                        "Marketplace-style covered center filled with street food vendors catering"
                                + " for the hungry crowds.",
                        "http://chinatownfoodstreet.sg/"));
        places.add(
                new Place(
                        "Chinatown Heritage Centre",
                        "ChIJ3eg6SXMZ2jERCxutt4adUyY",
                        "Restored shophouses displaying living spaces, furnishings & artifacts of"
                                + " early Chinatown settlers.",
                        "https://www.chinatownheritagecentre.com.sg/"));
        places.add(
                new Place(
                        "Thian Hock Keng Temple",
                        "ChIJ9e68CQ0Z2jER4G80iFVcJgo",
                        "Historic temple built in the classic Chinese style of architecture with"
                                + " columns & dragon sculptures.",
                        "https://www.thianhockkeng.com.sg/"));
        places.add(
                new Place(
                        "Eurasian Heritage Gallery",
                        "ChIJCxTorhMY2jERNG9vFuhEdQE",
                        "Learn about the history of the Eurasian community in Singapore across the"
                                + " three galleries of this quaint heritage gallery.",
                        "http://www.eurasians.sg/"));
        places.add(
                new Place(
                        "Capitol Building",
                        "ChIJc1XmpqYZ2jERPY-Bsk7YDQI",
                        "Capitol Building, formerly Shaws Building and Namazie Mansions, is a"
                            + " historic building at the junction of North Bridge Road and Stamford"
                            + " Road in the Downtown Core of Singapore.",
                        "http://eresources.nlb.gov.sg/infopedia/articles/SIP_2016-01-25_105628.html"));
        places.add(
                new Place(
                        "Esplanade - Theatres on the Bay, Singapore",
                        "ChIJSeUa7KcZ2jERNVg2CvmlVbk",
                        "Futuristic waterfront arts venue with 1,600-seat concert hall,"
                                + " 2,000-person theatre, studios & mall.",
                        "https://www.esplanade.com/"));
        places.add(
                new Place(
                        "Gardens by the Bay",
                        "ChIJMxZ-kwQZ2jERdsqftXeWCWI",
                        "A network of modern greenhouses & waterfront parks containing super trees"
                                + " lined with solar cells.",
                        "https://www.gardensbythebay.com.sg/"));
        places.add(
                new Place(
                        "The Interlace",
                        "ChIJi-aa3Mgb2jERGN15MeHVxtQ",
                        "The Interlace is a 1,040-unit apartment building complex located at the"
                            + " boundary between Bukit Merah and Queenstown, Singapore. Noteworthy"
                            + " for its break from the typical tower design in cities with high"
                            + " population densities, it resembles Jenga blocks or caskets"
                            + " irregularly stacked upon each other.",
                        "https://www.oma.com/projects/the-interlace"));
        places.add(
                new Place(
                        "PARKROYAL COLLECTION Pickering",
                        "ChIJo8cvPgsZ2jERzUp0v52SdI0",
                        "Luxe lodging with a trendy restaurant & a sleek spa, plus an infinity pool"
                                + " with city views.",
                        "https://www.panpacific.com/en/hotels-and-resorts/pr-collection-pickering.html?utm_source=google&utm_medium=business_listing&utm_campaign=googlemybusiness"));
        places.add(
                new Place(
                        "LASALLE College of the Arts",
                        "ChIJRbtK5bsZ2jERnDd0Le_s0A4",
                        "Asiaâs leading contemporary arts and design institution",
                        "http://www.lasalle.edu.sg/"));
        places.add(
                new Place(
                        "Kranji War Memorial",
                        "ChIJuwtSxDwS2jER8rdI5kRmbl0",
                        "British Commonwealth WWII memorial & military cemetery honoring thousands"
                                + " of Allied troops.",
                        "https://www.nlb.gov.sg/main/article-detail?cmsuuid=47f41a49-d2d0-4953-a113-698160242cda"));
        places.add(
                new Place(
                        "Indian National Army Historic Marker",
                        "ChIJdV9ag6cZ2jER9a31HQ2jN5w",
                        "The Former Indian National Army Monument is a historical site and a"
                            + " demolished war memorial at the Esplanade Park located at Connaught"
                            + " Drive within the downtown of Singapore.",
                        "https://www.roots.gov.sg/places/places-landing/Places/historic-sites/indian-national-army-memorial"));
        places.add(
                new Place(
                        "Reflections at Bukit Chandu",
                        "ChIJ97ZlvLkb2jERzq5JKKSBQEE",
                        "Restored colonial bungalow commemorating the Battle of Opium Hill through"
                                + " exhibits & artifacts.",
                        "http://bukitchandu.gov.sg/"));
        places.add(
                new Place(
                        "Singapore Cenotaph",
                        "ChIJdRyuoacZ2jERVtOObm6P2Cg",
                        "Towering memorial in a park unveiled in 1922 commemorating World War I &"
                                + " II heroes.",
                        "http://www.nas.gov.sg/archivesonline/photographs/record-details/d3e0af51-1161-11e3-83d5-0050568939ad"));
        places.add(
                new Place(
                        "Civilian War Memorial",
                        "ChIJw10lcqYZ2jERTvrSjFnHjts",
                        "This striking WWII memorial consists of 4 columns on a plinth surrounded"
                                + " by water.",
                        "https://www.nlb.gov.sg/main/article-detail?cmsuuid=5af9422f-99a6-4606-ad11-21e991ecfab2"));
        places.add(
                new Place(
                        "Sri Veeramakaliamman Temple",
                        "ChIJ8zLPY8cZ2jEROE-GeGiS7GQ",
                        "This landmark Hindu temple displays elaborate architecture & colorful"
                                + " statues of deities.",
                        "http://www.srivkt.org/"));
        places.add(
                new Place(
                        "Maghain Aboth Synagogue",
                        "ChIJc3Wuq6QZ2jERYH1Op8S-DL0",
                        "The Maghain Aboth Synagogue is an Hasidic Jewish congregation and"
                            + " synagogue, located at 24/26 Waterloo Street in Rochor, within the"
                            + " Central Area of Singapore. Constructed in 1878, it is the oldest"
                            + " and largest Jewish synagogue in Southeast Asia and the second"
                            + " largest in Asia itself, outside of Israel.",
                        "https://singaporejews.com/maghain-aboth-synagogues/"));
        places.add(
                new Place(
                        "Former Supreme Court",
                        "ChIJfSAMRacZ2jER_qPZbi1mx6w",
                        "The Former Supreme Court Building is the former courthouse of the Supreme"
                                + " Court of Singapore.",
                        "https://www.nationalgallery.sg/about/building/architecture"));
        places.add(
                new Place(
                        "Tiong Bahru View",
                        "ChIJrc5itnkZ2jERHOx697G2ylI",
                        "Tiong Bahru is a housing estate of low-rise apartment blocks that's become"
                            + " one of Singapore's hippest neighborhoods. It's home to a cluster of"
                            + " quirky cafes, chic bars and hip restaurants, as well as indie"
                            + " stores selling books, records and chic clothes. Tiong Bahru Plaza"
                            + " has shops selling edgy fashion, while the Tiong Bahru Market is"
                            + " known for street food such as wonton noodles and char siew rice.",
                        "https://www.edgeprop.sg/hdb/tiong-bahru-view"));
        places.add(
                new Place(
                        "Lau Pa Sat",
                        "ChIJ5Y6l4Q0Z2jERYL0KDIjT6v0",
                        "Rebuilt Victorian covered hawker centre in CBD with local delicacies &"
                                + " international food stalls.",
                        "https://laupasat.sg/"));
        places.add(
                new Place(
                        "Istana Kampong Glam",
                        "ChIJ2TH1froZ2jERnsehsXN2VJc",
                        "Istana Kampong Gelam, is a former Malay palace in Singapore. It is located"
                            + " near Masjid Sultan in Kampong Glam. The palace and compounds were"
                            + " refurbished into the Malay Heritage Centre in 2004.",
                        "https://www.nlb.gov.sg/main/article-detail?cmsuuid=36750cb4-e1df-45ed-90e4-db07e0e96365"));
        places.add(
                new Place(
                        "Peranakan Museum",
                        "ChIJWZX956MZ2jERIGdnbs_SgMw",
                        "Peranakan culture is explored through interactive & multimedia exhibits in"
                                + " a historic 1912 building.",
                        "http://www.peranakanmuseum.org.sg/"));
        places.add(
                new Place(
                        "Raffles Singapore",
                        "ChIJwUTKu6UZ2jERwt5ctkU4f2Q",
                        "Opulent hotel featuring elegant suites, international restaurants & 3"
                                + " polished bars.",
                        "https://www.raffles.com/singapore/?goto=fiche_hotel&code_hotel=A5E1&merchantid=seo-maps-SG-A5E1&sourceid=aw-cen&utm_medium=seo%20maps&utm_source=google%20Maps&utm_campaign=seo%20maps"));
        places.add(
                new Place(
                        "Gillman Barracks",
                        "ChIJd-ET2cUb2jER9nfWEH4mCX0",
                        "Vibrant creative compound offering art galleries, eateries & a kids' play"
                                + " space.",
                        "https://www.sla.gov.sg/visit-sla-properties/gillman-barracks-a-vibrant-lifestyle-enclave"));
        places.add(
                new Place(
                        "ArtScience Museum",
                        "ChIJnWdQKQQZ2jERScXuKeFHyIE",
                        "Design, science & technology exhibits in a distinctive, modern,"
                                + " flower-shaped building.",
                        "https://www.marinabaysands.com/museum.html"));
        places.add(
                new Place(
                        "SAM at 8Q",
                        "ChIJB0fevaQZ2jERCSQzB8iAY70",
                        "Colorful annex location of the Singapore Art Museum hosting diverse"
                                + " contemporary exhibitions.",
                        "https://www.singaporeartmuseum.sg/visit"));
        places.add(
                new Place(
                        "Masjid Jamae (Chulia)",
                        "ChIJVQ7VtwwZ2jERJiI42tiD34w",
                        "19th-century mosque with eclectic architecture, including Indo-Islamic &"
                                + " South Indian features.",
                        "http://www.masjidjamaechulia.sg/"));
        places.add(
                new Place(
                        "Institute of Contemporary Arts Singapore",
                        "ChIJp5zm5LsZ2jERHpGcpdFqjcI",
                        "The Institute of Contemporary Arts Singapore (ICAS) is the curatorial"
                            + " division of LASALLE College of the Arts. Situated at LASALLEâs"
                            + " dynamic city campus, ICASâs five galleries present a programme of"
                            + " curated international exhibitions alongside exhibitions organised"
                            + " with students, academic staff and alumni.",
                        "http://www.lasalle.edu.sg/institute-of-contemporary-arts-sg"));
        places.add(
                new Place(
                        "Singapore Flyer",
                        "ChIJzVHFNqkZ2jERboLN2YrltH8",
                        "Huge observation wheel opened in 2008 for views of the city plus high tea"
                                + " & butler-serviced flights.",
                        "https://www.singaporeflyer.com/"));
        places.add(
                new Place(
                        "Marina Bay Sands Singapore",
                        "ChIJA5LATO4Z2jER111V-v6abAI",
                        "Luxe hotel offering multiple restaurants & bars, plus bay views, a spa & a"
                                + " rooftop pool.",
                        "https://www.marinabaysands.com/?utm_source=google&utm_medium=GMB&utm_campaign=hotel_info_main"));
        places.add(
                new Place(
                        "Marina Barrage",
                        "ChIJ50uIMa0Z2jER0cTt5fLaZt0",
                        "Reservoir & kayaking destination featuring a gallery with interactive"
                                + " exhibits on sustainability.",
                        "https://www.pub.gov.sg/marinabarrage/aboutmarinabarrage"));
        places.add(
                new Place(
                        "Henderson Waves",
                        "ChIJeXrAvBcb2jERhWAujA0K0LE",
                        "This charming, wood-panelled bridge is the tallest pedestrian bridge in"
                            + " Singapore. It was built to improve connectivity between the nature"
                            + " areas along the Southern Ridges. A series of seven undulating"
                            + " âribsâ double up as alcoves and provide shelter to park users"
                            + " looking for rest.",
                        "https://www.nparks.gov.sg/visit/parks/telok-blangah-hill-park/special-features/henderson-waves"));
        places.add(
                new Place(
                        "Pinnacleï¼ Duxton",
                        "ChIJVSyoe2wZ2jERcL3VxGbPvHs",
                        "The Pinnacle@Duxton is a 50-storey residential development in Singapore's"
                                + " city center, next to the business district.",
                        "http://www.pinnacleduxton.com.sg/"));
        places.add(
                new Place(
                        "Masjid Hajjah Fatimah",
                        "ChIJI15m97MZ2jERI3pwY4tc-1o",
                        "The Hajjah Fatimah Mosque is a mosque located along Beach Road in the"
                            + " Kampong Glam district within the Kallang Planning Area in"
                            + " Singapore. The mosque was designed in a mix of Islamic and European"
                            + " architectural styles, and completed in 1846.",
                        "http://www.masjidhajjahfatimah.sg/"));
        places.add(
                new Place(
                        "Haw Par Villa",
                        "ChIJ9ak4I6wb2jERO6x4oAUh_-g",
                        "Unique park using giant statues & dioramas to retell historic Chinese"
                                + " legends & religious mythology.",
                        "https://www.hawparvilla.sg/"));
        places.add(
                new Place(
                        "NUS Baba House",
                        "ChIJvwiR9W4Z2jER3TtTDTsmA7I",
                        "Traditional 19th-century home featuring classic Peranakan architecture,"
                                + " antique furniture & art.",
                        "https://babahouse.nus.edu.sg/"));
        places.add(
                new Place(
                        "Hong San See Temple",
                        "ChIJ1dGx2p4Z2jERX7D0mj1D0Hk",
                        "Hong San See is a Chinese temple in Singapore, and is located at Mohamed"
                            + " Sultan Road in the River Valley Planning Area, within the Central"
                            + " Area. Hong San See Temple was constructed between 1908 and 1913,"
                            + " erected by migrants from Nan An County in Fujian province with"
                            + " materials imported from China.",
                        "https://www.roots.sg/Content/Places/national-monuments/hong-san-see"));
        places.add(
                new Place(
                        "Chinese Heritage Centre",
                        "ChIJwxpUtZ8P2jERPbeGV3upuaY",
                        "The Chinese Heritage Centre is an institute for the study of overseas"
                            + " Chinese under the Nanyang Technological University in Singapore. As"
                            + " the first and only university institute for the study of overseas"
                            + " Chinese, it serves as a research centre, a library and a museum.",
                        "https://www.ntu.edu.sg/chc"));
        places.add(
                new Place(
                        "Kampong Glam",
                        "ChIJ18kcLbEZ2jERHV-sR1RGyZc",
                        "Kampong Gelam is a neighbourhood of bold new boutiques and esteemed"
                            + " artisans, trendy cafes and traditional eateries. Street Vibes and"
                            + " Live Music.",
                        "https://www.visitsingapore.com/neighbourhood/featured-neighbourhood/kampong-gelam/"));
        places.add(
                new Place(
                        "NTU Centre for Contemporary Art Singapore",
                        "ChIJmc-Y_sMb2jERz1q_-aHQgRk",
                        "The NTU Centre for Contemporary Art Singapore is a research centre under"
                                + " Nanyang Technological University.",
                        "http://ntu.ccasingapore.org/"));
        places.add(
                new Place(
                        "Red Dot Design Museum",
                        "ChIJq1KHxhIZ2jER7fOxY9pR-vo",
                        "International product & communication design museum named after the major"
                                + " prize.",
                        "http://museum.red-dot.sg/"));
        places.add(
                new Place(
                        "Singapore Art Museum at Tanjong Pagar Distripark",
                        "ChIJPS0vSwsZ2jERulqTcd4CFDA",
                        "Singapore Art Museum opened in 1996 as the first art museum in"
                            + " Singapore.â¯Also known as SAM, we present contemporary artâ¯from"
                            + " a Southeast Asian perspective for artists, art lovers and the art"
                            + " curious in multiple venues across the island, including a new venue"
                            + " in the historic port area of Tanjong Pagar.",
                        "http://www.singaporeartmuseum.sg/"));
        places.add(
                new Place(
                        "National Design Centre",
                        "ChIJX3kq1roZ2jERA1959fq81us",
                        "The National Design Centre is the nexus for design. Designers, businesses,"
                                + " students and",
                        "https://www.designsingapore.org/national-design-centre"));
        places.add(
                new Place(
                        "Victoria Concert Hall",
                        "ChIJzf487wkZ2jERP3nJhCFBdrE",
                        "Home to the Singapore Symphony Orchestra, the refurbished Victorian"
                                + " building features a clock tower.",
                        "https://artshouselimited.sg/vtvch"));
        places.add(
                new Place(
                        "NUS Museum",
                        "ChIJcwAAUPoa2jERiv_0ph5iKlg",
                        "University museum with 8,000-plus exhibits from classical & contemporary"
                                + " Asian art to sculpture.",
                        "https://museum.nus.edu.sg/"));
        places.add(
                new Place(
                        "Sun Yat Sen Nanyang Memorial Hall",
                        "ChIJY4U5TGAX2jERQYsGly-qKIo",
                        "Victorian-era villa & former revolutionary headquarters with a museum"
                                + " dedicated to Dr. Sun Yat Sen.",
                        "https://www.sysnmh.org.sg/"));
        places.add(
                new Place(
                        "Asian Civilisations Museum",
                        "ChIJoZOhmQkZ2jERehLfvKlsoCA",
                        "Museum exploring the history & cultures of Asia & their shaping of"
                                + " contemporary Singapore.",
                        "https://www.nhb.gov.sg/acm/"));
        places.add(
                new Place(
                        "National Museum of Singapore",
                        "ChIJD1u-EaMZ2jERaLhNfFkR45I",
                        "Classical & modern architecture converge in this large, vibrant museum of"
                                + " Singaporean artifacts.",
                        "https://www.nhb.gov.sg/nationalmuseum/"));
        places.add(
                new Place(
                        "Japanese Cemetery Park",
                        "ChIJvb6oO1MW2jER05RT5zRS-o8",
                        "Cemetery founded in 1891, now a memorial park, containing graves of"
                                + " Japanese expats & WWII soldiers.",
                        "https://www.jas.org.sg/pages/shisetsu-facility"));
        places.add(
                new Place(
                        "Dalhousie Obelisk",
                        "ChIJRbqIowkZ2jERitZMRQRGCH0",
                        "The Dalhousie Obelisk is a memorial obelisk in the Civic District of"
                            + " Singapore, located on the north bank of the Singapore River in the"
                            + " Downtown Core, within the Central Area in Singapore's central"
                            + " business district. The memorial is erected to commemorate the visit"
                            + " of James Broun-Ramsay, 1st Marquis of Dalhousie.",
                        "https://www.nlb.gov.sg/main/article-detail?cmsuuid=e92fae65-a482-4dc9-97de-02f4b2aeaff5"));
        places.add(
                new Place(
                        "Statue of Sir Stamford Raffles",
                        "ChIJN21BlAkZ2jERGmrNMLOQQEI",
                        "Marble statue of Singapore's founder on the spot where Raffles is believed"
                                + " to have landed in 1819.",
                        "http://www.nlb.gov.sg/"));
        places.add(
                new Place(
                        "Children's Museum Singapore",
                        "ChIJixzGhuMZ2jERpijfVdsTdFw",
                        "The Children's Museum Singapore, formerly Singapore Philatelic Museum, was"
                            + " a museum about children's history and formerly the postal history"
                            + " of Singapore and its stamps.",
                        "https://www.nhb.gov.sg/childrensmuseum"));
        places.add(
                new Place(
                        "Former Ford Factory",
                        "ChIJTcyXK1oQ2jERMjEftNlLGk0",
                        "World War II museum in a former car factory, the 1942 site of the British"
                                + " surrender to the Japanese.",
                        "https://corporate.nas.gov.sg/former-ford-factory/overview/"));
        places.add(
                new Place(
                        "Singapore City Gallery",
                        "ChIJRaIcWw0Z2jERts3whuuZPsM",
                        "Museum & gallery focusing on city development & planning, with large"
                                + " models & interactive exhibits.",
                        "https://www.ura.gov.sg/Corporate/Singapore-City-Gallery"));
        places.add(
                new Place(
                        "Changi Chapel & Museum",
                        "ChIJCQHB6vc82jERq6iFty4Fzo4",
                        "Exhibits, displays & art documenting the Japanese occupation during WWII,"
                                + " plus a cafe & bookshop.",
                        "http://www.nhb.gov.sg/changichapelmuseum"));
        places.add(
                new Place(
                        "MINT Museum of Toys",
                        "ChIJW7B7zRIZ2jERINTI_uV1O-A",
                        "Unique musuem with a vast collection of vintage toys & childhood"
                                + " memorabilia from around the world.",
                        "https://emint.com/"));
        places.add(
                new Place(
                        "Lim Bo Seng Memorial",
                        "ChIJq6ru0QkZ2jERI5XgbHAqbnA",
                        "The Lim Bo Seng Memorial is a war memorial in Esplanade Park, Singapore."
                            + " It was erected in 1954 in honour of resistance fighter Lim Bo Seng"
                            + " for his acts during World War II.",
                        "https://www.visitsingapore.com/see-do-singapore/history/memorials/lim-bo-seng-memorial/"));
        places.add(
                new Place(
                        "Universal Studios Singapore",
                        "ChIJQ6MVplUZ2jERn1LmNH0DlDA",
                        "Movie amusement park with sets & rides on themes from Hollywood to sci-fi,"
                                + " plus live entertainment.",
                        "https://www.rwsentosa.com/en/play/universal-studios-singapore?utm_source=google&utm_medium=gmb"));
        places.add(
                new Place(
                        "Science Centre Singapore",
                        "ChIJY618FAQQ2jERzo1f5IAj4Bg",
                        "Interactive science museum offering kids indoor & outdoor exhibits on"
                                + " Earth, biology & space.",
                        "http://www.science.edu.sg/"));
        places.add(
                new Place(
                        "Fuk Tak Chi Museum",
                        "ChIJSQv4eg0Z2jERc2v3-CNAdPs",
                        "Snug museum, housed in the oldest Chinese temple in Singapore, with"
                                + " exhibits on early migrants.",
                        "http://fareastmalls.com.sg/Far-East-Square/index.aspx?key=fuktakchimuseum"));
        places.add(
                new Place(
                        "Katong Antique House",
                        "ChIJtThnSnMY2jER1o1poSw5EtE",
                        "The private museum is a trove of Peranakan antiques ranging from furniture"
                                + " to intricately crafted Nyonya costumes like sarong kebaya and"
                                + " kasut-manek.",
                        "https://www.tripadvisor.com.sg/Attraction_Review-g294265-d317437-Reviews-Katong_Antique_House-Singapore.html"));
        places.add(
                new Place(
                        "Chinese Garden",
                        "ChIJuZ4kdB0Q2jERdp1SrojDF_I",
                        "Expansive, landscaped gardens with walking paths, water features &"
                                + " numerous pagodas & bridges.",
                        "https://www.nparks.gov.sg/contact-us"));
        places.add(
                new Place(
                        "East Coast Park",
                        "ChIJ0QX_Brki2jER-pZKNdqk_a8",
                        "White, sandy beach with a skate park, water sports & sports facilities,"
                                + " plus local food sellers.",
                        "https://www.nparks.gov.sg/gardens-parks-and-nature/parks-and-nature-reserves/east-coast-park"));
        places.add(
                new Place(
                        "Fort Canning Park",
                        "ChIJVSYjJKIZ2jERpRFinATD52s",
                        "A well-known historical landmark, this grassy hilltop park hosts events"
                                + " such as concerts & plays.",
                        "https://www.nparks.gov.sg/gardens-parks-and-nature/parks-and-nature-reserves/fort-canning-park"));
        places.add(
                new Place(
                        "Kusu Island",
                        "ChIJSbEk2-Ye2jERy8TeRShVgQo",
                        "Tiny Singapore Straits islet with hilltop Malay shrines, Chinese temple,"
                                + " turtle sanctuary & beaches.",
                        "https://islandcruise.com.sg/southern-islands/kusu-island/"));
        places.add(
                new Place(
                        "Bird Paradise",
                        "ChIJkyCpkWcT2jER1Nsl0ZvOI4Q",
                        "Bird Paradise is an aviary located in Mandai, Singapore.",
                        "https://www.mandai.com/en/bird-paradise.html"));
        places.add(
                new Place(
                        "HortPark",
                        "ChIJC_hOgcgb2jERfF9HNB10xfI",
                        "Park with themed landscape gardens, gardening workshops, a shop & an"
                                + " elegant European eatery.",
                        "https://www.nparks.gov.sg/contact-us"));
        places.add(
                new Place(
                        "Adventure Cove Waterpark",
                        "ChIJS0Dhxf0b2jERAuG9YaPrYjA",
                        "Aquatic amusement park with an aquarium, waterslides & a wave pool, plus"
                                + " tubing & snorkeling.",
                        "https://www.rwsentosa.com/en/play/adventure-cove-waterpark?utm_source=google&utm_medium=serp&utm_placement=website"));
        places.add(
                new Place(
                        "Singapore River",
                        "ChIJb0N2WoIZ2jERUAQo0rer1jY",
                        "The Singapore River is a 3.2-kilometer long river flowing through the"
                                + " heart of Singapore, from Kim Seng Road to Marina Bay.",
                        "https://www.visitsingapore.com/neighbourhood/featured-neighbourhood/singapore-river/"));
        places.add(
                new Place(
                        "Peranakan Houses",
                        "ChIJQ83MYhIY2jERwAOUsQ_RdLA",
                        "Home to Peranakan culture in Singapore, Katong-Joo Chiat is filled with"
                            + " local flavours, boutique cafes, and charming heritage shophouses.",
                        "https://www.visitsingapore.com/neighbourhood/featured-neighbourhood/katong-joo-chiat/"));
        places.add(
                new Place(
                        "Sentosa",
                        "ChIJRYMSeKwe2jERAR2QXVU39vg",
                        "Vibrant island with sandy beaches, hotels & a casino, plus golf courses, a"
                                + " theme park & aquarium.",
                        "https://www.sentosa.com.sg/"));
        places.add(
                new Place(
                        "Chinatown",
                        "ChIJ42h1onIZ2jERBbs-VGqmwrs",
                        "Chinatown in Singapore is a vibrant, culturally rich area located in the"
                                + " Central Area's Outram district. It's known for its preserved"
                                + " shophouses, historic temples, bustling markets, and diverse"
                                + " culinary scene.",
                        "https://chinatown.sg/"));
        places.add(
                new Place(
                        "Buddha Tooth Relic Temple",
                        "ChIJ0bwmznIZ2jEREOCMNggtIBk",
                        "Tang dynastyâstyle temple housing religious relics, with ornate rooms &"
                                + " a tranquil rooftop garden.",
                        "https://www.buddhatoothrelictemple.org.sg/"));
        places.add(
                new Place(
                        "Labrador Nature Reserve",
                        "ChIJoy3rj-sb2jERA_wMVazFAiI",
                        "This nature reserve, located at the edge of a secondary forest and by the"
                            + " sea is a thriving habitat for plants and animals. Labrador Nature"
                            + " Park, located adjacent to and buffers the nature reserve, provides"
                            + " recreational spaces for the public.",
                        "https://www.nparks.gov.sg/visit/parks/park-detail/labrador-nature-reserve/"));
        places.add(
                new Place(
                        "Chek Jawa Wetlands",
                        "ChIJUxOzlaQ-2jERRAkZtSCzcXI",
                        "Nature preserve on 100 hectares with a visitor centre & jetty, plus a"
                                + " boardwalk & observation tower.",
                        "https://www.nparks.gov.sg/pulau-ubin/biodiversity/places-of-interest/chek-jawa-wetlands"));
        places.add(
                new Place(
                        "Merlion Park",
                        "ChIJBTYg1g4Z2jERp_MBbu5erWY",
                        "The 70-ton merlion statue is one of the city's most iconic sights & is"
                                + " located in this park.",
                        "http://bit.ly/river_cruise_singapore"));
        places.add(
                new Place(
                        "SkyPark Observation Deck",
                        "ChIJOeEf9S2vewIRM0B9a06CKwg",
                        "Hotel's observation deck on the 56th floor offering panoramic city views &"
                                + " 2 upscale restaurants.",
                        "https://www.marinabaysands.com/attractions/sands-skypark.html/?utm_source=google&utm_medium=GMB&utm_campaign=skypark_info_main"));
        places.add(
                new Place(
                        "Resorts World Sentosa",
                        "ChIJLR75v_0b2jERJrR28stYwMU",
                        "Premium resort with themed hotels, plus a theme park, an aquarium, a"
                                + " convention center & a casino.",
                        "http://www.rwsentosa.com/"));
        places.add(
                new Place(
                        "Singapore Botanic Gardens",
                        "ChIJvWDbfRwa2jERgNnTOpAU3-o",
                        "82-hectare botanical garden with sculptures, a swan lake & significant"
                                + " collection of tropical trees.",
                        "http://www.sbg.org.sg/"));
        places.add(
                new Place(
                        "Sungei Buloh Wetland Reserve",
                        "ChIJe0cEPoIS2jERs_rS2qvGOxw",
                        "Nature park & wetland area reserve known for migratory birds, with a"
                                + " visitor center.",
                        "https://www.nparks.gov.sg/sbwr"));
        places.add(
                new Place(
                        "Singapore Oceanarium",
                        "ChIJj-3Cq_0b2jERJv2MBkSVsPQ",
                        "Large aquarium & resort featuring 800 species of marine life in a variety"
                                + " of habitats.",
                        "https://www.singaporeoceanarium.com/en.html?utm_medium=referral&utm_source=google&utm_campaign=sgo_info_main&utm_term=link&utm_content=google_my_business"));
        places.add(
                new Place(
                        "Singapore Zoo",
                        "ChIJr9wqENkT2jERkRs7pMj6FLQ",
                        "Rainforest zoo with tram rides, trails & viewing platforms to see wildlife"
                                + " habitats & exhibits.",
                        "https://www.mandai.com/en/singapore-zoo.html"));
        places.add(
                new Place(
                        "River Wonders",
                        "ChIJxZfX_9gT2jERknwK8es7IHU",
                        "Zoo & aquarium with a river theme offering boat rides, freshwater fish &"
                                + " animals, including pandas.",
                        "https://www.mandai.com/en/river-wonders.html"));
        places.add(
                new Place(
                        "Singapore Cable Car",
                        "ChIJAQAAAK0e2jERMFQeIT5IR-E",
                        "Sky rides offering views of the city & harbour, unlimited ride tickets &"
                                + " an in-car dining option.",
                        "https://www.mountfaberleisure.com/attraction/singapore-cable-car/"));
        places.add(
                new Place(
                        "Night Safari",
                        "ChIJ9xUuiNcT2jER49FS2OpE8W8",
                        "Night zoo with tram rides and walking trails through rainforest past"
                                + " elephants, tigers & leopards.",
                        "https://www.mandai.com/en/night-safari.html"));
        places.add(
                new Place(
                        "Pulau Ubin",
                        "ChIJYcYVP2I-2jERQTiBy40oT6w",
                        "Verdant island with walking & cycling paths amid villages, old granite"
                                + " quarries & diverse bird life.",
                        "https://www.nparks.gov.sg/pulau-ubin"));
        places.add(
                new Place(
                        "Indian Heritage Centre",
                        "ChIJlShO3LgZ2jER4Xlf4Yo-jbs",
                        "Galleries & exhibits highlighting the distinctive Indian cultural heritage"
                                + " of Singapore.",
                        "http://www.indianheritage.org.sg/"));
        places.add(
                new Place(
                        "Orchard Road",
                        "ChIJu_7mSJEZ2jER-vT-Nz_3mY4",
                        "This well-known, 2.2-km route features shopping malls & boutiques, plus"
                                + " restaurants & luxe hotels.",
                        "https://www.visitsingapore.com/neighbourhood/featured-neighbourhood/orchard-road/"));
        places.add(
                new Place(
                        "MacRitchie Reservoir",
                        "ChIJVVRNg0oX2jERxH0FUCJhoz4",
                        "Scenic artificial lake with boating, perimeter trails & a public park with"
                                + " a bandstand.",
                        "https://www.nparks.gov.sg/visit/parks/central-catchment-nature-reserve/activities/hiking-nature-walk"));
        places.add(
                new Place(
                        "Civic District",
                        "ChIJw4huyqAZ2jERXIhnlRWOfhI",
                        "Skyscrapers and colonial buildings sit side by side in the Civic District,"
                                + " home to some of the island's finest museums and art galleries.",
                        "https://www.visitsingapore.com/neighbourhood/featured-neighbourhood/civic-district/"));
        places.add(
                new Place(
                        "HarbourFront",
                        "ChIJpV0-3uEb2jERl85WTf6I9n0",
                        "At the heart of family-friendly HarbourFront is the massive VivoCity mall,"
                                + " which has a scenic rooftop playground and monorail access to"
                                + " recreation on Sentosa Island.",
                        "https://www.harbourfrontcentre.com.sg/"));
        places.add(
                new Place(
                        "Bras Basah Complex",
                        "ChIJtxaD8KQZ2jERgCmvEt9PdiU",
                        "This shopping center built in the 1980s offers 5 floors of shops selling"
                                + " mostly books & art.",
                        "https://www.brasbasahcomplex.com/"));
        placeRepo.saveAll(places);

        log.info("Initialized places");
    }

    private void addPlaceEvents() {
        if (placeEventRepo.count() > 0) return; // skip seeding if alr present
        log.info("Initializing place events...");

        for (long pid = 1; pid <= 20; pid++) {
            placeRepo
                    .findById(pid)
                    .ifPresent(
                            place -> {
                                LocalDate start = LocalDate.now();
                                LocalDate end = start.plusDays(2);

                                PlaceEvent event = new PlaceEvent();
                                event.setName("Sample Event for " + place.getName());
                                event.setDescription(
                                        "Sample Event for "
                                                + place.getName()
                                                + ". It exists to test whether the programme can"
                                                + " run.");
                                event.setStartDate(start);
                                event.setEndDate(end);
                                event.setPlace(place);

                                placeEventRepo.save(event);
                            });
        }

        placeRepo
                .findById(21L)
                .ifPresent(
                        place -> {
                            LocalDate end = LocalDate.now().minusDays(2);
                            LocalDate start = end.minusDays(30);
                            PlaceEvent expiredEvent = new PlaceEvent();
                            expiredEvent.setName("Expired Event for " + place.getName());
                            expiredEvent.setDescription(
                                    "Sample Event for "
                                            + place.getName()
                                            + ". It should not show up in the Android Studio"
                                            + " emulator since it has expired.");
                            expiredEvent.setStartDate(start);
                            expiredEvent.setEndDate(end);
                            expiredEvent.setPlace(place);

                            placeEventRepo.save(expiredEvent);
                        });
    }

    private void addTourist() {
        Tourist checkTourist = touristRepo.findByEmail("a@kaki.com").orElse(null);
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

    private void addCategories() {
        InterestCategory interestCategory =
                interestCategoryRepo.findByName("food_and_beverage").orElse(null);
        if (interestCategory != null) return;

        log.info("Initializing categories...");
        interestCategoryRepo.save(new InterestCategory("food_and_beverage", "Food and Beverage"));
        interestCategoryRepo.save(new InterestCategory("gardens_and_nature", "Gardens and Nature"));
        interestCategoryRepo.save(new InterestCategory("museums", "Museums"));
        interestCategoryRepo.save(new InterestCategory("culture", "Culture"));
        interestCategoryRepo.save(new InterestCategory("entertainment", "Entertainment"));
        interestCategoryRepo.save(new InterestCategory("theme_parks", "Theme Parks"));
        interestCategoryRepo.save(new InterestCategory("wildlife_and_zoos", "Wildlife and Zoos"));
        interestCategoryRepo.save(new InterestCategory("shopping", "Shopping"));
        interestCategoryRepo.save(new InterestCategory("heritage_sites", "Heritage Sites"));
        log.info("Initialized categories");
    }

    private void addInterests() {
        InterestCategory interestCategory =
                interestCategoryRepo.findByName("food_and_beverage").orElse(null);
        if (interestCategory == null) return;

        // Get some categories
        InterestCategory museum = interestCategoryRepo.findByName("museums").orElse(null);
        InterestCategory food = interestCategoryRepo.findByName("food_and_beverage").orElse(null);
        InterestCategory zoo = interestCategoryRepo.findByName("wildlife_and_zoos").orElse(null);
        InterestCategory culture = interestCategoryRepo.findByName("culture").orElse(null);
        InterestCategory gardens =
                interestCategoryRepo.findByName("gardens_and_nature").orElse(null);

        Tourist adrian = touristRepo.findByEmail("a@kaki.com").orElse(null);
        if (adrian != null) {
            if (!adrian.getInterestCategories().isEmpty()) return;
            log.info("Initializing interests...");
            List<InterestCategory> ic = new ArrayList<>();
            ic.add(museum);
            ic.add(food);
            adrian.setInterestCategories(ic);
            touristRepo.save(adrian);
        }

        Tourist cy = touristRepo.findByEmail("cy@kaki.com").orElse(null);
        if (cy != null) {
            List<InterestCategory> ic = new ArrayList<>();
            ic.add(food);
            cy.setInterestCategories(ic);
            touristRepo.save(cy);
        }

        Tourist gy = touristRepo.findByEmail("gy@kaki.com").orElse(null);
        if (gy != null) {
            List<InterestCategory> ic = new ArrayList<>();
            ic.add(food);
            ic.add(culture);
            gy.setInterestCategories(ic);
            touristRepo.save(gy);
        }

        Tourist ks = touristRepo.findByEmail("ks@kaki.com").orElse(null);
        if (ks != null) {
            List<InterestCategory> ic = new ArrayList<>();
            ic.add(gardens);
            ic.add(zoo);
            ks.setInterestCategories(ic);
            touristRepo.save(ks);
        }

        Tourist bf = touristRepo.findByEmail("bf@kaki.com").orElse(null);
        if (bf != null) {
            List<InterestCategory> ic = new ArrayList<>();
            ic.add(museum);
            bf.setInterestCategories(ic);
            touristRepo.save(bf);
        }

        Tourist rx = touristRepo.findByEmail("rx@kaki.com").orElse(null);
        if (rx != null) {
            List<InterestCategory> ic = new ArrayList<>();
            ic.add(zoo);
            rx.setInterestCategories(ic);
            touristRepo.save(rx);
        }

        log.info("Initialized interests");
    }

    private void addAdmin() {
        String email = "admin@kaki.com";
        Admin checkAdmin = adminRepo.findByEmail(email).orElse(null);
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

        Optional<Tourist> tourist = touristRepo.findByEmail(email);
        if (tourist.isEmpty()) return;

        Itinerary i1 = new Itinerary("My awesome itinerary", LocalDate.of(2025, 8, 1));
        i1.setItineraryDetails(
                List.of(
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
                                placeRepo.findById(3L).get())));

        i1.setTourist(tourist.get());
        i1.getItineraryDetails()
                .forEach(
                        itineraryDetail -> {
                            itineraryDetail.setItinerary(i1);
                        });
        itineraryRepo.save(i1);

        Itinerary i2 = new Itinerary("My fun itinerary", LocalDate.of(2025, 8, 5));
        i2.setItineraryDetails(
                List.of(
                        new ItineraryDetail(
                                LocalDate.of(2025, 8, 5),
                                "Day 1 of fun",
                                1,
                                placeRepo.findById(4L).get()),
                        new ItineraryDetail(
                                LocalDate.of(2025, 8, 6),
                                "Day 2 of awesome-ness",
                                2,
                                placeRepo.findById(5L).get())));

        i2.setTourist(tourist.get());
        i2.getItineraryDetails()
                .forEach(
                        itineraryDetail -> {
                            itineraryDetail.setItinerary(i2);
                        });
        itineraryRepo.save(i2);
    }

    private void addDailyStats() {
        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 8, 10);

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            // skip if already present
            if (dailyStatsRepo.findByDate(d).isPresent()) continue;

            DailyStats ds = new DailyStats();
            ds.setDate(d);
            // add deterministic values
            ds.setNumberOfUniqueVisits(100 + d.getDayOfMonth());
            ds.setNumberOfSignUps(10 + (d.getDayOfMonth() % 5));

            dailyStatsRepo.save(ds);
        }
    }

    private void addPlaceStats() {
        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 8, 10);

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            DailyStats daily = dailyStatsRepo.findByDate(d).orElse(null);
            if (daily == null) continue;

            for (long placeId = 1L; placeId <= 100L; placeId++) {
                Place place = placeRepo.findById(placeId).orElse(null);
                if (place == null) continue;

                if (placeStatsRepo.findByDailyStatsAndPlace(daily, place).isPresent()) continue;

                PlaceStats ps = new PlaceStats();
                ps.setDailyStats(daily);
                ps.setPlace(place);

                // random 100..500 inclusive
                int visits = ThreadLocalRandom.current().nextInt(100, 501);
                ps.setNumberOfPageVisits(visits);

                placeStatsRepo.save(ps);
            }
        }
    }

    private void exportCsvs() {
        log.info("Exporting Places CSV file for ML...");
        exportPlaceService.exportPlaces();
        log.info("Exporting Ratings CSV file for ML...");
        exportRatingService.exportRatings();
        log.info("Exported CSV files.");
    }
}
