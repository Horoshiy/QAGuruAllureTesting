import com.codeborne.selenide.Configuration;
import com.example.hume.allure.Credentials;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;


public class CreateIssueInGithubRepositoryTests {
    private final static String GITHUB_URL = "https://github.com";
    private static String user;
    private static String password;
    private static String repository;

    @BeforeEach
    void getCredentials() throws IOException {
        Credentials credentials = new Credentials();
        user = credentials.getUserName();
        password = credentials.getPassword();
        repository = user + "/QAGuruAllureTesting";
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
        $("#label-filter-field").val("bug").pressEnter();
        $(withText("Labels")).click();
        $(withText("Labels")).click();
        $(withText("Submit new issue")).click();
    }

    @Test
    void createIssueWithStepsTest() {
        step("Открываем главную страницу", () -> open(GITHUB_URL));
        step("Ищем репозиторий " + repository, (step) -> {
            step.parameter("name", repository);
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(repository);
            $(".header-search-input").submit();
        });
        step("Открываем репозиторий " + repository, (step) -> {
            step.parameter("name", repository);
            $(linkText(repository)).click();
        });
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

    @Test
    public void createIssueWithAnnotationsTest() {
        final BaseSteps steps = new BaseSteps();
        steps.openMainPage();
        steps.searchForRepository(repository);
        steps.goToRepository(repository);
        steps.goToIssues();
        steps.pressButtonCreateIssue();
        steps.goToAuthorizationPage();
        steps.fillAuthorizationForm();
        steps.fillTitleFieldInCreateNewIssueForm();
        steps.fillAssigneesInCreateNewIssueForm();
        steps.fillLabelsInCreateNewIssueForm();
        steps.pressSubmitButtonInCreateNewIssueForm();
    }

    @AfterEach
    public void logout() {
        $(".js-feature-preview-indicator-container").scrollIntoView(false).click();
        $(".logout-form").submit();
    }

    public static class BaseSteps {
        @Step("Открываем главную страницу")
        public void openMainPage() {
            open(GITHUB_URL);
        }

        @Step("Ищем репозиторий {name}")
        public void searchForRepository(final String name) {
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(name);
            $(".header-search-input").submit();
        }

        @Step("Переходим в репозиторий {name}")
        public void goToRepository(final String name) {
            $(linkText(name)).click();
        }

        @Step("Переходим в раздел Issues")
        public void goToIssues() {
            $(withText("Issues")).click();
        }

        @Step("Жмём на кнопу 'Создать Issue'")
        public void pressButtonCreateIssue() {
            $(withText("New issue")).click();
        }

        @Step("Переходим на страницу авторизации")
        public void goToAuthorizationPage() {
            $(withText("Already on GitHub?")).$(linkText("Sign in")).click();
        }

        @Step("Заполняем авторизационную форму")
        public void fillAuthorizationForm() {
            $("#login_field").sendKeys(user);
            $("#password").sendKeys(password);
            $(byName("commit")).submit();
        }

        @Step("Заполняем поле Title в Create New Issue")
        public void fillTitleFieldInCreateNewIssueForm() {
            $("#issue_title").sendKeys("Issue");
        }

        @Step("Заполняем Assignees в Create New Issue")
        public void fillAssigneesInCreateNewIssueForm() {
            $(withText("Assignees")).click();
            $("#assignee-filter-field").val(user).pressEnter();
            $(withText("Assignees")).click();
        }

        @Step("Заполняем Labels в Create New Issue")
        public void fillLabelsInCreateNewIssueForm() {
            $(withText("Labels")).click();
            $("#label-filter-field").val("bug").pressEnter();
            $(withText("Labels")).click();
        }

        @Step("Жмем на кнопу Создания Issue")
        public void pressSubmitButtonInCreateNewIssueForm() {
            $(withText("Submit new issue")).click();
        }
    }
}
