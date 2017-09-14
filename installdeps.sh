#!/bin/bash

function installfromgit {
	git clone $1
	cd $2
	mvn install
	cd ..
}

if [ -d ./deps ]; then
	rm -Rf ./deps
fi

cp -r ./nonmavendeps/gwtchismes ~/.m2/repository/.
cp -r ./com/googlecode/* ~/.m2/repository/com/googlecode.

mkdir ./deps
cd ./deps

installfromgit https://github.com/billy1380/markdown4j-gwt.git markdown4j-gwt
installfromgit https://github.com/billy1380/emoji-gwt.git emoji-gwt
installfromgit https://github.com/billy1380/gson-gwt.git gson-gwt
installfromgit https://github.com/WillShex/willshex-gson.git willshex-gson
installfromgit https://github.com/WillShex/willshex-gson-web-service.git willshex-gson-web-service
installfromgit https://github.com/WillShex/willshex-server.git willshex-server
installfromgit https://github.com/WillShex/willshex-gson-web-service-appengine.git willshex-gson-web-service-appengine
installfromgit https://github.com/WillShex/willshex-service.git willshex-service