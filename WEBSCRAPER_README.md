# Web Scraper 使用说明 / Web Scraper Documentation

## 概述 / Overview

这是一个为 PositionMe Android 应用程序开发的网页爬虫工具类。它提供了从网页抓取和解析数据的功能。

This is a web scraper utility class developed for the PositionMe Android application. It provides functionality for fetching and parsing data from web pages.

## 功能特性 / Features

1. **异步网页抓取** / Asynchronous Web Scraping
   - 使用 OkHttp 进行网络请求
   - 不阻塞主线程
   - Uses OkHttp for network requests
   - Non-blocking main thread

2. **HTML 解析** / HTML Parsing
   - 使用 JSoup 解析 HTML
   - 支持 CSS 选择器
   - Uses JSoup for HTML parsing
   - Supports CSS selectors

3. **数据提取** / Data Extraction
   - 提取文本内容
   - 提取链接和图片
   - 自定义选择器提取
   - Extract text content
   - Extract links and images
   - Custom selector extraction

4. **批量操作** / Batch Operations
   - 同时抓取多个 URL
   - Scrape multiple URLs simultaneously

## 安装 / Installation

依赖已添加到 `app/build.gradle`:

Dependencies have been added to `app/build.gradle`:

```gradle
implementation 'com.squareup.okhttp3:okhttp:4.10.0'
implementation 'org.jsoup:jsoup:1.15.4'
```

## 使用示例 / Usage Examples

### 1. 基本网页抓取 / Basic Web Scraping

```java
WebScraper scraper = new WebScraper();

scraper.scrapeAsync("https://example.com", new WebScraper.ScraperListener() {
    @Override
    public void onSuccess(WebScraper.ScraperResult result) {
        // 获取页面标题 / Get page title
        String title = result.getTitle();
        
        // 获取所有链接 / Get all links
        List<String> links = result.extractAllLinks();
        
        // 获取所有图片 / Get all images
        List<String> images = result.extractAllImages();
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "Scraping failed: " + error);
    }
});
```

### 2. 使用 CSS 选择器提取数据 / Extract Data Using CSS Selectors

```java
scraper.scrapeAsync("https://example.com", new WebScraper.ScraperListener() {
    @Override
    public void onSuccess(WebScraper.ScraperResult result) {
        // 提取所有标题 / Extract all headings
        List<String> headings = result.extractTextBySelector("h1, h2, h3");
        
        // 提取特定类的段落 / Extract paragraphs with specific class
        List<String> paragraphs = result.extractTextBySelector("p.content");
        
        // 提取属性值 / Extract attribute values
        List<String> urls = result.extractAttributeBySelector("a", "href");
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "Failed: " + error);
    }
});
```

### 3. 简化的数据提取 / Simplified Data Extraction

```java
scraper.extractData("https://example.com", "div.data p", 
    new WebScraper.DataExtractionListener() {
        @Override
        public void onDataExtracted(List<String> data) {
            // 处理提取的数据 / Process extracted data
            for (String item : data) {
                Log.d(TAG, "Data: " + item);
            }
        }

        @Override
        public void onError(String error) {
            Log.e(TAG, "Error: " + error);
        }
    });
```

### 4. 检查 URL 可达性 / Check URL Reachability

```java
scraper.checkUrlReachable("https://example.com", 
    new WebScraper.UrlCheckListener() {
        @Override
        public void onCheckComplete(boolean isReachable, int responseCode) {
            if (isReachable) {
                // URL 可访问，继续抓取 / URL is reachable, proceed with scraping
                Log.d(TAG, "URL is reachable: " + responseCode);
            } else {
                Log.e(TAG, "URL is not reachable");
            }
        }
    });
```

### 5. 批量抓取多个 URL / Scrape Multiple URLs

```java
List<String> urls = Arrays.asList(
    "https://example.com/page1",
    "https://example.com/page2",
    "https://example.com/page3"
);

scraper.scrapeMultiple(urls, new WebScraper.MultiScraperListener() {
    @Override
    public void onComplete(List<WebScraper.ScraperResult> results, 
                          List<String> errors) {
        Log.d(TAG, "Success: " + results.size() + ", Errors: " + errors.size());
        
        for (WebScraper.ScraperResult result : results) {
            Log.d(TAG, "Title: " + result.getTitle());
        }
    }
});
```

## 在 PositionMe 中的应用场景 / Use Cases in PositionMe

1. **抓取轨迹数据** / Scrape Trajectory Data
   - 从网站获取公开的轨迹数据
   - Fetch public trajectory data from websites

2. **获取地图数据** / Fetch Map Data
   - 下载建筑物布局信息
   - Download building layout information

3. **数据同步** / Data Synchronization
   - 从服务器获取更新
   - Fetch updates from servers

4. **位置信息** / Location Information
   - 抓取位置相关的元数据
   - Scrape location-related metadata

## 注意事项 / Important Notes

1. **网络权限** / Network Permission
   - 确保 AndroidManifest.xml 中有网络权限
   - Ensure network permission in AndroidManifest.xml

2. **线程安全** / Thread Safety
   - 异步操作在后台线程执行
   - 回调在主线程中执行
   - Async operations run on background threads
   - Callbacks run on main thread

3. **资源清理** / Resource Cleanup
   - 使用完毕后调用 `scraper.shutdown()`
   - Call `scraper.shutdown()` when done

4. **遵守网站规则** / Respect Website Rules
   - 遵守 robots.txt
   - 不要过度频繁地请求
   - Respect robots.txt
   - Don't make excessive requests

## API 文档 / API Documentation

### WebScraper 类 / WebScraper Class

#### 方法 / Methods

- `scrapeAsync(String url, ScraperListener listener)` - 异步抓取单个 URL / Scrape a single URL asynchronously
- `scrapeSynchronous(String url)` - 同步抓取（不要在主线程调用）/ Scrape synchronously (don't call on main thread)
- `scrapeMultiple(List<String> urls, MultiScraperListener listener)` - 批量抓取 / Batch scraping
- `extractData(String url, String selector, DataExtractionListener listener)` - 提取特定数据 / Extract specific data
- `checkUrlReachable(String url, UrlCheckListener listener)` - 检查 URL / Check URL
- `shutdown()` - 关闭执行器 / Shutdown executor

### ScraperResult 类 / ScraperResult Class

#### 方法 / Methods

- `getTitle()` - 获取页面标题 / Get page title
- `getRawHtml()` - 获取原始 HTML / Get raw HTML
- `getDocument()` - 获取 JSoup Document / Get JSoup Document
- `extractTextBySelector(String selector)` - 按选择器提取文本 / Extract text by selector
- `extractAttributeBySelector(String selector, String attribute)` - 提取属性 / Extract attributes
- `extractAllLinks()` - 提取所有链接 / Extract all links
- `extractAllImages()` - 提取所有图片 / Extract all images

## 完整示例 / Complete Example

查看 `WebScraperExample.java` 获取更多使用示例。

See `WebScraperExample.java` for more usage examples.

## 许可证 / License

This code is part of the PositionMe project.
