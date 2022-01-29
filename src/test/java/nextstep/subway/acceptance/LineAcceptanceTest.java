package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공 한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름 으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패 한다.
     */
    @DisplayName("중복 이름 으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        final String 신분당선 = "신분당선";
        지하철_노선_생성_요청(신분당선, "bg-red-600", "강남역", "역삼역");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선, "bg-green-600", "합정역", "당산역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답 받는다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        final String 신분당선 = "신분당선";
        final String 지하철2호선 = "2호선";
        지하철_노선_생성_요청(신분당선, "bg-red-600", "강남역", "역삼역");
        지하철_노선_생성_요청(지하철2호선, "bg-green-600", "합정역", "당산역");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("name")).contains(신분당선, 지하철2호선)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답 받는다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        final String 강남역 = "강남역";
        final String 역삼역 = "역삼역";
        final ExtractableResponse<Response> upStationCreateResponse = 지하철_역_생성_요청(강남역);
        final ExtractableResponse<Response> downStationCreateResponse = 지하철_역_생성_요청(역삼역);

        final String 신분당선 = "신분당선";
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                신분당선,
                "bg-red-600",
                upStationCreateResponse.jsonPath().getLong("id"),
                downStationCreateResponse.jsonPath().getLong("id"),
                1
        );

        // when
        final String path = createResponse.header("Location");
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(path);

        // then
        final JsonPath responseBody = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.getLong("id")).isNotNull(),
                () -> assertThat(responseBody.getString("name")).isEqualTo(신분당선),
                () -> assertThat(responseBody.getList("stations.name")).contains(강남역, 역삼역)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공 한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when
        final String path = createResponse.header("Location");
        final String 구분당선 = "구분당선";
        final String color = "bg-blue-600";
        지하철_노선_수정_요청(path, 구분당선, color);

        // then
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(path);
        final JsonPath responseBody = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.getString("name")).isEqualTo(구분당선),
                () -> assertThat(responseBody.getString("color")).isEqualTo(color)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공 한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when
        final String path = createResponse.header("Location");
        final ExtractableResponse<Response> response = 지하철_노선_삭제_요청(path);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
