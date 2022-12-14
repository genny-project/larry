package io.gada;

//import io.gada.message.UserData;
import io.gada.profile.CustomTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
//import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.Test;

//import static io.restassured.RestAssured.given;
//import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestProfile(CustomTestProfile.class)
public class UserResourceTest {

    @Test
    public void testGenerateData() {
        System.out.println("testGenerateData");
    }

}