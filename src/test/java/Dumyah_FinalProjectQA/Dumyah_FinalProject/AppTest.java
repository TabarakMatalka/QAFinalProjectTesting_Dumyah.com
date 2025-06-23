package Dumyah_FinalProjectQA.Dumyah_FinalProject;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExcelReportListener.class)
public class AppTest extends TestData {

	@BeforeTest
	public void Setup() throws SQLException {
		mySetup();
	}

	@Test(priority = 1, enabled = true)
	public void homepageAccessibility() {

		WebElement header = driver.findElement(By.className("header-elements-first"));
		WebElement logoSvg = header.findElement(By.tagName("svg"));
		String expectedTitle = "Dumyah";

		WebElement searchBar = driver.findElement(By.cssSelector(".search-input.search-box-input"));
		// Locate the currency flag image element by id
		WebElement currencyFlag = header.findElement(By.id("currency-flag"));
		WebElement htmlTag = driver.findElement(By.tagName("html"));
		String lang = htmlTag.getAttribute("lang");
		String flagSrc = currencyFlag.getAttribute("src");

		WebElement loginButton = header.findElement(By.xpath("//div[@class='c_nav-current-language display-flex box-shadow']"));
		

		Assert.assertTrue(driver.getTitle().contains(expectedTitle), "Page title check failed");
		Assert.assertTrue(logoSvg.isDisplayed(), "Logo SVG is not found");
		Assert.assertTrue(searchBar.isDisplayed(), "Search bar is not displayed");
		Assert.assertTrue(loginButton.isDisplayed(), "Login/Register button or user icon is not visible.");
		Assert.assertTrue(flagSrc.contains("Jordan_Flag"), "Default currency flag is NOT Jordan (JOD)");
		Assert.assertEquals(lang.toLowerCase(), "en", "Default language is not English (lang attribute mismatch)");

	}


	@Test(priority = 2, enabled = true)
	public void login() throws SQLException, InterruptedException {
		WebElement loginLink = driver
				.findElement(By.xpath("//div[@class='c_nav-current-language display-flex box-shadow']"));
		loginLink.click();
		Thread.sleep(1000);
		retriveUserInfoFromDB();

		WebElement emailInput = driver.findElement(By.id("inputUserName"));
		emailInput.sendKeys(userEmail);

		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		passwordInput.sendKeys(userPassword);
		WebElement loginButton = driver.findElement(By.cssSelector(".btn.btn-default"));
		loginButton.click();
		// Check if the warning message is displayed
	    List<WebElement> warnings = driver.findElements(By.cssSelector(".warning"));
	    if (!warnings.isEmpty() && warnings.get(0).isDisplayed()) {
	        String warningText = warnings.get(0).getText();
	        System.out.println(warningText);
	    } else {
	    	String currentUrl = driver.getCurrentUrl();
			Assert.assertTrue(currentUrl.contains("account"),
					"URL does not contain 'account'. User might not be logged in.");
	    }

	}
	@Test(priority = 3, enabled = true)
	public void searchAndFilteringFunctionality() throws InterruptedException {
		WebElement searchBox = driver.findElement(By.cssSelector(".search-input.search-box-input"));
		searchBox.sendKeys(productKeyWord);
		searchBox.sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		List<WebElement> noProducts = driver.findElements(By.cssSelector(".no-products-message"));
		List<WebElement> resultMessage = driver.findElements(By.id("classMessage"));
		// Apply filter
		if (!resultMessage.isEmpty()) {
			// Apply stars filter
			WebElement starsFilter = driver.findElement(By.id("stars-button-filtering"));
			starsFilter.click();

			Thread.sleep(4000);

			// Apply price filter: Max Price = 50
			WebElement filterButton = driver.findElement(By.id("filter-button-nav"));
			Thread.sleep(1000);
			filterButton.click();
			Thread.sleep(3000);
			WebElement maxPriceInput = driver.findElement(By.cssSelector("input[name='maxPrice']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", maxPriceInput);
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].value='50'; arguments[0].dispatchEvent(new Event('change'));", maxPriceInput);
			Thread.sleep(3000);
			List<WebElement> filteredResults = driver.findElements(By.cssSelector(".product-card"));
			Assert.assertTrue(!filteredResults.isEmpty(), "No products found after applying filters.");

		} else {
			System.out.println("No search results. Filters skipped.");
			Assert.assertFalse(noProducts.isEmpty(), "No search results. Filters skipped");
		}

	}

	@Test(priority = 4, enabled = true)
	public void viewingRandomProductDetails() throws InterruptedException {
		//If there is results form search results
		List<WebElement> productsSearchResults = driver.findElements(By.cssSelector(".product-image.cursor-pointer"));

		if (!productsSearchResults.isEmpty()) {
			int randomProductIndex = random.nextInt(productsSearchResults.size());
			WebElement selectedProduct = productsSearchResults.get(randomProductIndex);
			selectedProduct.click();
			checkProductDetails();
		} else {
			selectRandomCategory();
			Thread.sleep(4000);
			selectRandomProduct();
			checkProductDetails();
		}
	}


	@Test(priority = 5, enabled = true)
	public void addProductToCart() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// to choose 2 random products from 2 random categories
		for (int i = 0; i <= 1; i++) {

			WebElement cart = driver.findElement(By.id("cart-total"));
			int initialTotal = Integer.parseInt(cart.getText());
			selectRandomCategory();
			selectRandomProduct();

			// Check if "Add to Cart" button is available
			// Select random color if available
			List<WebElement> colorElements = driver.findElements(By.xpath("//a[@data-color-name]"));
			if (!colorElements.isEmpty()) {
				int randomColorIndex = random.nextInt(colorElements.size());
				WebElement selectedColor = colorElements.get(randomColorIndex);
				selectedColor.click();
			}
			List<WebElement> addToCartButtons = driver
					.findElements(By.xpath("//div[@class='cart ']//a[@id='button-cart']"));
			if (!addToCartButtons.isEmpty()) {
				// Select random quantity
				WebElement quantityDropdown = driver.findElement(By.cssSelector("select[name='quantity']"));
				Select select = new Select(quantityDropdown);
				int optionsCount = select.getOptions().size();
				int randomIndex = random.nextInt(optionsCount);
				select.selectByIndex(randomIndex);

				int selectedQuantity = Integer.parseInt(select.getFirstSelectedOption().getAttribute("value"));
				Thread.sleep(1000);

				// Click "Add to Cart"
				addToCartButtons.get(0).click();
				String expectedProductName = driver.findElement(By.cssSelector(".heading-title.j-mb-0.j-pr-0"))
						.getText().trim();
				// Store added product name
				productsInCartNames.add(expectedProductName);
				Thread.sleep(2000);
				
				
				// Wait for popup and validate product name
			/*	WebElement popup = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-pnotify-text")));
				List<WebElement> links = popup.findElements(By.tagName("a"));
				String popupProductName = links.get(1).getText().trim();
				
				Assert.assertEquals(popupProductName, expectedProductName);*/
				  driver.navigate().refresh();
				// Validate cart item count update
				cart = driver.findElement(By.id("cart-total"));
				int totalItemsInCart = Integer.parseInt(cart.getText());
				int expectedItemsInCart = initialTotal + selectedQuantity;
				Assert.assertEquals(totalItemsInCart, expectedItemsInCart);


			} else {
				System.out.println("No 'Add to Cart' button found for the selected product.");
			}
		}
	}

	@Test(priority = 6, enabled = true)
	public void view_Cart() throws InterruptedException {
		Thread.sleep(1000);
		// click cart btn in nav bar
		WebElement cartButton = driver.findElement(By.id("cart"));
		cartButton.click();

		Thread.sleep(2000);

		List<WebElement> cartProducts = driver.findElements(By.cssSelector(".shopping_main"));
		if (!cartProducts.isEmpty()) {
			// check products (name,price ,quantity,remove Btn,error messages)
			for (int i = 0; i < cartProducts.size(); i++) {
				WebElement productToCheck = cartProducts.get(i);
				checkProductElementsInCart(productToCheck);
				// check error messages
				checkErrorMessages();
			}
		} else {
			noProdctsInCart();
		}

	}
	
	@Test(priority = 7, enabled = true)
	public void ModifyCart() throws InterruptedException {
		Thread.sleep(1000);

		List<WebElement> cartProducts = driver.findElements(By.cssSelector(".shopping_main"));

		if (!cartProducts.isEmpty()) {
			checkErrorMessages();

			if (!foundErrorrMessage) {
				// Avoid IndexOutOfBounds by checking size again
				if (cartProducts.size() > 0) {
					increaseQuantity();
					Thread.sleep(2000);
					decreaseQuantityOnce();
				} 
			} else {
				System.out.println("Cannot modify quantity due to stock/limit errors.");
			}
		} else {
			System.out.println("Cart is empty.Test can't continue modifying quantity.");
			noProdctsInCart(); // Show message or fail test as appropriate
		}

	}

	@Test(priority = 8, enabled = true)
	public void proceedingToCheckout() throws InterruptedException {
		// Check there is at least one item in cart
		List<WebElement> cartProducts = driver.findElements(By.cssSelector(".shopping_main"));
		if (cartProducts.isEmpty()) {
			noProdctsInCart();
		} else {
			// Check if there is error messages
			checkErrorMessages();
			if (foundErrorrMessage) {
				System.out.println("You can't proceeding to checkout because there is an error");
			} else {
				// Click on checkout Btn
				WebElement checkoutButton = driver
						.findElement(By.xpath("//div[@class='checkout_btn_div']//button[@type='button']"));
				checkoutButton.click();
				Thread.sleep(2000);
				// Check that Checkout have same products in cart , correct total value ,
				// address container
				boolean isSelectAddressPresent = !driver.findElements(By.className("default-address-box")).isEmpty();
				boolean isEditAddressPresent = !driver.findElements(By.className("add_map_body")).isEmpty();

				Assert.assertTrue(isSelectAddressPresent || isEditAddressPresent,
						"Neither 'default-address-box' nor 'add new address' is present on the page.");
				Thread.sleep(1000);
				checkProductsInCheckout();
				Thread.sleep(2000);
				getCartSubtotal(); // from your method

			}

		}

		driver.navigate().back();
		WebElement logo = driver.findElement(By.className("app-logo"));
		logo.click();
	}

	@Test(priority = 9, enabled = true)
	public void createRegistry() throws InterruptedException {
		Thread.sleep(2000);

		navigateToRegistryPage();

		// Create New Registry
		WebElement addNewRegistryButton = driver.findElement(By.xpath("//a[normalize-space()='Add New Registry']"));
		addNewRegistryButton.click();
		WebElement occasionsSection = driver.findElement(By.cssSelector(".mx-2.mx-sm-3.mx-md-4.my-5"));
		WebElement birthdayRegistryList = occasionsSection.findElement(By.xpath("//img[@alt='Birthday']"));
		birthdayRegistryList.click();

		Thread.sleep(2000);
		handleRegistryPopupIfExists();
		Thread.sleep(2000);
		fillSubmitRegistryInfo();

		Thread.sleep(5000);
		// created registry name = name that fill in creation form
		String actualRegistryTitle = driver.findElement(By.xpath(
				"//div[@class='jumbotron jumbotron-custom custom-padding text-center text-dark birthday-registry']//h2"))
				.getText();
		String expectedRegistryTitle = registryName;
		Assert.assertEquals(actualRegistryTitle, expectedRegistryTitle, "Registry title mismatch!");
	}

	@Test(priority = 10, enabled = true)
	public void addProductToRegistryFromProductsList() throws InterruptedException {
		Thread.sleep(4000);
		// Select random product from random category
		selectRandomCategory();
		Thread.sleep(4000);
		selectRandomProduct();
		String selectedProductName = driver.findElement(By.xpath("//h1[@class='heading-title j-mb-0 j-pr-0']"))
				.getText();

		WebElement addToRegistry = driver.findElement(By.xpath("//span[normalize-space()='Add to Registry']"));
		Thread.sleep(2000);
		addToRegistry.click();

		// If we have more than one registry
		List<WebElement> registryOptions = driver
				.findElements(By.cssSelector(".registry-chooser-title input[type='radio']"));
		if (!registryOptions.isEmpty()) {
			registryOptions.get(0).click();
			WebElement addButton = driver.findElement(By.id("registry-chooser-button-add"));
			addButton.click();
		}

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement successMessage = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.success")));
		String expectedMessage = "Success: You have added";
		boolean successfullMessage = successMessage.getText().contains(expectedMessage);

		navigateToRegistryPage();
		Thread.sleep(2000);
		// Retrieve all registries , select one that we choose
		WebElement selectedRegistry = driver.findElement(By.xpath("//tbody[1]/tr[1]/td[1]"));
		selectedRegistry.click();

		Thread.sleep(2000);
		// Get all product name elements
		boolean productFound = false;
		List<WebElement> productContainers = driver.findElements(By.cssSelector(".item-container"));
		for (WebElement container : productContainers) {
			WebElement nameElement = container
					.findElement(By.cssSelector(".desc-container .py-lg-1.py-0.overflow-hidden.text-truncate"));
			String productName = nameElement.getText().trim();
			if (productName.equalsIgnoreCase(selectedProductName)) {
				productFound = true;
				break;
			}
		}

		// Check registry Btn and that product added
		Assert.assertTrue(successfullMessage);
		Assert.assertTrue(productFound, "Product was not found in the selected registry!");

	}

	@Test(priority = 11, enabled = true)
	public void editAccountInfo() throws SQLException, InterruptedException {

		Thread.sleep(2000);
		Actions actions = new Actions(driver);
		WebElement accountMenu = driver.findElement(By.className("account-choices"));
		actions.moveToElement(accountMenu).perform();
		WebElement accountButton = driver
				.findElement(By.xpath("//a[@href='https://www.dumyah.com/en/account/account']"));
		Thread.sleep(2000);
		accountButton.click();
		// Click outside to close dropdown
		WebElement footer = driver.findElement(By.tagName("footer"));
		actions.moveToElement(footer).perform();
		Thread.sleep(1000);

		WebElement editAccountInfo = driver
				.findElement(By.xpath("//a[normalize-space()='Edit your account information']"));
		Thread.sleep(2000);
		editAccountInfo.click();

		WebElement nameInput = driver.findElement(By.xpath("//input[@name='name']"));
		nameInput.clear();
		nameInput.sendKeys(updateName);
		WebElement emailInput = driver.findElement(By.xpath("//input[@name='email']"));
		emailInput.clear();
		emailInput.sendKeys(updateEmail);
		WebElement mobileInput = driver.findElement(By.xpath("//input[@name='telephone']"));
		mobileInput.clear();
		mobileInput.sendKeys(updateMobileNum);
		WebElement saveButton = driver.findElement(By.id("button-save"));
		Thread.sleep(1000);
		saveButton.click();

		// Retrieve update on DB
		updateUserInfoInDB();

		Thread.sleep(4000);
		WebElement successMessage = driver
				.findElement(By.xpath("//div[@class='success' and contains(text(), 'successfully updated')]"));
		Assert.assertTrue(successMessage.isDisplayed(), "Success message not displayed!");
		Assert.assertTrue(updatedInDB, "no");

	}

	@Test(priority = 12, enabled = true)
	public void logout() throws SQLException, InterruptedException {
		Actions actions = new Actions(driver);
		WebElement accountMenu = driver.findElement(By.className("account-choices"));
		actions.moveToElement(accountMenu).perform();
		WebElement logoutButton = driver.findElement(By.className("logout-design"));
		Thread.sleep(2000);
		logoutButton.click();
		Thread.sleep(3000); // Not recommended for real projects, but okay for simple test cases

		WebElement loginLink = driver.findElement(By.linkText("Login"));
		Assert.assertTrue(loginLink.isDisplayed(), "Login link is not visible after logout.");
	}
}
