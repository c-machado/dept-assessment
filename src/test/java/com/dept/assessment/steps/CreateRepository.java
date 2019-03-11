package com.dept.assessment.steps;

import com.dept.assessment.consts.Constants;
import com.dept.assessment.pages.CreateRepositoryPage;
import com.dept.assessment.pages.HomePage;
import com.dept.assessment.pages.LoginPage;
import com.dept.assessment.pages.RepositoryPage;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.asserts.Assertion;


import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateRepository {

    private WebDriver driver;
    private LoginPage loginpPage;
    private HomePage homePage;
    private RepositoryPage repositoryPage;
    private CreateRepositoryPage createRepoPage;
    private String repoName = "DeptAssessmen1t";
    private String repoDescription = "Dept assessment repo for testing purposes";
    private String gitOptionsFilter = "J";
    private String gitIgnoreTemplate = "Java";

    @Before
    public void setUp() {
        System.out.println("en setup");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);
    }

    @Given("^I am logged in the Github's website$")
    public void iAmLoggedInTheGithubSWebsite() {
        System.out.println("en given");
        driver.get(Constants.LOGIN_URL);
        loginpPage = new LoginPage(driver);
        loginpPage.enterUsername(Constants.USERNAME);
        loginpPage.enterPassword(Constants.PASSWORD);
        loginpPage.clickLoginButton();
    }

    @Given("^I'm at the homepage$")
    public void iMAtTheHomepage() {
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isPageOpen());
    }

    public void close(){
        driver.close();
    }

    @When("^I click on the create Repository CTA$")
    public void iClickOnTheCreateRepositoryCTA() {
        homePage.clickStartAProjectCTA();
    }

    @And("^I submit the new repository's information$")
    public void iSubmitTheNewRepositorySInformation() {
        createRepoPage = new CreateRepositoryPage(driver);
        createRepoPage.enterRepoName(repoName);
        createRepoPage.enterRepoDescription(repoDescription);
        createRepoPage.selectPrivateRepo();
        createRepoPage.checkCreateReadme();
        createRepoPage.openGitIgnoreOptions();
        validateGitIgnoreFilter();
        createRepoPage.clickGitIgnoreOption(gitIgnoreTemplate);
        createRepoPage.clickOnSubmitRepo();
    }

    private void validateGitIgnoreFilter() {
        List<WebElement> filteredOptions = createRepoPage.filterGitIgnoreOptions(gitOptionsFilter);
        Iterator<WebElement> filterIterator = filteredOptions.iterator();
        while(filterIterator.hasNext()) {
            WebElement gitIgnoreOption = filterIterator.next();
            String optionLabel = gitIgnoreOption.getText().substring(0, 1);
            System.out.println("string ignore" + optionLabel);
            Assert.assertTrue(optionLabel.equals(gitOptionsFilter));
        }
    }

    @Then("^I should be at the new repository's page$")
    public void iShouldBeAtTheNewRepositorySPage() {
        repositoryPage = new RepositoryPage(driver);
        Assert.assertTrue(repositoryPage.getNameRepoCreated().equals(repoName));
    }
}
