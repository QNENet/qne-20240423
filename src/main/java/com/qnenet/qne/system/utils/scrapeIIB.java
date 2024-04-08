
package com.qnenet.qne.system.utils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class scrapeIIB {

    String username = "paulfraser";
    String password = "paul222";
    String login = username + ":" + password;
    String base64login = Base64.getEncoder().encodeToString(login.getBytes());

    @SuppressWarnings("deprecation")
    public void loginAndScrape() throws IOException {

        String username = "paulfraser";
        String password = "paul222";
        String login = username + ":" + password;
        String base64login = Base64.getEncoder().encodeToString(login.getBytes());

        for (int i = 7200; i < 7500; i++) {
            Document doc = Jsoup.connect("http://www.iib.ws/associates/ea/eadata/details.asp?UserID=" + i)
                    .header("Authorization", "Basic " + base64login)
                    .get();

            String txt = doc.toString();

            if (txt.contains("Sorry"))
                continue;

            FileUtils.writeStringToFile(new File("/home/paulf/Dropbox/iib-pages/" + i + ".txt"), txt);

            // System.out.println(doc);
        }

     
    }

    public static void main(String[] args) throws IOException {
        scrapeIIB scraper = new scrapeIIB();

        scraper.loginAndScrape();
    }
}
