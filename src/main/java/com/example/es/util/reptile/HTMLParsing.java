package com.example.es.util.reptile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用jsoup爬取网页数据
 * @Author Zhanzhan
 * @Date 2021/2/7 20:33
 */
@Component
public class HTMLParsing {

    /**
     * 爬取京东搜索商品页的商品数据
     * @param keyword 搜索关键字
     * @return 商品数据
     * @throws IOException
     */
    public List<Content> parseJD(String keyword) throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.parse(new URL(url), 3000);
        Element element = document.getElementById("J_goodsList");
        Elements li = element.getElementsByTag("li");
        List<Content> goodsList = new ArrayList<>();
        Content content = null;
        for (Element el : li){
            String image = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").get(0).text();
            content = new Content();
            content.setImg(image);
            content.setPrice(price);
            content.setTitle(title);
            goodsList.add(content);
        }
        return goodsList;
    }

    public void test() throws IOException {
        /**
         * 获取请求：https://search.jd.com/Search?keyword=%E8%8C%85%E5%8F%B0，
         * 此地址是从浏览器页面上找到的
         */
        String url = "https://search.jd.com/Search?keyword=心理学";

        // 解析网页(Jsoup返回Document，就是浏览器的Document对象)
        Document document = Jsoup.parse(new URL(url), 3000);
        // 所有在js中调用Document的方法，都可以使用
        // 这里可以在网页中按F12,然后查看页面的HTML代码，然后找到对应的元素
        Element element = document.getElementById("J_goodsList");
//        System.out.println(element.html());
        // 获取所有的li元素
        Elements li = element.getElementsByTag("li");
        // 获取元素中的内容
        for (Element el : li){
            String image = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            System.out.println("===========");
            System.out.println(image);
            System.out.println(price);
            System.out.println(title);
        }
    }
}
