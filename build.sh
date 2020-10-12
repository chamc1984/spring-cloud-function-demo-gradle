#!/usr/bin/env bash

# execute build
echo "exec ./gradlew build"
./gradlew build

# clean work directory.
echo "remove and make target/"
rm -rf target; mkdir target

# move builded jar file
echo "moving jar file"
cp -a build/libs/spring-cloud-function-demo-gradle.jar target

# cd target
echo "cd target"
cd target

# extract jar file
echo "extract jar file"
mv spring-cloud-function-demo-gradle.jar spring-cloud-function-demo-gradle.zip
unzip -q spring-cloud-function-demo-gradle.zip

# organize classes and libs file
echo "organize classes and libs file"
cp -a META-INF BOOT-INF/classes/
echo -n "BOOT-INF/classes:" > cp.txt
find BOOT-INF/lib | sort | tr '\n' ':' >> cp.txt

# execute native-image
echo "execute native-image"
#native-image --verbose -H:Name=demo -Dspring.native.remove-yaml-support=true -cp `cat cp.txt` com.example.springcloudfunctiondemo.SpringCloudFunctionDemoApplication >> output.txt
native-image --verbose -H:Name=demo -cp `cat cp.txt` com.example.springcloudfunctiondemo.SpringCloudFunctionDemoApplication >> output.txt

# create native-zip file
echo "create native-zip file 'demo.zip'"
cp ../scripts/_bootstrap/bootstrap ./
zip demo.zip demo bootstrap
