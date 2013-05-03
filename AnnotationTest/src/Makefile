all: processor com/dbobrov/annotation/factorial/Main.java
	javac -processor com.dbobrov.annotation.factorial.FactorialAnnotationProcessor com/dbobrov/annotation/factorial/Main.java

processor: com/dbobrov/annotation/factorial/FactorialAnnotationProcessor.java com/dbobrov/annotation/factorial/Factorial.java
	javac -cp "${JDK_HOME}/lib/tools.jar" com/dbobrov/annotation/factorial/FactorialAnnotationProcessor.java com/dbobrov/annotation/factorial/Factorial.java

clean:
	rm com/dbobrov/annotation/factorial/*.class
