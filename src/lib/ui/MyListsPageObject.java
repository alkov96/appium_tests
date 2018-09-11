package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * @author a.kovtun
 * @since 10.09.2018.
 */
public class MyListsPageObject extends MainPageObject {

    public static final String
    FOLDER_BY_NAME_TPL = "//*[@text='{FOLDER_NAME}']",
    ARTICLE_BY_TITLE = "//*[@text='{TITLE}']";

    private static String getFolderNameByXpath(String name_of_folder)
    {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }

    private static String getSavedArticleXpathTitle(String article_title)
    {
        return ARTICLE_BY_TITLE.replace("{TITLE}", article_title);
    }
    public MyListsPageObject(AppiumDriver driver)
    {
        super(driver);
    }

    public void openFolderByName(String name_of_folder)
    {
        String folder_name_xpath = getFolderNameByXpath(name_of_folder);
        this.waitForElementAndClick(
                By.xpath(folder_name_xpath),
                "Cannot find Learning programming in 'My Lists'",
                10
        );
    }

    public void swipeArticleToDelete(String article_title)
    {
        this.waitForArticleToAppearByTitle(article_title);
        String article_xpath = getFolderNameByXpath(article_title);
        this.swipeUpToLeft(
                By.xpath(article_xpath),
                "Cannot find SAVED ARTICLE"
        );
        this.waitForArticleToDassappearByTitle(article_title);
    }

    public void waitForArticleToAppearByTitle(String article_title)
    {
       String  article_xpath = getFolderNameByXpath(article_title);
        this.waitForElementPresent(
                By.xpath(article_xpath),
                "Cannot find saved article " + article_title, 15);
    }

    public void waitForArticleToDassappearByTitle(String article_title)
    {
        String  article_xpath = getFolderNameByXpath(article_title);
        this.waitForElementNotPresent(
                By.xpath(article_xpath),
                "Saved article still present with title" + article_title, 15);
    }
}
