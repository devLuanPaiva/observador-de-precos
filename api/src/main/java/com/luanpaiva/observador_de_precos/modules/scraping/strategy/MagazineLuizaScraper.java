package com.luanpaiva.observador_de_precos.modules.scraping.strategy;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;
import com.luanpaiva.observador_de_precos.shared.utils.CurrencyUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class MagazineLuizaScraper implements ScraperStrategy {

        private final ObjectMapper objectMapper;

        @Override
        public boolean supports(String url) {
                return url.contains("magazineluiza.com.br");
        }

        @Override
        public ScrapingResultDTO scrape(String url) {

                Document document;

                try (Playwright playwright = Playwright.create()) {

                        Browser browser = playwright.chromium().launch(
                                        new BrowserType.LaunchOptions()
                                                        .setHeadless(true));

                        Page page = browser.newPage();

                        page.setExtraHTTPHeaders(
                                        java.util.Map.of("Accept-Language", "pt-BR,pt;q=0.9", "Referer",
                                                        "https://www.google.com"));

                        page.navigate(url, new Page.NavigateOptions().setTimeout(30000));

                        page.waitForLoadState();

                        String html = page.content();

                        document = Jsoup.parse(html);

                        browser.close();

                } catch (Exception e) {

                        throw new RuntimeException("Erro ao acessar Magalu: " + e.getMessage(), e);
                }

                ScrapingResultDTO fromJson = extractFromJsonLd(document);

                if (fromJson != null) {
                        return fromJson;
                }

                return extractFromHtml(document, url);
        }

        private ScrapingResultDTO extractFromJsonLd(Document document) {

                try {

                        Elements scripts = document.select("script[type=application/ld+json]");

                        for (Element script : scripts) {

                                JsonNode root = objectMapper.readTree(script.html());

                                if (!"Product".equals(root.path("@type").asText())) {
                                        continue;
                                }

                                BigDecimal price = new BigDecimal(
                                                root.path("offers")
                                                                .path("price")
                                                                .asText("0"));

                                return new ScrapingResultDTO(
                                                root.path("name").asText(),
                                                price,
                                                "InStock".equalsIgnoreCase(
                                                                root.path("offers")
                                                                                .path("availability")
                                                                                .asText()),
                                                root.path("image").asText(),
                                                "Magazine Luiza",
                                                root.path("sku").asText());
                        }

                } catch (Exception e) {

                        e.printStackTrace();

                        throw new RuntimeException("Erro ao acessar Magalu: " + e.getMessage(), e);
                }

                return null;
        }

        private ScrapingResultDTO extractFromHtml(
                        Document document,
                        String url) {

                String title = document.select(
                                "h1[data-testid=heading-product-title]")
                                .text();

                String imageUrl = document.select(
                                "img[data-testid=image-selected-thumbnail]")
                                .attr("src");

                String priceText = document.select(
                                "[data-testid=price-value]")
                                .text();

                BigDecimal price = CurrencyUtils.parseBrazilianPrice(
                                priceText);

                String sku = extractSku(url);

                return new ScrapingResultDTO(
                                title,
                                price,
                                true,
                                imageUrl,
                                "Magazine Luiza",
                                sku);
        }

        private String extractSku(String url) {

                Matcher matcher = Pattern.compile("/p/([^/]+)/")
                                .matcher(url);

                return matcher.find()
                                ? matcher.group(1)
                                : null;
        }
}