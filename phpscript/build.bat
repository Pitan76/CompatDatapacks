cd "..\"

set modid=compatdatapacks76

git fetch origin
timeout /T 1
call gradlew.bat build
call gradlew.bat generatePomFileForMavenCommonPublication
call gradlew.bat generatePomFileForMavenFabricPublication
rem call gradlew.bat generatePomFileForMavenForgePublication
call gradlew.bat generatePomFileForMavenNeoForgePublication

copy /y ".\build\publications\mavenCommon\pom-default.xml" ".\build\publications\mavenCommon\%modid%.pom"
copy /y ".\build\publications\mavenFabric\pom-default.xml" ".\build\publications\mavenFabric\%modid%.pom"
rem copy /y ".\build\publications\mavenForge\pom-default.xml" ".\build\publications\mavenForge\%modid%.pom"
copy /y ".\build\publications\mavenNeoForge\pom-default.xml" ".\build\publications\mavenNeoForge\%modid%.pom"

timeout /T 1

cd ".\phpscript"

start upload_maven.bat
pause