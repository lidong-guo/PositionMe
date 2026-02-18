package com.openpositioning.PositionMe.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * WebScraper utility class for fetching and parsing HTML content from web sources.
 * This class provides methods to scrape data from websites, parse HTML content,
 * and extract specific elements or data points.
 * 
 * <p>Features:</p>
 * <ul>
 *   <li>Asynchronous web scraping using OkHttp</li>
 *   <li>HTML parsing with JSoup</li>
 *   <li>Element extraction by CSS selectors</li>
 *   <li>Data extraction with custom patterns</li>
 * </ul>
 *
 * @author PositionMe Team
 */
public class WebScraper {
    private static final String TAG = "WebScraper";
    private final OkHttpClient client;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    /**
     * Listener interface for scraping operations.
     */
    public interface ScraperListener {
        /**
         * Called when scraping is successful.
         * @param result The scraped data
         */
        void onSuccess(ScraperResult result);

        /**
         * Called when scraping fails.
         * @param error The error message
         */
        void onError(String error);
    }

    /**
     * Container class for scraper results.
     */
    public static class ScraperResult {
        private final String url;
        private final String rawHtml;
        private final Document document;
        private final Map<String, String> metadata;

        public ScraperResult(String url, String rawHtml, Document document) {
            this.url = url;
            this.rawHtml = rawHtml;
            this.document = document;
            this.metadata = new HashMap<>();
        }

        public String getUrl() {
            return url;
        }

        public String getRawHtml() {
            return rawHtml;
        }

        public Document getDocument() {
            return document;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }

        public void addMetadata(String key, String value) {
            metadata.put(key, value);
        }

        /**
         * Extract text from elements matching a CSS selector.
         * @param selector CSS selector
         * @return List of text content from matching elements
         */
        public List<String> extractTextBySelector(String selector) {
            List<String> results = new ArrayList<>();
            Elements elements = document.select(selector);
            for (Element element : elements) {
                results.add(element.text());
            }
            return results;
        }

        /**
         * Extract attribute values from elements matching a CSS selector.
         * @param selector CSS selector
         * @param attribute Attribute name
         * @return List of attribute values from matching elements
         */
        public List<String> extractAttributeBySelector(String selector, String attribute) {
            List<String> results = new ArrayList<>();
            Elements elements = document.select(selector);
            for (Element element : elements) {
                String attr = element.attr(attribute);
                if (!attr.isEmpty()) {
                    results.add(attr);
                }
            }
            return results;
        }

        /**
         * Extract all links from the page.
         * @return List of URLs
         */
        public List<String> extractAllLinks() {
            return extractAttributeBySelector("a[href]", "href");
        }

        /**
         * Extract all images from the page.
         * @return List of image URLs
         */
        public List<String> extractAllImages() {
            return extractAttributeBySelector("img[src]", "src");
        }

        /**
         * Get the page title.
         * @return Page title or empty string if not found
         */
        public String getTitle() {
            return document.title();
        }
    }

    /**
     * Default constructor initializing the web scraper with default settings.
     */
    public WebScraper() {
        this.client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
        this.executorService = Executors.newFixedThreadPool(3);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Constructor with custom OkHttpClient.
     * @param client Custom OkHttpClient instance
     */
    public WebScraper(OkHttpClient client) {
        this.client = client;
        this.executorService = Executors.newFixedThreadPool(3);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Scrape a web page asynchronously.
     * 
     * @param url The URL to scrape
     * @param listener Callback listener for results
     */
    public void scrapeAsync(String url, ScraperListener listener) {
        executorService.execute(() -> {
            try {
                ScraperResult result = scrapeSynchronous(url);
                mainHandler.post(() -> listener.onSuccess(result));
            } catch (IOException e) {
                Log.e(TAG, "Error scraping URL: " + url, e);
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        });
    }

    /**
     * Scrape a web page synchronously.
     * Note: Should not be called on the main thread.
     * 
     * @param url The URL to scrape
     * @return ScraperResult containing the parsed data
     * @throws IOException if the request fails
     */
    public ScraperResult scrapeSynchronous(String url) throws IOException {
        Log.d(TAG, "Scraping URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) PositionMe/1.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Response body is null");
            }

            String html = body.string();
            Document document = Jsoup.parse(html, url);
            
            ScraperResult result = new ScraperResult(url, html, document);
            
            // Add some basic metadata
            result.addMetadata("response_code", String.valueOf(response.code()));
            result.addMetadata("content_type", response.header("Content-Type", "unknown"));
            result.addMetadata("content_length", String.valueOf(html.length()));

            Log.d(TAG, "Successfully scraped: " + url + " (length: " + html.length() + ")");
            return result;
        }
    }

    /**
     * Scrape multiple URLs asynchronously.
     * 
     * @param urls List of URLs to scrape
     * @param listener Callback listener for results
     */
    public void scrapeMultiple(List<String> urls, MultiScraperListener listener) {
        List<ScraperResult> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        final int[] completedCount = {0};

        for (String url : urls) {
            executorService.execute(() -> {
                try {
                    ScraperResult result = scrapeSynchronous(url);
                    synchronized (results) {
                        results.add(result);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error scraping URL: " + url, e);
                    synchronized (errors) {
                        errors.add(url + ": " + e.getMessage());
                    }
                }

                synchronized (completedCount) {
                    completedCount[0]++;
                    if (completedCount[0] == urls.size()) {
                        mainHandler.post(() -> listener.onComplete(results, errors));
                    }
                }
            });
        }
    }

    /**
     * Listener interface for multiple scraping operations.
     */
    public interface MultiScraperListener {
        /**
         * Called when all scraping operations are complete.
         * @param results List of successful results
         * @param errors List of error messages
         */
        void onComplete(List<ScraperResult> results, List<String> errors);
    }

    /**
     * Extract data from a URL using a custom CSS selector.
     * 
     * @param url The URL to scrape
     * @param selector CSS selector for elements to extract
     * @param listener Callback listener for results
     */
    public void extractData(String url, String selector, DataExtractionListener listener) {
        scrapeAsync(url, new ScraperListener() {
            @Override
            public void onSuccess(ScraperResult result) {
                List<String> extractedData = result.extractTextBySelector(selector);
                listener.onDataExtracted(extractedData);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    /**
     * Listener interface for data extraction operations.
     */
    public interface DataExtractionListener {
        /**
         * Called when data extraction is successful.
         * @param data List of extracted text data
         */
        void onDataExtracted(List<String> data);

        /**
         * Called when data extraction fails.
         * @param error The error message
         */
        void onError(String error);
    }

    /**
     * Shutdown the executor service.
     * Should be called when the scraper is no longer needed.
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Log.d(TAG, "WebScraper executor service shut down");
        }
    }

    /**
     * Check if a URL is valid and reachable.
     * 
     * @param url The URL to check
     * @param listener Callback listener for the check result
     */
    public void checkUrlReachable(String url, UrlCheckListener listener) {
        executorService.execute(() -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .head()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    boolean isReachable = response.isSuccessful();
                    int code = response.code();
                    mainHandler.post(() -> listener.onCheckComplete(isReachable, code));
                }
            } catch (IOException e) {
                Log.e(TAG, "Error checking URL: " + url, e);
                mainHandler.post(() -> listener.onCheckComplete(false, -1));
            }
        });
    }

    /**
     * Listener interface for URL reachability checks.
     */
    public interface UrlCheckListener {
        /**
         * Called when URL check is complete.
         * @param isReachable Whether the URL is reachable
         * @param responseCode HTTP response code (-1 if error)
         */
        void onCheckComplete(boolean isReachable, int responseCode);
    }
}
