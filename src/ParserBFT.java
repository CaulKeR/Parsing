import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ParserBFT {

    public void parse(String product) throws Exception {

        String startUrl = "https://bft.by/catalog/" + product + "_1/filter/brend-is-e7c9f931-c958-11e4-ab3b-00259068e7f0-or-e7c9f930-c958-11e4-ab3b-00259068e7f0/apply/?num=64&view=list";
        product = titleCorrector(product);

        try (PrintWriter pw = new PrintWriter(new File( product + ".csv"), StandardCharsets.UTF_8)) {
            String url = startUrl;
            int lastPage = defineNumberOfPages(url);
            int countOfProduct = 0;
            for (int i = 1; i <= lastPage; i++) {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getElementsByClass("bxr-ecommerce-v1-list");
                for (Element element : elements) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String urlString = element.getElementsByClass("bxr-element-image-col").select("img").attr("src");
                    if (urlString.contains("/upload")) {
                        urlString = urlShortener(urlString);
                    }
                    String title = element.getElementsByClass("bxr-element-name").text();
                    stringBuilder.append(title);
                    stringBuilder.append(';');
                    String description = element.getElementsByClass("bxr-props-table").text();
                    description = description.replace("Бренд ", "");
                    description = description.replace("Артикул ", ";");
                    description = description.replace(" Базовая единица шт", ";");
                    stringBuilder.append(description);
                    String available = element.getElementsByClass("bxr-element-avail").text();
                    available = available.replace("В наличии (", "");
                    available = available.replace("шт)", "");
                    stringBuilder.append(available);
                    stringBuilder.append(';');
                    String price = element.getElementsByClass("bxr-element-price").text();
                    price = price.replace(" руб.", "");
                    stringBuilder.append(price);
                    stringBuilder.append(';');
                    urlString = "https://bft.by" + urlString;
                    stringBuilder.append(urlString);
                    stringBuilder.append(';');
                    stringBuilder.append('\n');
                    pw.write(stringBuilder.toString());
                    countOfProduct++;
                }
                url = startUrl + "&PAGEN_1=" + (i + 1);
            }
            pw.flush();
            System.out.println("Count of " + product + ": " + countOfProduct);
        } catch (IOException e) {
            throw new Exception(e);
        }

    }

    private int defineNumberOfPages(String url) throws Exception {

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("navigation-pages");
            String s = "";
            for (Element element : elements) {
                s += element.text() + "\n";
            }
            String[] pages = s.split(" ");
            if (pages.length > 1) {
                System.out.println("Count of pages with products: " + Integer.parseInt(pages[pages.length - 2]));
                return Integer.parseInt(pages[pages.length - 2]);
            } else {
                System.out.println("Count of pages with products: " + 1);
                return 1;
            }
        } catch (IOException e) {
            throw new Exception(e);
        }

    }

    private String urlShortener(String urlString) {

        urlString = urlString.replace("/resize_cache", "");
        urlString = urlString.replace("/160_160_1", "");
        return urlString;

    }

    private String titleCorrector(String title) {

        if (title.contains("mebel_1/")) {
            return title.replace("mebel_1/", "");
        } else {
            return title;
        }

    }

}