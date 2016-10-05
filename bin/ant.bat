set ANT_HOME=c:\java\jakarta-ant-1.5
set JAVA_HOME=c:\j2sdk1.4.0_01
%JAVA_HOME%\bin\java -classpath %ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\jaxp.jar;%ANT_HOME%\lib\parser.jar;%JAVA_HOME%\lib\tools.jar -Dant.home="%ANT_HOME%" %ANT_OPTS% org.apache.tools.ant.Main %1 %2 %3 %4
