package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.seleniumhq.jetty9.server.handler.gzip.GzipHttpOutputInterceptor.LOG;

/**
 * @author a.kovtun
 * @since 10.09.2018.
 */
public class MainPageObject {

    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver){
        this.driver = driver;
    }

    public WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    public WebElement waitForElementPresent(By by, String error_message) {
        return waitForElementPresent(by, error_message, 5);
    }

    public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public void findElementInSearch(String value) {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find search input",
                5
        );
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                value,
                "No search result",
                5
        );
    }

    public void openFoundElement(By xpath_to_click_on_element, By xpath_to_wait_element_present ){
        waitForElementAndClick(
                xpath_to_click_on_element,
                "Cannot find search result " + xpath_to_click_on_element,
                5
        );

        waitForElementPresent(
                xpath_to_wait_element_present,
                "Cannot find article title",
                15
        );
    }

    public void add_article_to_list(By xpath_to_options, By xpath_to_add_to_list){
        waitForElementAndClick(
                xpath_to_options,
                "Cannot find button to open article options",
                5
        );
        waitForElementAndClick(
                xpath_to_add_to_list,
                "Cannot find option 'Add to reading list'",
                5
        );

    }

    public void putNameOfListAndCloseArticle(String name_of_folder, By xpath_of_input, By xpath_button_OK, By close_article){
        waitForElementAndSendKeys(
                xpath_of_input,
                name_of_folder,
                "Cannot put text into text area",
                5
        );
        waitForElementAndClick(
                xpath_button_OK,
                "Cannot press OK",
                5
        );

        waitForElementAndClick(
                close_article,
                "Cannot find button to close article",
                5
        );
    }

    public void goToMyListAndFindArticle(By go_to_my_list, By title_of_article_in_mylist){
        waitForElementAndClick(
                go_to_my_list,
                "Cannot find navigation button to 'My Lists'",
                5
        );
        waitForElementAndClick(
                title_of_article_in_mylist,
                "Cannot find Learning programming in 'My Lists'",
                5
        );
    }

    public void deleteArticleFromMyListBySwipe(By what_to_delete, By article_is_not_present_in_mylist){
        swipeUpToLeft(
                what_to_delete,
                "Cannot find SAVED ARTICLE"
        );
        waitForElementNotPresent(
                article_is_not_present_in_mylist,
                "Cannot delete saved article",
                5
        );
    }



    public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        element.sendKeys(value);
        return element;
    }

    public boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    public WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public void assertThatText(By by, String value) {
        WebElement element = waitForElementPresent(by, value, 5);
        Assert.assertEquals("error! We see unexpected text " + value, element.getAttribute("text"), value);
    }

    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);

        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);


        action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
    }

    protected void swipeUpQuick() {
        swipeUp(200);
    }

    public void swipeUpToFindElement(By by, String error_message, int max_swipes) {
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) {
            if (already_swiped > max_swipes) {
                waitForElementPresent(by, "Cannot find element by swiping up. \n" + error_message, 0);
                return;
            }

            swipeUpQuick();
            ++already_swiped;
        }
    }

    public void swipeUpToLeft(By by, String error_message) {
        WebElement element = waitForElementPresent(
                by,
                error_message,
                10
        );

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(150)
                .moveTo(left_x, middle_y).release()
                .perform();
    }

    public int getAmountOfElements(By by){
        List elements = driver.findElements(by);
        return  elements.size();
    }
    public void assertElementNotPresent(By by, String error_message){
        int amount_of_element = getAmountOfElements(by);
        if (amount_of_element > 0) {
            String default_message = "An element " + by.toString() + "supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }
    public void assertElementPresent(By by, String error_message){
        int amount_of_element = getAmountOfElements(by);
        if (amount_of_element <= 0) {
            throw new AssertionError(error_message);
        } else LOG.info("OK");
    }

    public String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }
}
