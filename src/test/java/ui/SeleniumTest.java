package ui;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import util.Utils;

import java.io.*;

import java.util.List;
import static org.openqa.selenium.By.cssSelector;

public class SeleniumTest {

    @Test
    public static void SearchImageTest() throws IOException, InterruptedException {

        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        WebDriver wd = new ChromeDriver();
        String url = "http://www.baidu.com";
        wd.get(url);
        //点击“按图片搜索”图标
        wd.findElement(By.className("soutu-btn")).click();

        //搜索项目路径下“/test-output/search/image1.jpg”
        String projectPath = System.getProperty("user.dir");
        wd.findElement(cssSelector("input[type='file']")).sendKeys(projectPath+"/test-output/search/image1.jpg");

        //向下滑动页面，展示搜索结果中的“相似图片”
        Thread.sleep(5000);
        ((JavascriptExecutor) wd).executeScript("window.scrollTo(0,600)");

        //获取与搜索图片相似的图片列表
//        https://mms1.baidu.com/it/u=3030095686,1934878083&fm=253&app=120&f=JPEG&fmt=auto&q=75?w=640&h=385
        List<WebElement> wes = wd.findElements(By.cssSelector("img[src*='.baidu.com/it/u=']"));
        System.out.println(wes.size());
        //获取配置文件中"VISIT_RESULT"的值
        System.out.println(Utils.getParam("VISIT_RESULT"));
        String src = wes.get(Integer.valueOf(Utils.getParam("VISIT_RESULT"))).getAttribute("src");
        //浏览器打开相似图片列表的第VISIT_RESULT个图片
        wd.get(src);

        //将打开的相似图片截图保存到项目路径下的/test-output/result/下，并以"image_时间戳.jpg"命名
        WebElement targetImg = wd.findElement(By.tagName("img"));
        File srcfile = targetImg.getScreenshotAs(OutputType.FILE);
        String srcPath = projectPath+"/test-output/result/image_"+System.currentTimeMillis()+".jpg";
        FileUtils.copyFile(srcfile, new File(srcPath));

        //获取搜索结果中相似图片列表的第VISIT_RESULT个图片跟查询图片的相似度
        //查询图片存放的路径为/test-output/search/，查询结果存放路径为/test-output/result/
        float result = Utils.compare(Utils.getData(projectPath+"/test-output/search/image1.jpg"),
                Utils.getData(srcPath));
        System.out.println(result);

    }

}
