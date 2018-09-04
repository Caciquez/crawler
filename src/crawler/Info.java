package crawler;

public class Info {

	private Boolean available;
	private Float price;
	private String name;
	private String imageURL;
	private String url;

	public Info(Boolean available, Float price, String name, String imageURL) {
		this.available = available;
		this.price = price;
		this.name = name;
		this.imageURL = imageURL;
	}

	public Info() {
		super();
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("<html>\n")
				.append("<head>\n").append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n").append( "<title>$title</title>\n").append("</head>\n")
				.append("<body>\n")
				.append("<table style="+"width:100%"+">")
				.append("<tr>")
				.append("<th>Name</th>")
				.append("<th>Price</th>")
				.append("<th>ImageURL</th>")
				.append("<th>Available</th>")
				.append("<th>Url</th>")
				.append("</tr>")
				.append("<tr>")
				.append("<td>").append(this.name).append("</td>\n")
				.append("<td>").append(this.price).append("</td>\n")
				.append("<td> <img src=\"").append(this.imageURL).append("\"></td>\n")
				.append("<td>").append(this.available).append("</td>\n")
				.append("<td> <a href=\"").append(this.url).append("\"> See the store </a></td>\n")
				.append("</tr>")
				.append("</table>")
				.append("</body>\n")
				.append("</html>")
				.toString();

	}
}
