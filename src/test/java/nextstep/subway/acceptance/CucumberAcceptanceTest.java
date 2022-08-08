package nextstep.subway.acceptance;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.StationSteps.지하철_역_목록_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    @Given("지하철 역 {} 생성을 요청한다")
    public void 지하철_역_생성을_요청한다(final String stationName) {
        RestAssured.port = port;

        지하철_역_생성_요청(stationName);
    }

    @Then("지하철 역 목록을 요청하면, {} 을 응답한다")
    public void 지하철_역_목록을_요청하면_응답한다(final String stationNames) {
        RestAssured.port = port;

        final List<String> expected = Arrays.stream(stationNames.split(", ")).collect(Collectors.toList());

        final ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        final List<String> actual = response.jsonPath().getList("name");
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).containsExactlyElementsOf(expected)
        );
    }
}
