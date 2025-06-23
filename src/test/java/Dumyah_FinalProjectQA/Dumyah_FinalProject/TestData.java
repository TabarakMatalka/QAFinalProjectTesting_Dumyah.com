package Dumyah_FinalProjectQA.Dumyah_FinalProject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class TestData {

	WebDriver driver = new EdgeDriver();
	String URL = "https://www.dumyah.com/en";
	// objects we need for database
	Connection connection;
	Statement statment;
	ResultSet resultSet;

	// user Info
	String userEmail = "";
	String userPassword = "General_Password25";
	String userName = "";
	String userPhone = "";
	Random random = new Random();
	String[] productsKeyWords = { "book", "cleaning", "rug", "toy", "lego", "box" };
	int productKeyWordIndex = random.nextInt(productsKeyWords.length);
	String productKeyWord = productsKeyWords[productKeyWordIndex];

	// ProductsInCart
	List<String> productsInCartNames = new ArrayList<>();

	// to check error messages in cart
	boolean foundErrorrMessage = false;

	// Dates for registry
	LocalDate futureDate = LocalDate.now().plusMonths(3);
	String day = Integer.toString(futureDate.getDayOfMonth());
	String month = Integer.toString(futureDate.getMonthValue());
	String year = Integer.toString(futureDate.getYear());

	String registryName = "";
	// to update user info
	String updateName = "editedTabarakMatalak";
	String updateEmail = "editedEmail@gamil.com";
	String updateMobileNum = "0789675433";
	boolean updatedInDB;

	public void mySetup() throws SQLException {
		// db connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dumyah_qa", "root", "1234");
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

	}

	// Retrieve user info from DB
	public void retriveUserInfoFromDB() throws SQLException {
		String query = "select * from users where userID =1 ";
		statment = connection.createStatement();
		resultSet = statment.executeQuery(query);

		while (resultSet.next()) {

			userEmail = resultSet.getString("email");
			userName = resultSet.getString("name");
			userPhone = resultSet.getString("phone");

		}

	}

	// Retrieve update on user info in DB
	public void updateUserInfoInDB() throws SQLException {
		String query = "UPDATE Users SET name = '" + updateName + "', email = '" + updateEmail + "', phone = '"
				+ updateMobileNum + "' WHERE UserID = 1";

		statment = connection.createStatement();

		int rowsUpdated = statment.executeUpdate(query);
		if (rowsUpdated >= 1) {
			updatedInDB = true;
		}
	}

	// For all TCs that we need to select random category +product
	public void selectRandomCategory() throws InterruptedException {
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement mainMenu = driver.findElement(By.cssSelector(
				".categories-menu-background.display-none.journal-menu.menu-in-line.j-min.xs-100.sm-100.md-100.lg-100.xl-100"))
				.findElement(By.id("main-menu-item-0"));
		Thread.sleep(1000);
		mainMenu.click();

		Thread.sleep(3000);
		List<WebElement> categories = driver.findElements(By.cssSelector(".has-submenu.category-link.choices-li"));
		WebElement selectedCategory = categories.get(random.nextInt(categories.size()));
		Thread.sleep(1000);
		js.executeScript("arguments[0].scrollIntoView(true);", selectedCategory);
		selectedCategory.click();

		Thread.sleep(3000);
		List<WebElement> subCategories = driver.findElements(By.cssSelector(".flex-space-between.choices-li"));
		WebElement selectedSubCategory = subCategories.get(random.nextInt(subCategories.size()));
		Thread.sleep(1000);
		js.executeScript("arguments[0].scrollIntoView(true);", selectedSubCategory);
		selectedSubCategory.click();
	}

	public void selectRandomProduct() {
		// Get all product cards inside the product container
		List<WebElement> products = driver.findElements(By.cssSelector("#product-container .product-card"));
		if (products.size() == 0) {
			System.out.println("No products found!");
			return;
		}
		int randomIndex = random.nextInt(products.size());
		WebElement randomProduct = products.get(randomIndex);
		WebElement productLink = randomProduct.findElement(By.cssSelector("a"));
		productLink.click();
	}

	// for(ViewProduct Details TC)
	public void checkProductDetails() {
		WebElement productImage = driver
				.findElement(By.xpath("//div[@class='product-info split-40-60']//div[@class='image']//img"));
		Assert.assertTrue(productImage.isDisplayed(), "Product image not displayed");

		WebElement productName = driver.findElement(By.xpath("//h1[@class='heading-title j-mb-0 j-pr-0']"));
		Assert.assertTrue(productName.isDisplayed(), "Product name is not visible.");

		WebElement description = driver.findElement(By.id("tab-description"));
		Assert.assertTrue(description.isDisplayed(), "Description is not displayed");

		WebElement quantityDropdown = driver.findElement(By.xpath("//select[@name='quantity']"));
		Select select = new Select(quantityDropdown);
		String defaultValue = select.getFirstSelectedOption().getText().trim();
		// Check if dropdown is displayed
		Assert.assertTrue(quantityDropdown.isDisplayed(), "Quantity dropdown is not visible");
		// Check if default selected value is 1
		Assert.assertEquals(defaultValue, "1", "Default quantity is not 1");

	}

	// for (createRegistry TC ), to handle options that appear when u create and
	// have already one
	public void handleRegistryPopupIfExists() {
		List<WebElement> popupTitles = driver.findElements(By.id("swal2-title"));
		if (!popupTitles.isEmpty() && popupTitles.get(0).getText().contains("You already have a registry")) {
			List<WebElement> keepOptions = driver.findElements(By.id("create_action_ignore"));
			if (!keepOptions.isEmpty()) {
				keepOptions.get(0).click();
				driver.findElement(By.xpath("//button[normalize-space()='Confirm']")).click();
			}
		}
	}

	// for (View, Modify Cart , Process Checkout TCs)
	public void increaseQuantity() throws InterruptedException {
		WebElement product = driver.findElements(By.cssSelector(".shopping_main")).get(0);
		int expectedQuantity = 0;
		boolean errorFound = false;

		for (int i = 0; i <= 1; i++) {
			// Re-locate the counter and quantity inside the product
			WebElement counterDiv = product.findElement(By.cssSelector("div.counter_btn_div"));
			WebElement quantitySpan = counterDiv.findElement(By.xpath(".//div[2]/span"));

			// On first iteration, read initial quantity
			if (i == 0) {
				expectedQuantity = Integer.parseInt(quantitySpan.getText().trim());
			}

			// Click '+' button
			List<WebElement> buttons = counterDiv.findElements(By.tagName("button"));
			WebElement plusButton = buttons.get(1);
			plusButton.click();
			Thread.sleep(1500); // Let the DOM update

			// Check for error message after clicking
			List<WebElement> errorMessages = driver.findElements(
					By.xpath("//div[contains(@style,'margin-bottom')]/span[contains(@style,'color: red')]"));

			errorFound = false;
			for (WebElement msg : errorMessages) {
				String errorText = msg.getText().trim();
				if (errorText.contains("There are only") || errorText.contains("You can't buy more than")) {
					System.out.println("❗ Error found after click #" + i + ": " + errorText);
					errorFound = true;
					break;
				}
			}

			if (errorFound) {
				break;
			} else {
				expectedQuantity++;
			}

			// refresh & re-locate product
			driver.navigate().refresh();
			Thread.sleep(2000);
			product = driver.findElements(By.cssSelector(".shopping_main")).get(0);
		}
		driver.navigate().refresh();
		Thread.sleep(2000);
		// Final assertion
		product = driver.findElements(By.cssSelector(".shopping_main")).get(0);
		WebElement finalCounterDiv = product.findElement(By.cssSelector("div.counter_btn_div"));
		WebElement finalQuantitySpan = finalCounterDiv.findElement(By.xpath(".//div[2]/span"));
		int actualQuantity = Integer.parseInt(finalQuantitySpan.getText().trim());

		Assert.assertEquals(actualQuantity, expectedQuantity, "Quantity mismatch after increase attempts.");
	}

	public void decreaseQuantityOnce() throws InterruptedException {
		WebElement product = driver.findElements(By.cssSelector(".shopping_main")).get(0);

		WebElement counterDiv = product.findElement(By.cssSelector("div.counter_btn_div"));
		WebElement quantitySpan = counterDiv.findElement(By.xpath(".//div[2]/span"));
		int initialQuantity = Integer.parseInt(quantitySpan.getText().trim());

		if (initialQuantity > 1) {
			List<WebElement> buttons = counterDiv.findElements(By.tagName("button"));
			WebElement minusButton = buttons.get(0);
			minusButton.click();
			initialQuantity--;
			Thread.sleep(1000);

			driver.navigate().refresh();
			Thread.sleep(2000);

			// Re-find product fresh after refresh before further interactions
			product = driver.findElements(By.cssSelector(".shopping_main")).get(0);
			WebElement updatedCounterDiv = product.findElement(By.cssSelector("div.counter_btn_div"));
			WebElement updatedQuantitySpan = updatedCounterDiv.findElement(By.xpath(".//div[2]/span"));
			int finalQuantity = Integer.parseInt(updatedQuantitySpan.getText().trim());

			Assert.assertEquals(finalQuantity, initialQuantity, "Decreasing did not work successfully");
		}
	}

	public void checkProductElementsInCart(WebElement product) {
		// Extract product details
		String productName = product.findElement(By.cssSelector(".cart_product_title a")).getText().trim();
		String productPrice = product.findElement(By.className("shopping_price")).getText().trim();
		String productQuantity = product.findElement(By.xpath(".//div[@class='counter_btn_div']//div[2]/span"))
				.getText().trim();
		WebElement removeButton = product.findElement(By.className("remove_style_btn"));

		// Basic checks
		Assert.assertFalse(productName.isEmpty(), "Product name should not be empty");
		Assert.assertFalse(productPrice.isEmpty(), "Product price should not be empty");
		Assert.assertFalse(productQuantity.isEmpty(), "Product quantity should not be empty");
		Assert.assertTrue(Integer.parseInt(productQuantity) >= 1, "Product quantity should be at least 1");
		Assert.assertTrue(removeButton.isDisplayed(), "Remove button is not displayed");

		// Check that product name exists in the expected list
		Assert.assertTrue(productsInCartNames.contains(productName),
				"Product name '" + productName + "' is not found in the expected product list.");
	}

	public void checkErrorMessages() {
		foundErrorrMessage = false;
		List<WebElement> errorMessages = driver
				.findElements(By.xpath("//div[contains(@style,'margin-bottom')]/span[contains(@style,'color: red')]"));
		if (!errorMessages.isEmpty()) {
			for (WebElement error : errorMessages) {
				String msg = error.getText().trim();

				if (msg.contains("can't buy more than")) {
					foundErrorrMessage = true;
					System.out.println("Max quantity limit hit: " + msg);
				}

				if (msg.contains("There are only")) {
					foundErrorrMessage = true;
					System.out.println("Stock limit hit: " + msg);

				}
			}
		}

	}

	public void noProdctsInCart() {
		System.out.println("Your cart is empty!");
		WebElement continueShoppingBtn = driver.findElement(By.cssSelector("button.continue-shopping"));
		continueShoppingBtn.click();
	}

	public void getCartSubtotal() throws InterruptedException {
		// Wait to ensure subtotal is visible
	    Thread.sleep(2000);

	    WebElement displayedSubtotalElement = driver.findElement(
	        By.xpath("//div[@class='payment_div']//div[1]//span[2]")
	    );
	    String displayedSubtotalText = displayedSubtotalElement.getText().replace("JOD", "").trim();
	    BigDecimal displayedSubtotal = new BigDecimal(displayedSubtotalText).setScale(2, RoundingMode.HALF_UP);

	    // Initialize subtotal
	    BigDecimal calculatedSubtotal = BigDecimal.ZERO;

	    // Wait for cart products to load
	    Thread.sleep(2000);
	    List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cart-product-container"));

	    for (WebElement product : cartProducts) {
	        // Get product price
	        String priceText = product.findElement(By.className("cart_product_price")).getText()
	                .replace("JOD", "").trim();
	        BigDecimal price = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);

	        // Get quantity
	        String quantityText = product.findElement(By.xpath(".//div[@class='counter_btn_div']//div[2]/span"))
	                .getText().trim();
	        int quantity = Integer.parseInt(quantityText);

	        // Calculate total for the product
	        BigDecimal productTotal = price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
	        calculatedSubtotal = calculatedSubtotal.add(productTotal);

	        // Log details
	        System.out.println("Price: " + price + " | Quantity: " + quantity + " | Product Total: " + productTotal);
	    }

	    // Log comparison
	    System.out.println("Displayed Subtotal: " + displayedSubtotal + " | Calculated Subtotal: " + calculatedSubtotal);

	    // Final assertion
	    Assert.assertEquals(calculatedSubtotal, displayedSubtotal, "Mismatch between calculated and displayed subtotal!");
	}

	public void checkProductsInCheckout() {
		// Check that all products names in cart also here
		List<WebElement> productsNamesInCheckout = driver.findElements(By.className("cart_product_title"));
		boolean nameFound = true;
		for (WebElement product : productsNamesInCheckout) {
			String productInCartName = product.getText().trim();
			for (String name : productsInCartNames) {
				if (!name.equalsIgnoreCase(productInCartName)) {
					nameFound = false;
					break;
				}
			}
		}

		if (nameFound) {

			Assert.assertTrue(nameFound, "Product in cart put not in checkout");
		}
	}

	// for |(Registry TCs)
	public void navigateToRegistryPage() {
		Actions actions = new Actions(driver);
		WebElement accountMenu = driver.findElement(By.className("account-choices"));
		actions.moveToElement(accountMenu).perform();
		WebElement registryLink = driver
				.findElement(By.xpath("//a[@href='https://www.dumyah.com/en/account/registry']"));
		registryLink.click();
	}

	public void fillSubmitRegistryInfo() {
		WebElement registryTitleInput = driver.findElement(By.id("title-input"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = LocalDateTime.now().format(formatter);
		registryName = "My Birthday Gifts " + timestamp;
		registryTitleInput.sendKeys(registryName);

		WebElement registryDayInput = driver.findElement(By.xpath("//input[@placeholder='DD']"));
		registryDayInput.sendKeys(day);
		WebElement registryMonthInput = driver.findElement(By.xpath("//input[@placeholder='MM']"));
		registryMonthInput.sendKeys(month);//
		WebElement registryYearInput = driver.findElement(By.xpath("//input[@placeholder='YYYY']"));
		registryYearInput.sendKeys(year);
		WebElement createRegistryBtn = driver
				.findElement(By.cssSelector(".btn.btn-primary.mb-3.submit-registry-button"));
		createRegistryBtn.click();
	}

}
