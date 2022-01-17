standaloneJar:
	mvn clean package tomcat7:exec-war-only -Pwebprotege.release -DskipTests

run:
	cd webprotege-server/target && rm -rf .extract && java -jar webprotege-standalone.jar && cd ../..

buildDocker: standaloneJar
	docker build -t webprotege-standalone .

buildDockerOnly:
	docker build -t webprotege-standalone .

runDocker:
	docker-compose up

createAdmin:
	docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account