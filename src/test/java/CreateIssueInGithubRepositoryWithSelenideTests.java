import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.linkText;


public class CreateIssueInGithubRepositoryWithSelenideTests {
    private final static String REPOSITORY = "test3";
    private final static String GITHUB_URL = "https://github.com";
    private static String user;
    private static String password;

    @BeforeEach
    void getCredentials() throws IOException {
        Credentials credentials = new Credentials();
        user = credentials.getUserName();
        password = credentials.getPassword();
    }

    @BeforeEach
    void selenideSettings() {
        Configuration.startMaximized = true;
    }

    @Test
    void githubTest() {

        System.out.println(user);
        System.out.println(password);

        open(GITHUB_URL);
        $(".header-search-input").click();
        $(".header-search-input").sendKeys(user + "/" + REPOSITORY);
        $(".header-search-input").submit();
        $(linkText(user + "/" + REPOSITORY)).click();
        $(withText("Issues")).click();
        $(withText("New issue")).click();
        $(withText("Already on GitHub?")).$(linkText("Sign in")).click();
        $("#login_field").sendKeys(user);
        $("#password").sendKeys(password);
        $(byName("commit")).submit();
        $("#issue_title").sendKeys("Issue");
        $(withText("Assignees")).click();
        $("#assignee-filter-field").val(user).pressEnter();
        $(withText("Assignees")).click();

        $(withText("Labels")).click();
        $("#label-filter-field").val("Issue");
        $(withText("Create new label")).click();
        $("#new-label-name").val("Issue").waitUntil(visible, 1000);
        $(".Box-footer button").submit();
        $(withText("Labels")).click();
        $(withText("Submit new issue")).click();
    }
}
