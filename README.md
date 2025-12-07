# ![Blogwt](https://raw.githubusercontent.com/billy1380/blogwt/master/src/main/resources/com/willshex/blogwt/client/res/brand.png "Blogwt") Blo(gG)WT

Looking for a better name! Let me know if you have one :wink:.

## Intro
This project is a blog written in GWT on top of app engine. I started this project because I wanted a blog site for my [willshex.com](http://www.willshex.com "willshex.com") domain. The project does not use gwtrpc, I am using a json based API (or 4 - search, user, blog, and page split by functional area).

## Incomplete
There are things missing from the project, but I am working on it everyday, and making good progress too.

I am also adding things that I think I might need as I go along, so if you think there is something useful that you would like to request, please, raise an issue if there isn't one there yet.

I will put a road map shortly on the wiki, maybe even with a "how to".

## Setup
Make sure that you have __git__ and __maven__ installed before you start.

1. **Clone the repository:**
   ```bash
   git clone https://github.com/billy1380/blogwt.git
   cd blogwt
   ```

2. **Install Dependencies:**
   This project relies on several non-Maven dependencies which need to be installed into your local Maven repository. Run the provided script:
   ```bash
   chmod +x installdeps.sh
   ./installdeps.sh
   ```
   *Note: This script installs dependencies into `~/.m2/repository`. Edit the script if your local repository is located elsewhere.*

3. **Build and Install:**
   Compile the project and install artifacts to your local repository:
   ```bash
   mvn clean install
   ```

4. **Run Locally:**
   To verify the application locally:
   ```bash
   mvn appengine:devserver
   ```
   The site should be accessible at `http://localhost:8888`.

   *Note: To change GWT client code on the fly, you also need to run the code server: `mvn gwt:run-codeserver`.*

## Deployment
To deploy the application to Google App Engine, ensure you have built the project dependencies first.

1. **Build (skipping GWT for speed):**
   ```bash
   mvn install -Dgwt.compiler.skip=true -DskipTests
   ```
   *Note: This installs the parent and shared modules to your local repository. The `-Dgwt.compiler.skip=true` flag saves time by skipping the GWT JavaScript compilation if you don't need to update the client.*

2. **Deploy:**
   ```bash
   cd blogwt-server
   mvn appengine:deploy
   ```
This command uploads the application to the App Engine project configured in `pom.xml`.

## Project Structure
The project is a multi-module Maven project:
- **blogwt-shared**: Common code shared between client and server (DTOs, Enums, Utils).
- **blogwt-client**: GWT client-side code (UI, Layouts, Pages).
- **blogwt-server**: App Engine server-side code (Servlets, API Actions, Persistence, Objectify logic).
- **deps**: Folder created by `installdeps.sh` containing cloned dependencies.
