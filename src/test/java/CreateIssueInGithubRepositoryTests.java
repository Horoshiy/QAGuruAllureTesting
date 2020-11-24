import com.codeborne.selenide.Configuration;
import com.example.hume.allure.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;


public class CreateIssueInGithubRepositoryTests {
    private static String user;
    private static String password;
    private static String repository;
    private final static String GITHUB_URL = "https://github.com";

    @BeforeEach
    void getCredentials() throws IOException {
        Credentials credentials = new Credentials();
        user = credentials.getUserName();
        password = credentials.getPassword();
        repository = user + "/test3";
    }

    @BeforeEach
    void selenideSettings() {
        Configuration.startMaximized = true;
    }

    @Test
    void createIssueWithCleanSelenideTest() {
        open(GITHUB_URL);
        $(".header-search-input").click();
        $(".header-search-input").sendKeys(repository);
        $(".header-search-input").submit();
        $(linkText(repository)).click();
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

    @Test
    void createIssueWithStepsTest() {
        step("Открываем главную страницу", () -> open(GITHUB_URL));
        step("Ищем репозиторий " + repository, () -> {
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(repository);
            $(".header-search-input").submit();
        });
        step("Открываем репозиторий " + repository, () -> $(linkText(repository)).click());
        step("Переходим в раздел Issues", () -> $(withText("Issues")).click());
        step("Жмём на кнопу 'Создать Issue'", () -> $(withText("New issue")).click());
        step("Переходим на страницу авторизации", () -> $(withText("Already on GitHub?")).$(linkText("Sign in")).click());
        step("Заполняем авторизационную форму", () -> {
            $("#login_field").sendKeys(user);
            $("#password").sendKeys(password);
            $(byName("commit")).submit();
        });
        step("Заполняем поле Title в Create New Issue", () -> $("#issue_title").sendKeys("Issue"));
        step("Заполняем Assignees", () -> {
            $(withText("Assignees")).click();
            $("#assignee-filter-field").val(user).pressEnter();
            $(withText("Assignees")).click();
        });
        step("Заполняем Labels", () -> {
            $(withText("Labels")).click();
            $("#label-filter-field").val("bug").pressEnter();
            $(withText("Labels")).click();
        });
        step("Жмем на кнопу Создания Issue", () -> $(withText("Submit new issue")).click());
    }
}
