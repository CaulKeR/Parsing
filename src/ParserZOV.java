import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParserZOV {

    public void parseKitchens() throws Exception {

        String startUrl = "https://zov.by/katalog/kuhni/";

        try (PrintWriter pw = new PrintWriter(new File( "kuhni.csv"), StandardCharsets.UTF_8)) {
            String url = startUrl;
            int lastPage = defineNumberOfPages(startUrl);
            int countOfKitchens = 0;
            ArrayList<String> links = new ArrayList<>();
            for (int i = 1; i <= lastPage; i++) {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getElementsByClass("catalog__item");
                for (Element element : elements) {
                    if (element.html().contains("catalog__new")) {
                        links.add(element.child(0).attr("href") + "\r\n");
                        countOfKitchens++;
                    }
                }
                url = startUrl + "?page=" + (i + 1);
            }
            pw.write("Название;Тип фасадов;Цвет фасадов;Столешница;Ручки;Витрины;Декоративные элементы;Цоколь;" +
                    "Кромка;Стол;Открытые полки;Стеллажная система;Стеновая панель;Вагонка;Пилястры;Дополнительная информация\n");
            for (int i = 0; i < countOfKitchens; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                Document doc = Jsoup.connect(links.get(i)).get();
                Elements elements = doc.getElementsByClass("layout inner");
                stringBuilder.append(elements.select("h1").text());
                stringBuilder.append(";");
                stringBuilder.append(elements.select("span.card-order__price").text());
                Elements rows = elements.select("tr");
                String str = "";
                for (int j = 0; j < rows.size(); j++) {
                    Element row = rows.get(j);
                    Elements cols = row.select("td");
                    str+= cols.get(0).text() + "--" + cols.get(1).text() + ";";
                }
                str = descriptionCorrector(str);
                stringBuilder.append(str);
                for (Element pic : elements) {
                    List<String> list = pic.getElementsByClass("fotorama").select("a").eachAttr("href");
                    for (int j = 0; j < list.size(); j++) {
                        stringBuilder.append(list.get(j));
                        stringBuilder.append("|");
                    }
                }
                stringBuilder.append('\n');
                pw.write(stringBuilder.toString());
            }
            pw.flush();
            System.out.println("Count of kitchens: " + countOfKitchens);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private int defineNumberOfPages(String url) throws Exception {

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("pagelist__item");
            String s = "";
            for (Element element : elements) {
                s += element.text() + " ";
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

    private String descriptionCorrector(String description) {

        String[] params = description.split(";");
        String[] res = {";", ";", ";", ";", ";", ";", ";", ";", ";", ";", ";", ";", ";", ";", ";"};

        for (int i = 0; i < params.length; i++) {
            if (params[i].contains("Тип фасадов--")) {
                res[0] = params[i] + ';';
                res[0] = res[0].substring(res[0].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Цвет фасадов--")) {
                res[1] = params[i] + ';';
                res[1] = res[1].substring(res[1].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Столешница--")) {
                res[2] = params[i] + ';';
                res[2] = res[2].substring(res[2].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Ручки--")) {
                res[3] = params[i] + ';';
                res[3] = res[3].substring(res[3].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Витрины--")) {
                res[4] = params[i] + ';';
                res[4] = res[4].substring(res[4].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Декоративные элементы--")) {
                res[5] = params[i] + ';';
                res[5] = res[5].substring(res[5].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Цоколь--")) {
                res[6] = params[i] + ';';
                res[6] = res[6].substring(res[6].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Кромка--")) {
                res[7] = params[i] + ';';
                res[7] = res[7].substring(res[7].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Стол--")) {
                res[8] = params[i] + ';';
                res[8] = res[8].substring(res[8].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Открытые полки--")) {
                res[9] = params[i] + ';';
                res[9] = res[9].substring(res[9].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Стеллажная система--")) {
                res[10] = params[i] + ';';
                res[10] = res[10].substring(res[10].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Стеновая панель--")) {
                res[11] = params[i] + ';';
                res[11] = res[11].substring(res[11].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Вагонка--")) {
                res[12] = params[i] + ';';
                res[12] = res[12].substring(res[12].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Пилястры--")) {
                res[13] = params[i] + ';';
                res[13] = res[13].substring(res[13].indexOf("--") + 2);
                continue;
            }
            if (params[i].contains("Дополнительная информация--")) {
                res[14] = params[i] + ';';
                res[14] = res[14].substring(res[14].indexOf("--") + 2);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String item : res) {
            sb.append(item);
        }
        return sb.toString();

    }

}
