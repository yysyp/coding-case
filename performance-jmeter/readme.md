

cd performance-jmeter && mvn clean -DnumOfThreads=1 -DrampUp=10 -DloopCount=1 -Dserver= -Duser=admin -Dpassword= -DjmeterTestFile=MyTestPlan1.jmx verify
mvn clean "-Dpasswd=" "-DjmeterTestFile=MyTestPlan2-bzm.jmx" verify

