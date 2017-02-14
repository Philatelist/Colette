package com.colette.controller;

import com.colette.model.ColorCard;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import utils.view.ColorView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Controller
public class ColorController {

    private static final Logger log = LogManager.getLogger(ColorController.class);
    private static long supercounter;
    private boolean grayRequest = false;

    @RequestMapping(value = "/rest/v1/getcolors",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    @JsonView(ColorView.MediumInfoLevel.class)
    List<ColorCard> getColors(@RequestParam(value = "url") String inputUrl,
                              @RequestParam(value = "gray", required = false) String gray) {

        List<ColorCard> result = null;
        if (gray != null && !gray.isEmpty() && gray.equals("true")) {
            grayRequest = true;
        }

        try {
            WebDriver driver = new PhantomJSDriver();
            driver.get(inputUrl);
            File image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage bufferedImage = ImageIO.read(image);
//            FileUtils.copyFile(image, new File("saved.jpg"));

            result = getColor(bufferedImage);
            driver.quit();
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;

    }


    private List<ColorCard> getColor(BufferedImage image) throws IOException, AWTException {
        int height = image.getHeight();
        int width = image.getWidth();

        System.out.println("height " + height);
        System.out.println("width " + width);

        Map m = new HashMap();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Raster raster = image.getRaster();
                ColorModel model = image.getColorModel();
                Object data = raster.getDataElements(i, j, null);
                int argb = model.getRGB(data);
                Color color = new Color(argb, true);
                int[] rgbArray = getRGBArr(argb);

//                if (!checkIsGray(rgbArray)) {
                    Integer counter = (Integer) m.get(color.getRGB());
                    if (counter == null)
                        counter = 0;
                    counter++;
                    supercounter++;
                    m.put(color.getRGB(), counter);
//                }
            }
        }
        System.out.println(m.entrySet());
        List<ColorCard> colours = getMostCommonColour(m);
        return colours;
    }

//    private void countColorAndAddToMap(Map m, Color color) {
//        Integer counter = (Integer) m.get(color.getRGB());
//        if (counter == null)
//            counter = 0;
//        counter++;
//        supercounter++;
//        m.put(color.getRGB(), counter);
//    }


    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red, green, blue};

    }

    public static boolean checkIsGray(int[] rgbArr) {
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        // Filter out black, white and grays...... (tolerance within 10 pixels)
        int tolerance = 10;
        if (rgDiff > tolerance || rgDiff < -tolerance)
            if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false;
            }
        return true;
    }

    public List<ColorCard> getMostCommonColour(Map map) {
        List list = new LinkedList(map.entrySet());
        List result = new ArrayList<>();
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Collections.reverse(list);
        for (int i = 0; i < 10; i++) {

            Map.Entry me = (Map.Entry) list.get(i);
            double value = (double) (int) me.getValue();
            String percent = String.format("%.2f", value * 100 / supercounter);
            int[] rgb = getRGBArr((Integer) me.getKey());
            String hex = "";
            if (Integer.toHexString(rgb[0]).equals("0")) {
                hex += "00";
            } else {
                hex += Integer.toHexString(rgb[0]);
            }
            if (Integer.toHexString(rgb[1]).equals("0")) {
                hex += "00";
            } else {
                hex += Integer.toHexString(rgb[1]);
            }
            if (Integer.toHexString(rgb[2]).equals("0")) {
                hex += "00";
            } else {
                hex += Integer.toHexString(rgb[2]);
            }
            ColorCard colorCard = new ColorCard(percent, hex.toUpperCase(), rgb[0] + "," + rgb[1] + "," + rgb[2]);
            result.add(colorCard);
            System.out.println("Value = " + percent + "%" + " == RGB " + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + " == HEX #" + hex.toUpperCase());

        }

        return result;
    }

}
