# ![Blogwt](https://raw.githubusercontent.com/billy1380/blogwt/master/src/main/resources/com/willshex/blogwt/client/res/brand.png "Blogwt") Blo(gG)WT

Looking for a better name! Let me know if you have one :wink:.

## Intro
This project is a blog written in GWT on top of app engine. I started this project because I wanted a blog site for my [willshex.com](http://www.willshex.com "willshex.com") domain. The project does not use gwtrpc, I am using a json based API (or 4 - search, user, blog, and page split by functional area).

## Incomplete
There are things missing from the project, but I am working on it everyday, and making good progress too.

I am also adding things that I think I might need as I go along, so if you think there is something useful that you would like to request, please, raise an issue if there isn't one there yet.

I will put a road map shortly on the wiki, maybe even with a "how to".

## Setup
Make sure that you have __git__, __maven__ (3.6.3 or newer) and __JDK 17__ installed before you start.

Persistence uses [Objectify](https://github.com/objectify/objectify) 6, which talks to Datastore through the `google-cloud-datastore` client. `ObjectifyService.init()` is called once from the static block in `PersistenceServiceProvider`, so all entity access must go through `PersistenceServiceProvider.provide()`.

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
   Objectify 6 talks to Datastore through the Cloud Datastore client, which the App Engine dev server no longer stubs, so a local Datastore emulator is needed. Install it once (`gcloud components install cloud-datastore-emulator`), then start it in its own terminal (the data is empty and in-memory):
   ```bash
   "$(gcloud info --format='value(installation.sdk_root)')/platform/cloud-datastore-emulator/cloud_datastore_emulator" \
       start --host=localhost --port=8081 --consistency=1.0 --store_on_disk=false /tmp/datastore-emulator
   ```
   The App Engine plugin is declared in `blogwt-server`, so run it from that module (running it from the root fails with "No plugin found for prefix 'appengine'"), pointing it at the emulator:
   ```bash
   cd blogwt-server
   export DATASTORE_EMULATOR_HOST=localhost:8081
   export DATASTORE_USE_CLOUD_DATASTORE=true
   export DATASTORE_PROJECT_ID=blogwtproject
   export DATASTORE_USE_PROJECT_ID_AS_APP_ID=true
   mvn appengine:run
   ```
   Without `DATASTORE_EMULATOR_HOST` the Datastore client has no credentials and every request fails with `Unauthenticated`. The other three variables are needed because the legacy App Engine task queue (used when indexing a user or post) opens its own legacy Datastore handle, which refuses to start with an emulator host set unless it is told to use Cloud Datastore. Without them, logging in fails with an `IllegalStateException` from `DatastoreServiceGlobalConfig`.
   The site should be accessible at `http://localhost:8888`.

   *Note: To change GWT client code on the fly, you also need to run the code server from the client module (the GWT plugin is only declared there): `cd blogwt-client && mvn gwt:run-codeserver`.*

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

## API Definition
`docs/api_defintion.json` (the file name is spelled that way) is the code generator definition of the API: every data type (name, parent, variables) and every service with its operations (inputs and outputs). It is what the generator uses to produce the request, response and data type classes for the other clients, including the Dart ones in `blogwt_flutter_client`.

**It has to be updated whenever the API changes**, in the same change as the Java:
- a field added to, removed from or renamed on a data type in `blogwt-shared` (`api/datatype`): update that type's `variables`;
- a new data type or enum: add it to `types`;
- a call added, removed or changed (a `*Request` / `*Response` pair in `api/blog/call` and friends, its `*ActionHandler`, its entry in the servlet): update the matching service's `operations`, with `inputs` from the request fields and `outputs` from the response fields.

Keep the file's formatting (4 space indent, no space after the colon) so a diff only shows what really changed.
