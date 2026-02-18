package com.openpositioning.PositionMe.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Example usage of the WebScraper utility class.
 * This class demonstrates various ways to use the WebScraper for fetching
 * and parsing web content.
 * 
 * @author PositionMe Team
 */
public class WebScraperExample {
    private static final String TAG = "WebScraperExample";
    private final WebScraper scraper;

    public WebScraperExample() {
        this.scraper = new WebScraper();
    }

    /**
     * Example 1: Simple web page scraping
     * Scrapes a URL and processes the result.
     */
    public void exampleBasicScraping() {
        String url = "https://example.com";
        
        scraper.scrapeAsync(url, new WebScraper.ScraperListener() {
            @Override
            public void onSuccess(WebScraper.ScraperResult result) {
                Log.d(TAG, "Page title: " + result.getTitle());
                Log.d(TAG, "Page URL: " + result.getUrl());
                Log.d(TAG, "HTML length: " + result.getRawHtml().length());
                
                // Extract all links
                List<String> links = result.extractAllLinks();
                Log.d(TAG, "Found " + links.size() + " links");
                for (String link : links) {
                    Log.d(TAG, "Link: " + link);
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Scraping failed: " + error);
            }
        });
    }

    /**
     * Example 2: Extract specific data using CSS selectors
     * Demonstrates how to extract specific elements from a web page.
     */
    public void exampleExtractSpecificData() {
        String url = "https://example.com";
        
        scraper.scrapeAsync(url, new WebScraper.ScraperListener() {
            @Override
            public void onSuccess(WebScraper.ScraperResult result) {
                // Extract all headings
                List<String> headings = result.extractTextBySelector("h1, h2, h3");
                Log.d(TAG, "Headings found: " + headings.size());
                for (String heading : headings) {
                    Log.d(TAG, "Heading: " + heading);
                }

                // Extract all paragraph text
                List<String> paragraphs = result.extractTextBySelector("p");
                Log.d(TAG, "Paragraphs found: " + paragraphs.size());

                // Extract image sources
                List<String> images = result.extractAllImages();
                Log.d(TAG, "Images found: " + images.size());
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Extraction failed: " + error);
            }
        });
    }

    /**
     * Example 3: Extract data with a specific selector
     * Shows how to use the extractData convenience method.
     */
    public void exampleExtractWithSelector() {
        String url = "https://example.com";
        String selector = "div.content p"; // Extract all paragraphs inside div with class "content"
        
        scraper.extractData(url, selector, new WebScraper.DataExtractionListener() {
            @Override
            public void onDataExtracted(List<String> data) {
                Log.d(TAG, "Extracted " + data.size() + " items");
                for (String item : data) {
                    Log.d(TAG, "Item: " + item);
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Data extraction failed: " + error);
            }
        });
    }

    /**
     * Example 4: Check if a URL is reachable
     * Useful for validating URLs before scraping.
     */
    public void exampleCheckUrlReachable() {
        String url = "https://example.com";
        
        scraper.checkUrlReachable(url, new WebScraper.UrlCheckListener() {
            @Override
            public void onCheckComplete(boolean isReachable, int responseCode) {
                if (isReachable) {
                    Log.d(TAG, "URL is reachable. Response code: " + responseCode);
                    // Proceed with scraping
                    exampleBasicScraping();
                } else {
                    Log.e(TAG, "URL is not reachable. Response code: " + responseCode);
                }
            }
        });
    }

    /**
     * Example 5: Scrape multiple URLs
     * Demonstrates batch scraping of multiple pages.
     */
    public void exampleScrapeMultiple() {
        List<String> urls = new ArrayList<>();
        urls.add("https://example.com/page1");
        urls.add("https://example.com/page2");
        urls.add("https://example.com/page3");
        
        scraper.scrapeMultiple(urls, new WebScraper.MultiScraperListener() {
            @Override
            public void onComplete(List<WebScraper.ScraperResult> results, List<String> errors) {
                Log.d(TAG, "Successfully scraped " + results.size() + " pages");
                Log.d(TAG, "Failed to scrape " + errors.size() + " pages");
                
                for (WebScraper.ScraperResult result : results) {
                    Log.d(TAG, "Title: " + result.getTitle());
                }
                
                for (String error : errors) {
                    Log.e(TAG, "Error: " + error);
                }
            }
        });
    }

    /**
     * Example 6: Scrape positioning/trajectory data from a custom website
     * This example shows how to scrape trajectory data if available on a web source.
     */
    public void exampleScrapeTrajectoryData() {
        String url = "https://openpositioning.org/trajectories"; // Example URL
        
        scraper.scrapeAsync(url, new WebScraper.ScraperListener() {
            @Override
            public void onSuccess(WebScraper.ScraperResult result) {
                // Extract trajectory IDs (example selector)
                List<String> trajectoryIds = result.extractTextBySelector(".trajectory-id");
                
                // Extract trajectory timestamps
                List<String> timestamps = result.extractTextBySelector(".trajectory-timestamp");
                
                // Extract download links
                List<String> downloadLinks = result.extractAttributeBySelector("a.download-link", "href");
                
                Log.d(TAG, "Found " + trajectoryIds.size() + " trajectories");
                // Only iterate up to the minimum size to avoid index out of bounds
                int minSize = Math.min(trajectoryIds.size(), timestamps.size());
                for (int i = 0; i < minSize; i++) {
                    Log.d(TAG, "Trajectory " + trajectoryIds.get(i) + " - " + timestamps.get(i));
                }
                
                // Log a warning if data sizes don't match
                if (trajectoryIds.size() != timestamps.size()) {
                    Log.w(TAG, "Warning: trajectory IDs and timestamps count mismatch");
                }
                
                Log.d(TAG, "Found " + downloadLinks.size() + " download links");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Failed to scrape trajectory data: " + error);
            }
        });
    }

    /**
     * Cleanup method to be called when scraping is complete.
     */
    public void cleanup() {
        scraper.shutdown();
    }
}
