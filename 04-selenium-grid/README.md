# Selenium Grid with Docker - Complete Guide

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Prerequisites](#prerequisites)
4. [Project Structure](#project-structure)
5. [Key Components](#key-components)
6. [Setup Instructions](#setup-instructions)
7. [Running Tests](#running-tests)
8. [Configuration Options](#configuration-options)
9. [Scaling Selenium Grid](#scaling-selenium-grid)
10. [Troubleshooting](#troubleshooting)
11. [Best Practices](#best-practices)

---

## Overview

This project demonstrates how to run Selenium test automation using **Selenium Grid** with **Docker**. It enables parallel test execution across multiple browsers (Chrome and Firefox) in isolated Docker containers, providing a scalable and maintainable solution for test automation.

### What is Selenium Grid?

Selenium Grid is a distributed testing framework that allows you to:
- Run tests on multiple machines in parallel
- Execute tests on different browser versions
- Reduce test execution time
- Provide isolated test environments

### Why Use Docker with Selenium Grid?

- **Consistency**: Same environment across all machines
- **Scalability**: Easy to add/remove browser nodes
- **Isolation**: Each test runs in its own container
- **No Setup Hassle**: No need to install browsers locally
- **Resource Optimization**: Containers use fewer resources than VMs

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Test Execution                        │
│              (Maven/TestNG/Java Tests)                   │
└──────────────────────┬──────────────────────────────────┘
                       │
                       │ Remote WebDriver
                       │ http://localhost:4444/wd/hub
                       ▼
┌─────────────────────────────────────────────────────────┐
│              Selenium Grid Hub (Port 4444)               │
│           (Manages & Routes Test Requests)               │
└──────────────┬──────────────────────┬────────────────────┘
               │                      │
       ┌───────▼────────┐    ┌───────▼────────┐
       │  Chrome Node   │    │ Firefox Node   │
       │  (Container)   │    │  (Container)   │
       │                │    │                │
       │  - Runs tests  │    │  - Runs tests  │
       │  - Chrome      │    │  - Firefox     │
       │    browser     │    │    browser     │
       └────────────────┘    └────────────────┘
```

### Components:
1. **Hub**: Central point that receives test requests and distributes them to available nodes
2. **Nodes**: Browser containers (Chrome/Firefox) that execute the actual tests
3. **Test Code**: Java/TestNG tests that connect to the hub via RemoteWebDriver

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK 21** or higher
- **Maven 3.6+**
- **Docker** (latest version)
- **Docker Compose** (comes with Docker Desktop)
- **Git** (for cloning repositories)

### Verify Installations

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version
```

---

## Project Structure

```
04-selenium-grid/
├── 01-simple-grid-compose-file/
│   └── docker-compose.yaml          # Basic Grid setup (1 Chrome + 1 Firefox)
├── 02-scale-container-replica/
│   └── docker-compose.yaml          # Scaled Grid setup (4 Chrome + 4 Firefox)
└── selenium-docker/
    ├── pom.xml                      # Maven configuration
    ├── src/
    │   ├── main/java/               # Page Objects
    │   └── test/java/
    │       ├── com/test/
    │       │   ├── AbstractTest.java        # Base test class with Grid config
    │       │   ├── flightreserve/          # Flight booking tests
    │       │   └── vendorapplication/      # Vendor app tests
    │       └── com/utils/           # Utility classes
    └── target/
        └── docker-resources/        # Packaged test artifacts
```

---

## Key Components

### 1. AbstractTest.java (Base Test Class)

**Location**: `selenium-docker/src/test/java/com/test/AbstractTest.java`

This is the **core configuration** file that enables Selenium Grid integration:

```java
public class AbstractTest {
    protected WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setDriver(String browser) throws MalformedURLException {
        // Check if Selenium Grid is enabled via system property
        Boolean status = Boolean.getBoolean("selenium.grid.enable");

        if (status) {
            this.driver = launchRemoteBrowser(browser);  // Use Grid
        } else {
            this.driver = launchLocalBrowser(browser);   // Use local browser
        }
        this.driver.manage().window().maximize();
    }

    // Launches browser on Selenium Grid
    private WebDriver launchRemoteBrowser(String browser) throws MalformedURLException {
        Capabilities capabilities;

        if (browser.equalsIgnoreCase("chrome")) {
            capabilities = new ChromeOptions();
        } else if (browser.equalsIgnoreCase("firefox")) {
            capabilities = new FirefoxOptions();
        } else {
            capabilities = new ChromeOptions();
        }

        // Connect to Grid Hub at localhost:4444
        return new RemoteWebDriver(
            new URL("http://localhost:4444/wd/hub"),
            capabilities
        );
    }

    // Launches local browser (for non-Grid execution)
    private WebDriver launchLocalBrowser(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            return new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            return new FirefoxDriver();
        } else {
            return new ChromeDriver();
        }
    }

    @AfterTest
    public void quitBrowsers() {
        this.driver.quit();
    }
}
```

**Key Points:**
- `selenium.grid.enable` property controls Grid usage
- `RemoteWebDriver` connects to Grid Hub URL: `http://localhost:4444/wd/hub`
- Browser type is passed via TestNG parameter
- Falls back to local browser if Grid is disabled

---

### 2. pom.xml (Maven Configuration)

**Location**: `selenium-docker/pom.xml`

Key sections for Grid configuration:

```xml
<dependencies>
    <!-- Selenium Java (includes RemoteWebDriver) -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.33.0</version>
    </dependency>

    <!-- TestNG for test execution -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.11.0</version>
    </dependency>

    <!-- Firefox Driver support -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-firefox-driver</artifactId>
        <version>4.36.0</version>
    </dependency>
</dependencies>
```

**Maven Surefire Plugin Configuration:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <systemPropertyVariables>
            <!-- Enable Selenium Grid -->
            <selenium.grid.enable>true</selenium.grid.enable>
        </systemPropertyVariables>
        <suiteXmlFiles>
            <suiteXmlFile>test-suite.xml</suiteXmlFile>
        </suiteXmlFiles>
        <threadCount>1</threadCount>
    </configuration>
</plugin>
```

**Important Properties:**
- `selenium.grid.enable=true`: Activates Grid mode
- `threadCount`: Controls parallel test execution
- Packages tests and dependencies into `target/docker-resources/`

---

### 3. Docker Compose Files

#### Simple Grid Setup (1 Node per Browser)

**Location**: `01-simple-grid-compose-file/docker-compose.yaml`

```yaml
services:
  hub:
    image: selenium/hub:4.32
    ports:
      - "4444:4444"

  chrome:
    image: selenium/node-chromium:4.32
    shm_size: "2g"
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443

  firefox:
    image: selenium/node-firefox:4.32
    shm_size: "2g"
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
```

#### Scaled Grid Setup (Multiple Nodes)

**Location**: `02-scale-container-replica/docker-compose.yaml`

```yaml
services:
  hub:
    image: selenium/hub:4.32
    ports:
      - "4444:4444"

  chrome:
    image: selenium/node-chromium:4.32
    shm_size: "2g"
    deploy:
      replicas: 4  # Run 4 Chrome containers
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443

  firefox:
    image: selenium/node-firefox:4.32
    shm_size: "2g"
    deploy:
      replicas: 4  # Run 4 Firefox containers
    depends_on:
      - hub
    environment:
      - SE_EVENT_BUS_HOST=hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
```

**Configuration Explained:**

| Parameter | Description |
|-----------|-------------|
| `image: selenium/hub:4.32` | Selenium Grid Hub version 4.32 |
| `ports: "4444:4444"` | Exposes Hub on port 4444 |
| `shm_size: "2g"` | Shared memory size (prevents browser crashes) |
| `depends_on: - hub` | Ensures Hub starts before nodes |
| `SE_EVENT_BUS_HOST=hub` | Tells nodes to connect to Hub |
| `SE_EVENT_BUS_PUBLISH_PORT=4442` | Hub publish port |
| `SE_EVENT_BUS_SUBSCRIBE_PORT=4443` | Hub subscribe port |
| `replicas: 4` | Creates 4 instances of the browser node |

---

## Setup Instructions

### Step 1: Clone/Navigate to Project

```bash
cd /path/to/docker-learning/04-selenium-grid
```

### Step 2: Start Selenium Grid

**Option A: Basic Grid (1 Chrome + 1 Firefox)**

```bash
cd 01-simple-grid-compose-file
docker-compose up -d
```

**Option B: Scaled Grid (4 Chrome + 4 Firefox)**

```bash
cd 02-scale-container-replica
docker-compose up -d
```

**Verify Grid is Running:**

```bash
# Check running containers
docker ps

# You should see:
# - 1 selenium/hub container
# - 1 or 4 selenium/node-chromium containers
# - 1 or 4 selenium/node-firefox containers
```

**Access Grid Console:**

Open browser and navigate to: `http://localhost:4444`

You'll see the Grid dashboard showing available nodes.

### Step 3: Build Test Project

```bash
cd selenium-docker
mvn clean package -DskipTests
```

This command:
- Compiles all Java source code
- Packages tests into a JAR file
- Copies all dependencies to `target/docker-resources/libs/`
- Creates test resources in `target/docker-resources/`

---

## Running Tests

### Method 1: Run with Selenium Grid (Recommended)

```bash
cd selenium-docker
mvn test -Dselenium.grid.enable=true
```

### Method 2: Run with Local Browsers

```bash
mvn test -Dselenium.grid.enable=false
```

### Method 3: Specify Browser via TestNG XML

Edit your `test-suite.xml`:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="All Test Suite">
    <test name="Chrome Tests">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="com.test.vendorapplication.VendorApplicationTest"/>
        </classes>
    </test>

    <test name="Firefox Tests">
        <parameter name="browser" value="firefox"/>
        <classes>
            <class name="com.test.flightreserve.BookFlightTicket"/>
        </classes>
    </test>
</suite>
```

Then run:
```bash
mvn test -Dselenium.grid.enable=true
```

### Method 4: Run Specific Test Class

```bash
mvn test -Dtest=VendorApplicationTest -Dselenium.grid.enable=true
```

### View Test Reports

After execution:
```bash
# Open TestNG report
open target/test-output/index.html

# Or on Linux
xdg-open target/test-output/index.html
```

---

## Configuration Options

### 1. Enable/Disable Grid

**In pom.xml:**
```xml
<systemPropertyVariables>
    <selenium.grid.enable>true</selenium.grid.enable>
</systemPropertyVariables>
```

**Via Command Line:**
```bash
mvn test -Dselenium.grid.enable=true
```

### 2. Change Browser

**Via TestNG XML:**
```xml
<parameter name="browser" value="chrome"/>
<!-- or -->
<parameter name="browser" value="firefox"/>
```

### 3. Parallel Test Execution

**In pom.xml:**
```xml
<configuration>
    <threadCount>4</threadCount>  <!-- Run 4 tests in parallel -->
    <parallel>methods</parallel>   <!-- Parallel execution mode -->
</configuration>
```

### 4. Change Grid Hub URL

If Grid is running on a different machine:

**In AbstractTest.java:**
```java
return new RemoteWebDriver(
    new URL("http://your-grid-host:4444/wd/hub"),
    capabilities
);
```

### 5. Custom Chrome/Firefox Options

```java
private WebDriver launchRemoteBrowser(String browser) {
    Capabilities capabilities;

    if (browser.equalsIgnoreCase("chrome")) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Run headless
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        capabilities = options;
    } else {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        capabilities = options;
    }

    return new RemoteWebDriver(
        new URL("http://localhost:4444/wd/hub"),
        capabilities
    );
}
```

---

## Scaling Selenium Grid

### Understanding Replicas

The `replicas` parameter creates multiple instances of the same browser node:

```yaml
chrome:
  image: selenium/node-chromium:4.32
  deploy:
    replicas: 4  # Creates 4 Chrome containers
```

**Benefits:**
- **Parallel Execution**: Run 4 Chrome tests simultaneously
- **Load Distribution**: Hub distributes tests across available nodes
- **Faster Execution**: Total test time = Time / Number of nodes

### Scaling Strategies

#### Strategy 1: Docker Compose Replicas

```bash
# Start with 8 Chrome nodes
docker-compose up -d --scale chrome=8
```

#### Strategy 2: Add More Browser Types

```yaml
services:
  hub:
    image: selenium/hub:4.32
    ports:
      - "4444:4444"

  chrome:
    image: selenium/node-chromium:4.32
    shm_size: "2g"
    deploy:
      replicas: 3

  firefox:
    image: selenium/node-firefox:4.32
    shm_size: "2g"
    deploy:
      replicas: 3

  edge:
    image: selenium/node-edge:4.32
    shm_size: "2g"
    deploy:
      replicas: 2
```

#### Strategy 3: Dynamic Scaling

```bash
# Check current scale
docker-compose ps

# Scale up
docker-compose up -d --scale chrome=10 --scale firefox=10

# Scale down
docker-compose up -d --scale chrome=2 --scale firefox=2
```

### Resource Considerations

Each browser node requires:
- **CPU**: 0.5 - 1 core
- **RAM**: 512MB - 1GB
- **Shared Memory**: 2GB (`shm_size`)

**Calculate Max Nodes:**
```
Max Nodes = (Total System RAM - 2GB for OS) / 1GB per node
```

Example: 16GB RAM system → ~14 browser nodes maximum

---

## Troubleshooting

### Issue 1: Tests Fail with "Could not start a new session"

**Problem**: Grid Hub is not running or not accessible

**Solution:**
```bash
# Check if Hub is running
docker ps | grep selenium/hub

# If not running, start Grid
docker-compose up -d

# Check Hub logs
docker logs <hub-container-id>

# Verify Grid is accessible
curl http://localhost:4444/status
```

### Issue 2: Browser Crashes Inside Container

**Problem**: Insufficient shared memory

**Solution:**
```yaml
chrome:
  image: selenium/node-chromium:4.32
  shm_size: "2g"  # Increase to 2GB or 4GB
```

### Issue 3: No Available Nodes

**Problem**: All nodes are busy or not registered

**Solution:**
```bash
# Check Grid console
open http://localhost:4444

# Check node logs
docker logs <chrome-node-container-id>

# Restart nodes
docker-compose restart chrome firefox
```

### Issue 4: Connection Refused - localhost:4444

**Problem**: Grid is running inside Docker network, not accessible from host

**Solution:**
- Ensure port mapping is correct: `"4444:4444"`
- Try `host.docker.internal:4444` instead of `localhost:4444` (on Mac/Windows)

### Issue 5: Tests Run Locally But Not on Grid

**Problem**: System property not being passed correctly

**Solution:**
```bash
# Verify property is set
mvn test -Dselenium.grid.enable=true -X

# Check AbstractTest.java logs
System.out.println("Grid enabled: " +
    Boolean.getBoolean("selenium.grid.enable"));
```

### Issue 6: Slow Test Execution

**Problem**: Not enough parallel threads or nodes

**Solution:**
```xml
<!-- In pom.xml -->
<configuration>
    <threadCount>8</threadCount>
    <parallel>methods</parallel>
</configuration>
```

```bash
# Scale up nodes
docker-compose up -d --scale chrome=8
```

---

## Best Practices

### 1. Test Design
- **Independent Tests**: Each test should run independently
- **No Shared State**: Avoid dependencies between tests
- **Clean Up**: Always quit driver in `@AfterTest`

### 2. Grid Management
- **Monitor Resources**: Keep an eye on CPU/RAM usage
- **Optimal Nodes**: Don't over-scale; find the sweet spot
- **Regular Cleanup**: Remove stopped containers
  ```bash
  docker system prune -a
  ```

### 3. Configuration
- **Environment Variables**: Use env vars for Grid URL
  ```java
  String gridUrl = System.getenv("GRID_URL");
  if (gridUrl == null) {
      gridUrl = "http://localhost:4444/wd/hub";
  }
  ```
- **Timeouts**: Set appropriate timeouts
  ```java
  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
  ```

### 4. Parallel Execution
- **Thread Count = Nodes**: Set `threadCount` equal to available nodes
- **Test Isolation**: Ensure tests don't interfere with each other
- **Data Management**: Use unique test data for each parallel test

### 5. CI/CD Integration
```yaml
# Example Jenkins/GitLab CI pipeline
steps:
  - name: Start Selenium Grid
    run: |
      cd 02-scale-container-replica
      docker-compose up -d
      sleep 10  # Wait for Grid to be ready

  - name: Run Tests
    run: |
      cd selenium-docker
      mvn test -Dselenium.grid.enable=true

  - name: Stop Grid
    run: |
      cd 02-scale-container-replica
      docker-compose down
```

### 6. Debugging
- **Video Recording**: Enable video recording in Grid
  ```yaml
  chrome:
    image: selenium/node-chromium:4.32
    environment:
      - SE_ENABLE_VIDEO=true
  ```
- **VNC Access**: Use Selenium debug images
  ```yaml
  chrome:
    image: selenium/node-chromium-debug:4.32
    ports:
      - "5900:5900"  # VNC port
  ```

---

## Quick Reference Commands

```bash
# Start Grid
docker-compose up -d

# View Grid Console
open http://localhost:4444

# Check running containers
docker ps

# View container logs
docker logs <container-id>

# Stop Grid
docker-compose down

# Remove all containers and images
docker-compose down --rmi all

# Scale nodes dynamically
docker-compose up -d --scale chrome=5

# Build project
mvn clean package -DskipTests

# Run tests with Grid
mvn test -Dselenium.grid.enable=true

# Run specific test
mvn test -Dtest=VendorApplicationTest -Dselenium.grid.enable=true

# Run tests in parallel
mvn test -Dselenium.grid.enable=true -DthreadCount=4
```

---

## Conclusion

This project provides a complete foundation for running Selenium tests with Docker and Selenium Grid. The modular architecture allows you to:

1. Switch between local and Grid execution easily
2. Scale browser nodes as needed
3. Run tests in parallel for faster feedback
4. Maintain consistent test environments

For future implementations, simply:
1. Copy the `AbstractTest.java` class
2. Configure `pom.xml` with the Grid enable property
3. Start your Grid using docker-compose
4. Run your tests with `-Dselenium.grid.enable=true`

---

## Additional Resources

- [Selenium Grid Documentation](https://www.selenium.dev/documentation/grid/)
- [Docker Selenium GitHub](https://github.com/SeleniumHQ/docker-selenium)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

---

**Author**: Ajay Chandru
**Project**: Docker + Selenium Grid Test Automation
**Last Updated**: October 2025
