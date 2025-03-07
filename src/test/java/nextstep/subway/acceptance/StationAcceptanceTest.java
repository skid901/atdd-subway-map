package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 역 생성을 요청 하면
     * Then 지하철 역 생성이 성공 한다.
     */
    @DisplayName("지하철 역 생성")
    @Test
    void createStation() {
        // when
        final ExtractableResponse<Response> response = 지하철_역_생성_요청("강남역");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * When 같은 이름 으로 지하철 역 생성을 요청 하면
     * Then 지하철 역 생성이 실패 한다.
     */
    @DisplayName("중복 이름 으로 지하철 역 생성")
    @Test
    void createStationWithDuplicateName() {
        // given
        final String 강남역 = "강남역";
        지하철_역_생성_요청(강남역);

        // when
        final ExtractableResponse<Response> response = 지하철_역_생성_요청(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * Given 새로운 지하철 역 생성을 요청 하고
     * When 지하철 역 목록 조회를 요청 하면
     * Then 두 지하철 역이 포함된 지하철 역 목록을 응답 받는다.
     */
    @DisplayName("지하철 역 목록 조회")
    @Test
    void getStations() {
        // given
        final String 강남역 = "강남역";
        final String 역삼역 = "역삼역";
        지하철_역_생성_요청(강남역);
        지하철_역_생성_요청(역삼역);

        // when
        final ExtractableResponse<Response> response = 지하철_역_목록_조회_요청();

        // then
        final List<String> stationNames = response.jsonPath().getList("name");
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).contains(강남역, 역삼역)
        );
    }

    /**
     * Given 지하철 역 생성을 요청 하고
     * When 생성한 지하철 역 삭제를 요청 하면
     * Then 생성한 지하철 역 삭제가 성공 한다.
     */
    @DisplayName("지하철 역 삭제")
    @Test
    void deleteStation() {
        // given
        final ExtractableResponse<Response> createResponse = 지하철_역_생성_요청("강남역");

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = 지하철_역_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
