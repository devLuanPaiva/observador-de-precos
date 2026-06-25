package com.luanpaiva.observador_de_precos.modules.scraping.strategy;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;
import com.luanpaiva.observador_de_precos.shared.utils.CurrencyUtils;

import lombok.RequiredArgsConstructor;

@Component
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
                try {
                        document = Jsoup.connect(url)
                                        .userAgent("Mozilla/5.0")
                                        .timeout(30000)
                                        .get();

                } catch (Exception e) {
                        throw new RuntimeException("Erro ao acessar Magalu", e);
                }

                ScrapingResultDTO fromJson = extractFromJsonLd(document);

                if (fromJson != null) {
                        return fromJson;
                }
                return extractFromHtml(document, url);
        }

        private ScrapingResultDTO extractFromJsonLd(
                        Document document) {

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

                                                "InStock".equalsIgnoreCase(root.path("offers")
                                                                .path("availability")
                                                                .asText()),

                                                root.path("image").asText(),

                                                "Magazine Luiza",

                                                root.path("sku").asText());
                        }

                } catch (Exception ignored) {
                        throw new RuntimeException("Erro ao extrair dados do JSON-LD", ignored);
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

                Matcher matcher = Pattern.compile("/p/([^/]+)/").matcher(url);

                return matcher.find() ? matcher.group(1) : null;
        }
}