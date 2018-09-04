package crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class WallMartScrapper {

	private static final String SKU_PRICE_REGEX = "<p class=\"product-price\"\\s+data-price-old=\"(\\d?+\\.?\\d+\\.?\\d+)\"\\s+data-price-sell=\"(\\d+\\.?\\d+)\">";
	private static final String SKU_NAME_REGEX = "<h1 class=\"product-name\">([\\s\\S]*)<\\/h1>";
	private static final String SKU_IMAGE_REGEX = "<img class=\"main-picture\"\\s+src=\"([\\s\\S]+?)\" width=\"";
	private static final String SKU_AVAILABLE_REGEX = "<div class=\"product-quantity-qt product-quantity-container clearfix\"\\s* data-value=\"(\\d+)\"\\s* aria-labelledby=\"Quantidade de produtos\">";
	private String html;
	private String url;

	public WallMartScrapper(String html, String url) {
		this.html = html;
		this.url = url;
	}

	/**
	 * Format HTML data get from the URL
	 * @param Info Class that has the getters and setters plus a StringBuilder to return all values parsed.
	 */
	public void parseInfoFromHtml() {
		Info info = new Info();		

		info.setName(parseSkuName());
		info.setPrice(parseSkuPrice());
		info.setAvailable(parseSkuAvailable());
		info.setImageURL(parseSkuImage());
		info.setUrl(this.url);

		String formatedInfo = info.toString();

		writeHtml(formatedInfo,info.getName());

	}

	/**
	 * Attempts to parse the sku price from the html code.
	 * @param SKU_PRICE_REGEX - the regex to match the Prices on the html code
	 * @param html the html - code of the webpage
	 * @return the price or null if it wasn't found
	 */
	private Float parseSkuPrice() {
		Pattern skuPrice = Pattern.compile(SKU_PRICE_REGEX);
		Matcher matcher = skuPrice.matcher(html);

		while (matcher.find()) {
			try {
				String priceString = matcher.group(2);
				return Float.parseFloat(priceString);
			} catch (IndexOutOfBoundsException ex) {
				System.err.println("Haven't found a product price returning null ...");
				ex.printStackTrace();
			} catch (NumberFormatException ex) {
				System.err.println("Couldn't parse the number");
				ex.printStackTrace();
			}
		}

		return null;
	}
	/**
	 * Attempts to parse the sku Name from the html code.
	 * 
	 * @param SKU_NAME_REGEX - the regex to match the Name of the sku on the html code
	 * @param html - the html code of the webpage
	 * @return the price or null if it wasn't found
	 */
	private String parseSkuName() {
		Pattern skuName = Pattern.compile(SKU_NAME_REGEX);
		Matcher matcher = skuName.matcher(html);

		while(matcher.find()) {
			try {
				String nameString = matcher.group(1);
				return nameString;
			}catch(IndexOutOfBoundsException ex) {
				System.err.println("Haven't found a product name returning null ...");
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Attempts to parse the sku Image URL from the html code.
	 * 
	 * @param SKU_IMAGE_REGEX - the regex to match the sku image URL on the html code
	 * @param html - the html code of the webpage
	 * @return the price or null if it wasn't found
	 */
	private String parseSkuImage() {
		Pattern skuImageUrl = Pattern.compile(SKU_IMAGE_REGEX);
		Matcher matcher = skuImageUrl.matcher(html);
		matcher.reset();

		while(matcher.find()) {
			try {
				String imageUrlString = matcher.group(1);
				return "https:" + imageUrlString;
			} catch(IndexOutOfBoundsException ex) {
				System.err.println("Haven't found a product image returning null ...");
			}
		}
		return null;
	}

	/**
	 * Attempts to parse the sku availability from the html code.
	 * 
	 * @param SKU_AVAILABLE_REGEX - the regex to match the availability of the sku on the html code
	 * @param html - the html code of the webpage
	 * @return the price or null if it wasn't found
	 */
	private Boolean parseSkuAvailable() {
		Pattern skuAvailable = Pattern.compile(SKU_AVAILABLE_REGEX);
		Matcher matcher = skuAvailable.matcher(html);

		while(matcher.find()) {
			try {
				if(matcher.group(1) != null) {
					return true;
				}
			}catch(IndexOutOfBoundsException ex) {
				System.err.println("Haven't found a product available returning null ... ...");
			}
		}
		return null;
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
			System.out.println("Data scrapped and saved!");	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
