"# QAFinalProjectTesting_Dumyah.com" 
# 🧪 Dumyah.com Manual and Automation QA Testing Project

This repository contains a comprehensive **Quality Assurance (QA) Testing Project** for [Dumyah.com](https://www.dumyah.com/en), an e-commerce platform. It includes both **Manual Testing** documentation and **Automated Test Scripts** built using **Java**, **Selenium WebDriver**, **TestNG**, **MySQL**, and **Apache JMeter**.

---

## 🎯 Project Objective

- Ensure Dumyah.com functions correctly and meets user expectations
- Document detailed manual test cases for all critical features
- Automate major user flows to improve test efficiency and coverage
- Perform basic performance testing with Apache JMeter
- Verify database updates through direct SQL integration

---

## 🔧 Tools & Technologies Used

| Technology          | Purpose                                  |
|---------------------|------------------------------------------|
| Java                | Programming language for automation      |
| Selenium WebDriver  | Web UI automation                        |
| TestNG              | Test execution and reporting             |
| MySQL + JDBC        | Dynamic data handling from DB            |
| Apache JMeter       | Performance testing                      |
| ExcelReportListener | Custom test execution reporting          |
| Manual Test Cases   | Functional validation of user flows      |

---

## 📂 Project Structure

Dumyah_FinalProjectQA/
├── AppTest.java # Contains 12 automated test cases
├── TestData.java # Shared functions and DB logic
├── ExcelReportListener.java # Listener for custom Excel reporting
├── Manual_Test_Cases.docx # (Optional) Manual test case documentation
├── JMeter_Scenarios/ # Performance test plan + screenshots
└── README.md # Project overview (this file)

yaml
Copy
Edit

---

## ✅ Automated Test Cases Overview

| Test Case ID    | Description                                  |
|-----------------|----------------------------------------------|
| TC_DUMYAH_001   | Verify homepage elements                     |
| TC_DUMYAH_002   | Login using DB credentials                   |
| TC_DUMYAH_003   | Search + filter products                     |
| TC_DUMYAH_004   | View product details                         |
| TC_DUMYAH_005   | Add product to cart                          |
| TC_DUMYAH_006   | View cart contents                           |
| TC_DUMYAH_007   | Modify quantity in cart                      |
| TC_DUMYAH_008   | Proceed to checkout                          |
| TC_DUMYAH_009   | Create gift registry                         |
| TC_DUMYAH_010   | Add product to registry                      |
| TC_DUMYAH_011   | Edit user account info (verify DB update)    |
| TC_DUMYAH_012   | Logout                                        |

---

## 📝 Manual Testing Scope

Manual test cases were documented for:

- Navigation bar behavior
- Registration and login/logout
- Wishlist & registry
- Search and filters
- UI validation (elements, layout, responsiveness)
- Negative scenarios (invalid login, empty forms, etc.)

Each test case includes:

- Test ID
- Objective
- Pre-conditions
- Test steps
- Expected vs actual results
- Status

---

## 📊 Performance Testing (JMeter)

Two load test scenarios were created using Apache JMeter:

| Scenario | Users | Duration | Loops | Result       |
|----------|-------|----------|-------|--------------|
| 1        | 1000  | 20 sec   | 5     | 36.46% error |
| 2        | 100   | 20 sec   | 5     | 0% error     |



---

## ⚙️ How to Run the Automation

### Prerequisites:
- Java JDK 8+
- Maven
- Microsoft Edge + Edge WebDriver
- MySQL running with `dumyah_qa` database
- TestNG plugin in your IDE

### Steps:
1. Clone the project:
```bash
git clone https://github.com/your-username/Dumyah_FinalProjectQA.git
Update DB connection (in TestData.java):

java
Copy
Edit
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dumyah_qa", "root", "1234");
Run tests via your IDE or with Maven:

bash
Copy
Edit
mvn test
📝 Make sure the users table in your DB includes at least one user with userID = 1.

📄 Test Reports
Execution status is tracked using a custom ExcelReportListener

Excel reports are generated under the /reports folder (auto-created if missing)

Includes test name, status, and timestamp

🙋 Author
Tabarak Matalka
Quality Assurance Engineer
📫 LinkedIn (Add your link here if desired)

📃 License
This project is licensed under the MIT License.
See the LICENSE file for more details.

yaml
Copy
Edit

---

Let me know if you want:

- The `README.md` file ready to download  
- A sample `LICENSE` file  
- A `pom.xml` for Maven setup  
- A diagram or visual summary for GitHub repo preview

Just say the word!







You said:
give me it ad readme


ChatGPT said:
Here is your comprehensive README.md file content for your Manual and Automation QA Testing Project for Dumyah.com, formatted and ready for GitHub:

✅ To use in GitHub:
Open your GitHub repo.

Click Add file → Create new file.

Name it README.md.

Paste the full content below.

Commit and you're done.

markdown
Copy
Edit
# 🧪 Dumyah.com Manual and Automation QA Testing Project

This repository contains a comprehensive **Quality Assurance (QA) Testing Project** for [Dumyah.com](https://www.dumyah.com/en), an e-commerce platform. It includes both **Manual Testing** documentation and **Automated Test Scripts** built using **Java**, **Selenium WebDriver**, **TestNG**, **MySQL**, and **Apache JMeter**.

---

## 🎯 Project Objective

- Ensure Dumyah.com functions correctly and meets user expectations
- Document detailed manual test cases for all critical features
- Automate major user flows to improve test efficiency and coverage
- Perform basic performance testing with Apache JMeter
- Verify database updates through direct SQL integration

---

## 🔧 Tools & Technologies Used

| Technology          | Purpose                                  |
|---------------------|------------------------------------------|
| Java                | Programming language for automation      |
| Selenium WebDriver  | Web UI automation                        |
| TestNG              | Test execution and reporting             |
| MySQL + JDBC        | Dynamic data handling from DB            |
| Apache JMeter       | Performance testing                      |
| ExcelReportListener | Custom test execution reporting          |
| Manual Test Cases   | Functional validation of user flows      |

---

## 📂 Project Structure

Dumyah_FinalProjectQA/
├── AppTest.java # Contains 12 automated test cases
├── TestData.java # Shared functions and DB logic
├── ExcelReportListener.java # Listener for custom Excel reporting
├── Manual_Test_Cases.docx # (Optional) Manual test case documentation
├── JMeter_Scenarios/ # Performance test plan + screenshots
└── README.md # Project overview (this file)

yaml
Copy
Edit

---

## ✅ Automated Test Cases Overview

| Test Case ID    | Description                                  |
|-----------------|----------------------------------------------|
| TC_DUMYAH_001   | Verify homepage elements                     |
| TC_DUMYAH_002   | Login using DB credentials                   |
| TC_DUMYAH_003   | Search + filter products                     |
| TC_DUMYAH_004   | View product details                         |
| TC_DUMYAH_005   | Add product to cart                          |
| TC_DUMYAH_006   | View cart contents                           |
| TC_DUMYAH_007   | Modify quantity in cart                      |
| TC_DUMYAH_008   | Proceed to checkout                          |
| TC_DUMYAH_009   | Create gift registry                         |
| TC_DUMYAH_010   | Add product to registry                      |
| TC_DUMYAH_011   | Edit user account info (verify DB update)    |
| TC_DUMYAH_012   | Logout                                        |

---

## 📝 Manual Testing Scope

Manual test cases were documented for:

- Navigation bar behavior
- Registration and login/logout
- Wishlist & registry
- Search and filters
- UI validation (elements, layout, responsiveness)
- Negative scenarios (invalid login, empty forms, etc.)

Each test case includes:

- Test ID
- Objective
- Pre-conditions
- Test steps
- Expected vs actual results
- Status

---

## 📊 Performance Testing (JMeter)

Two load test scenarios were created using Apache JMeter:

| Scenario | Users | Duration | Loops | Result       |
|----------|-------|----------|-------|--------------|
| 1        | 1000  | 20 sec   | 5     | 36.46% error |
| 2        | 100   | 20 sec   | 5     | 0% error     |



---

## ⚙️ How to Run the Automation

### Prerequisites:
- Java JDK 8+
- Maven
- Microsoft Edge + Edge WebDriver
- MySQL running with `dumyah_qa` database
- TestNG plugin in your IDE

### Steps:
1. Clone the project:
```bash
git clone https://github.com/your-username/Dumyah_FinalProjectQA.git
Update DB connection (in TestData.java):

java
Copy
Edit
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dumyah_qa", "root", "1234");
Run tests via your IDE or with Maven:

bash
Copy
Edit
mvn test
📝 Make sure the users table in your DB includes at least one user with userID = 1.

📄 Test Reports
Execution status is tracked using a custom ExcelReportListener

Excel reports are generated under the /reports folder (auto-created if missing)

Includes test name, status, and timestamp

🙋 Author
Tabarak Matalka
Quality Assurance Engineer
📫 LinkedIn (linkedin.com/in/tabarakmatalk)

