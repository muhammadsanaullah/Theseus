package Downloader;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;

public class SeleniumDownloader {
    public SeleniumDownloader(){
        download();
    }
    public void download(){

        System.setProperty("webdriver.chrome.driver", "/Users/erimerdal/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String baseUrl = "https://www.nytimes.com/crosswords/game/mini";
        driver.get(baseUrl);
        System.out.println("basliyom");

        driver.findElements(By.className("buttons-modalButton--1REsR")).get(0).click();
        List<WebElement> li = driver.findElements(By.cssSelector("li.Tool-button--39W4J:nth-child(2) > button:nth-child(1)"));
        li.get(0).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div[4]/div/main/div[2]/div/div/ul/div[1]/li[2]/ul/li[3]/a")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/article/div[2]/button[2]/div")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/span")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String pageSource = driver.getPageSource();
        //String html = Jsoup.parse(pageSource).html();
        System.out.println(pageSource);
        //System.out.println(html);
        File f = new File("/Users/erimerdal/Desktop/AIPuzzleSolver/src/Example/reveal-17-12-2018.txt");
        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter fr = null;
        try {
            fr = new FileWriter(f);
            fr.write(pageSource);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

