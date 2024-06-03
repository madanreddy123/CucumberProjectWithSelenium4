package stepdefinitions;

import com.Cucumber.automation.framework.basetest.TestContext;
import com.Cucumber.automation.framework.constants.AppConstants.Api.AuthenticationType;
import com.Cucumber.automation.framework.constants.AppConstants.Api.GrantType;
import com.Cucumber.automation.framework.utils.APIUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.ThreadContext;

public class APITestSteps {

    TestContext testContext;
    APIUtils apiUtils;

    public APITestSteps(TestContext context) throws Exception {
        this.testContext = context;
        ThreadContext.pop();
        ThreadContext.push(this.getClass().getSimpleName());

        apiUtils = new APIUtils(AuthenticationType.OAuth2, GrantType.Client_Credentials);
    }

    @When("^user makes a POST api call$")
    public void executePostApiCall() {

    }

    @Then("^verify response status code is 201$")
    public void verifyPostApiStatus() {

    }
}
