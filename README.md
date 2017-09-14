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

- cd to a new folder where you are happy to install the project
- run `git clone https://github.com/billy1380/blogwt.git`
- cd to the folder created `cd blogwt`
- allow the install install depenencies script to run by setting the executable flag on the file `chmod +x installdeps.sh`
- run the installation script using `./installdeps.sh`. This script assumes that the maven files are stored under `~/.m2/repository` so if this is not the case, you might want to edit the file to reflect your setup
- build and install the package using `mvn install`
- build the gwt part using `mvn gwt:compile`
- run the dev server using `mvn appengine:devserver`

The site should now appear under `http://localhost:8888`

__Note:__ To change the gwt code on the fly the code server has to be running. To start the code server run `mvn gwt:run-codeserver`.

## Demo
I have previously had issues with discrepancies between the appengine sdk behaviour and production so to use for testing and also for demo purposes I there is a relatively up to date version of the project running __[here](http://blogwtproject.appspot.com "Blogwt demo")__ so go have a look.
