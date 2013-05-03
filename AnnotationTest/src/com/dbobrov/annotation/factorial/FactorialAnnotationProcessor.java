package com.dbobrov.annotation.factorial;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Set;

@SupportedAnnotationTypes(value = {FactorialAnnotationProcessor.ANNOTATION_NAME})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class FactorialAnnotationProcessor extends AbstractProcessor {

    public static final String ANNOTATION_NAME = "com.dbobrov.annotation.factorial.Factorial";
    private JavacProcessingEnvironment environment;
    private TreeMaker maker;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return environment.getSourceVersion();
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        environment = (JavacProcessingEnvironment)processingEnvironment;
        maker = TreeMaker.instance(environment.getContext());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        final Elements elements = environment.getElementUtils();
        final TypeElement annotation = elements.getTypeElement(ANNOTATION_NAME);

        if (annotation != null) {
            final Set<? extends Element> decls = roundEnvironment.getElementsAnnotatedWith(annotation);
            JavacElements utils = environment.getElementUtils();

            for (final Element decl: decls) {
                Factorial fact = decl.getAnnotation(Factorial.class);

                if (fact != null) {
                    int val = fact.value();
                    int factorialValue = 1;
                    if (val > 0) {
                        for (int i = 1; i <= val; i++) {
                            factorialValue *= i;
                        }
                    }
                    JCTree tree = utils.getTree(decl);

                    if (tree instanceof JCTree.JCMethodDecl) {
                        List<JCTree.JCStatement> newStatements = List.nil();

                        JCTree.JCExpression factExpr = maker.Literal(factorialValue);

                        newStatements = newStatements.append(maker.Return(factExpr));

                        ((JCTree.JCMethodDecl)tree).body.stats = newStatements;
                    } else if (tree instanceof JCTree.JCVariableDecl) {
                        JCTree.JCExpression factExpr = maker.Literal(factorialValue);

                        ((JCTree.JCVariableDecl)tree).init = factExpr;
                    }

                }
            }
            return true;
        }
        return false;
    }
}
