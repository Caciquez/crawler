package crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WallMartScrapperJsoup {

	private static final String SKU_PAGE_CLASS = "a.product-brand";
	private static final String SKU_NAME_CLASS = "h1.product-name";
	private static final String SKU_PRICE_CLASS = "p.product-price[data-price-sell] .product-price-value";
	private static final String SKU_AVAILABLE_CLASS = "div.product-quantity-qt";
	private static final String SKU_IMAGE_CLASS = "a.item.active";

	private String html;
	private String url;


	public WallMartScrapperJsoup(String html, String url) {
		this.html = html;
		this.url = url;
	}


	/**Verify if the Html page is a SKUpage or not to start scrapping the specific data 
	 * @return
	 */
	public void parseDataFromHTML() {
		Info info = new Info();

		Document doc = Jsoup.parse(html);


		if(checkPage(doc)) {
			System.out.println("Product page has been acessed");
			info.setName(parseSkuName(doc));
			info.setPrice(parseSkuPriceFloat(doc));
			info.setImageURL(parseSkuImage(doc));
			info.setAvailable(parseSkuAvailable(doc));
			info.setUrl(url);

			String formatedInfo = info.toString();

			writeHtml(formatedInfo,info.getName());


		}else {
			System.out.println("This url isn't a product page: " + url);
		}

	}

	private boolean checkPage(Document doc) {
		Elements data = doc.select(SKU_PAGE_CLASS);

		for(Element el: data) {
			if((el.attr("href") != null)) 
				return true;
		}

		return false;
	}


	/**
	 * Grab the SKU name as Element.
	 * @return skuName convert to String
	 */
	private String parseSkuName(Document doc) {
		Element skuName = doc.select(SKU_NAME_CLASS).first();

		if(skuName != null) {
			return skuName.text();			
		}

		return null;
	}	

	private Float parseSkuPriceFloat(Document doc) {
		Float price = null;

		Element skuPrice = doc.selectFirst(SKU_PRICE_CLASS);
		if(skuPrice != null) {
			String priceString = skuPrice.text().replaceAll("[^0-9,]", "").replace(",", ".").trim();

			if(!priceString.isEmpty()) {
				price = Float.parseFloat(priceString);
			}
		}

		return price;
	}

	private String parseSkuImage(Document doc) {
		Elements skuImage = doc.select(SKU_IMAGE_CLASS);
		String urlImage = "";

		for(Element el: skuImage) {
			urlImage = el.attr("data-zoom");
		}

		if(urlImage != null) 
			return urlImage;


		return null;
	}

	private boolean parseSkuAvailable(Document doc) {
		Elements skuAvailable = doc.select(SKU_AVAILABLE_CLASS);
		int available = 0;

		for(Element el: skuAvailable) {
			available = Integer.parseInt(el.attr("data-value"));
		}

		if(available > 1) 
			return true;

		return false;
	}

	/**
	 * Write's data into a html file.
	 * @param info 
	 */
	public void writeHtml(String info,String skuName) {
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter("wallmartSKU" + skuName + ".html"));		
			out.println(info);
			out.close();
			System.out.println("Data Scrapped and saved!");	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
