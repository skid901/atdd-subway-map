package nextstep.subway.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/cucumber",
        plugin = {"pretty", "json:target/cucumber-report.json"})
public class CucumberAcceptanceRunner {
}
